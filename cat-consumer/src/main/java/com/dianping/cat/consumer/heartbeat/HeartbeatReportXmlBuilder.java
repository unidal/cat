package com.dianping.cat.consumer.heartbeat;

import com.dianping.cat.consumer.heartbeat.model.transform.DefaultXmlBuilder;

public class HeartbeatReportXmlBuilder extends DefaultXmlBuilder {
	private String m_ip;

	public HeartbeatReportXmlBuilder(String ip) {
		m_ip = ip;
	}

	@Override
	public void visitMachine(com.dianping.cat.consumer.heartbeat.model.entity.Machine machine) {
		if (machine.getIp().equals(m_ip)) {
			super.visitMachine(machine);
		}
	}
}