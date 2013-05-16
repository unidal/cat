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
				if ("name".equals(m_key)) {
					return n1.getId().compareTo(n2.getId());
				} else if ("total".equals(m_key)) {
					return (int) (n1.getTotalCount() - n2.getTotalCount());
				} else if ("fail".equals(m_key)) {
					return (int) (n1.getFailCount() - n2.getFailCount());
				}

				return 0;
			}
		});

		chain.visitType(type);
	}
}