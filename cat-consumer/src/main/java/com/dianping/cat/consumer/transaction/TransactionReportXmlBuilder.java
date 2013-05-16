package com.dianping.cat.consumer.transaction;

import com.dianping.cat.consumer.transaction.model.entity.AllDuration;
import com.dianping.cat.consumer.transaction.model.entity.Duration;
import com.dianping.cat.consumer.transaction.model.entity.Range;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.DefaultXmlBuilder;
import com.dianping.cat.report.ReportConstants;

public class TransactionReportXmlBuilder extends DefaultXmlBuilder {
	private String m_ipAddress;

	private String m_name;

	private String m_type;

	public TransactionReportXmlBuilder(String type, String name, String ip) {
		m_type = type;
		m_name = name;
		m_ipAddress = ip;
	}

	@Override
	public void visitAllDuration(AllDuration duration) {
	}

	@Override
	public void visitDuration(Duration duration) {
		if (m_type != null && m_name != null) {
			super.visitDuration(duration);
		}
	}

	@Override
	public void visitMachine(com.dianping.cat.consumer.transaction.model.entity.Machine machine) {
		synchronized (machine) {
			if (m_ipAddress == null || m_ipAddress.equals(ReportConstants.ALL)) {
				super.visitMachine(machine);
			} else if (machine.getIp().equals(m_ipAddress)) {
				super.visitMachine(machine);
			} else {
				// skip it
			}
		}
	}

	@Override
	public void visitName(TransactionName name) {
		if (m_type == null) {
			// skip it
		} else if (m_name != null && name.getId().equals(m_name)) {
			visitTransactionName(name);
		} else if ("*".equals(m_name)) {
			visitTransactionName(name);
		} else {
			visitTransactionName(name);
		}
	}

	@Override
	public void visitRange(Range range) {
		if (m_type != null && m_name != null) {
			super.visitRange(range);
		}
	}

	private void visitTransactionName(TransactionName name) {
		super.visitName(name);
	}

	@Override
	public void visitTransactionReport(TransactionReport transactionReport) {
		synchronized (transactionReport) {
			super.visitTransactionReport(transactionReport);
		}
	}

	@Override
	public void visitType(TransactionType type) {
		if (m_type == null) {
			super.visitType(type);
		} else if (m_type != null && type.getId().equals(m_type)) {
			super.visitType(type);
		} else {
			// skip it
		}
	}
}