package com.dianping.cat.consumer.heartbeat;

import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.heartbeat.model.transform.DefaultMerger;

public class HeartbeatReportMerger extends DefaultMerger {
	private HeartbeatReport m_heartbeatReport;

	public HeartbeatReportMerger(HeartbeatReport heartbeatReport) {
		m_heartbeatReport = heartbeatReport;
		getObjects().push(heartbeatReport);
	}

	public HeartbeatReport getHeartbeatReport() {
		return m_heartbeatReport;
	}
}
