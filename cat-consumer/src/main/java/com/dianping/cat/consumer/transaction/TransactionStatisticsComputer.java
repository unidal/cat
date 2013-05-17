package com.dianping.cat.consumer.transaction;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.dianping.cat.consumer.transaction.model.entity.AllDuration;
import com.dianping.cat.consumer.transaction.model.entity.Range;
import com.dianping.cat.consumer.transaction.model.entity.TransactionName;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionType;
import com.dianping.cat.consumer.transaction.model.transform.BaseVisitor;

public class TransactionStatisticsComputer extends BaseVisitor {
	private long m_duration;

	private double compute95LineValue(Map<Integer, AllDuration> durations) {
		int totalCount = 0;
		Map<Integer, AllDuration> sorted = new TreeMap<Integer, AllDuration>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o2 - o1;
			}
		});

		sorted.putAll(durations);

		for (AllDuration duration : durations.values()) {
			totalCount += duration.getCount();
		}

		int remaining = totalCount * 5 / 100;

		for (Entry<Integer, AllDuration> entry : sorted.entrySet()) {
			remaining -= entry.getValue().getCount();

			if (remaining <= 0) {
				return entry.getKey();
			}
		}

		return 0.0;
	}

	private long getDuration(TransactionReport transactionReport) {
		long start = transactionReport.getStartTime().getTime();
		long end = transactionReport.getEndTime().getTime();
		long now = System.currentTimeMillis();

		if (end > now) {
			end = now;
		}

		return end - start;
	}

	double std(long count, double avg, double sum2, double max) {
		double value = sum2 / count - avg * avg;

		if (value <= 0 || count <= 1) {
			return 0;
		} else if (count == 2) {
			return max - avg;
		} else {
			return Math.sqrt(value);
		}
	}

	@Override
	public void visitName(TransactionName name) {
		super.visitName(name);

		long count = name.getTotalCount();

		if (count > 0) {
			long failCount = name.getFailCount();
			double avg = name.getSum() / count;
			double std = std(count, avg, name.getSum2(), name.getMax());
			double failPercent = 100.0 * failCount / count;

			name.setFailPercent(failPercent);
			name.setAvg(avg);
			name.setStd(std);

			if (m_duration > 0) {
				name.setTps(name.getTotalCount() * 1000.0 / m_duration);
			}
		}
	}

	@Override
	public void visitRange(Range range) {
		if (range.getCount() > 0) {
			range.setAvg(range.getSum() / range.getCount());
		}
	}

	@Override
	public void visitTransactionReport(TransactionReport transactionReport) {
		m_duration = getDuration(transactionReport);

		super.visitTransactionReport(transactionReport);
	}

	@Override
	public void visitType(TransactionType type) {
		super.visitType(type);

		long count = type.getTotalCount();

		if (count > 0) {
			long failCount = type.getFailCount();
			double avg = type.getSum() / count;
			double std = std(count, avg, type.getSum2(), type.getMax());
			double failPercent = 100.0 * failCount / count;

			type.setFailPercent(failPercent);
			type.setAvg(avg);
			type.setStd(std);

			if (m_duration > 0) {
				type.setTps(type.getTotalCount() * 1000.0 / m_duration);
			}

			double typeValue = compute95LineValue(type.getAllDurations());

			type.setLine95Value(typeValue);
		}
	}
}