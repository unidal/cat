package com.dianping.cat;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dianping.cat.report.graph.ValueTranslaterTest;
import com.dianping.cat.report.page.event.EventGraphDataTest;
import com.dianping.cat.report.page.heartbeat.HeartbeatGraphDataTest;
import com.dianping.cat.report.page.model.EventReportFilterTest;
import com.dianping.cat.report.page.model.event.EventModelServiceTest;
import com.dianping.cat.report.page.problem.ProblemGraphDataTest;
import com.dianping.cat.report.page.problem.ProblemReportMergerTest;
import com.dianping.cat.report.page.transaction.PayloadTest;
import com.dianping.cat.report.page.transaction.TransactionGraphDataTest;
import com.dianping.cat.report.page.transaction.TransactionReportMergerTest;
import com.dianping.cat.report.service.ReportServiceImplTest;
import com.dianping.cat.report.task.TaskConsumerTest;
import com.dianping.cat.report.task.TaskHelperTest;
import com.dianping.cat.report.task.event.EventDailyGraphMergerTest;
import com.dianping.cat.report.task.event.EventGraphCreatorTest;
import com.dianping.cat.report.task.event.HistoryEventMergerTest;
import com.dianping.cat.report.task.problem.ProblemCreateGraphDataTest;
import com.dianping.cat.report.task.problem.ProblemDailyGraphMergerTest;
import com.dianping.cat.report.task.problem.ProblemDailyGraphTest;
import com.dianping.cat.report.task.problem.ProblemGraphCreatorTest;
import com.dianping.cat.report.task.transaction.DailyTransactionReportGraphTest;
import com.dianping.cat.report.task.transaction.HistoryTransactionMergerTest;
import com.dianping.cat.report.task.transaction.TransactionDailyGraphMergerTest;
import com.dianping.cat.report.task.transaction.TransactionGraphCreatorTest;

@RunWith(Suite.class)
@SuiteClasses({
/*
 * TestHttp.class
 */

/* .report.page.model.event */
EventModelServiceTest.class,

/* .report.graph */
ValueTranslaterTest.class,

/* .report.page.model */
EventReportFilterTest.class,

/* . report.page.transcation */
PayloadTest.class, TransactionReportMergerTest.class,

/* graph test */
EventGraphDataTest.class, HeartbeatGraphDataTest.class,

ProblemGraphDataTest.class, TransactionGraphDataTest.class,

ProblemReportMergerTest.class,

/* .report.task */
TaskConsumerTest.class, TaskHelperTest.class,

HistoryEventMergerTest.class, HistoryTransactionMergerTest.class,

ProblemCreateGraphDataTest.class, ProblemGraphCreatorTest.class,

TransactionGraphCreatorTest.class, EventGraphCreatorTest.class, EventDailyGraphMergerTest.class,

TransactionDailyGraphMergerTest.class, ProblemDailyGraphMergerTest.class,

ReportServiceImplTest.class,

/* Daily Graph Test */
DailyTransactionReportGraphTest.class, ProblemDailyGraphTest.class,

})
public class AllTests {
}
