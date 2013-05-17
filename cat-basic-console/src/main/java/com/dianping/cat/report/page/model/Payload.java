package com.dianping.cat.report.page.model;

import java.util.Arrays;

import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.ActionPayload;
import org.unidal.web.mvc.payload.annotation.FieldMeta;
import org.unidal.web.mvc.payload.annotation.PathMeta;

import com.dianping.cat.report.ReportPage;
import com.dianping.cat.report.model.ModelPeriod;

public class Payload implements ActionPayload<ReportPage, Action> {
	@FieldMeta("op")
	private Action m_action;

	@FieldMeta("ip")
	private String m_ipAddress;

	@FieldMeta("type")
	private String m_type;

	@FieldMeta("name")
	private String m_name;

	@FieldMeta("graph")
	private String m_graph;
	
	@FieldMeta("pattern")
	private String m_pattern;
	
	@FieldMeta("sort")
	private String m_sortBy;

	@FieldMeta("messageId")
	private String m_messageId;

	@FieldMeta("waterfall")
	private boolean m_waterfall;

	private ReportPage m_page;

	// /<report>/<domain>/<period>
	@PathMeta("path")
	private String[] m_path;

	@FieldMeta("thread")
	private String m_threadId;

	@FieldMeta("database")
	private String m_database;

	@FieldMeta("channel")
	private String m_channel;

	@Override
	public Action getAction() {
		return m_action;
	}

	public String getChannel() {
		return m_channel;
	}

	public String getDatabase() {
		return m_database;
	}

	public String getDomain() {
		if (m_path.length > 1) {
			return m_path[1];
		} else {
			return null;
		}
	}

	public String getGraph() {
   	return m_graph;
   }

	public String getIpAddress() {
		return m_ipAddress;
	}

	public String getMessageId() {
		return m_messageId;
	}

	public String getName() {
		return m_name;
	}

	@Override
	public ReportPage getPage() {
		return m_page;
	}

	public String getPattern() {
	   return m_pattern;
   }

	public ModelPeriod getPeriod() {
		if (m_path.length > 2) {
			return ModelPeriod.getByName(m_path[2], ModelPeriod.CURRENT);
		} else {
			return ModelPeriod.CURRENT;
		}
	}

	public String getReport() {
		if (m_path.length > 0) {
			return m_path[0];
		} else {
			return null;
		}
	}

	public String getSortBy() {
   	return m_sortBy;
   }

	public String getThreadId() {
		return m_threadId;
	}

	public String getType() {
		return m_type;
	}

	public boolean isWaterfall() {
		return m_waterfall;
	}

	public void setAction(String action) {
		m_action = Action.getByName(action, Action.XML);
	}

	@Override
	public void setPage(String page) {
		m_page = ReportPage.getByName(page, ReportPage.MODEL);
	}

	public void setPath(String[] path) {
		if (path == null) {
			m_path = new String[0];
		} else {
			m_path = Arrays.copyOf(path, path.length);
		}
	}

	@Override
	public void validate(ActionContext<?> ctx) {
		if (m_action == null) {
			m_action = Action.XML;
		}
	}
}
