package com.dianping.cat.consumer.transaction.model;

import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.Machine;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;
import com.dianping.cat.report.ReportConstants;

public class TransactionAggregatorForMachine extends BaseFilter {
	private TransactionReport m_transactionReport;

	@Override
	public void visitTransactionReport(TransactionReport transactionReport, IVisitor chain) {
		if (m_transactionReport == null) {
			Machine all = new Machine(ReportConstants.ALL);
			TransactionReportMerger merger = new TransactionReportMerger(null);

			m_transactionReport = new TransactionReport(transactionReport.getDomain());
			merger.merge(m_transactionReport, transactionReport);

			for (Machine machine : transactionReport.getMachines().values()) {
				merger.merge(all, machine);
			}

			m_transactionReport.addMachine(all);
		}

		chain.visitTransactionReport(m_transactionReport);
	}
}