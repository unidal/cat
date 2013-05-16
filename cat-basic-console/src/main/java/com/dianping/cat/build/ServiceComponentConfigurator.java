package com.dianping.cat.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.cat.configuration.ServerConfigManager;
import com.dianping.cat.consumer.RealtimeConsumer;
import com.dianping.cat.hadoop.hdfs.HdfsMessageBucketManager;
import com.dianping.cat.message.spi.MessageCodec;
import com.dianping.cat.message.spi.MessageConsumer;
import com.dianping.cat.report.page.model.event.CompositeEventService;
import com.dianping.cat.report.page.model.event.HistoricalEventService;
import com.dianping.cat.report.page.model.event.LocalEventService;
import com.dianping.cat.report.page.model.heartbeat.CompositeHeartbeatService;
import com.dianping.cat.report.page.model.heartbeat.HistoricalHeartbeatService;
import com.dianping.cat.report.page.model.heartbeat.LocalHeartbeatService;
import com.dianping.cat.report.page.model.logview.CompositeLogViewService;
import com.dianping.cat.report.page.model.logview.HistoricalMessageService;
import com.dianping.cat.report.page.model.logview.LocalMessageService;
import com.dianping.cat.report.page.model.problem.CompositeProblemService;
import com.dianping.cat.report.page.model.problem.HistoricalProblemService;
import com.dianping.cat.report.page.model.problem.LocalProblemService;
import com.dianping.cat.report.page.model.spi.ModelService;
import com.dianping.cat.report.service.ReportService;
import com.dianping.cat.storage.BucketManager;
import com.dianping.cat.storage.dump.LocalMessageBucketManager;
import com.dianping.cat.storage.dump.MessageBucketManager;

class ServiceComponentConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(ModelService.class, "event-local", LocalEventService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "event-historical", HistoricalEventService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "event", CompositeEventService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "event-historical" }, "m_services"));

		all.add(C(ModelService.class, "problem-local", LocalProblemService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "problem-historical", HistoricalProblemService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "problem", CompositeProblemService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "problem-historical" }, "m_services"));

		all.add(C(ModelService.class, "heartbeat-local", LocalHeartbeatService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "heartbeat-historical", HistoricalHeartbeatService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "heartbeat", CompositeHeartbeatService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "heartbeat-historical" }, "m_services"));

		all.add(C(ModelService.class, "message-local", LocalMessageService.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID) //
		      .req(MessageBucketManager.class, LocalMessageBucketManager.ID) //
		      .req(MessageCodec.class, "html"));
		all.add(C(ModelService.class, "message-historical", HistoricalMessageService.class) //
		      .req(MessageBucketManager.class, LocalMessageBucketManager.ID, "m_localBucketManager") //
		      .req(MessageBucketManager.class, HdfsMessageBucketManager.ID, "m_hdfsBucketManager") //
		      .req(MessageCodec.class, "html"));

		all.add(C(ModelService.class, "logview", CompositeLogViewService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "message-historical", "logview-historical" }, "m_services"));

		return all;
	}
}
