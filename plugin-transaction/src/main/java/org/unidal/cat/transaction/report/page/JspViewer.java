package org.unidal.cat.transaction.report.page;

import org.unidal.cat.transaction.report.ReportPage;
import org.unidal.web.mvc.view.BaseJspViewer;

public class JspViewer extends BaseJspViewer<ReportPage, Action, Context, Model> {
	@Override
	protected String getJspFilePath(Context ctx, Model model) {
		Action action = model.getAction();

		switch (action) {
		default:
			return JspFile.VIEW.getPath();
		}
	}
}
