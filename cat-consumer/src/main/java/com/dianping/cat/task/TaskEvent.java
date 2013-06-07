package com.dianping.cat.task;

import java.util.Date;
import java.util.Map;

public interface TaskEvent {
	public String getConsumer();

	public Date getCreationDate();

	public Date getDateProperty(String name, Date defaultValue);

	public Date getDateProperty(String name, String format, Date defaultValue);

	public java.util.Date getEndDate();

	public int getFailureCount();

	public String getFailureReason();

	public int getId();

	public int getMaxRetryCount();

	public String getProducer();

	public Map<String, String> getProperties();

	public String getProperty(String name, String defaultValue);

	public String getRefKey();

	public java.util.Date getStartDate();

	public int getStatus();

	public String getSubject();

	public void setConsumer(String consumer);

	public void setCreationDate(java.util.Date creationDate);

	public void setEndDate(java.util.Date endDate);

	public void setFailureCount(int failureCount);

	public void setFailureReason(String failureReason);

	public void setId(int id);

	public void setMaxRetryCount(int maxRetryCount);

	public void setProducer(String producer);

	public TaskEvent setProperty(String name, String value);

	public void setRefKey(String refKey);

	public void setStartDate(java.util.Date startDate);

	public void setStatus(int status);

	public void setSubject(String subject);
}