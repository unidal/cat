package com.dianping.cat.consumer.transaction.model;

import java.util.List;

import org.unidal.helper.Splitters;

import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterByTypePattern extends BaseFilter {
	private List<String> m_patterns;

	public TransactionFilterByTypePattern(String typesPattern) {
		m_patterns = Splitters.by('|').noEmptyItem().trim().split(typesPattern);
	}

	@Override
	public void visitType(TransactionType type, IVisitor chain) {
		for (String pattern : m_patterns) {
			if (type.getId().contains(pattern)) {
				chain.visitType(type);
				break;
			}
		}
	}
}