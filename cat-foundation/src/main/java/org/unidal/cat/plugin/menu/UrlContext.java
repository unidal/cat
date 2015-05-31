package org.unidal.cat.plugin.menu;

import java.util.Map;

public interface UrlContext {
	public String getPluginId();
	
	public String getPulginOp();
	
	public String getRequestUrl();
	
	public String getContextPath();

	public String getDomain();

	public String getDate();

	public Map<String, String> getAttributes();

	public String getAttribute(String name);

	public String buildParams(String prepend, String... keys);

}
