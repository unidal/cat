package com.dianping.cat.report.service;

import java.util.Date;
import java.util.Set;

import com.dianping.cat.consumer.cross.model.entity.CrossReport;
import com.dianping.cat.consumer.database.model.entity.DatabaseReport;
import com.dianping.cat.consumer.health.model.entity.HealthReport;
import com.dianping.cat.consumer.matrix.model.entity.MatrixReport;
import com.dianping.cat.consumer.metric.model.entity.MetricReport;
import com.dianping.cat.consumer.sql.model.entity.SqlReport;
import com.dianping.cat.consumer.state.model.entity.StateReport;
import com.dianping.cat.consumer.top.model.entity.TopReport;

public interface HourlyReportService {
	public Set<String> queryAllDatabaseNames(Date start, Date end, String reportName);
	
	public Set<String> queryAllDomainNames(Date start, Date end, String reportName);

	public CrossReport queryCrossReport(String domain, Date start, Date end);

	public DatabaseReport queryDatabaseReport(String database, Date start, Date end);

	public HealthReport queryHealthReport(String domain, Date start, Date end);

	public MatrixReport queryMatrixReport(String domain, Date start, Date end);

	public MetricReport queryMetricReport(String group, Date start, Date end);

	public SqlReport querySqlReport(String domain, Date start, Date end);
	
	public StateReport queryStateReport(String domain,Date start,Date end);
	
	public TopReport queryTopReport(String domain,Date start,Date end);
}
