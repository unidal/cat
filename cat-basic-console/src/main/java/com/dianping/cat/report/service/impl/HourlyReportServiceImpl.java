package com.dianping.cat.report.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.Report;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.ReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.consumer.event.EventReportMerger;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.HeartbeatReportMerger;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.ProblemReportMerger;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.helper.TimeUtil;
import com.dianping.cat.message.Event;
import com.dianping.cat.report.service.HourlyReportService;

public class HourlyReportServiceImpl implements HourlyReportService {
	@Inject
	private ReportDao m_reportDao;
	
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
	public EventReport queryEventReport(String domain, Date start, Date end) {
		EventReportMerger merger = new EventReportMerger(new EventReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "event",
			      ReportEntity.READSET_FULL);

			for (Report report : reports) {
				String xml = report.getContent();

				try {
					EventReport reportModel = com.dianping.cat.consumer.event.model.transform.DefaultSaxParser.parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "event", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		EventReport eventReport = merger.getEventReport();

		eventReport.setStartTime(start);
		eventReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "event");
		eventReport.getDomainNames().addAll(domains);
		return eventReport;
	}

	@Override
	public HeartbeatReport queryHeartbeatReport(String domain, Date start, Date end) {
		HeartbeatReportMerger merger = new HeartbeatReportMerger(new HeartbeatReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "heartbeat",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					HeartbeatReport reportModel = com.dianping.cat.consumer.heartbeat.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "heartbeat", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		HeartbeatReport heartbeatReport = merger.getHeartbeatReport();

		heartbeatReport.setStartTime(start);
		heartbeatReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "heartbeat");
		heartbeatReport.getDomainNames().addAll(domains);
		return heartbeatReport;
	}

	@Override
	public ProblemReport queryProblemReport(String domain, Date start, Date end) {
		ProblemReportMerger merger = new ProblemReportMerger(new ProblemReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "problem",
			      ReportEntity.READSET_FULL);
			for (Report report : reports) {
				String xml = report.getContent();

				try {
					ProblemReport reportModel = com.dianping.cat.consumer.problem.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "problem", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		ProblemReport problemReport = merger.getProblemReport();

		problemReport.setStartTime(start);
		problemReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "problem");
		problemReport.getDomainNames().addAll(domains);
		return problemReport;
	}

	@Override
	public TransactionReport queryTransactionReport(String domain, Date start, Date end) {
		TransactionReportMerger merger = new TransactionReportMerger(new TransactionReport(domain));

		try {
			List<Report> reports = m_reportDao.findAllByDomainNameDuration(start, end, domain, "transaction",
			      ReportEntity.READSET_FULL);

			for (Report report : reports) {
				String xml = report.getContent();

				try {
					TransactionReport reportModel = com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser
					      .parse(xml);
					reportModel.accept(merger);
				} catch (Exception e) {
					Cat.logError(e);
					Cat.getProducer().logEvent("ErrorXML", "transaction", Event.SUCCESS,
					      report.getDomain() + " " + report.getPeriod() + " " + report.getId());
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}
		TransactionReport transactionReport = merger.getTransactionReport();

		transactionReport.setStartTime(start);
		transactionReport.setEndTime(end);

		Set<String> domains = queryAllDomainNames(start, end, "transaction");
		transactionReport.getDomainNames().addAll(domains);
		return transactionReport;
	}

}
