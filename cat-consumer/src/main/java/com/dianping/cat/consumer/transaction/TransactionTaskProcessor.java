package com.dianping.cat.consumer.transaction;

import com.dianping.cat.task.TaskEvent;
import com.dianping.cat.task.TaskEventConsumer;
import com.dianping.cat.task.TaskEventContext;
import com.dianping.cat.task.TaskEventException;

public enum TransactionTaskProcessor implements TaskEventConsumer {
	HOURLY_REPORT(TransactionTask.HOURLY_REPORT.getName()),
	
	HOURLY_GRAPH(TransactionTask.HOURLY_GRAPH.getName()),

	HOURLY_LAST_24H(TransactionTask.HOURLY_GRAPH_LAST_24H.getName()),

	DAILY_REPORT(TransactionTask.DAILY_REPORT.getName()),

	DAILY_GRAPH(TransactionTask.DAILY_GRAPH.getName()),

	WEEKLY_REPORT(TransactionTask.WEEKLY_REPORT.getName()),

	WEEKLY_GRAPH(TransactionTask.WEEKLY_GRAPH.getName()),

	MONTHLY_REPORT(TransactionTask.MONTHLY_REPORT.getName()),

	MONTHLY_GRAPH(TransactionTask.MONTHLY_GRAPH.getName());

	private String[] m_subjects;

	private TransactionTaskProcessor(String... subjects) {
		m_subjects = subjects;
	}

	@Override
	public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException {
		// TODO Auto-generated method stub

	}

	@Override
	public String[] getSubjectsToSubcribe() {
		return m_subjects;
	}
}
