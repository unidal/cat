package com.dianping.cat.consumer.problem;

import com.dianping.cat.consumer.problem.model.entity.Duration;
import com.dianping.cat.consumer.problem.model.entity.JavaThread;
import com.dianping.cat.consumer.problem.model.entity.Machine;
import com.dianping.cat.consumer.problem.model.entity.Segment;
import com.dianping.cat.consumer.problem.model.transform.DefaultXmlBuilder;

public class ProblemReportXmlBuilder extends DefaultXmlBuilder {
	private String m_ipAddress;

	// view is show the summary, detail show the thread info
	private String m_type;

	public ProblemReportXmlBuilder(String ipAddress, String type) {
		m_ipAddress = ipAddress;
		m_type = type;
	}

	@Override
	public void visitDuration(Duration duration) {
		if ("view".equals(m_type)) {
			super.visitDuration(duration);
		} else if ("graph".equals(m_type)) {
			// skip it
		} else {
			super.visitDuration(duration);
		}
	}

	@Override
	public void visitMachine(Machine machine) {
		if (m_ipAddress == null) {
			super.visitMachine(machine);
		} else if (machine.getIp().equals(m_ipAddress)) {
			super.visitMachine(machine);
		} else {
			// skip it
		}
	}

	@Override
	public void visitSegment(Segment segment) {
		super.visitSegment(segment);
	}

	@Override
	public void visitThread(JavaThread thread) {
		if ("graph".equals(m_type)) {
			super.visitThread(thread);
		} else if ("view".equals(m_type)) {
			// skip it
		} else {
			super.visitThread(thread);
		}
	}
}