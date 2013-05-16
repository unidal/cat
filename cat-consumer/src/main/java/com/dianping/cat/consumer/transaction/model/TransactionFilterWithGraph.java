package com.dianping.cat.consumer.transaction.model;

import com.dianping.cat.consumer.transaction.model.entity.Duration;
import com.dianping.cat.consumer.transaction.model.entity.Range;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionFilterWithGraph extends BaseFilter {
	private boolean m_withGraph;

	public TransactionFilterWithGraph(boolean withGraph) {
		m_withGraph = withGraph;
	}

	@Override
	public void visitDuration(Duration duration, IVisitor chain) {
		if (m_withGraph) {
			chain.visitDuration(duration);
		}
	}

	@Override
	public void visitRange(Range range, IVisitor chain) {
		if (m_withGraph) {
			chain.visitRange(range);
		}
	}
}