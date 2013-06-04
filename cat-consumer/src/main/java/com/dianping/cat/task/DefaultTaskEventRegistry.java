package com.dianping.cat.task;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.Initializable;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.unidal.lookup.ContainerHolder;

public class DefaultTaskEventRegistry extends ContainerHolder implements TaskEventRegistry, Initializable {
	private Map<String, TaskEventConsumer> m_map = new LinkedHashMap<String, TaskEventConsumer>();

	private Map<String, TaskEventConsumer> m_prefixedMap = new LinkedHashMap<String, TaskEventConsumer>();

	@Override
	public TaskEventConsumer findConsumer(String subject) {
		TaskEventConsumer consumer = m_map.get(subject);

		if (consumer == null) {
			for (Map.Entry<String, TaskEventConsumer> e : m_prefixedMap.entrySet()) {
				if (subject.startsWith(e.getKey())) {
					consumer = e.getValue();
					break;
				}
			}
		}

		if (consumer == null) {
			throw new RuntimeException(String.format("No consumer registered for subject(%s)!", subject));
		} else {
			return consumer;
		}
	}

	@Override
	public void initialize() throws InitializationException {
		List<TaskEventConsumer> consumers = lookupList(TaskEventConsumer.class);

		for (TaskEventConsumer consumer : consumers) {
			for (String subject : consumer.getSubjectsToSubcribe()) {
				TaskEventConsumer old = null;

				if (subject.endsWith(".*")) {
					old = m_prefixedMap.put(subject.substring(0, subject.length() - 2), consumer);
				} else {
					old = m_map.put(subject, consumer);
				}

				if (old != null) {
					throw new RuntimeException(String.format("Subject(%s) is already subscribed by %s, " + //
					      "it can't be subscribed again by %s!", subject, old.getClass(), consumer.getClass()));
				}
			}
		}
	}
}
