package com.dianping.cat.task;

public class TaskEventException extends Exception {
	private static final long serialVersionUID = 1L;

	public TaskEventException(String message) {
		super(message);
	}

	public TaskEventException(String message, Throwable cause) {
		super(message, cause);
	}
}
