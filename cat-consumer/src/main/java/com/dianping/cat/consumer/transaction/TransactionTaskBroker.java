package com.dianping.cat.consumer.transaction;

import java.util.Date;

import com.dianping.cat.task.TaskEvent;
import com.dianping.cat.task.TaskEventConsumer;
import com.dianping.cat.task.TaskEventContext;
import com.dianping.cat.task.TaskEventException;

public enum TransactionTaskBroker implements TaskEventConsumer {
	HOURLY(TransactionTask.HOURLY.getName()) {
		@Override
		public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
			String domain = event.getProperty("domain", null);
			Date period = event.getDateProperty("period", null);

			ctx.produce(event, TransactionTask.HOURLY_GRAPH.getName(), TransactionTask.HOURLY_GRAPH.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.HOURLY_GRAPH_LAST_24H.getName(), TransactionTask.HOURLY_GRAPH_LAST_24H.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.DAILY.getName(), TransactionTask.DAILY.getRefKey(domain, period));
		}
	},

	DAILY(TransactionTask.DAILY.getName()) {
		@Override
		public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
			String domain = event.getProperty("domain", null);
			Date period = event.getDateProperty("period", null);

			ctx.produce(event, TransactionTask.DAILY_REPORT.getName(), TransactionTask.DAILY_REPORT.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.DAILY_GRAPH.getName(), TransactionTask.DAILY_GRAPH.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.WEEKLY.getName(), TransactionTask.WEEKLY.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.MONTHLY.getName(), TransactionTask.MONTHLY.getRefKey(domain, period));
		}
	},

	WEEKLY(TransactionTask.WEEKLY.getName()) {
		@Override
		public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
			String domain = event.getProperty("domain", null);
			Date period = event.getDateProperty("period", null);

			ctx.produce(event, TransactionTask.WEEKLY_REPORT.getName(), TransactionTask.WEEKLY_REPORT.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.WEEKLY_GRAPH.getName(), TransactionTask.WEEKLY_GRAPH.getRefKey(domain, period));
		}
	},

	MONTHLY(TransactionTask.MONTHLY.getName()) {
		@Override
		public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
			String domain = event.getProperty("domain", null);
			Date period = event.getDateProperty("period", null);

			ctx.produce(event, TransactionTask.MONTHLY_REPORT.getName(), TransactionTask.MONTHLY_REPORT.getRefKey(domain, period));
			ctx.produce(event, TransactionTask.MONTHLY_GRAPH.getName(), TransactionTask.MONTHLY_GRAPH.getRefKey(domain, period));
		}
	};

	private String[] m_subjects;

	private TransactionTaskBroker(String... subjects) {
		m_subjects = subjects;
	}

	@Override
	public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
		throw new TaskEventException(String.format("Task(%s) is subscribed but not implemented by %s!", //
		      event.getSubject(), getClass()));
	}

	@Override
	public String[] getSubjectsToSubcribe() {
		return m_subjects;
	}
}
