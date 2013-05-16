package com.dianping.cat.consumer.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.initialization.Module;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

import com.dainping.cat.consumer.advanced.dal.BusinessReportDao;
import com.dainping.cat.consumer.advanced.dal.SqltableDao;
import com.dainping.cat.consumer.core.dal.HostinfoDao;
import com.dainping.cat.consumer.core.dal.ProjectDao;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.TaskDao;
import com.dianping.cat.consumer.CatConsumerAdvancedModule;
import com.dianping.cat.consumer.MessageAnalyzer;
import com.dianping.cat.consumer.advanced.CrossAnalyzer;
import com.dianping.cat.consumer.advanced.DatabaseAnalyzer;
import com.dianping.cat.consumer.advanced.MatrixAnalyzer;
import com.dianping.cat.consumer.advanced.MetricAnalyzer;
import com.dianping.cat.consumer.advanced.SqlAnalyzer;
import com.dianping.cat.consumer.advanced.StateAnalyzer;
import com.dianping.cat.consumer.advanced.TopAnalyzer;
import com.dianping.cat.consumer.sql.SqlParseManager;
import com.dianping.cat.status.ServerStateManager;
import com.dianping.cat.storage.BucketManager;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(C(SqlParseManager.class)//
		      .req(SqltableDao.class));

		all.add(C(MessageAnalyzer.class, CrossAnalyzer.ID, CrossAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, ReportDao.class));

		all.add(C(MessageAnalyzer.class, DatabaseAnalyzer.ID, DatabaseAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, ReportDao.class, SqlParseManager.class));

		all.add(C(MessageAnalyzer.class, SqlAnalyzer.ID, SqlAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, ReportDao.class, SqlParseManager.class));

		all.add(C(MessageAnalyzer.class, MatrixAnalyzer.ID, MatrixAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, ReportDao.class));

		all.add(C(MessageAnalyzer.class, MetricAnalyzer.ID, MetricAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, BusinessReportDao.class));
		
		all.add(C(MessageAnalyzer.class, StateAnalyzer.ID, StateAnalyzer.class).is(PER_LOOKUP)//
		      .req(HostinfoDao.class, TaskDao.class, ReportDao.class, ProjectDao.class)//
		      .req(BucketManager.class, ServerStateManager.class));

		all.add(C(MessageAnalyzer.class, TopAnalyzer.ID, TopAnalyzer.class).is(PER_LOOKUP) //
		      .req(BucketManager.class, ReportDao.class));

		all.add(C(Module.class, CatConsumerAdvancedModule.ID, CatConsumerAdvancedModule.class));

		all.addAll(new CatAdvancedDatabaseConfigurator().defineComponents());

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
