package com.dianping.cat.report.service;

import java.util.Date;

import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;

public interface MonthReportService {

	public EventReport queryEventReport(String domain, Date start);

	public HeartbeatReport queryHeartbeatReport(String domain, Date start);

	public ProblemReport queryProblemReport(String domain, Date start);

	public TransactionReport queryTransactionReport(String domain, Date start);
}
