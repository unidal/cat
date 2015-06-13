package org.unidal.cat.transaction.report.page;

import org.unidal.cat.nav.NavRegister;
import org.unidal.cat.transaction.report.ReportPage;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

public class Payload implements ActionPayload<ReportPage, Action> {
	private ReportPage m_page;

	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("date")
	private long m_date;

	@FieldMeta("domain")
	private String m_domain;

	@FieldMeta("ip")
	private String m_ipAddress;

	@Override
	public Action getAction() {
		return m_action;
	}

	public long getDate() {
		return m_date;
	}

	public String getDomain() {
		return m_domain;
	}

	public String getIpAddress() {
		return m_ipAddress;
	}

	@Override
	public ReportPage getPage() {
		return m_page;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.VIEW);
	}

	@Override
	public void setPage(String page) {
		m_page = ReportPage.getByName(page, ReportPage.TRANSACTION);
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.VIEW;
		}

		NavRegister nav = ctx.getAttribute("nav");

		m_domain = nav.refreshValue("domain", m_domain);
	}
}
