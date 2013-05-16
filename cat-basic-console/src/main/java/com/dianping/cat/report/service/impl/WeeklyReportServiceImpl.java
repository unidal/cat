package com.dianping.cat.report.service.impl;

import java.util.Date;

import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.WeeklyReport;
import com.dainping.cat.consumer.core.dal.WeeklyReportDao;
import com.dainping.cat.consumer.core.dal.WeeklyReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.report.service.WeeklyReportService;

public class WeeklyReportServiceImpl implements WeeklyReportService {

	@Inject
	private WeeklyReportDao m_weeklyreportDao;

	@Override
	public EventReport queryEventReport(String domain, Date start) {
		try {
			WeeklyReport entity = m_weeklyreportDao.findReportByDomainNamePeriod(start, domain, "event",
			      WeeklyReportEntity.READSET_FULL);
			String content = entity.getContent();

			return com.dianping.cat.consumer.event.model.transform.DefaultSaxParser.parse(content);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return new EventReport(domain);
	}

	@Override
	public HeartbeatReport queryHeartbeatReport(String domain, Date start) {
		try {
			WeeklyReport entity = m_weeklyreportDao.findReportByDomainNamePeriod(start, domain, "heartbeat",
			      WeeklyReportEntity.READSET_FULL);
			String content = entity.getContent();

			return com.dianping.cat.consumer.heartbeat.model.transform.DefaultSaxParser.parse(content);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return new HeartbeatReport(domain);
	}

	@Override
	public ProblemReport queryProblemReport(String domain, Date start) {
		try {
			WeeklyReport entity = m_weeklyreportDao.findReportByDomainNamePeriod(start, domain, "problem",
			      WeeklyReportEntity.READSET_FULL);
			String content = entity.getContent();

			return com.dianping.cat.consumer.problem.model.transform.DefaultSaxParser.parse(content);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return new ProblemReport(domain);
	}

	@Override
	public TransactionReport queryTransactionReport(String domain, Date start) {
		try {
			WeeklyReport entity = m_weeklyreportDao.findReportByDomainNamePeriod(start, domain, "transaction",
			      WeeklyReportEntity.READSET_FULL);
			String content = entity.getContent();

			return com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser.parse(content);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return new TransactionReport(domain);
	}

}
