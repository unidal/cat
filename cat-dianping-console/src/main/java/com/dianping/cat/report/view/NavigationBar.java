package com.dianping.cat.report.view;

import org.unidal.web.mvc.Page;

import com.dianping.cat.report.ReportPage;

public class NavigationBar {
	public Page[] getSystemPages() {
		return new Page[] {

		};
	}

	public Page[] getVisiblePages() {
		return new Page[] {

		ReportPage.HOME,

		ReportPage.METRIC,

		ReportPage.TRANSACTION,

		ReportPage.EVENT,

		ReportPage.PROBLEM,

		ReportPage.HEARTBEAT,

		ReportPage.CROSS,

		ReportPage.CACHE,

		ReportPage.SQL,

		ReportPage.DATABASE,

		ReportPage.MATRIX,
		
		ReportPage.HEALTH,

		ReportPage.TOP,
		
		ReportPage.STATE,

		ReportPage.LOGVIEW

		};
	}
}
