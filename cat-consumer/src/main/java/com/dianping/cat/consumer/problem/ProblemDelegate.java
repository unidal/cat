package com.dianping.cat.consumer.problem;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.DefaultSaxParser;
import com.dianping.cat.report.BaseReportDelegate;

public class ProblemDelegate extends BaseReportDelegate<ProblemReport> {
	@Override
	public void beforeSave(Map<String, ProblemReport> reports) {
		for (ProblemReport report : reports.values()) {
			Set<String> domainNames = report.getDomainNames();

			domainNames.clear();
			domainNames.addAll(reports.keySet());
		}
	}

	public String buildXml(ProblemReport report, Object... creterias) {
		if (creterias.length == 0) {
			String xml = report.toString();

			return xml;
		} else {
			int index = 0;
			String ip = (String) creterias[index++];
			String type = (String) creterias[index++];

			ProblemReportXmlBuilder filter = new ProblemReportXmlBuilder(ip, type);

			return filter.buildXml(report);
		}
	}

	@Override
	public String getDomain(ProblemReport report) {
		return report.getDomain();
	}

	@Override
	public ProblemReport makeReport(String domain, long startTime, long duration) {
		ProblemReport report = new ProblemReport(domain);

		report.setStartTime(new Date(startTime));
		report.setEndTime(new Date(startTime + duration - 1));

		return report;
	}

	@Override
	public ProblemReport mergeReport(ProblemReport old, ProblemReport other) {
		ProblemReportMerger merger = new ProblemReportMerger(old);

		other.accept(merger);
		return old;
	}

	@Override
	public ProblemReport parseXml(String xml) throws Exception {
		ProblemReport report = DefaultSaxParser.parse(xml);

		return report;
	}
}
