package org.unidal.cat.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.cat.view.NavigationInterceptor;
import org.unidal.cat.view.NavRegisterInterceptor;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(A(NavRegisterInterceptor.class));
		all.add(A(NavigationInterceptor.class));

		// Please keep it as last
		all.addAll(new WebComponentConfigurator().defineComponents());

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
