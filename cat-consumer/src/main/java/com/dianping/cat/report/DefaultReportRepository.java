package com.dianping.cat.report;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;
import org.unidal.tuple.Pair;

import com.dainping.cat.consumer.core.dal.DailyReport;
import com.dainping.cat.consumer.core.dal.DailyReportDao;
import com.dainping.cat.consumer.core.dal.DailyReportEntity;
import com.dainping.cat.consumer.core.dal.MonthlyReport;
import com.dainping.cat.consumer.core.dal.MonthlyReportDao;
import com.dainping.cat.consumer.core.dal.MonthlyReportEntity;
import com.dainping.cat.consumer.core.dal.Report;
import com.dainping.cat.consumer.core.dal.ReportDao;
import com.dainping.cat.consumer.core.dal.ReportEntity;
import com.dainping.cat.consumer.core.dal.WeeklyReport;
import com.dainping.cat.consumer.core.dal.WeeklyReportDao;
import com.dainping.cat.consumer.core.dal.WeeklyReportEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.configuration.ServerConfigManager;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.report.RemoteModelService.HttpServiceCallback;
import com.dianping.cat.report.model.ModelRequest;

/**
 * Report service to get timed (hourly, daily, weekly, monthly etc.) reports from various medias (memory, database etc.).
 */
public class DefaultReportRepository<T> extends ContainerHolder implements ReportRepository<T>, Initializable {
	@Inject
	private ServerConfigManager m_configManager;

	@Inject
	private ReportDao m_hourlyReportDao;

	@Inject
	private DailyReportDao m_dailyReportDao;

	@Inject
	private WeeklyReportDao m_weeklyReportDao;

	@Inject
	private MonthlyReportDao m_monthlyReportDao;

	@Inject
	private RemoteModelService m_hourlyService;

	private Map<String, ReportDelegate<T>> m_delegates = new ConcurrentHashMap<String, ReportDelegate<T>>();

	private List<Pair<String, Integer>> m_endpoints;

	protected long getDuration(long startTime, int field) {
		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(startTime);
		cal.add(field, 1);

		return cal.getTimeInMillis() - startTime - 1;
	}

	@Override
	public void initialize() throws InitializationException {
		m_endpoints = m_configManager.getConsoleEndpoints();
	}

