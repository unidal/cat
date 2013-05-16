package com.dianping.cat.report;

import java.util.Map;

public interface ReportDelegate<T> {
	public void afterLoad(Map<String, T> reports);

	public void beforeSave(Map<String, T> reports);

	public String buildXml(T report, Object... creterias);

	public String getDomain(T report);

	public T makeReport(String domain, long startTime, long duration);

	public T mergeReport(T old, T other);

	public T pack(T report, Map<String, String> properties);

	public T parseXml(String xml) throws Exception;

}