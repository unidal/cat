package com.dianping.cat.consumer.transaction.model;

import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterByType extends BaseFilter {
	private String m_type;

	public TransactionFilterByType(String type) {
		m_type = type;
	}

	@Override
	public void visitType(TransactionType type, IVisitor chain) {
		if (type.getId().equals(m_type)) {
			chain.visitType(type);
		}
	}
}