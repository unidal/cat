package org.unidal.cat.matrix.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.cat.matrix.report.ReportModule;

import org.unidal.lookup.configuration.Component;
import org.unidal.web.configuration.AbstractWebComponentsConfigurator;

class WebComponentConfigurator extends AbstractWebComponentsConfigurator {
	@SuppressWarnings("unchecked")
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		defineModuleRegistry(all, ReportModule.class, ReportModule.class);

		return all;
	}
}
