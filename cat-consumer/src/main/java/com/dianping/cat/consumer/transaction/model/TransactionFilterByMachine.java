package com.dianping.cat.consumer.transaction.model;

import com.dianping.cat.consumer.transaction.model.entity.Machine;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterByMachine extends BaseFilter {
	private String m_ip;

	public TransactionFilterByMachine(String ip) {
		m_ip = ip;
	}

	@Override
	public void visitMachine(Machine machine, IVisitor chain) {
		if (machine.getIp().equals(m_ip)) {
			chain.visitMachine(machine);
		}
	}
}