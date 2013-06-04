package com.dianping.cat.task;

public interface TaskEventConsumer {
	public String[] getSubjectsToSubcribe();

	public void consume(TaskEventContext ctx, TaskEvent event) throws TaskEventException;
}
