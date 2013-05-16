package com.dianping.cat.consumer.transaction.model;

import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.Duration;
import com.dianping.cat.consumer.transaction.model.entity.Range;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.transform.BaseFilter;

public class TransactionAggregatorForGraph extends BaseFilter {
	private Map<String, TransactionName> m_names = new HashMap<String, TransactionName>();

	@Override
	public void visitName(TransactionName from, IVisitor chain) {
		String id = from.getId();
		TransactionName name = m_names.get(id);

		if (name == null) {
			TransactionReportMerger merger = new TransactionReportMerger(null);

			name = new TransactionName(id);
			m_names.put(id, name);

			for (Duration duration : from.getDurations().values()) {
				Duration d = name.findOrCreateDuration(duration.getValue());

				merger.merge(d, duration);
			}

			for (Range range : from.getRanges().values()) {
				Range r = name.findOrCreateRange(range.getValue());

				merger.merge(r, range);
			}
		}

		chain.visitName(name);
	}
}