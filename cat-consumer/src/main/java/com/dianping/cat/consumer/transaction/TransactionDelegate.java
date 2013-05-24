package com.dianping.cat.consumer.transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dianping.cat.Cat;
import com.dianping.cat.consumer.transaction.model.IFilter;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForMachine;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForName;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByMachine;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByName;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByNamePattern;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByType;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByTypePattern;
import com.dianping.cat.consumer.transaction.model.TransactionFilterWithGraph;
import com.dianping.cat.consumer.transaction.model.TransactionSorterByName;
import com.dianping.cat.consumer.transaction.model.TransactionSorterByType;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.DefaultMaker;
import com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.transaction.model.transform.VisitorChain;
import com.dianping.cat.report.BaseReportDelegate;
import com.dianping.cat.report.ReportConstants;

public class TransactionDelegate extends BaseReportDelegate<TransactionReport> implements ReportConstants {
	@Override
	public void beforeSave(Map<String, TransactionReport> reports) {
		for (TransactionReport report : reports.values()) {
			Set<String> domainNames = report.getDomainNames();

			domainNames.clear();
			domainNames.addAll(reports.keySet());
		}

		TransactionReport all = createAggregatedTypeReport(reports);

		reports.put(all.getDomain(), all);
	}

	@Override
	public String buildXml(TransactionReport report, Object... creterias) {
		report.accept(new TransactionStatisticsComputer());

		return report.toString();
	}

	private TransactionReport createAggregatedTypeReport(Map<String, TransactionReport> reports) {
		TransactionReport first = reports.values().iterator().next();
		TransactionReport all = makeReport(ALL, first.getStartTime().getTime(), ReportConstants.HOUR);
		TransactionReportTypeAggregator visitor = new TransactionReportTypeAggregator(all);

		try {
			for (TransactionReport report : reports.values()) {
				String domain = report.getDomain();

				all.getIps().add(domain);
				all.getDomainNames().add(domain);

				visitor.visitTransactionReport(report);
			}
		} catch (Exception e) {
			Cat.logError(e);
		}

		return all;
	}

	@Override
	public String getDomain(TransactionReport report) {
		return report.getDomain();
	}

	@Override
	public TransactionReport makeReport(String domain, long startTime, long duration) {
		TransactionReport report = new TransactionReport(domain);

		report.setStartTime(new Date(startTime));
		report.setEndTime(new Date(startTime + duration - 1));

		return report;
	}

	@Override
	public TransactionReport mergeReport(TransactionReport old, TransactionReport other) {
		TransactionReportMerger merger = new TransactionReportMerger(old);

		// TODO hack now
		other.setDomain(old.getDomain());

		other.accept(merger);
		return old;
	}

	@Override
	public TransactionReport pack(TransactionReport report, Map<String, String> properties) {
		// to work around existing report issues
		if ("true".equals(properties.get(PROPERTY_EXCLUDE_ALL))) {
			report.getMachines().remove(ALL);
		}

		String ip = properties.get(PROPERTY_IP);
		String type = properties.get(PROPERTY_TYPE);
		String name = properties.get(PROPERTY_NAME);
		String graph = properties.get(PROPERTY_GRAPH);
		String pattern = properties.get(PROPERTY_PATTERN);
		String sortBy = properties.get(PROPERTY_SORT_BY);
		List<IFilter> filters = new ArrayList<IFilter>();

		if (isAll(ip)) {
			filters.add(new TransactionAggregatorForMachine());
		} else if (!isEmpty(ip)) {
			filters.add(new TransactionFilterByMachine(ip));
		}

		if (!isEmpty(type)) { // name page
			filters.add(new TransactionFilterByType(type));

			if (!isEmpty(pattern)) {
				filters.add(new TransactionFilterByNamePattern(pattern));
			}

			if (!isEmpty(sortBy)) {
				filters.add(new TransactionSorterByName(sortBy));
			}
		} else { // type page
			if (!isEmpty(pattern)) {
				filters.add(new TransactionFilterByTypePattern(pattern));
			}

			filters.add(new TransactionFilterByName(null));

			if (!isEmpty(sortBy)) {
				filters.add(new TransactionSorterByType(sortBy));
			}
		}

		if (isAll(name)) {
			filters.add(new TransactionAggregatorForName());
		} else if (!isEmpty(name)) {
			filters.add(new TransactionFilterByName(name));
		}

		if (!isTrue(graph)) {
			filters.add(new TransactionFilterWithGraph(false));
		}

		DefaultMaker maker = new DefaultMaker();

		report.accept(new VisitorChain(maker, filters));

		return maker.getTransactionReport();
	}

	@Override
	public TransactionReport parseXml(String xml) throws Exception {
		TransactionReport report = DefaultSaxParser.parse(xml);

		return report;
	}
}
