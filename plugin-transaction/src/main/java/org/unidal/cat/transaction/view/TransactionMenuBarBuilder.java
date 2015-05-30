package org.unidal.cat.transaction.view;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.cat.plugin.menu.MenuBarBuilder;
import org.unidal.cat.plugin.menu.LinkBuilder;
import org.unidal.cat.transaction.TransactionConstant;
import org.unidal.lookup.annotation.Named;

@Named(type = MenuBarBuilder.class, value = TransactionConstant.ID)
public class TransactionMenuBarBuilder implements MenuBarBuilder, Initializable {
	private Map<String, LinkBuilder> m_builders = new HashMap<String, LinkBuilder>();

	@Override
	public Map<String, LinkBuilder> getBuilders() {
		return m_builders;
	}

	@Override
	public String getId() {
		return TransactionConstant.ID;
	}

	@Override
	public String getPath() {
		return TransactionConstant.REPORT + "/" + TransactionConstant.ID;
	}

	@Override
	public String getTitle() {
		return TransactionConstant.ID;
	}

	@Override
	public void initialize() throws InitializationException {
		for (TransactionLinkBuilder builder : TransactionLinkBuilder.values()) {
			m_builders.put(builder.getId(), builder);
		}
	}
}
