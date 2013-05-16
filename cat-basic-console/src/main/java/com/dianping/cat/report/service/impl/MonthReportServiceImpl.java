package com.dianping.cat.report.service.impl;

import java.util.Date;

import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.MonthlyReport;
import com.dainping.cat.consumer.core.dal.MonthlyReportDao;
import com.dainping.cat.consumer.core.dal.MonthlyReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.report.service.MonthReportService;

public class MonthReportServiceImpl implements MonthReportService {

	@Inject
	private MonthlyReportDao m_monthreportDao;

	@Override
	public EventReport queryEventReport(String domain, Date start) {
		try {
			MonthlyReport entity = m_monthreportDao.findReportByDomainNamePeriod(start, domain, "event", MonthlyReportEntity.READSET_FULL);
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
			MonthlyReport entity = m_monthreportDao.findReportByDomainNamePeriod(start, domain, "heartbeat",
			      MonthlyReportEntity.READSET_FULL);
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
			MonthlyReport entity = m_monthreportDao.findReportByDomainNamePeriod(start, domain, "problem",
			      MonthlyReportEntity.READSET_FULL);
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
			MonthlyReport entity = m_monthreportDao.findReportByDomainNamePeriod(start, domain, "transaction",
			      MonthlyReportEntity.READSET_FULL);
			String content = entity.getContent();

			return com.dianping.cat.consumer.transaction.model.transform.DefaultSaxParser.parse(content);
		} catch (Exception e) {
			Cat.logError(e);
		}
		return new TransactionReport(domain);
	}

}
