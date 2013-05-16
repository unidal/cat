package com.dianping.cat.report.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.heartbeat.model.entity.HeartbeatReport;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.transaction.model.entity.TransactionReport;
import com.dianping.cat.helper.TimeUtil;
import com.dianping.cat.report.service.DailyReportService;
import com.dianping.cat.report.service.HourlyReportService;
import com.dianping.cat.report.service.MonthReportCache;
import com.dianping.cat.report.service.MonthReportService;
import com.dianping.cat.report.service.ReportService;
import com.dianping.cat.report.service.WeeklyReportCache;
import com.dianping.cat.report.service.WeeklyReportService;

public class ReportServiceImpl implements ReportService {
	@Inject
	private HourlyReportService m_hourlyReportService;

	@Inject
	private DailyReportService m_dailyReportService;

	@Inject
	private WeeklyReportService m_weeklyReportService;

	@Inject
	private MonthReportService m_monthReportService;

	@Inject
	private WeeklyReportCache m_weeklyReportCache;

	@Inject
	private MonthReportCache m_monthReportCache;

	public static final int s_hourly = 1;

	public static final int s_daily = 2;

	public static final int s_historyDaily = 3;

	public static final int s_currentWeekly = 4;

	public static final int s_historyWeekly = 5;

	public static final int s_currentMonth = 6;

	public static final int s_historyMonth = 7;

	public static final int s_customer = 8;

	public int getQueryType(Date start, Date end) {
		long endTime = end.getTime();
		long startTime = start.getTime();
		long currentWeek = TimeUtil.getCurrentWeek().getTime();
		long currentMonth = TimeUtil.getCurrentMonth().getTime();
		long duration = endTime - startTime;

		if (duration == TimeUtil.ONE_HOUR) {
			return s_hourly;
		}
		if (duration == TimeUtil.ONE_DAY) {
			return s_daily;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(start);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 7) {
			if (duration == TimeUtil.ONE_DAY * 7) {
				if (startTime == currentWeek) {
					return s_currentWeekly;
				} else {
					return s_historyWeekly;
				}
			}
		}

		cal = Calendar.getInstance();
		cal.setTime(start);
		if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
			cal.setTime(end);
			if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
				if (startTime == currentMonth) {
					return s_currentMonth;
				} else {
					return s_historyMonth;
				}
			}
		}
		return s_customer;
	}

	@Override
	public Set<String> queryAllDatabaseNames(Date start, Date end, String reportName) {
		return m_hourlyReportService.queryAllDatabaseNames(start, end, reportName);
	}

	@Override
	public Set<String> queryAllDomainNames(Date start, Date end, String reportName) {
		return m_hourlyReportService.queryAllDomainNames(start, end, reportName);
	}

	@Override
	public EventReport queryEventReport(String domain, Date start, Date end) {
		int type = getQueryType(start, end);
		EventReport report = null;

		if (type == s_hourly) {
			report = m_hourlyReportService.queryEventReport(domain, start, end);
		} else if (type == s_daily) {
			report = m_dailyReportService.queryEventReport(domain, start, end);
		} else if (type == s_historyDaily) {
			report = m_dailyReportService.queryEventReport(domain, start, end);
		} else if (type == s_historyWeekly) {
			report = m_weeklyReportService.queryEventReport(domain, start);
		} else if (type == s_currentWeekly) {
			report = m_weeklyReportCache.queryEventReport(domain, start);
		} else if (type == s_historyMonth) {
			report = m_monthReportService.queryEventReport(domain, start);
		} else {
			report = m_dailyReportService.queryEventReport(domain, start, end);
		}
		if (report == null) {
			report = new EventReport(domain);
			report.setStartTime(start).setEndTime(end);
		}
		return report;
	}

	@Override
	public HeartbeatReport queryHeartbeatReport(String domain, Date start, Date end) {
		int type = getQueryType(start, end);
		HeartbeatReport report = null;

		if (type == s_hourly) {
			report = m_hourlyReportService.queryHeartbeatReport(domain, start, end);
		} else if (type == s_daily) {
			report = m_dailyReportService.queryHeartbeatReport(domain, start, end);
		} else if (type == s_historyDaily) {
			report = m_dailyReportService.queryHeartbeatReport(domain, start, end);
		} else if (type == s_historyWeekly) {
			report = m_weeklyReportService.queryHeartbeatReport(domain, start);
		} else if (type == s_currentWeekly) {
			report = m_weeklyReportCache.queryHeartbeatReport(domain, start);
		} else if (type == s_historyMonth) {
			report = m_monthReportService.queryHeartbeatReport(domain, start);
		} else if (type == s_currentMonth) {
			report = m_monthReportCache.queryHeartbeatReport(domain, start);
		} else {
			report = m_dailyReportService.queryHeartbeatReport(domain, start, end);
		}
		if (report == null) {
			report = new HeartbeatReport(domain);
			report.setStartTime(start).setEndTime(end);
		}
		return report;
	}

	@Override
	public ProblemReport queryProblemReport(String domain, Date start, Date end) {
		int type = getQueryType(start, end);
		ProblemReport report = null;

		if (type == s_hourly) {
			report = m_hourlyReportService.queryProblemReport(domain, start, end);
		} else if (type == s_daily) {
			report = m_dailyReportService.queryProblemReport(domain, start, end);
		} else if (type == s_historyDaily) {
			report = m_dailyReportService.queryProblemReport(domain, start, end);
		} else if (type == s_historyWeekly) {
			report = m_weeklyReportService.queryProblemReport(domain, start);
		} else if (type == s_currentWeekly) {
			report = m_weeklyReportCache.queryProblemReport(domain, start);
		} else if (type == s_historyMonth) {
			report = m_monthReportService.queryProblemReport(domain, start);
		} else if (type == s_currentMonth) {
			report = m_monthReportCache.queryProblemReport(domain, start);
		} else {
			report = m_dailyReportService.queryProblemReport(domain, start, end);
		}
		if (report == null) {
			report = new ProblemReport(domain);
			report.setStartTime(start).setEndTime(end);
		}
		return report;
	}

	@Override
	public TransactionReport queryTransactionReport(String domain, Date start, Date end) {
		int type = getQueryType(start, end);
		TransactionReport report = null;

		if (type == s_hourly) {
			report = m_hourlyReportService.queryTransactionReport(domain, start, end);
		} else if (type == s_daily) {
			report = m_dailyReportService.queryTransactionReport(domain, start, end);
		} else if (type == s_historyDaily) {
			report = m_dailyReportService.queryTransactionReport(domain, start, end);
		} else if (type == s_historyWeekly) {
			report = m_weeklyReportService.queryTransactionReport(domain, start);
		} else if (type == s_currentWeekly) {
			report = m_weeklyReportCache.queryTransactionReport(domain, start);
		} else if (type == s_historyMonth) {
			report = m_monthReportService.queryTransactionReport(domain, start);
		} else if (type == s_currentMonth) {
			report = m_monthReportCache.queryTransactionReport(domain, start);
		} else {
			report = m_dailyReportService.queryTransactionReport(domain, start, end);
		}
		if (report == null) {
			report = new TransactionReport(domain);
			report.setStartTime(start).setEndTime(end);
		}
		return report;
	}
}
