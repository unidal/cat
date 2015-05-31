package org.unidal.cat.matrix.report.page;

import java.io.IOException;

import javax.servlet.ServletException;

import org.unidal.cat.matrix.report.ReportPage;
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
	@InboundActionMeta(name = "matrix")
	public void handleInbound(Context ctx) throws ServletException, IOException {
		// display only, no action here
	}

	@Override
	@OutboundActionMeta(name = "matrix")
	public void handleOutbound(Context ctx) throws ServletException, IOException {
		Model model = new Model(ctx);

		model.setAction(Action.VIEW);
		model.setPage(ReportPage.MATRIX);

		if (!ctx.isProcessStopped()) {
		   m_jspViewer.view(ctx, model);
		}
	}
}
