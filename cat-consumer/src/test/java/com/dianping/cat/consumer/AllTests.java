package com.dianping.cat.consumer;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.dianping.cat.consumer.core.FormatTest;
import com.dianping.cat.consumer.core.GsonTest;
import com.dianping.cat.consumer.core.NumberFormatTest;
import com.dianping.cat.consumer.core.aggregation.CompositeFormatTest;
import com.dianping.cat.consumer.core.aggregation.DefaultFormatTest;
import com.dianping.cat.consumer.cross.CrossAnalyzerTest;
import com.dianping.cat.consumer.cross.CrossInfoTest;
import com.dianping.cat.consumer.cross.CrossReportMergerTest;
import com.dianping.cat.consumer.dependency.DependencyAnalyzerTest;
import com.dianping.cat.consumer.dependency.DependencyReportMergerTest;
import com.dianping.cat.consumer.dump.DumpAnalyzerTest;
import com.dianping.cat.consumer.event.EventAnalyzerTest;
import com.dianping.cat.consumer.event.EventReportMergerTest;
import com.dianping.cat.consumer.heartbeat.HeartbeatAnalyzerTest;
import com.dianping.cat.consumer.heartbeat.HeartbeatReportMergerTest;
import com.dianping.cat.consumer.metric.MetricAnalyzerTest;
import com.dianping.cat.consumer.metric.ProductLineConfigManagerTest;
import com.dianping.cat.consumer.problem.ProblemAnalyzerTest;
import com.dianping.cat.consumer.problem.ProblemFilterTest;
import com.dianping.cat.consumer.problem.ProblemHandlerTest;
import com.dianping.cat.consumer.problem.ProblemReportConvertorTest;
import com.dianping.cat.consumer.problem.ProblemReportMergerTest;
import com.dianping.cat.consumer.problem.ProblemReportTest;
import com.dianping.cat.consumer.state.StateAnalyzerTest;
import com.dianping.cat.consumer.state.StateReportMergerTest;
import com.dianping.cat.consumer.top.TopAnalyzerTest;
import com.dianping.cat.consumer.top.TopReportMergerTest;

@RunWith(Suite.class)
@SuiteClasses({

ProblemHandlerTest.class,

FormatTest.class,

GsonTest.class,

NumberFormatTest.class,

MetricAnalyzerTest.class,

/* event */
EventAnalyzerTest.class,

EventReportMergerTest.class,

/* heartbeat */
HeartbeatAnalyzerTest.class,

HeartbeatReportMergerTest.class,

/* state */
StateAnalyzerTest.class,

StateReportMergerTest.class,

/* top */
TopAnalyzerTest.class,

TopReportMergerTest.class,

/* problem */

ProblemHandlerTest.class,

ProblemReportTest.class,

ProblemAnalyzerTest.class,

ProblemReportMergerTest.class,

CompositeFormatTest.class,

DefaultFormatTest.class,

DumpAnalyzerTest.class,

ProblemFilterTest.class,

//MetricAnalyzerTest.class,

ProblemReportConvertorTest.class,

CrossInfoTest.class,

CrossReportMergerTest.class,

CrossAnalyzerTest.class,

DependencyAnalyzerTest.class,

DependencyReportMergerTest.class,

ProductLineConfigManagerTest.class })
public class AllTests {

}
