package com.dianping.cat.report.page.model;

import java.io.IOException;

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

	@SuppressWarnings("unchecked")
	private String doFilter(Payload payload, Object dataModel) {
		String name = payload.getReport();

		if ("transaction".equals(name)) {
			ReportDelegate<Object> delegate = lookup(ReportDelegate.class, name);

			return delegate.buildXml(dataModel, payload.getType(), payload.getName(), payload.getIpAddress());
		} else if ("event".equals(name)) {
			ReportDelegate<Object> delegate = lookup(ReportDelegate.class, name);

			return delegate.buildXml(dataModel, payload.getType(), payload.getName(), payload.getIpAddress());
		} else if ("problem".equals(name)) {
			ReportDelegate<Object> delegate = lookup(ReportDelegate.class, name);

			return delegate.buildXml(dataModel, payload.getIpAddress(), payload.getType());
		} else if ("heartbeat".equals(name)) {
			ReportDelegate<Object> delegate = lookup(ReportDelegate.class, name);

			return delegate.buildXml(dataModel, payload.getIpAddress());
		} else {
			return String.valueOf(dataModel);
		}
	}

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "model")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@SuppressWarnings("unchecked")
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
					ReportManager<Object> m_transactionManager = lookup(ReportManager.class, name);
					ModelPeriod period = request.getPeriod();
					Object report;

					if (ReportConstants.ALL.equals(domain)) {
						report = m_transactionManager.getHourlyReportForAllDomains(period.getStartTime());
					} else {
						report = m_transactionManager.getHourlyReport(period.getStartTime(), domain, false);
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
}
