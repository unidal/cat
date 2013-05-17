package com.dianping.cat.consumer.transaction.model;

import java.util.Comparator;

import org.unidal.helper.Sorters;

import com.dianping.cat.consumer.transaction.model.entity.Machine;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionSorterByType extends BaseFilter {
	private String m_key;

	public TransactionSorterByType(String key) {
		m_key = key;
	}

	@Override
	public void visitMachine(Machine machine, IVisitor chain) {
		Sorters.forMap().descend().sort(machine.getTypes(), new Comparator<TransactionType>() {
			@Override
			public int compare(TransactionType t1, TransactionType t2) {
				if ("avg".equals(m_key)) {
					return t1.getAvg() - t2.getAvg() >= 0 ? 1 : -1;
				} else if ("id".equals(m_key) || "type".equals(m_key) || "name".equals(m_key)) {
					return t2.getId().compareTo(t1.getId());
				} else if ("total".equals(m_key)) {
					return (int) (t1.getTotalCount() - t2.getTotalCount());
				} else if ("failure".equals(m_key) || "failurePercent".equals(m_key)) {
					return (int) (t1.getFailCount() - t2.getFailCount());
				} else if ("95line".equals(m_key)) {
					return t1.getAvg() - t2.getAvg() >= 0 ? 1 : -1;
				}

				return 0;
			}
		});

		chain.visitMachine(machine);
	}
}