package com.dianping.cat.consumer.transaction.model;

import java.util.Comparator;

import org.unidal.helper.Sorters;

import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionSorterByName extends BaseFilter {
	private String m_key;

	public TransactionSorterByName(String key) {
		m_key = key;
	}

	@Override
	public void visitType(TransactionType type, IVisitor chain) {
		Sorters.forMap().descend().sort(type.getNames(), new Comparator<TransactionName>() {
			@Override
			public int compare(TransactionName n1, TransactionName n2) {
				if ("avg".equals(m_key)) {
					return n1.getAvg() - n2.getAvg() >= 0 ? 1 : -1;
				} else if ("id".equals(m_key) || "type".equals(m_key) || "name".equals(m_key)) {
					return n2.getId().compareTo(n1.getId());
				} else if ("total".equals(m_key)) {
					return (int) (n1.getTotalCount() - n2.getTotalCount());
				} else if ("failure".equals(m_key) || "failurePercent".equals(m_key)) {
					return (int) (n1.getFailCount() - n2.getFailCount());
				} else if ("95line".equals(m_key)) {
					return n1.getAvg() - n2.getAvg() >= 0 ? 1 : -1;
				}

				return 0;
			}
		});

		chain.visitType(type);
	}
}