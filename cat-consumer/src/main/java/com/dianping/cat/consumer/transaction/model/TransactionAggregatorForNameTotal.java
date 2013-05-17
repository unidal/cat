package com.dianping.cat.consumer.transaction.model;

import static com.dianping.cat.report.ReportConstants.TOTAL;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionAggregatorForNameTotal extends BaseFilter {
	@Override
	public void visitType(TransactionType type, IVisitor chain) {
		TransactionName total = type.findName(TOTAL);

		if (total == null) {
			TransactionReportMerger merger = new TransactionReportMerger(null);
			Map<String, TransactionName> names = new LinkedHashMap<String, TransactionName>(type.getNames());

			total = type.findOrCreateName(TOTAL);

			for (TransactionName name : names.values()) {
				merger.merge(total, name);
			}

			type.getNames().clear();
			type.getNames().put(TOTAL, total);
			type.getNames().putAll(names);
		}

		chain.visitType(type);
	}
}