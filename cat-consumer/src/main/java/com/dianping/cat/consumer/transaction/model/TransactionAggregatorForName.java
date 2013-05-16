package com.dianping.cat.consumer.transaction.model;

import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;
import com.dianping.cat.report.ReportConstants;

public class TransactionAggregatorForName extends BaseFilter {
	private Map<String, TransactionType> m_types = new HashMap<String, TransactionType>();

	@Override
	public void visitType(TransactionType from, IVisitor chain) {
		String id = from.getId();
		TransactionType type = m_types.get(id);

		if (type == null) {
			type = new TransactionType(id);
			m_types.put(id, type);

			TransactionName n = type.findOrCreateName(ReportConstants.ALL);
			TransactionReportMerger merger = new TransactionReportMerger(null);

			for (TransactionName name : from.getNames().values()) {
				merger.merge(n, name);
			}
		}

		chain.visitType(type);
	}
}