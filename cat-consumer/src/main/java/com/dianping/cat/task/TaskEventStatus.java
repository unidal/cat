package com.dianping.cat.task;

public enum TaskEventStatus {
	TODO(1),

	DOING(2),

	DONE(3),

	FAILED(4);

	private int m_code;

	private TaskEventStatus(int code) {
		m_code = code;
	}

	public static TaskEventStatus getByCode(int code, TaskEventStatus defaultValue) {
		for (TaskEventStatus value : values()) {
			if (value.getCode() == code) {
				return value;
			}
		}

		return defaultValue;
	}

	public int getCode() {
		return m_code;
	}
}
