package com.dianping.cat.task;

public interface TaskEventContext {
	public int produce(TaskEvent baseEvent, String subject, String refKey, String... properties);
}
