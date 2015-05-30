package org.unidal.cat.plugin.menu;

public interface LinkBuilder {
	/**
	 * Build URI parameters after '?'.
	 * 
	 * @param ctx
	 * @return key values pairs.
	 */
	public String buildParameters(UrlContext ctx);
}
