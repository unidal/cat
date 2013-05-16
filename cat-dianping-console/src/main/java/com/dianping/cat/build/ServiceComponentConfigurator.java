package com.dianping.cat.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dianping.cat.configuration.ServerConfigManager;
import com.dianping.cat.consumer.RealtimeConsumer;
import com.dianping.cat.message.spi.MessageConsumer;
import com.dianping.cat.report.page.model.cross.CompositeCrossService;
import com.dianping.cat.report.page.model.cross.HistoricalCrossService;
import com.dianping.cat.report.page.model.cross.LocalCrossService;
import com.dianping.cat.report.page.model.database.CompositeDatabaseService;
import com.dianping.cat.report.page.model.database.HistoricalDatabaseService;
import com.dianping.cat.report.page.model.database.LocalDatabaseService;
import com.dianping.cat.report.page.model.matrix.CompositeMatrixService;
import com.dianping.cat.report.page.model.matrix.HistoricalMatrixService;
import com.dianping.cat.report.page.model.matrix.LocalMatrixService;
import com.dianping.cat.report.page.model.metric.CompositeMetricService;
import com.dianping.cat.report.page.model.metric.HistoricalMetricService;
import com.dianping.cat.report.page.model.metric.LocalMetricService;
import com.dianping.cat.report.page.model.spi.ModelService;
import com.dianping.cat.report.page.model.sql.CompositeSqlService;
import com.dianping.cat.report.page.model.sql.HistoricalSqlService;
import com.dianping.cat.report.page.model.sql.LocalSqlService;
import com.dianping.cat.report.page.model.state.CompositeStateService;
import com.dianping.cat.report.page.model.state.HistoricalStateService;
import com.dianping.cat.report.page.model.state.LocalStateService;
import com.dianping.cat.report.page.model.top.CompositeTopService;
import com.dianping.cat.report.page.model.top.HistoricalTopService;
import com.dianping.cat.report.page.model.top.LocalTopService;
import com.dianping.cat.report.service.ReportService;
import com.dianping.cat.storage.BucketManager;

class ServiceComponentConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(ModelService.class, "matrix-local", LocalMatrixService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "matrix-historical", HistoricalMatrixService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "matrix", CompositeMatrixService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "matrix-historical" }, "m_services"));

		all.add(C(ModelService.class, "state-local", LocalStateService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "state-historical", HistoricalStateService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "state", CompositeStateService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "state-historical" }, "m_services"));

		all.add(C(ModelService.class, "cross-local", LocalCrossService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "cross-historical", HistoricalCrossService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "cross", CompositeCrossService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "cross-historical" }, "m_services"));

		all.add(C(ModelService.class, "database-local", LocalDatabaseService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "database-historical", HistoricalDatabaseService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "database", CompositeDatabaseService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "database-historical" }, "m_services"));

		all.add(C(ModelService.class, "sql-local", LocalSqlService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "sql-historical", HistoricalSqlService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "sql", CompositeSqlService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "sql-historical" }, "m_services"));

		all.add(C(ModelService.class, "top-local", LocalTopService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, RealtimeConsumer.ID));
		all.add(C(ModelService.class, "top-historical", HistoricalTopService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "top", CompositeTopService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "top-historical" }, "m_services"));
		
		all.add(C(ModelService.class, "metric-local", LocalMetricService.class) //
		      .req(BucketManager.class) //
		      .req(MessageConsumer.class, "realtime"));
		all.add(C(ModelService.class, "metric-historical", HistoricalMetricService.class) //
		      .req(BucketManager.class, ReportService.class));
		all.add(C(ModelService.class, "metric", CompositeMetricService.class) //
		      .req(ServerConfigManager.class) //
		      .req(ModelService.class, new String[] { "metric-historical" }, "m_services"));

		return all;
	}
}
