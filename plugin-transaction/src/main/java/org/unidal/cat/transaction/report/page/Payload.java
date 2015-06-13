package org.unidal.cat.transaction.report.page;

import java.net.URLEncoder;

import org.unidal.cat.nav.NavStore;
import org.unidal.cat.transaction.report.ReportPage;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.payload.annotation.FieldMeta;

import com.dianping.cat.mvc.AbstractReportPayload;

public class Payload extends AbstractReportPayload<Action, ReportPage> {
	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("name")
	private String m_name;

	@FieldMeta("queryname")
	private String m_queryName;

	@FieldMeta("sort")
	private String m_sortBy;

	@FieldMeta("type")
	private String m_type;

	@FieldMeta("xml")
	private boolean m_xml;

	@FieldMeta("group")
	private String m_group;

	public Payload() {
		super(ReportPage.TRANSACTION);
	}

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getEncodedType() {
		try {
			return URLEncoder.encode(m_type, "utf-8");
		} catch (Exception e) {
			return m_type;
		}
	}

	public String getGroup() {
		return m_group;
	}

	public String getName() {
		return m_name;
	}

	public String getQueryName() {
		return m_queryName;
	}

	public String getSortBy() {
		return m_sortBy;
	}

	public String getType() {
		return m_type;
	}

	public boolean isXml() {
		return m_xml;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.HOURLY_REPORT);
	}

	public void setGroup(String group) {
		m_group = group;
	}

	public void setName(String name) {
		m_name = name;
	}

	@Override
	public void setPage(String page) {
		m_page = ReportPage.getByName(page, ReportPage.TRANSACTION);
	}

	public void setQueryName(String queryName) {
		m_queryName = queryName;
	}

	public void setSortBy(String sortBy) {
		m_sortBy = sortBy;
	}

	public void setType(String type) {
		m_type = type;
	}

	public void setXml(boolean xml) {
		m_xml = xml;
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.HOURLY_REPORT;
		}

		NavStore store = ctx.getAttribute(NavStore.ID);

		m_domain = store.refreshDomain(m_domain);
		m_ipAddress = store.refreshIp(m_ipAddress);
		m_reportType = store.refreshReportType(m_reportType);
		m_date = store.refreshDate(m_date);
	}
}