package com.dianping.cat.task;

import static com.dianping.cat.task.TaskEventStatus.DOING;
import static com.dianping.cat.task.TaskEventStatus.DONE;
import static com.dianping.cat.task.TaskEventStatus.FAILED;
import static com.dianping.cat.task.TaskEventStatus.TODO;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.unidal.dal.jdbc.DalException;
import org.unidal.helper.Dates;
import org.unidal.helper.Threads;
import org.unidal.helper.Threads.Task;
import org.unidal.lookup.annotation.Inject;

import com.dainping.cat.consumer.core.dal.TaskPayload;
import com.dainping.cat.consumer.core.dal.TaskPayloadDao;
import com.dainping.cat.consumer.core.dal.TaskPayloadEntity;
import com.dianping.cat.Cat;
import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

public class DefaultTaskEventEngine implements TaskEventEngine, LogEnabled, Task {
	@Inject
	private TaskEventRegistry m_registry;

	@Inject
	private TaskEventContext m_context;

	@Inject
	private TaskEventHelper m_helper;

	@Inject
	private TaskPayloadDao m_taskPayloadDao;

	private boolean m_active = true;

	private Logger m_logger;

	private long m_checkInternal = 60 * 1000L;

	@Override
	public void enableLogging(Logger logger) {
		m_logger = logger;
	}

	private void execute(TaskPayload task) {
		Transaction t = Cat.newTransaction("Task", task.getSubject());

		t.addData("payload", task.getPayload());

		if (tryLockTask(task)) {
			Throwable exception = null;

			try {
				TaskEventConsumer consumer = m_registry.findConsumer(task.getSubject());

				consumer.consume(m_context, m_helper.asEvent(task));
				t.setStatus(Message.SUCCESS);
			} catch (Throwable e) {
				t.setStatus(e);
				Cat.logError(e);
				exception = e;
			} finally {
				finishTask(task, exception);
				t.complete();
			}
		}
	}

	private void finishTask(TaskPayload task, Throwable exception) {
		if (exception != null) {
			task.setStatus(FAILED.getCode());
			task.setFailureCount(task.getFailureCount() + 1);
			task.setFailureReason(toString(exception));
		} else {
			task.setStatus(DONE.getCode());
		}

		task.setOldStatus(DOING.getCode());
		task.setEndDate(new Date());

		try {
			m_taskPayloadDao.updateByIdAndStatus(task, TaskPayloadEntity.UPDATESET_FULL);
		} catch (DalException e) {
			Cat.logError(e);
		}
	}

	@Override
	public String getName() {
		return getClass().getSimpleName();
	}

	@Override
	public void run() {
		String ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();

		try {
			while (m_active) {
				try {
					List<TaskPayload> tasks = m_taskPayloadDao.findAllByStatus(TODO.getCode(), TaskPayloadEntity.READSET_FULL);

					if (tasks.isEmpty()) { // try to pick up some timeout tasks
						Date beforeStartDate = Dates.now().minute(-5).asDate(); // started 5 minutes ago

						tasks = m_taskPayloadDao.findAllZombies(DOING.getCode(), beforeStartDate, TaskPayloadEntity.READSET_FULL);
					}

					if (tasks.size() > 0) {
						Transaction t = Cat.newTransaction("System", "TaskConsumer:" + ip);

						t.addData("size", String.valueOf(tasks.size()));

						try {
							for (TaskPayload task : tasks) {
								execute(task);
							}

							t.setStatus(Message.SUCCESS);
						} catch (Throwable e) {
							t.setStatus(e);
							Cat.logError(e);
						} finally {
							t.complete();
						}
					}
				} catch (DalException e) {
					Cat.logError(e);
					m_logger.warn(e.toString());
				}

				TimeUnit.MILLISECONDS.sleep(m_checkInternal);
			}
		} catch (InterruptedException e) {
			// ignore it
		}
	}

	public void setCheckInternal(long checkInternal) {
		m_checkInternal = checkInternal;
	}

	@Override
	public void shutdown() {
		m_active = false;
	}

	@Override
	public void start() {
		Threads.forGroup("Cat").start(this);
	}

	private String toString(Throwable e) {
		StringWriter sw = new StringWriter(2048);

		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	private boolean tryLockTask(TaskPayload task) {
		String ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();

		task.setOldStatus(task.getStatus()); // todo or doing
		task.setStatus(DOING.getCode());
		task.setConsumer(ip);
		task.setStartDate(new Date());

		try {
			return m_taskPayloadDao.updateByIdAndStatus(task, TaskPayloadEntity.UPDATESET_FULL) > 0;
		} catch (DalException e) {
			Cat.logError(e);
		}

		return false;
	}
}
