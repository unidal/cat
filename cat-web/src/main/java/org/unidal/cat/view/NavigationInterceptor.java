package org.unidal.cat.view;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.cat.plugin.menu.LinkBuilder;
import org.unidal.cat.plugin.menu.MenuBarBuilder;
import org.unidal.cat.plugin.menu.UrlContext;
import org.unidal.cat.web.layout.entity.Group;
import org.unidal.cat.web.layout.entity.Layout;
import org.unidal.cat.web.layout.entity.Menu;
import org.unidal.cat.web.layout.entity.MenuBar;
import org.unidal.cat.web.layout.transform.BaseVisitor2;
import org.unidal.cat.web.layout.transform.DefaultSaxParser;
import org.unidal.helper.Files;
import org.unidal.lookup.ContainerHolder;
import org.unidal.lookup.annotation.Named;
import org.unidal.web.mvc.ActionContext;
import org.unidal.web.mvc.Validator;

@Named(type = Validator.class, value = "nav")
public class NavigationInterceptor extends ContainerHolder implements Validator<ActionContext<?>>, Initializable,
      LogEnabled {

	private Map<String, MenuBarBuilder> m_builders;

	private Logger m_logger;

	private String m_xml;

	private static final String DOMAIN = "domain";

	private static final String DATE = "date";

	private static final String IP = "ip";

	private static final String OP = "op";

	@Override
	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	@Override
	public void initialize() throws InitializationException {
		InputStream in = getClass().getResourceAsStream("/org/unidal/cat/view/layout.xml");

		try {
			m_xml = Files.forIO().readFrom(in, "utf-8");
			m_builders = lookupMap(MenuBarBuilder.class);
		} catch (Exception e) {
			throw new InitializationException("Unable to build layout!", e);
		}
	}

	@Override
	public void validate(ActionContext<?> ctx) throws Exception {
		HttpServletRequest request = ctx.getHttpServletRequest();
		String contextPath = request.getContextPath();
		String url = request.getRequestURI();
		String domain = request.getParameter(DOMAIN);
		String date = request.getParameter(DATE);
		String ip = request.getParameter(IP);
		String op = request.getParameter(OP);

		Map<String, String> pars = new HashMap<String, String>();

		pars.put(DOMAIN, domain);
		pars.put(DATE, date);
		pars.put(IP, ip);
		pars.put(OP, op);

		UrlContext urlCtx = new DefaultUrlContext(contextPath, url, pars);
		Layout m_layout = DefaultSaxParser.parse(m_xml);
		LayoutVisitor visitor = new LayoutVisitor(urlCtx);

		m_layout.accept(visitor);

		if (!visitor.isHasSet()) {
			m_layout.getMenus().get(0).setActive(true);
		}

		ctx.getHttpServletRequest().setAttribute("layout", m_layout);
		
		//string encode
		
		//payload validate
		
		// inbound
		
		// bussniess 
		
		//Navigation receptor
		
		//view
	}

	static class DefaultUrlContext implements UrlContext {

		private Map<String, String> m_pars = new HashMap<String, String>();

		private String m_requestUrl;

		private String m_contextPath;

		private String m_domain;

		private String m_date;

		public DefaultUrlContext(String contextPath, String reqeustUrl, Map<String, String> pars) {
			m_contextPath = contextPath;
			m_pars = pars;
			m_requestUrl = reqeustUrl;
			m_domain = pars.get("domain");
			m_date = pars.get("date");

			if (m_domain == null) {
				m_domain = "cat";
			}
		}

		@Override
		public String buildParams(String prepend, String... keys) {
			StringBuilder sb = new StringBuilder(256);
			
			sb.append(prepend).append('&');

			for (String key : keys) {
				String value = getAttribute(key);

				if (value != null) {
					try {
						sb.append(key).append('=').append(URLEncoder.encode(value, "utf-8")).append('&');
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return sb.toString();
		}

		@Override
		public String getAttribute(String name) {
			return m_pars.get(name);
		}

		@Override
		public Map<String, String> getAttributes() {
			return m_pars;
		}

		@Override
		public String getContextPath() {
			return m_contextPath;
		}

		@Override
		public String getDate() {
			return m_date;
		}

		@Override
		public String getDomain() {
			return m_domain;
		}

		@Override
		public String getRequestUrl() {
			return m_requestUrl;
		}

		@Override
		public String getPluginId() {
			int index = m_requestUrl.lastIndexOf('/');

			return m_requestUrl.substring(index + 1);
		}

		@Override
		public String getPulginOp() {
			return m_pars.get(OP);
		}
	}

	class LayoutVisitor extends BaseVisitor2 {
		private UrlContext m_ctx;

		private boolean m_hasSet;

		public LayoutVisitor(UrlContext ctx) {
			m_ctx = ctx;
		}

		public boolean isHasSet() {
			return m_hasSet;
		}

		@Override
		protected void visitMenuBarChildren(MenuBar menuBar) {
			String pluginId = m_ctx.getPluginId();
			String pluginOp = m_ctx.getPulginOp();
			String key = pluginId + '_' + pluginOp;
			String id = menuBar.getId();
			
			if (id.equalsIgnoreCase(key)) {
				m_hasSet = true;
				menuBar.setActive(true);

				Group group = super.getAncestor(2);
				Menu menu = super.getAncestor(3);

				group.setActive(true);
				menu.setActive(true);
			}
			int index = id.indexOf('_');
			String reportId = id.substring(0, index);
			String linkId = id.substring(index + 1);
			MenuBarBuilder builder = m_builders.get(reportId);

			if (builder != null) {
				LinkBuilder linkBuilder = builder.getBuilders().get(linkId);

				if (linkBuilder != null) {
					menuBar.setUrl(linkBuilder.buildParameters(m_ctx));
				} else {
					m_logger.error("error when find link builder, linkId:" + linkId);
				}
			} else {
				m_logger.error("error when find menu bar builder, builder:" + reportId);
			}
		}
	}
}
