package com.dianping.cat.task;

import java.util.Arrays;

import org.unidal.dal.jdbc.DalException;
import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.TaskPayloadDao;
import com.dianping.cat.Cat;
import com.dianping.cat.configuration.NetworkInterfaceManager;

public class DefaultTaskEventContext implements TaskEventContext {
	@Inject
	private TaskEventHelper m_helper;

	@Inject
	private TaskPayloadDao m_taskPayloadDao;

	@Override
	public int produce(TaskEvent baseEvent, String subject, String refKey, String... keyValuePairs) {
		int len = keyValuePairs.length;

		if (len % 2 != 0) {
			throw new IllegalArgumentException(String.format("Key values(%s) is not paired!", Arrays.asList(keyValuePairs)));
		}

		String ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
		TaskEvent event = m_helper.asEvent(m_helper.asPayload(baseEvent)); // clone

		event.setSubject(subject);
		event.setRefKey(refKey);
		event.setProducer(ip);
		event.setStatus(TaskEventStatus.TODO.getCode());

		for (int i = 0; i < len; i += 2) {
			event.setProperty(keyValuePairs[i], keyValuePairs[i + 1]);
		}

		try {
			return m_taskPayloadDao.insert(m_helper.asPayload(event));
		} catch (DalException e) {
			Cat.logError(e);
		}

		return 0;
	}
}
