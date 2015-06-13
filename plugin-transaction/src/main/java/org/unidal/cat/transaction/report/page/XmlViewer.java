package org.unidal.cat.transaction.report.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.unidal.web.mvc.view.Viewer;

import org.unidal.cat.transaction.report.ReportPage;
import org.unidal.cat.transaction.model.entity.TransactionReport;

public class XmlViewer implements Viewer<ReportPage, Action, Context, Model> {
	@Override
	public void view(Context ctx, Model model) throws ServletException, IOException {
		TransactionReport report = model.getReport();
		HttpServletResponse res = ctx.getHttpServletResponse();

		if (report != null) {
			ServletOutputStream out = res.getOutputStream();

			res.setContentType("text/xml");
			out.print(report.toString());
		} else {
			res.sendError(404, "Not found!");
		}
	}
}
