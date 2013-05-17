package com.dianping.cat.report.page.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;

import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

import com.dianping.cat.Cat;
import com.dianping.cat.message.internal.MessageId;
import com.dianping.cat.report.ReportConstants;
import com.dianping.cat.report.ReportDelegate;
import com.dianping.cat.report.ReportManager;
import com.dianping.cat.report.ReportPage;
import com.dianping.cat.report.model.ModelPeriod;
import com.dianping.cat.report.model.ModelRequest;
import com.dianping.cat.report.model.ModelResponse;
import com.dianping.cat.report.page.model.logview.LocalMessageService;
import com.dianping.cat.report.page.model.spi.ModelService;

public class Handler extends ContainerHolder implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Inject(type = ModelService.class, value = "message-local")
	private LocalMessageService m_messageService;

	private Map<String, ReportDelegate<Object>> m_delegates = new ConcurrentHashMap<String, ReportDelegate<Object>>();

	private Map<String, ReportManager<Object>> m_managers = new ConcurrentHashMap<String, ReportManager<Object>>();

	private String doFilter(Payload payload, Object report) {
		String name = payload.getReport();

		if ("transaction".equals(name)) {
			ReportDelegate<Object> delegate = lookupDelegate(name);
			Map<String, String> properties = new HashMap<String, String>();

			properties.put(ReportConstants.PROPERTY_IP, payload.getIpAddress());
			properties.put(ReportConstants.PROPERTY_TYPE, payload.getType());
			properties.put(ReportConstants.PROPERTY_NAME, payload.getName());
			properties.put(ReportConstants.PROPERTY_GRAPH, payload.getGraph());
			properties.put(ReportConstants.PROPERTY_PATTERN, payload.getPattern());
			properties.put(ReportConstants.PROPERTY_SORT_BY, payload.getSortBy());

			Object result = delegate.pack(report, properties);
			
			return delegate.buildXml(result);
		} else if ("event".equals(name)) {
			ReportDelegate<Object> delegate = lookupDelegate(name);

			return delegate.buildXml(report, payload.getType(), payload.getName(), payload.getIpAddress());
		} else if ("problem".equals(name)) {
			ReportDelegate<Object> delegate = lookupDelegate(name);

			return delegate.buildXml(report, payload.getIpAddress(), payload.getType());
		} else if ("heartbeat".equals(name)) {
			ReportDelegate<Object> delegate = lookupDelegate(name);

			return delegate.buildXml(report, payload.getIpAddress());
		} else {
			return String.valueOf(report);
		}
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "model")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "model")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);
		Payload payload = ctx.getPayload();

		model.setAction(Action.XML);
		model.setPage(ReportPage.MODEL);

		try {
			String name = payload.getReport();
			String domain = payload.getDomain();
			ModelRequest request = new ModelRequest(domain, payload.getPeriod());

			if ("logview".equals(name)) {
				String messageId = payload.getMessageId();
				MessageId id = MessageId.parse(messageId);

				request.setProperty("messageId", messageId);
				request.setProperty("waterfall", String.valueOf(payload.isWaterfall()));

				if (id.getVersion() == 1) {
					// not supported
				} else {
					ModelResponse<?> response = m_messageService.invoke(request);

					if (response != null) {
						Object dataModel = response.getModel();

						model.setModel(dataModel);
						model.setModelInXml(dataModel == null ? "" : doFilter(payload, dataModel));
					}
				}
			} else {
				try {
					ReportManager<Object> transactionManager = lookupManager(name);
					ModelPeriod period = request.getPeriod();
					Object report;

					if (ReportConstants.ALL.equals(domain)) {
						report = transactionManager.getHourlyReportForAllDomains(period.getStartTime());
					} else {
						report = transactionManager.getHourlyReport(period.getStartTime(), domain, false);
					}

					model.setModel(report);
					model.setModelInXml(report == null ? "" : doFilter(payload, report));
				} catch (Exception e) {
					throw new RuntimeException("Unsupported report: " + name + "!", e);
				}
			}
		} catch (Throwable e) {
			e.printStackTrace(); // TODO remove it
			model.setException(e);
			Cat.logError(e);
		}

		m_jspViewer.view(ctx, model);
	}

	@SuppressWarnings("unchecked")
	private ReportDelegate<Object> lookupDelegate(String name) {
		ReportDelegate<Object> delegate = m_delegates.get(name);

		if (delegate == null) {
			delegate = lookup(ReportDelegate.class, name);
			m_delegates.put(name, delegate);
		}
		return delegate;
	}

	@SuppressWarnings("unchecked")
	private ReportManager<Object> lookupManager(String name) {
		ReportManager<Object> manager = m_managers.get(name);

		if (manager == null) {
			manager = lookup(ReportManager.class, name);
			m_managers.put(name, manager);
		}

		return manager;
	}
}
