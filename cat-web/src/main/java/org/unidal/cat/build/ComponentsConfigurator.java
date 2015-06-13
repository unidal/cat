package org.unidal.cat.build;

import java.util.ArrayList;
import java.util.List;

import org.unidal.cat.CatWebModule;
import org.unidal.cat.view.NavRegisterInterceptor;
import org.unidal.cat.view.NavigationInterceptor;
import org.unidal.dal.jdbc.datasource.JdbcDataSourceDescriptorManager;
import org.unidal.initialization.DefaultModuleManager;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleManager;
import org.unidal.lookup.configuration.AbstractResourceConfigurator;
import org.unidal.lookup.configuration.Component;

public class ComponentsConfigurator extends AbstractResourceConfigurator {
	@Override
	public List<Component> defineComponents() {
		List<Component> all = new ArrayList<Component>();

		all.add(A(NavRegisterInterceptor.class));
		all.add(A(NavigationInterceptor.class));

		all.add(C(Module.class, CatWebModule.ID, CatWebModule.class));

		all.add(C(ModuleManager.class, DefaultModuleManager.class) //
		      .config(E("topLevelModules").value(CatWebModule.ID)));

		// database
		all.add(C(JdbcDataSourceDescriptorManager.class) //
		      .config(E("datasourceFile").value("/data/appdatas/cat/datasources.xml")));

		// Please keep it as last
		all.addAll(new WebComponentConfigurator().defineComponents());

		return all;
	}

	public static void main(String[] args) {
		generatePlexusComponentsXmlFile(new ComponentsConfigurator());
	}
}
