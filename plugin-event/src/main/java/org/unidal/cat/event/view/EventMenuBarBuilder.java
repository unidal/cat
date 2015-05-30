package org.unidal.cat.event.view;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.cat.event.EventConstant;
import org.unidal.cat.plugin.menu.LinkBuilder;
import org.unidal.cat.plugin.menu.MenuBarBuilder;
import org.unidal.lookup.annotation.Named;

@Named(type = MenuBarBuilder.class, value = EventConstant.ID)
public class EventMenuBarBuilder implements MenuBarBuilder, Initializable {
	private Map<String, LinkBuilder> m_builders = new HashMap<String, LinkBuilder>();

	@Override
	public Map<String, LinkBuilder> getBuilders() {
		return m_builders;
	}

	@Override
	public String getId() {
		return EventConstant.ID;
	}

	@Override
	public String getPath() {
		return EventConstant.REPORT + "/" + EventConstant.ID;
	}

	@Override
	public String getTitle() {
		return EventConstant.ID;
	}

	@Override
	public void initialize() throws InitializationException {
		for (EventLinkBuilder builder : EventLinkBuilder.values()) {
			m_builders.put(builder.getId(), builder);
		}
	}
}
