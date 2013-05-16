package com.dianping.cat.report;

import static com.dianping.cat.report.ReportConstants.ALL;
import static com.dianping.cat.report.ReportConstants.VALUE_ALL;

import java.util.Map;

public abstract class BaseReportDelegate<T> implements ReportDelegate<T> {
	@Override
	public void afterLoad(Map<String, T> reports) {
	}

	@Override
	public void beforeSave(Map<String, T> reports) {
	}

	protected boolean isAll(String str) {
		return VALUE_ALL.equals(str) || ALL.equals(str);
	}

	protected boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	protected boolean isTrue(String str) {
		return Boolean.parseBoolean(str);
	}

	@Override
	public T pack(T report, Map<String, String> properties) {
		return report;
	}

}
