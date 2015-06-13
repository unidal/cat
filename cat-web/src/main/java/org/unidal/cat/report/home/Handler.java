package org.unidal.cat.report.home;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;

import org.unidal.cat.report.ReportPage;
import org.unidal.lookup.annotation.Inject;
import org.unidal.web.mvc.PageHandler;
import org.unidal.web.mvc.annotation.InboundActionMeta;
import org.unidal.web.mvc.annotation.OutboundActionMeta;
import org.unidal.web.mvc.annotation.PayloadMeta;

public class Handler implements PageHandler<Context> {
	@Inject
	private JspViewer m_jspViewer;

	@Override
	@PayloadMeta(Payload.class)
	@InboundActionMeta(name = "home")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "home")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(ReportPage.HOME);
		
		if (!ctx.isProcessStopped()) {
			try {
				m_jspViewer.view(ctx, model);
			} catch (Exception e) {
				ctx.addError("view.error", e);
				
				StringWriter sw = new StringWriter(2048);
				e.printStackTrace(new PrintWriter(sw));
				ctx.getHttpServletResponse().getWriter().write(sw.toString());
			}
		}
	}
}
