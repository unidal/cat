package com.dianping.cat.consumer.transaction.model;

import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterByName extends BaseFilter {
	private String m_name; // required

	public TransactionFilterByName(String name) {
		m_name = name;
	}

	@Override
	public void visitName(TransactionName name, IVisitor chain) {
		if (name.getId().equals(m_name)) {
			chain.visitName(name);
		}
	}
}