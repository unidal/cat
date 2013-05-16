package com.dianping.cat.consumer.transaction.model;

import java.util.List;

import org.unidal.helper.Splitters;

import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterByNamePattern extends BaseFilter {
	private List<String> m_patterns;

	public TransactionFilterByNamePattern(String namesPattern) {
		m_patterns = Splitters.by('|').noEmptyItem().trim().split(namesPattern);
	}

	@Override
	public void visitName(TransactionName name, IVisitor chain) {
		for (String pattern : m_patterns) {
			if (name.getId().contains(pattern)) {
				chain.visitName(name);
				break;
			}
		}
	}
}