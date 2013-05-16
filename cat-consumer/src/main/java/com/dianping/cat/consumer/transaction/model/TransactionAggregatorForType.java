package com.dianping.cat.consumer.transaction.model;

import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.Machine;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;
import com.dianping.cat.report.ReportConstants;

public class TransactionAggregatorForType extends BaseFilter {
	private Map<String, Machine> m_machines = new HashMap<String, Machine>();

	@Override
	public void visitMachine(Machine from, IVisitor chain) {
		String ip = from.getIp();
		Machine machine = m_machines.get(ip);

		if (machine == null) {
			machine = new Machine(ip);
			m_machines.put(ip, machine);

			TransactionType t = machine.findOrCreateType(ReportConstants.ALL);
			TransactionReportMerger merger = new TransactionReportMerger(null);

			for (TransactionType type : from.getTypes().values()) {
				merger.merge(t, type);
			}
		}

		chain.visitMachine(machine);
	}
}