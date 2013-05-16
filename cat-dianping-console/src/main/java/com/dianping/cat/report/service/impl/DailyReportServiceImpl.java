package com.dianping.cat.report.service.impl;

import java.util.Date;
import java.util.List;

import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.Cat;
import com.dianping.cat.consumer.cross.model.entity.CrossReport;
import com.dianping.cat.consumer.database.model.entity.DatabaseReport;
import com.dianping.cat.consumer.health.model.entity.HealthReport;
import com.dianping.cat.consumer.matrix.model.entity.MatrixReport;
import com.dianping.cat.consumer.sql.model.entity.SqlReport;
import com.dianping.cat.consumer.state.model.entity.StateReport;
import com.dianping.cat.home.dal.report.Dailyreport;
import com.dianping.cat.home.dal.report.DailyreportDao;
import com.dianping.cat.home.dal.report.DailyreportEntity;
import com.dianping.cat.message.Event;
import com.dianping.cat.report.page.model.cross.CrossReportMerger;
import com.dianping.cat.report.page.model.database.DatabaseReportMerger;
import com.dianping.cat.report.page.model.matrix.MatrixReportMerger;
import com.dianping.cat.report.page.model.sql.SqlReportMerger;
import com.dianping.cat.report.page.model.state.StateReportMerger;
import com.dianping.cat.report.service.DailyReportService;
import com.dianping.cat.report.task.health.HealthReportMerger;

public class DailyReportServiceImpl implements DailyReportService {

	@Inject
	private DailyreportDao m_dailyreportDao;

	@Override
	public CrossReport queryCrossReport(String domain, Date start, Date end) {
		CrossReportMerger merger = new CrossReportMerger(new CrossReport(domain));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "cross",
			      DailyreportEntity.READSET_FULL);
			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					CrossReport reportModel = com.dianping.cat.consumer.cross.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "cross", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		CrossReport crossReport = merger.getCrossReport();
		
		crossReport.setStartTime(start);
		crossReport.setEndTime(end);
		return crossReport;
	}

	@Override
	public DatabaseReport queryDatabaseReport(String database, Date start, Date end) {
		DatabaseReportMerger merger = new DatabaseReportMerger(new DatabaseReport(database));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findDatabaseAllByDomainNameDuration(start, end, database, "database",
			      DailyreportEntity.READSET_FULL);
			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					DatabaseReport reportModel = com.dianping.cat.consumer.database.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "database", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		DatabaseReport databaseReport = merger.getDatabaseReport();
		
		databaseReport.setStartTime(start);
		databaseReport.setEndTime(end);
		return databaseReport;
	}

	@Override
	public HealthReport queryHealthReport(String domain, Date start, Date end) {
		HealthReportMerger merger = new HealthReportMerger(new HealthReport(domain));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "health",
			      DailyreportEntity.READSET_FULL);
			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					HealthReport reportModel = com.dianping.cat.consumer.health.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "health", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		HealthReport healthReport = merger.getHealthReport();
		
		healthReport.setStartTime(start);
		healthReport.setEndTime(end);
		return healthReport;
	}

	@Override
	public MatrixReport queryMatrixReport(String domain, Date start, Date end) {
		MatrixReportMerger merger = new MatrixReportMerger(new MatrixReport(domain));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "matrix",
			      DailyreportEntity.READSET_FULL);
			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					MatrixReport reportModel = com.dianping.cat.consumer.matrix.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "matrix", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		MatrixReport matrixReport = merger.getMatrixReport();
		
		matrixReport.setStartTime(start);
		matrixReport.setEndTime(end);
		return matrixReport;
	}

	@Override
	public SqlReport querySqlReport(String domain, Date start, Date end) {
		SqlReportMerger merger = new SqlReportMerger(new SqlReport(domain));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "sql",
			      DailyreportEntity.READSET_FULL);
			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					SqlReport reportModel = com.dianping.cat.consumer.sql.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "sql", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		SqlReport sqlReport = merger.getSqlReport();
		
		sqlReport.setStartTime(start);
		sqlReport.setEndTime(end);
		return sqlReport;
	}

	@Override
   public StateReport queryStateReport(String domain, Date start, Date end) {
		StateReportMerger merger = new StateReportMerger(new StateReport(domain));

		try {
			List<Dailyreport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "state",
			      DailyreportEntity.READSET_FULL);

			for (Dailyreport report : reports) {
				String xml = report.getContent();

				try {
					StateReport reportModel = com.dianping.cat.consumer.state.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "state", Event.SUCCESS, report.getDomain()+" "+report.getPeriod()+" "+report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		StateReport stateReport = merger.getStateReport();
		
		stateReport.setStartTime(start);
		stateReport.setEndTime(end);
		return stateReport;
   }
}
