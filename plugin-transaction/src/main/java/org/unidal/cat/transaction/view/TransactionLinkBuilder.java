package org.unidal.cat.transaction.view;

import org.unidal.cat.plugin.menu.LinkBuilder;
import org.unidal.cat.plugin.menu.UrlContext;

public enum TransactionLinkBuilder implements LinkBuilder {
	HOUR {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("op=hour", "domain", "ip", "date");
		}
	},

	HELP {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("op=help", "domain", "ip", "date");
		}
	},

	CONFIG {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("op=config", "domain", "ip", "date");
		}
	};

	public String getId() {
		return name().toLowerCase();
	}

}