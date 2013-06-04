package com.dianping.cat.task;

public interface TaskEventRegistry {
	public TaskEventConsumer findConsumer(String subject);
}
