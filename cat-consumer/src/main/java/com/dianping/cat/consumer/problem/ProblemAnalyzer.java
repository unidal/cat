package com.dianping.cat.consumer.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.consumer.AbstractMessageAnalyzer;
import com.dianping.cat.consumer.problem.model.entity.Duration;
import com.dianping.cat.consumer.problem.model.entity.Entry;
import com.dianping.cat.consumer.problem.model.entity.Machine;
import com.dianping.cat.consumer.problem.model.entity.ProblemReport;
import com.dianping.cat.consumer.problem.model.transform.BaseVisitor;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.DefaultReportManager.StoragePolicy;
import com.dianping.cat.report.ReportManager;

public class ProblemAnalyzer extends AbstractMessageAnalyzer<ProblemReport> implements LogEnabled, Initializable {
	public static final String ID = "problem";

	@Inject(ID)
	private ReportManager<ProblemReport> m_reportManager;

	@Inject
	private List<ProblemHandler> m_handlers;

	private Map<String, ProblemReport> m_reports = new HashMap<String, ProblemReport>();

	@Override
	public void doCheckpoint(boolean atEnd) {
		if (atEnd && !isLocalMode()) {
			m_reportManager.storeHourlyReports(getStartTime(), StoragePolicy.FILE_AND_DB);
		} else {
			m_reportManager.storeHourlyReports(getStartTime(), StoragePolicy.FILE);
		}
	}

	@Override
	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	@Override
	public ProblemReport getReport(String domain) {
		return m_reportManager.getHourlyReport(getStartTime(), domain, false);
	}

	@Override
	public void initialize() throws InitializationException {
		// to work around a performance issue within plexus
		m_handlers = new ArrayList<ProblemHandler>(m_handlers);
	}

	protected void loadReports() {
	// m_reports = m_reportManager.loadReports(getStartTime());
	}

	@Override
	public void process(MessageTree tree) {
		String domain = tree.getDomain();
		ProblemReport report = m_reports.get(domain);

		if (report == null) {
			report = m_reportManager.getHourlyReport(getStartTime(), domain, true);
			m_reports.put(domain, report);
		}

		report.addIp(tree.getIpAddress());
		Machine machine = report.findOrCreateMachine(tree.getIpAddress());

		for (ProblemHandler handler : m_handlers) {
			handler.handle(machine, tree);
		}
	}


	static class ProblemReportVisitor extends BaseVisitor {
		private ProblemReport m_report;

		private String m_currentDomain;

		private String m_currentType;

		private String m_currentState;

		public ProblemReportVisitor(ProblemReport report) {
			m_report = report;
		}

		protected Entry findOrCreatEntry(Machine machine, String type, String status) {
			List<Entry> entries = machine.getEntries();

			for (Entry entry : entries) {
				if (entry.getType().equals(type) && entry.getStatus().equals(status)) {
					return entry;
				}
			}
			Entry entry = new Entry();

			entry.setStatus(status);
			entry.setType(type);
			entries.add(entry);
			return entry;
		}

		@Override
		public void visitDuration(Duration duration) {
			int value = duration.getValue();
			Machine machine = m_report.findOrCreateMachine(m_currentDomain);
			Entry entry = findOrCreatEntry(machine, m_currentType, m_currentState);
			Duration temp = entry.findOrCreateDuration(value);

			temp.setCount(temp.getCount() + duration.getCount());
		}

		@Override
		public void visitEntry(Entry entry) {
			m_currentType = entry.getType();
			m_currentState = entry.getStatus();
			super.visitEntry(entry);
		}

		@Override
		public void visitProblemReport(ProblemReport problemReport) {
			m_currentDomain = problemReport.getDomain();
			super.visitProblemReport(problemReport);
		}
	}
}
