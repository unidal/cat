package com.dianping.cat.consumer.heartbeat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.heartbeat.model.transform.DefaultSaxParser;
import com.dianping.cat.report.BaseReportDelegate;

public class HeartbeatDelegate extends BaseReportDelegate<HeartbeatReport> {
	@Override
	public void beforeSave(Map<String, HeartbeatReport> reports) {
		for (HeartbeatReport report : reports.values()) {
			Set<String> domainNames = report.getDomainNames();

			domainNames.clear();
			domainNames.addAll(reports.keySet());
		}
	}

	@Override
	public String buildXml(HeartbeatReport report, Object... creterias) {
		if (creterias.length == 0) {
			String xml = report.toString();

			return xml;
		} else {
			int index = 0;
			String ip = (String) creterias[index++];

			if (ip == null || ip.length() == 0) {
				List<String> ips = new ArrayList<String>(report.getIps());

				if (ips.size() > 0) {
					Collections.sort(ips);

					ip = ips.get(0);
				}
			}

			HeartbeatReportXmlBuilder filter = new HeartbeatReportXmlBuilder(ip);

			return filter.buildXml(report);
		}
	}

	@Override
	public String getDomain(HeartbeatReport report) {
		return report.getDomain();
	}

	@Override
	public HeartbeatReport makeReport(String domain, long startTime, long duration) {
		HeartbeatReport report = new HeartbeatReport(domain);

		report.setStartTime(new Date(startTime));
		report.setEndTime(new Date(startTime + duration - 1));

		return report;
	}

	@Override
	public HeartbeatReport mergeReport(HeartbeatReport old, HeartbeatReport other) {
		HeartbeatReportMerger merger = new HeartbeatReportMerger(old);

		other.accept(merger);
		return old;
	}

	@Override
	public HeartbeatReport parseXml(String xml) throws Exception {
		HeartbeatReport report = DefaultSaxParser.parse(xml);

		return report;
	}
}
