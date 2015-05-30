package org.unidal.cat.plugin.menu;

import java.util.Map;

public interface MenuBarBuilder {
	public String getId();

	public String getTitle();

	public String getPath();

	public Map<String, LinkBuilder> getBuilders();
}
