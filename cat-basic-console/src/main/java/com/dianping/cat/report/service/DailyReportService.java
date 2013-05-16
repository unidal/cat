package com.dianping.cat.report.service;

import java.util.Date;

import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;

public interface DailyReportService {

	public EventReport queryEventReport(String domain, Date start, Date end);

	public HeartbeatReport queryHeartbeatReport(String domain, Date start, Date end);

	public ProblemReport queryProblemReport(String domain, Date start, Date end);

	public TransactionReport queryTransactionReport(String domain, Date start, Date end);
}
