package com.dianping.cat.report.page.transaction;

public enum JspFile {
	HOURLY_REPORT("/jsp/report/transaction.jsp"),

	HOURLY_GRAPHS("/jsp/report/transactionGraphs.jsp"),

	HISTORY_REPORT("/jsp/report/transactionHistory.jsp"),

	HISTORY_GRAPHS("/jsp/report/transactionHistoryGraphs.jsp"),

	;

	private String m_path;

	private JspFile(String path) {
		m_path = path;
	}

	public String getPath() {
		return m_path;
	}
}
