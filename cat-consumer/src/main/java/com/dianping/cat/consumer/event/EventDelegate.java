package com.dianping.cat.consumer.event;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.event.model.transform.DefaultSaxParser;
import com.dianping.cat.report.BaseReportDelegate;

public class EventDelegate extends BaseReportDelegate<EventReport> {
	@Override
	public void beforeSave(Map<String, EventReport> reports) {
		for (EventReport report : reports.values()) {
			Set<String> domainNames = report.getDomainNames();

			domainNames.clear();
			domainNames.addAll(reports.keySet());
		}
	}

	@Override
	public String buildXml(EventReport report, Object... creterias) {
		if (creterias.length == 0) {
			report.accept(new EventStatisticsComputer());

			String xml = report.toString();

			return xml;
		} else {
			int index = 0;
			String type = (String) creterias[index++];
			String name = (String) creterias[index++];
			String ip = (String) creterias[index++];

			EventReportXmlBuilder filter = new EventReportXmlBuilder(type, name, ip);

			return filter.buildXml(report);
		}
	}

	@Override
	public String getDomain(EventReport report) {
		return report.getDomain();
	}

	@Override
	public EventReport makeReport(String domain, long startTime, long duration) {
		EventReport report = new EventReport(domain);

		report.setStartTime(new Date(startTime));
		report.setEndTime(new Date(startTime + duration - 1));

		return report;
	}

	@Override
	public EventReport mergeReport(EventReport old, EventReport other) {
		EventReportMerger merger = new EventReportMerger(old);

		other.accept(merger);
		return old;
	}

	@Override
	public EventReport parseXml(String xml) throws Exception {
		EventReport report = DefaultSaxParser.parse(xml);

		return report;
	}
}
