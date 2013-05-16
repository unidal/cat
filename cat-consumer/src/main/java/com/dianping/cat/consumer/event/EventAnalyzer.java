package com.dianping.cat.consumer.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.lookup.annotation.Inject;

import com.dianping.cat.consumer.AbstractMessageAnalyzer;
import com.dianping.cat.consumer.event.model.entity.EventName;
import com.dianping.cat.consumer.event.model.entity.EventReport;
import com.dianping.cat.consumer.event.model.entity.EventType;
import com.dianping.cat.consumer.event.model.entity.Range;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.spi.MessageTree;
import com.dianping.cat.report.DefaultReportManager.StoragePolicy;
import com.dianping.cat.report.ReportManager;

public class EventAnalyzer extends AbstractMessageAnalyzer<EventReport> implements LogEnabled {
	public static final String ID = "event";

	@Inject(ID)
	private ReportManager<EventReport> m_reportManager;

	private Map<String, EventReport> m_reports = new HashMap<String, EventReport>();

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
	public EventReport getReport(String domain) {
		return m_reportManager.getHourlyReport(getStartTime(), domain, false);
	}

	@Override
	protected void loadReports() {
		// m_reports = m_reportManager.loadReports(getStartTime());
	}

	@Override
	public void process(MessageTree tree) {
		String domain = tree.getDomain();
		EventReport report = m_reports.get(domain);
		Message message = tree.getMessage();

		if (report == null) {
			report = m_reportManager.getHourlyReport(getStartTime(), domain, true);
			m_reports.put(domain, report);
		}

		if (message instanceof Transaction) {
			processTransaction(report, tree, (Transaction) message);
		} else if (message instanceof Event) {
			processEvent(report, tree, (Event) message);
		}
	}

	private int processEvent(EventReport report, MessageTree tree, Event event) {
		String ip = tree.getIpAddress();
		EventType type = report.findOrCreateMachine(ip).findOrCreateType(event.getType());
		EventName name = type.findOrCreateName(event.getName());
		String messageId = tree.getMessageId();
		int count = 0;

		report.addIp(tree.getIpAddress());
		type.incTotalCount();
		name.incTotalCount();

		if (event.isSuccess()) {
			if (type.getSuccessMessageUrl() == null) {
				type.setSuccessMessageUrl(messageId);
				count++;
			}

			if (name.getSuccessMessageUrl() == null) {
				name.setSuccessMessageUrl(messageId);
				count++;
			}
		} else {
			type.incFailCount();
			name.incFailCount();

			if (type.getFailMessageUrl() == null) {
				type.setFailMessageUrl(messageId);
				count++;
			}

			if (name.getFailMessageUrl() == null) {
				name.setFailMessageUrl(messageId);
				count++;
			}
		}

		processEventGrpah(name, event);

		return count;
	}

	private void processEventGrpah(EventName name, Event t) {
		long current = t.getTimestamp() / 1000 / 60;
		int min = (int) (current % (60));
		int tk = min - min % 5;

		synchronized (name) {
			Range range = name.findOrCreateRange(tk);

			range.incCount();
			if (!t.isSuccess()) {
				range.incFails();
			}
		}
	}

	private int processTransaction(EventReport report, MessageTree tree, Transaction t) {
		int count = 0;
		List<Message> children = t.getChildren();

		for (Message child : children) {
			if (child instanceof Transaction) {
				count += processTransaction(report, tree, (Transaction) child);
			} else if (child instanceof Event) {
				count += processEvent(report, tree, (Event) child);
			}
		}

		return count;
	}
}
