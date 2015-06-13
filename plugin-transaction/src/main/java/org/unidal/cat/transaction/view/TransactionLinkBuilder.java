package org.unidal.cat.transaction.view;

import org.unidal.cat.plugin.menu.LinkBuilder;
import org.unidal.cat.plugin.menu.UrlContext;

public enum TransactionLinkBuilder implements LinkBuilder {
	HOUR {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("transaction?op=view");
		}
	},

	HELP {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("transaction?op=help");
		}
	},

	CONFIG {
		@Override
		public String buildParameters(UrlContext ctx) {
			return ctx.buildParams("transaction?op=config");
		}
	};

	public String getId() {
		return name().toLowerCase();
	}

}