	protected T loadDailyReportFromDatabase(ModelRequest request) {
		String domain = request.getDomain();
		long startTime = request.getStartTime();
		String name = request.getReportName();
		ReportDelegate<T> delegate = lookupReportDelegate(name);
		T result = delegate.makeReport(domain, startTime, ReportConstants.DAY);

		try {
			List<DailyReport> reports = m_dailyReportDao.findAllByPeriodDomainName(new Date(startTime), domain, name,
			      DailyReportEntity.READSET_FULL);

			for (DailyReport report : reports) {
				try {
					String xml = report.getContent();
					T model = delegate.parseXml(xml);

					result = delegate.mergeReport(result, model);
				} catch (Exception e) {
					Cat.logError(e);
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}

		return result;
	}

	protected T loadHourlyReportFromRemote(final ModelRequest request) {
		String domain = request.getDomain();
		long startTime = request.getStartTime();
		String name = request.getReportName();
		final ReportDelegate<T> delegate = lookupReportDelegate(name);
		final T result = delegate.makeReport(domain, startTime, ReportConstants.HOUR);
		final Semaphore semaphore = new Semaphore(0);
		final Transaction t = Cat.getProducer().newTransaction("ModelService", name);
		int count = 0;

		t.setStatus(Message.SUCCESS);
		t.addData("domain", domain);

		try {
			for (Pair<String, Integer> endpoint : m_endpoints) {
				m_hourlyService.invoke(endpoint, t, name, request, new HttpServiceCallback() {
					@Override
					public void onComplete(String content) {
						try {
							T model = delegate.parseXml(content);

							delegate.mergeReport(result, model);
						} catch (Exception e) {
							Cat.logError(e);
						} finally {
							semaphore.release();
						}
					}

					@Override
					public void onException(Exception e, boolean timeout) {
						semaphore.release();
						Cat.logError(e);
					}
				});
				count++;
			}

			semaphore.tryAcquire(count, 5000, TimeUnit.MILLISECONDS); // 5 seconds timeout
		} catch (Throwable e) {
			t.setStatus(e);
			Cat.logError(e);
		} finally {
			t.complete();
		}

		return result;
	}

	protected T loadHouylyReportFromDatabase(ModelRequest request) {
		String domain = request.getDomain();
		long startTime = request.getStartTime();
		String name = request.getReportName();
		ReportDelegate<T> delegate = lookupReportDelegate(name);
		T result = delegate.makeReport(domain, startTime, ReportConstants.HOUR);

		try {
			List<Report> reports = m_hourlyReportDao.findAllByPeriodDomainName(new Date(startTime), domain, name,
			      ReportEntity.READSET_CONTENT);

			for (Report report : reports) {
				try {
					String xml = report.getContent();
					T model = delegate.parseXml(xml);

					result = delegate.mergeReport(result, model);
				} catch (Exception e) {
					Cat.logError(e);
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}

		return result;
	}

	protected T loadMonthlyReportFromDatabase(ModelRequest request) {
		String domain = request.getDomain();
		long startTime = request.getStartTime();
		String name = request.getReportName();
		ReportDelegate<T> delegate = lookupReportDelegate(name);
		T result = delegate.makeReport(domain, startTime, getDuration(startTime, Calendar.MONTH));

		try {
			List<MonthlyReport> reports = m_monthlyReportDao.findAllByPeriodDomainName(new Date(startTime), domain, name,
			      MonthlyReportEntity.READSET_FULL);

			for (MonthlyReport report : reports) {
				try {
					String xml = report.getContent();
					T model = delegate.parseXml(xml);

					result = delegate.mergeReport(result, model);
				} catch (Exception e) {
					Cat.logError(e);
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}

		return result;
	}

	protected T loadWeeklyReportFromDatabase(ModelRequest request) {
		String domain = request.getDomain();
		long startTime = request.getStartTime();
		String name = request.getReportName();
		ReportDelegate<T> delegate = lookupReportDelegate(name);
		T result = delegate.makeReport(domain, startTime, ReportConstants.WEEK);

		try {
			List<WeeklyReport> reports = m_weeklyReportDao.findAllByPeriodDomainName(new Date(startTime), domain, name,
			      WeeklyReportEntity.READSET_FULL);

			for (WeeklyReport report : reports) {
				try {
					String xml = report.getContent();
					T model = delegate.parseXml(xml);

					result = delegate.mergeReport(result, model);
				} catch (Exception e) {
					Cat.logError(e);
				}
			}
		} catch (Exception e) {
			Cat.logError(e);
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	protected ReportDelegate<T> lookupReportDelegate(String name) {
		ReportDelegate<T> delegate = m_delegates.get(name);

		if (delegate == null) {
			delegate = lookup(ReportDelegate.class, name);
			m_delegates.put(name, delegate);
		}

		return delegate;
	}

	@Override
	public T queryDailyReport(ModelRequest request) {
		T report = loadDailyReportFromDatabase(request);
		ReportDelegate<T> delegate = lookupReportDelegate(request.getReportName());

		report = delegate.pack(report, request.getProperties());
		return report;
	}

	@Override
	public T queryHourlyReport(ModelRequest request) {
		T report = null;

		switch (request.getPeriod()) {
		case CURRENT:
		case LAST:
			report = loadHourlyReportFromRemote(request);
			break;
		case HISTORICAL:
			if (m_configManager.isLocalMode()) {
				report = loadHourlyReportFromRemote(request);
			} else {
				report = loadHouylyReportFromDatabase(request);
			}
			break;
		default:
			throw new UnsupportedOperationException(String.format("Not future report available for %s!", request.getPeriod()));
		}

		ReportDelegate<T> delegate = lookupReportDelegate(request.getReportName());

		report = delegate.pack(report, request.getProperties());
		return report;
	}

	@Override
	public T queryMonthlyReport(ModelRequest request) {
		T report = loadMonthlyReportFromDatabase(request);
		ReportDelegate<T> delegate = lookupReportDelegate(request.getReportName());

		report = delegate.pack(report, request.getProperties());
		return report;
	}

	@Override
	public T queryWeeklyReport(ModelRequest request) {
		T report = loadWeeklyReportFromDatabase(request);
		ReportDelegate<T> delegate = lookupReportDelegate(request.getReportName());

		report = delegate.pack(report, request.getProperties());
		return report;
	}
}
