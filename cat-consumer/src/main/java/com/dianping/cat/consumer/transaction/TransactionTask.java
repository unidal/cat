package com.dianping.cat.consumer.transaction;

import java.text.MessageFormat;
import java.util.Date;

public enum TransactionTask {
	HOURLY("hourly", "{0}.{1,date,yyyyMMddHH}.{2}"),

	HOURLY_REPORT("hourly_report", "{0}.{1,date,yyyyMMddHH}.{2}"),

	HOURLY_GRAPH("hourly_graph", "{0}.{1,date,yyyyMMddHH}.{2}"),

	HOURLY_GRAPH_LAST_24H("hourly_graph_last_24h", "{0}.{1,date,yyyyMMdd}.{2}"),

	DAILY("daily", "{0}.{1,date,yyyyMMdd}.{2}"),

	DAILY_REPORT("daily_report", "{0}.{1,date,yyyyMMdd}.{2}"),

	DAILY_GRAPH("daily_graph", "{0}.{1,date,yyyyMMdd}.{2}"),

	WEEKLY("weekly", "{0}.{1,date,yyyyMMdd}.{2}"),

	WEEKLY_REPORT("weekly_report", "{0}.{1,date,yyyyMMdd}.{2}"),

	WEEKLY_GRAPH("weekly_graph", "{0}.{1,date,yyyyMMdd}.{2}"),

	MONTHLY("monthly", "{0}.{1,date,yyyyMM}.{2}"),

	MONTHLY_REPORT("monthly_report", "{0}.{1,date,yyyyMM}.{2}"),

	MONTHLY_GRAPH("monthly_graph", "{0}.{1,date,yyyyMM}.{2}");

	private static final String PREFIX = "transaction.";

	private String m_name;

	private String m_pattern;

	private TransactionTask(String id, String pattern) {
		m_name = PREFIX + id;
		m_pattern = pattern;
	}

	public String getName() {
		return m_name;
	}

	public String getRefKey(String domain, Date period) {
		return new MessageFormat(m_pattern).format(new Object[] { getName(), period, domain });
	}
}
