package com.dianping.cat.report;

public interface ReportConstants {
	public static final String ALL = "All";

	public static final String TOTAL = "TOTAL";

	public static final String PROPERTY_DATE = "date";

	public static final String PROPERTY_IP = "ip";

	public static final String PROPERTY_TYPE = "type";

	public static final String PROPERTY_NAME = "name";

	public static final String PROPERTY_GRAPH = "graph";

	public static final String PROPERTY_PATTERN = "pattern";

	public static final String PROPERTY_SORT_BY = "sortBy";

	// to exclude all machine in historical report
	public static final String PROPERTY_EXCLUDE_ALL = "excludeAll";

	public static final String VALUE_ALL = "*";

	// following should be deleted
	public static final String ALL_Domain = "All";

	public static final String ALL_Database = "All";

	public static final String ALL_NAME = "All";

	public static final String ONLINE = "cat.dianpingoa.com";

	public static final String OFFLINE = "cat.qa.dianpingoa.com";

	public static final String CAT = "Cat";

	public static final String EXCEPTION = " CAT异常过多告警";

	public static final String SERVICE = " CAT服务调用告警";

	public static final long MINUTE = 60 * 1000L;

	public static final long HOUR = 60 * 60 * 1000L;

	public static final long DAY = 24 * HOUR;

	public static final long WEEK = 7 * DAY;
}
