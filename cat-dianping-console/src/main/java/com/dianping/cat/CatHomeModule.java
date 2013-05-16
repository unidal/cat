package com.dianping.cat;

import java.io.File;

import org.unidal.helper.Threads;
import org.unidal.initialization.AbstractModule;
import org.unidal.initialization.Module;
import org.unidal.initialization.ModuleContext;

import com.dianping.cat.configuration.ServerConfigManager;
import com.dianping.cat.consumer.CatConsumerAdvancedModule;
import com.dianping.cat.consumer.CatConsumerModule;
import com.dianping.cat.consumer.RealtimeConsumer;
import com.dianping.cat.message.io.TcpSocketReceiver;
import com.dianping.cat.message.spi.MessageConsumer;
import com.dianping.cat.report.task.thread.DefaultTaskConsumer;
import com.dianping.cat.report.task.thread.TaskProducer;
import com.dianping.cat.report.view.DomainNavManager;

public class CatHomeModule extends AbstractModule {
	public static final String ID = "cat-home";

	@Override
	protected void execute(ModuleContext ctx) throws Exception {
		// warm up IP seeker
		// IPSeekerManager.initailize(new File(serverConfigManager.getStorageLocalBaseDir()));
		ServerConfigManager serverConfigManager = ctx.lookup(ServerConfigManager.class);

		ctx.lookup(MessageConsumer.class, RealtimeConsumer.ID);
		ctx.lookup(DomainNavManager.class);

		DefaultTaskConsumer taskConsumer = ctx.lookup(DefaultTaskConsumer.class);
		TaskProducer dailyTaskProducer = ctx.lookup(TaskProducer.class);

		if (serverConfigManager.isJobMachine() && !serverConfigManager.isLocalMode()) {
			Threads.forGroup("Cat").start(taskConsumer);
			Threads.forGroup("Cat").start(dailyTaskProducer);
		}
	}

	@Override
	public Module[] getDependencies(ModuleContext ctx) {
		return ctx.getModules(CatConsumerModule.ID, CatConsumerAdvancedModule.ID);
	}

	@Override
	protected void setup(ModuleContext ctx) throws Exception {
		File serverConfigFile = ctx.getAttribute("cat-server-config-file");
		ServerConfigManager serverConfigManager = ctx.lookup(ServerConfigManager.class);

		serverConfigManager.initialize(serverConfigFile);

		TcpSocketReceiver server = ctx.lookup(TcpSocketReceiver.class);
		server.init();
	}

}
