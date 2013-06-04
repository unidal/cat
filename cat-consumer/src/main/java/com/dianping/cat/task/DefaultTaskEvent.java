package com.dianping.cat.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dianping.cat.Cat;

public class DefaultTaskEvent implements TaskEvent {
	private int m_id;

	private String m_subject;

	private String m_refKey;

	private int m_status;

	private String m_producer;

	private String m_consumer;

	private int m_failureCount;

	private String m_failureReason;

	private int m_maxRetryCount;

	private java.util.Date m_creationDate;

	private java.util.Date m_startDate;

	private java.util.Date m_endDate;

	private Map<String, String> m_properties = new HashMap<String, String>();

	@Override
	public String getConsumer() {
		return m_consumer;
	}

	@Override
	public Date getCreationDate() {
		return m_creationDate;
	}

	@Override
	public Date getDateProperty(String name, Date defaultValue) {
		String value = getProperty(name, null);

		if (value != null) {
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value);
			} catch (Exception e) {
				Cat.logError(String.format("Invalid date format of property(%s) of event(%s)!", name, this), e);
			}
		}

		return defaultValue;
	}

	@Override
   public java.util.Date getEndDate() {
   	return m_endDate;
   }

	@Override
	public int getFailureCount() {
		return m_failureCount;
	}

	@Override
   public String getFailureReason() {
   	return m_failureReason;
   }

	@Override
	public int getId() {
		return m_id;
	}

	@Override
	public int getMaxRetryCount() {
		return m_maxRetryCount;
	}

	@Override
	public String getProducer() {
		return m_producer;
	}

	@Override
	public Map<String, String> getProperties() {
		return m_properties;
	}

	@Override
	public String getProperty(String name, String defaultValue) {
		if (!m_properties.containsKey(name)) {
			return defaultValue;
		} else {
			return m_properties.get(name);
		}
	}

	@Override
	public String getRefKey() {
		return m_refKey;
	}

	@Override
   public java.util.Date getStartDate() {
   	return m_startDate;
   }

	@Override
   public int getStatus() {
   	return m_status;
   }

	@Override
	public String getSubject() {
		return m_subject;
	}

	@Override
   public void setConsumer(String consumer) {
		m_consumer = consumer;
	}

	@Override
   public void setCreationDate(java.util.Date creationDate) {
		m_creationDate = creationDate;
	}

	@Override
   public void setEndDate(java.util.Date endDate) {
		m_endDate = endDate;
	}

	@Override
   public void setFailureCount(int failureCount) {
		m_failureCount = failureCount;
	}

	@Override
   public void setFailureReason(String failureReason) {
		m_failureReason = failureReason;
	}

	@Override
   public void setId(int id) {
		m_id = id;
	}

	@Override
   public void setMaxRetryCount(int maxRetryCount) {
		m_maxRetryCount = maxRetryCount;
	}

	@Override
   public void setProducer(String producer) {
		m_producer = producer;
	}

	@Override
	public TaskEvent setProperty(String name, String value) {
		m_properties.put(name, value);
		return this;
	}

	@Override
   public void setRefKey(String refKey) {
		m_refKey = refKey;
	}

	@Override
   public void setStartDate(java.util.Date startDate) {
		m_startDate = startDate;
	}

	@Override
   public void setStatus(int status) {
		m_status = status;
	}

	@Override
   public void setSubject(String subject) {
		m_subject = subject;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);

		sb.append("DefaultTaskEvent[");
		sb.append("consumer: ").append(m_consumer);
		sb.append(", creation-date: ").append(m_creationDate);
		sb.append(", end-date: ").append(m_endDate);
		sb.append(", failure-count: ").append(m_failureCount);
		sb.append(", failure-reason: ").append(m_failureReason);
		sb.append(", id: ").append(m_id);
		sb.append(", max-retry-count: ").append(m_maxRetryCount);
		sb.append(", producer: ").append(m_producer);
		sb.append(", ref-key: ").append(m_refKey);
		sb.append(", start-date: ").append(m_startDate);
		sb.append(", status: ").append(m_status);
		sb.append(", subject: ").append(m_subject);
		sb.append(", properties: ").append(m_properties);
		sb.append("]");
		return sb.toString();
	}
}
