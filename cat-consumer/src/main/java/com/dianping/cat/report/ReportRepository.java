package com.dianping.cat.report;

import com.dianping.cat.report.model.ModelRequest;

public interface ReportRepository<T> {
	public T queryHourlyReport(ModelRequest request);

	public T queryDailyReport(ModelRequest request);

	public T queryWeeklyReport(ModelRequest request);

	public T queryMonthlyReport(ModelRequest request);
}
