package com.dianping.cat.consumer.transaction;

import static com.dianping.cat.report.ReportConstants.ALL;
import static com.dianping.cat.report.ReportConstants.PROPERTY_IP;
import static com.dianping.cat.report.ReportConstants.PROPERTY_NAME;
import static com.dianping.cat.report.ReportConstants.PROPERTY_TYPE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dianping.cat.Cat;
import com.dianping.cat.consumer.transaction.model.IFilter;
import com.dianping.cat.consumer.transaction.model.TransactionAggregatorForMachine;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByMachine;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByName;
import com.dianping.cat.consumer.transaction.model.TransactionFilterByType;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.DefaultMaker;
import com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser;
import com.dianping.cat.consumer.transaction.model.transform.VisitorChain;
import com.dianping.cat.report.BaseReportDelegate;
import com.dianping.cat.report.ReportConstants;

public class TransactionDelegate extends BaseReportDelegate<TransactionReport> {
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
		if (creterias.length == 0) {
			report.accept(new TransactionStatisticsComputer());

			TransactionReportUrlFilter filter = new TransactionReportUrlFilter();

			return filter.buildXml(report);
		} else {
			int index = 0;
			String type = (String) creterias[index++];
			String name = (String) creterias[index++];
			String ip = (String) creterias[index++];

			TransactionReportXmlBuilder filter = new TransactionReportXmlBuilder(type, name, ip);

			return filter.buildXml(report);
		}
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
		String ip = properties.get(PROPERTY_IP);
		String type = properties.get(PROPERTY_TYPE);
		String name = properties.get(PROPERTY_NAME);
		List<IFilter> filters = new ArrayList<IFilter>();

		if (!isEmpty(type)) {
			filters.add(new TransactionFilterByType(type));
		}

		if (isAll(ip)) {
			filters.add(new TransactionAggregatorForMachine());
		} else {
			filters.add(new TransactionFilterByMachine(ip));
		}

		if (isEmpty(name)) {
			filters.add(new TransactionFilterByName(null));
		} else {
			filters.add(new TransactionFilterByName(name));
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
