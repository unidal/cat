package com.dianping.cat.consumer.transaction;

import org.junit.Assert;
import org.junit.Test;
import org.unidal.lookup.ComponentTestCase;

import com.dianping.cat.task.TaskEventConsumer;
import com.dianping.cat.task.TaskEventEngine;

public class TransactionTaskTest extends ComponentTestCase {
	@Test
	public void testLookup() throws Exception {
		for (TransactionTaskBroker value : TransactionTaskBroker.values()) {
			String roleHint = value.name() + ":" + value.getClass().getName();
			TaskEventConsumer consumer = lookup(TaskEventConsumer.class, roleHint);

			Assert.assertNotNull(value.name(), consumer.toString());
		}

		for (TransactionTaskProcessor value : TransactionTaskProcessor.values()) {
			String roleHint = value.name() + ":" + value.getClass().getName();
			TaskEventConsumer consumer = lookup(TaskEventConsumer.class, roleHint);

			Assert.assertNotNull(value.name(), consumer.toString());
		}
	}

	@Test
	public void testConsume() throws Exception {
		TaskEventEngine engine = lookup(TaskEventEngine.class);

		engine.start(); // TODO
	}
}
