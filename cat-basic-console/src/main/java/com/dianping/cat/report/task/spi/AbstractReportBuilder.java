package com.dianping.cat.report.task.spi;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.DailyReportDao;
import com.dainping.cat.consumer.core.dal.DailygraphDao;
import com.dainping.cat.consumer.core.dal.GraphDao;
import com.dainping.cat.consumer.core.dal.MonthlyReportDao;
import com.dainping.cat.consumer.core.dal.Report;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.ReportEntity;
import com.dainping.cat.consumer.core.dal.WeeklyReportDao;
import com.dianping.cat.Cat;

public abstract class AbstractReportBuilder {

	@Inject
	protected DailyReportDao m_dailyReportDao;
	
	@Inject
	protected WeeklyReportDao m_weeklyreportDao;
	
	@Inject
	protected MonthlyReportDao m_monthreportDao;

	@Inject
	protected GraphDao m_graphDao;

	@Inject
	protected ReportDao m_reportDao;

	@Inject
	protected DailygraphDao m_dailygraphDao;
	
	protected Set<String> getDatabasesFromHoulyReport(Date start, Date end) {
		List<Report> databaseNames = new ArrayList<Report>();
		Set<String> result = new HashSet<String>();

		try {
			databaseNames = m_reportDao.findDatabaseAllByDomainNameDuration(start, end, null, "database",
			      ReportEntity.READSET_DOMAIN_NAME);
		} catch (DalException e) {
			Cat.logError(e);
		}
		if (databaseNames != null) {
			for (Report domainName : databaseNames) {
				result.add(domainName.getDomain());
			}
		}
		return result;
	}

	protected Set<String> getDomainsFromHourlyReport(Date start, Date end) {
		List<Report> domainNames = new ArrayList<Report>();
		Set<String> result = new HashSet<String>();
		
		try {
			domainNames = m_reportDao
			      .findAllByDomainNameDuration(start, end, null, null, ReportEntity.READSET_DOMAIN_NAME);
		} catch (DalException e) {
			Cat.logError(e);
		}
		if (domainNames != null) {
			for (Report domainName : domainNames) {
				result.add(domainName.getDomain());
			}
		}
		return result;
	}
}
