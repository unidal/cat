package com.dianping.cat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dianping.cat.report.graph.ValueTranslaterTest;
import com.dianping.cat.report.page.cross.CrossReportMergerTest;
import com.dianping.cat.report.page.database.DatabaseReportMergerTest;
import com.dianping.cat.report.page.metric.MetricReportMergerTest;
import com.dianping.cat.report.page.metric.MetricReportParseTest;
import com.dianping.cat.report.page.model.EventReportFilterTest;
import com.dianping.cat.report.page.model.TransactionReportFilterTest;
import com.dianping.cat.report.page.sql.SqlReportMergerTest;
import com.dianping.cat.report.page.state.StateReportMergerTest;
import com.dianping.cat.report.service.ReportServiceImplTest;
import com.dianping.cat.report.task.TaskConsumerTest;
import com.dianping.cat.report.task.TaskHelperTest;
import com.dianping.cat.report.task.health.HealthReportBuilderTest;

@RunWith(Suite.class)
@SuiteClasses({

/* .report.graph */
ValueTranslaterTest.class,

/* .report.page.model */
EventReportFilterTest.class, TransactionReportFilterTest.class,

/* . report.page.cross */
CrossReportMergerTest.class,

/* database test */
DatabaseReportMergerTest.class,

/* sql test */
SqlReportMergerTest.class,

/* .report.task */
TaskConsumerTest.class, TaskHelperTest.class,

/* .health report */
HealthReportBuilderTest.class,

ReportServiceImplTest.class, StateReportMergerTest.class,

/* Metric */
MetricReportParseTest.class, MetricReportMergerTest.class})
public class AllTests {
}
