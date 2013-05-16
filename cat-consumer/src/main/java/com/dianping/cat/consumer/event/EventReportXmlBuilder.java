package com.dianping.cat.consumer.event;

import com.dianping.cat.consumer.event.model.entity.EventName;
import com.dianping.cat.consumer.event.model.entity.EventType;
import com.dianping.cat.consumer.event.model.transform.DefaultXmlBuilder;
import com.dianping.cat.report.ReportConstants;

public class EventReportXmlBuilder extends DefaultXmlBuilder {
	private String m_ipAddress;

	private String m_name;

	private String m_type;

	public EventReportXmlBuilder(String type, String name, String ip) {
		m_type = type;
		m_name = name;
		m_ipAddress = ip;
	}

	@Override
	public void visitMachine(com.dianping.cat.consumer.event.model.entity.Machine machine) {
		if (m_ipAddress == null || m_ipAddress.equals(ReportConstants.ALL)) {
			super.visitMachine(machine);
		} else if (machine.getIp().equals(m_ipAddress)) {
			super.visitMachine(machine);
		} else {
			// skip it
		}
	}

	@Override
	public void visitName(EventName name) {
		if (m_type == null) {
			// skip it
		} else if (m_name != null && name.getId().equals(m_name)) {
			super.visitName(name);
		} else if ("*".equals(m_name)) {
			super.visitName(name);
		} else {
			super.visitName(name);
		}
	}

	@Override
	public void visitRange(com.dianping.cat.consumer.event.model.entity.Range range) {
		if (m_type != null && m_name != null) {
			super.visitRange(range);
		}
	}

	@Override
	public void visitType(EventType type) {
		if (m_type == null) {
			super.visitType(type);
		} else if (m_type != null && type.getId().equals(m_type)) {
			type.setSuccessMessageUrl(null);
			type.setFailMessageUrl(null);

			super.visitType(type);
		} else {
			// skip it
		}
	}
}