package com.dianping.cat.consumer.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.unidal.dal.jdbc.datasource.JdbcDataSourceConfigurationManager;
import org.unidal.initialization.Module;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dainping.cat.consumer.core.dal.DailyReportDao;
import com.dainping.cat.consumer.core.dal.MonthlyReportDao;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.TaskDao;
import com.dainping.cat.consumer.core.dal.WeeklyReportDao;
import com.dianping.cat.configuration.ServerConfigManager;
import com.dianping.cat.consumer.CatConsumerModule;
import com.dianping.cat.consumer.DefaultMessageAnalyzerManager;
import com.dianping.cat.consumer.MessageAnalyzer;
import com.dianping.cat.consumer.MessageAnalyzerManager;
import com.dianping.cat.consumer.RealtimeConsumer;
import com.dianping.cat.consumer.dump.DumpAnalyzer;
import com.dianping.cat.consumer.event.EventAnalyzer;
import com.dianping.cat.consumer.event.EventDelegate;
import com.dianping.cat.consumer.heartbeat.HeartbeatAnalyzer;
import com.dianping.cat.consumer.heartbeat.HeartbeatDelegate;
import com.dianping.cat.consumer.problem.DefaultProblemHandler;
import com.dianping.cat.consumer.problem.LongExecutionProblemHandler;
import com.dianping.cat.consumer.problem.ProblemAnalyzer;
import com.dianping.cat.consumer.problem.ProblemDelegate;
import com.dianping.cat.consumer.problem.ProblemHandler;
import com.dianping.cat.consumer.transaction.TransactionAnalyzer;
import com.dianping.cat.consumer.transaction.TransactionDelegate;
import com.dianping.cat.message.spi.MessageConsumer;
import com.dianping.cat.report.DefaultReportManager;
import com.dianping.cat.report.DefaultReportRepository;
import com.dianping.cat.report.RemoteModelService;
import com.dianping.cat.report.ReportDelegate;
import com.dianping.cat.report.ReportManager;
import com.dianping.cat.report.ReportRepository;
import com.dianping.cat.status.ServerStateManager;
import com.dianping.cat.storage.BucketManager;
import com.dianping.cat.storage.dump.LocalMessageBucketManager;
import com.dianping.cat.storage.dump.MessageBucketManager;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}

	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(MessageAnalyzerManager.class, DefaultMessageAnalyzerManager.class));

		all.add(C(MessageConsumer.class, RealtimeConsumer.ID, RealtimeConsumer.class) //
		      .req(MessageAnalyzerManager.class, ServerStateManager.class));

		all.add(C(RemoteModelService.class));
		all.add(C(ReportRepository.class, DefaultReportRepository.class) //
		      .req(ServerConfigManager.class, RemoteModelService.class) //
		      .req(ReportDao.class, DailyReportDao.class, WeeklyReportDao.class, MonthlyReportDao.class));

		all.addAll(defineTransactionComponents());
		all.addAll(defineEventComponents());
		all.addAll(defineProblemComponents());
		all.addAll(defineHeartbeatComponents());
		all.addAll(defineDumpComponents());

		all.add(C(Module.class, CatConsumerModule.ID, CatConsumerModule.class));

		all.addAll(new CatCoreDatabaseConfigurator().defineComponents());
		
		// database
		all.add(C(JdbcDataSourceConfigurationManager.class) //
		      .config(E("datasourceFile").value("config/datasources.xml"), //
		            E("baseDirRef").value("CAT_HOME"), E("defaultBaseDir").value("/data/appdatas/cat"), null));

		return all;
	}

	private Collection<Component> defineDumpComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(MessageAnalyzer.class, DumpAnalyzer.ID, DumpAnalyzer.class).is(PER_LOOKUP) //
		      .req(ServerStateManager.class) //
		      .req(MessageBucketManager.class, LocalMessageBucketManager.ID));

		return all;
	}

	private Collection<Component> defineEventComponents() {
		List<Component> all = new ArrayList<Component>();
		final String ID = EventAnalyzer.ID;

		all.add(C(MessageAnalyzer.class, ID, EventAnalyzer.class).is(PER_LOOKUP) //
		      .req(ReportManager.class, ID));
		all.add(C(ReportManager.class, ID, DefaultReportManager.class) //
		      .req(ReportDelegate.class, ID) //
		      .req(BucketManager.class, ReportDao.class, TaskDao.class) //
		      .config(E("name").value(ID)));
		all.add(C(ReportDelegate.class, ID, EventDelegate.class));

		return all;
	}

	private Collection<Component> defineHeartbeatComponents() {
		List<Component> all = new ArrayList<Component>();
		final String ID = HeartbeatAnalyzer.ID;

		all.add(C(MessageAnalyzer.class, ID, HeartbeatAnalyzer.class).is(PER_LOOKUP) //
		      .req(ReportManager.class, ID));
		all.add(C(ReportManager.class, ID, DefaultReportManager.class) //
		      .req(ReportDelegate.class, ID) //
		      .req(BucketManager.class, ReportDao.class, TaskDao.class) //
		      .config(E("name").value(ID)));
		all.add(C(ReportDelegate.class, ID, HeartbeatDelegate.class));

		return all;
	}

	private Collection<Component> defineProblemComponents() {
		List<Component> all = new ArrayList<Component>();
		final String ID = ProblemAnalyzer.ID;

		all.add(C(MessageAnalyzer.class, ID, ProblemAnalyzer.class).is(PER_LOOKUP) //
		      .req(ReportManager.class, ID) //
		      .req(ProblemHandler.class, new String[] { DefaultProblemHandler.ID, LongExecutionProblemHandler.ID }, "m_handlers"));
		all.add(C(ReportManager.class, ID, DefaultReportManager.class) //
		      .req(ReportDelegate.class, ID) //
		      .req(BucketManager.class, ReportDao.class, TaskDao.class) //
		      .config(E("name").value(ID)));
		all.add(C(ReportDelegate.class, ID, ProblemDelegate.class));

		all.add(C(ProblemHandler.class, DefaultProblemHandler.ID, DefaultProblemHandler.class)//
		      .config(E("failureType").value("URL,SQL,Call,PigeonCall,Cache"))//
		      .config(E("errorType").value("Error,RuntimeException,Exception")));

		all.add(C(ProblemHandler.class, LongExecutionProblemHandler.ID, LongExecutionProblemHandler.class) //
		      .req(ServerConfigManager.class));

		return all;
	}

	private Collection<Component> defineTransactionComponents() {
		List<Component> all = new ArrayList<Component>();
		final String ID = TransactionAnalyzer.ID;

		all.add(C(MessageAnalyzer.class, ID, TransactionAnalyzer.class).is(PER_LOOKUP) //
		      .req(ReportManager.class, ID));
		all.add(C(ReportManager.class, ID, DefaultReportManager.class) //
		      .req(ReportDelegate.class, ID) //
		      .req(BucketManager.class, ReportDao.class, TaskDao.class) //
		      .config(E("name").value(ID)));
		all.add(C(ReportDelegate.class, ID, TransactionDelegate.class));

		return all;
	}
}
