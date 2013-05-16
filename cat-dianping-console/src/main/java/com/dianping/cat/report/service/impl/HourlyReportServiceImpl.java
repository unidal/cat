package com.dianping.cat.report.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.advanced.dal.BusinessReport;
import com.dainping.cat.consumer.advanced.dal.BusinessReportDao;
import com.dainping.cat.consumer.advanced.dal.BusinessReportEntity;
import com.dainping.cat.consumer.core.dal.Report;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.ReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.consumer.cross.model.entity.CrossReport;
import com.dianping.cat.consumer.database.model.entity.DatabaseReport;
import com.dianping.cat.consumer.health.model.entity.HealthReport;
import com.dianping.cat.consumer.matrix.model.entity.MatrixReport;
import com.dianping.cat.consumer.metric.model.entity.MetricReport;
import com.dianping.cat.consumer.metric.model.transform.DefaultNativeParser;
import com.dianping.cat.consumer.sql.model.entity.SqlReport;
import com.dianping.cat.consumer.state.model.entity.StateReport;
import com.dianping.cat.consumer.top.model.entity.TopReport;
import com.dianping.cat.helper.TimeUtil;
import com.dianping.cat.message.Event;
import com.dianping.cat.report.page.model.cross.CrossReportMerger;
import com.dianping.cat.report.page.model.database.DatabaseReportMerger;
import com.dianping.cat.report.page.model.matrix.MatrixReportMerger;
import com.dianping.cat.report.page.model.metric.MetricReportMerger;
import com.dianping.cat.report.page.model.sql.SqlReportMerger;
import com.dianping.cat.report.page.model.state.StateReportMerger;
import com.dianping.cat.report.page.model.top.TopReportMerger;
import com.dianping.cat.report.service.HourlyReportService;
import com.dianping.cat.report.task.health.HealthReportMerger;

public class HourlyReportServiceImpl implements HourlyReportService {

	@Inject
	private ReportDao m_reportDao;
	
	@Inject
	private BusinessReportDao m_businessReportDao;

	@Override
	public Set<String> queryAllDatabaseNames(Date start, Date end, String reportName) {
		if (end.getTime() == start.getTime()) {
			start = new Date(start.getTime() - TimeUtil.ONE_HOUR);
		}
		Set<String> domains = new HashSet<String>();

		try {
			List<Report> reports = m_reportDao.findDatabaseAllByDomainNameDuration(start, end, null, reportName,
			      ReportEntity.READSET_DOMAIN_NAME);

			for (Report report : reports) {
				domains.add(report.getDomain());
			}
		} catch (DalException e) {
			Cat.logError(e);
		}
		return domains;
	}

	@Override
	public Set<String> queryAllDomainNames(Date start, Date end, String reportName) {
		if (end.getTime() == start.getTime()) {
			start = new Date(start.getTime() - TimeUtil.ONE_HOUR);
		}
		Set<String> domains = new HashSet<String>();

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, null, reportName,
			      ReportEntity.READSET_DOMAIN_NAME);

			for (Report report : reports) {
				domains.add(report.getDomain());
			}
		} catch (DalException e) {
			Cat.logError(e);
		}
		return domains;
	}

	@Override
	public CrossReport queryCrossReport(String domain, Date start, Date end) {
		CrossReportMerger merger = new CrossReportMerger(new CrossReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "cross",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					CrossReport reportModel = com.dianping.cat.consumer.cross.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "cross", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		CrossReport crossReport = merger.getCrossReport();

		crossReport.setStartTime(start);
		crossReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "cross");
		crossReport.getDomainNames().addAll(domains);
		return crossReport;
	}

	@Override
	public DatabaseReport queryDatabaseReport(String database, Date start, Date end) {
		DatabaseReportMerger merger = new DatabaseReportMerger(new DatabaseReport(database));

		try {
			List<Report> reports = m_reportDao.findDatabaseAllByDomainNameDuration(start, end, database, "database",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					DatabaseReport reportModel = com.dianping.cat.consumer.database.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "database", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		DatabaseReport databaseReport = merger.getDatabaseReport();

		databaseReport.setStartTime(start);
		databaseReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "database");
		databaseReport.getDomainNames().addAll(domains);
		return databaseReport;
	}

	@Override
	public HealthReport queryHealthReport(String domain, Date start, Date end) {
		HealthReportMerger merger = new HealthReportMerger(new HealthReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "health",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					HealthReport reportModel = com.dianping.cat.consumer.health.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "health", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		HealthReport healthReport = merger.getHealthReport();

		healthReport.setStartTime(start);
		healthReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "health");
		healthReport.getDomainNames().addAll(domains);
		return healthReport;
	}

	@Override
	public MatrixReport queryMatrixReport(String domain, Date start, Date end) {
		MatrixReportMerger merger = new MatrixReportMerger(new MatrixReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "matrix",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					MatrixReport reportModel = com.dianping.cat.consumer.matrix.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "matrix", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		MatrixReport matrixReport = merger.getMatrixReport();

		matrixReport.setStartTime(start);
		matrixReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "matrix");
		matrixReport.getDomainNames().addAll(domains);
		return matrixReport;
	}

	@Override
	public MetricReport queryMetricReport(String group, Date start, Date end) {
		MetricReportMerger merger = new MetricReportMerger(new MetricReport(group));

		try {
			List<BusinessReport> reports = m_businessReportDao.findAllByProductLineNameDuration(start, end, group, "metric",
			      BusinessReportEntity.READSET_FULL);

			for (BusinessReport report : reports) {
				byte[] content = report.getContent();

				try {
					MetricReport reportModel = DefaultNativeParser.parse(content);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "metric", Event.SUCCESS,
					      report.getProductLine() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		MetricReport metricReport = merger.getMetricReport();

		metricReport.setStartTime(start);
		metricReport.setEndTime(end);
		return metricReport;
	}

	@Override
	public SqlReport querySqlReport(String domain, Date start, Date end) {
		SqlReportMerger merger = new SqlReportMerger(new SqlReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "sql",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					SqlReport reportModel = com.dianping.cat.consumer.sql.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "sql", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		SqlReport sqlReport = merger.getSqlReport();

		sqlReport.setStartTime(start);
		sqlReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "sql");
		sqlReport.getDomainNames().addAll(domains);
		return sqlReport;
	}

	@Override
	public StateReport queryStateReport(String domain, Date start, Date end) {
		StateReportMerger merger = new StateReportMerger(new StateReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "state",
			      ReportEntity.READSET_FULL);

			for (Report report : reports) {
				String xml = report.getContent();

				try {
					StateReport reportModel = com.dianping.cat.consumer.state.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "state", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
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

	@Override
	public TopReport queryTopReport(String domain, Date start, Date end) {
		TopReportMerger merger = new TopReportMerger(new TopReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "top",
			      ReportEntity.READSET_FULL);

			for (Report report : reports) {
				String xml = report.getContent();

				try {
					TopReport reportModel = com.dianping.cat.consumer.top.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "top", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		TopReport topReport = merger.getTopReport();

		topReport.setStartTime(start);
		topReport.setEndTime(end);
		return topReport;
	}
}
