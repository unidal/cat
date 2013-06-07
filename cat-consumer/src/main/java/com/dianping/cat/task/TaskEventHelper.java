package com.dianping.cat.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.dainping.cat.consumer.core.dal.TaskPayload;

public class TaskEventHelper {
	public TaskEvent asEvent(TaskPayload payload) {
		TaskEvent event = new DefaultTaskEvent();

		event.setId(payload.getId());
		event.setConsumer(payload.getConsumer());
		event.setCreationDate(payload.getCreationDate());
		event.setEndDate(payload.getEndDate());
		event.setFailureCount(payload.getFailureCount());
		event.setFailureReason(payload.getFailureReason());
		event.setMaxRetryCount(payload.getMaxRetryCount());
		event.setProducer(payload.getProducer());
		event.setRefKey(payload.getRefKey());
		event.setStartDate(payload.getStartDate());
		event.setStatus(payload.getStatus());
		event.setSubject(payload.getSubject());

		event.getProperties().putAll(parse(payload.getPayload()));
		return event;
	}

	public TaskPayload asPayload(TaskEvent event) {
		TaskPayload payload = new TaskPayload();

		payload.setId(event.getId());
		payload.setConsumer(event.getConsumer());
		payload.setCreationDate(event.getCreationDate());
		payload.setEndDate(event.getEndDate());
		payload.setFailureCount(event.getFailureCount());
		payload.setFailureReason(event.getFailureReason());
		payload.setMaxRetryCount(event.getMaxRetryCount());
		payload.setProducer(event.getProducer());
		payload.setRefKey(event.getRefKey());
		payload.setStartDate(event.getStartDate());
		payload.setStatus(event.getStatus());
		payload.setSubject(event.getSubject());

		payload.setPayload(format(event.getProperties()));
		return payload;
	}

	private String escape(String str) {
		int len = str.length();
		StringBuilder sb = new StringBuilder(len + 16);

		for (int i = 0; i < len; i++) {
			char ch = str.charAt(i);

			switch (ch) {
			case '\\':
				sb.append('\\').append(ch);
				break;
			case '\t':
				sb.append('\\').append('t');
				break;
			case '\r':
				sb.append('\\').append('r');
				break;
			case '\n':
				sb.append('\\').append('n');
				break;
			default:
				sb.append(ch);
				break;
			}
		}

		return sb.toString();
	}

	private String format(Map<String, String> properties) {
		StringBuilder sb = new StringBuilder(1024);

		for (Map.Entry<String, String> e : properties.entrySet()) {
			String key = e.getKey();
			String value = e.getValue();

			sb.append(escape(key)).append('=').append(escape(value)).append('\n');
		}

		return sb.toString();
	}

	private Map<String, String> parse(String payload) {
		Map<String, String> properties = new LinkedHashMap<String, String>();
		StringBuilder key = new StringBuilder();
		StringBuilder value = new StringBuilder();
		int len = payload.length();
		boolean inKey = true;

		for (int i = 0; i < len; i++) {
			char ch = payload.charAt(i);

			switch (ch) {
			case '=':
				if (inKey) {
					inKey = false;
				} else {
					value.append(ch);
				}

				break;
			case '\n':
				properties.put(key.toString(), value.toString());

				key.setLength(0);
				value.setLength(0);
				inKey = true;
				break;
			case '\\':
				if (i + 1 < len) {
					char ch2 = payload.charAt(i + 1);

					switch (ch2) {
					case 't':
						ch = '\t';
						break;
					case 'r':
						ch = '\r';
						break;
					case 'n':
						ch = '\n';
						break;
					default:
						ch = ch2;
						break;
					}
				}

				// fall through
			default:
				if (inKey) {
					key.append(ch);
				} else {
					value.append(ch);
				}
				break;
			}
		}

		if (key.length() > 0) {
			properties.put(key.toString(), value.toString());
		}

		return properties;
	}
}
