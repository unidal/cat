package com.dianping.cat.report.service.impl;

import java.util.Date;
import java.util.List;

import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.DailyReport;
import com.dainping.cat.consumer.core.dal.DailyReportDao;
import com.dainping.cat.consumer.core.dal.DailyReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.consumer.event.EventReportMerger;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.HeartbeatReportMerger;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.ProblemReportMerger;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.TransactionReportMerger;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.message.Event;
import com.dianping.cat.report.service.DailyReportService;

public class DailyReportServiceImpl implements DailyReportService {

	@Inject
	private DailyReportDao m_dailyreportDao;

	@Override
	public EventReport queryEventReport(String domain, Date start, Date end) {
		EventReportMerger merger = new EventReportMerger(new EventReport(domain));

		try {
			List<DailyReport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "event",
			      DailyReportEntity.READSET_FULL);

			for (DailyReport report : reports) {
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
		return eventReport;
	}

	@Override
	public HeartbeatReport queryHeartbeatReport(String domain, Date start, Date end) {
		HeartbeatReportMerger merger = new HeartbeatReportMerger(new HeartbeatReport(domain));

		try {
			List<DailyReport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "heartbeat",
			      DailyReportEntity.READSET_FULL);
			for (DailyReport report : reports) {
				String xml = report.getContent();

				try {
					HeartbeatReport reportModel = com.dianping.cat.consumer.heartbeat.model.transform.DefaultSaxParser.parse(xml);
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
		return heartbeatReport;
	}

	@Override
	public ProblemReport queryProblemReport(String domain, Date start, Date end) {
		ProblemReportMerger merger = new ProblemReportMerger(new ProblemReport(domain));

		try {
			List<DailyReport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "problem",
			      DailyReportEntity.READSET_FULL);
			for (DailyReport report : reports) {
				String xml = report.getContent();

				try {
					ProblemReport reportModel = com.dianping.cat.consumer.problem.model.transform.DefaultSaxParser.parse(xml);
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
		return problemReport;
	}

	@Override
	public TransactionReport queryTransactionReport(String domain, Date start, Date end) {
		TransactionReportMerger merger = new TransactionReportMerger(new TransactionReport(domain));

		try {
			List<DailyReport> reports = m_dailyreportDao.findAllByDomainNameDuration(start, end, domain, "transaction",
			      DailyReportEntity.READSET_FULL);

			for (DailyReport report : reports) {
				String xml = report.getContent();

				try {
					TransactionReport reportModel = com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser.parse(xml);
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
		return transactionReport;
	}

}
