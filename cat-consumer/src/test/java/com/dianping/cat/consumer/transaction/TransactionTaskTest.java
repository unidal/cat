package com.dianping.cat.consumer.transaction;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;
import org.unidal.dal.jdbc.DalException;
import org.unidal.dal.jdbc.Readset;
import org.unidal.dal.jdbc.Updateset;
import org.unidal.helper.Files;
import org.unidal.lookup.ComponentTestCase;

import com.dainping.cat.consumer.core.dal.TaskPayload;
import com.dainping.cat.consumer.core.dal.TaskPayloadDao;
import com.dianping.cat.task.DefaultTaskEventEngine;
import com.dianping.cat.task.TaskEventConsumer;
import com.dianping.cat.task.TaskEventEngine;
import com.dianping.cat.task.TaskEventStatus;

public class TransactionTaskTest extends ComponentTestCase {
	@Test
	public void testConsume() throws Exception {
		DefaultTaskEventEngine engine = (DefaultTaskEventEngine) lookup(TaskEventEngine.class);
		MockTaskPayloadDao dao = (MockTaskPayloadDao) lookup(TaskPayloadDao.class);
		StringBuilder sb = dao.getResult();
		CountDownLatch latch = new CountDownLatch(1);

		dao.setLatch(latch);
		engine.setCheckInternal(1);
		dao.prepare();
		engine.start();

		latch.await();

		String expected = Files.forIO().readFrom(getClass().getResourceAsStream("task-result.xml"), "utf-8");

		Assert.assertEquals(expected.replaceAll("\r", ""), sb.toString());
	}

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

	public static class MockTaskPayloadDao extends TaskPayloadDao {
		private List<TaskPayload> m_tasks = new ArrayList<TaskPayload>();

		private StringBuilder m_sb = new StringBuilder(1024);

		private int m_id = 1;

		private CountDownLatch m_latch;

		@Override
		public List<TaskPayload> findAllByStatus(int status, Readset<TaskPayload> readset) throws DalException {
			List<TaskPayload> list = new ArrayList<TaskPayload>();

			for (TaskPayload task : m_tasks) {
				if (task.getStatus() == status) {
					list.add(task);
				}
			}

			if (list.size() > 0) {
				log("findAllByStatus", "status", status, "found", list.size());
			} else {
				m_latch.countDown();
			}

			return list;
		}

		@Override
		public List<TaskPayload> findAllZombies(int status, Date startDate, Readset<TaskPayload> readset) throws DalException {
			List<TaskPayload> list = new ArrayList<TaskPayload>();

			for (TaskPayload task : m_tasks) {
				if (task.getStatus() == TaskEventStatus.DOING.getCode()) {
					if (task.getEndDate() == null && task.getStartDate() != null) {
						if (task.getStartDate().before(startDate)) {
							list.add(task);
						}
					}
				}
			}

			if (list.size() > 0) {
				log("findAllZombies", "status", status, "found", list.size());
			}

			return list;
		}

		public StringBuilder getResult() {
			return m_sb;
		}

		@Override
		public int insert(TaskPayload task) throws DalException {
			task.setId(m_id++);
			m_tasks.add(task);

			log("insert", "id", task.getId(), "subject", task.getSubject(), "refKey", task.getRefKey());
			return 1;
		}

		private void log(String method, Object... args) {
			boolean first = true;

			m_sb.append(method).append(':');

			for (Object arg : args) {
				if (first) {
					first = false;
				} else {
					m_sb.append(',');
				}

				m_sb.append(arg);
			}

			m_sb.append('\n');
		}

		public void prepare() {
			TaskPayload task = new TaskPayload();
			Date period = new Date(1370415600000L);
			String domain = "Cat";

			task.setPayload(new MessageFormat("domain={0}\nperiod={1,date,yyyyMMddHH}").format(new Object[] { domain, period }));
			task.setStatus(1);
			task.setCreationDate(new Date());
			task.setSubject(TransactionTask.HOURLY.getName());
			task.setRefKey(TransactionTask.HOURLY.getRefKey(domain, period));
			task.setProducer("mock");

			m_tasks.add(task);
		}

		public void setLatch(CountDownLatch latch) {
			m_latch = latch;
		}

		@Override
		public int updateByIdAndStatus(TaskPayload proto, Updateset<TaskPayload> updateset) throws DalException {
			int len = m_tasks.size();

			for (int i = 0; i < len; i++) {
				TaskPayload task = m_tasks.get(i);

				if (task.getId() == proto.getId() && task.getStatus() == proto.getStatus()) {
					m_tasks.remove(i);
					m_tasks.add(i, proto);

					log("updateByIdAndStatus", "id", proto.getId(), "oldStatus", proto.getOldStatus(), "status", proto.getStatus());
					return 1;
				}
			}

			return 0;
		}
	}
}
