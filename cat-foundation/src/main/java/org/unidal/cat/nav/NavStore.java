package org.unidal.cat.nav;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.unidal.helper.Splitters;

public class NavStore {
	public static final String ID = "_n";

	private Map<String, String> m_map = new LinkedHashMap<String, String>();

	private HttpServletRequest m_req;

	private boolean m_initialized;

	public NavStore(HttpServletRequest req) {
		m_req = req;
	}

	public String getValue(String key) {
		return map().get(key);
	}

	public boolean hasValue(String key) {
		return map().containsKey(key);
	}

	private Map<String, String> map() {
		if (!m_initialized) {
			String encoded = m_req.getParameter(ID);

			if (encoded != null && encoded.length() > 0) {
				Splitters.by(',', ':').trim().split(encoded, m_map);
			}
		}

		return m_map;
	}

	public String refresh(String key, String newValue) {
		if (newValue == null) {
			return map().get(key);
		} else {
			// override the value
			map().put(key, newValue);

			return newValue;
		}
	}

	public long refreshDate(long newValue) {
		if (newValue == 0) {
			String value = map().get("date");

			try {
				return Long.parseLong(value);
			} catch (Exception e) {
				return newValue;
			}
		} else {
			// override the value
			map().put("date", String.valueOf(newValue));

			return newValue;
		}
	}

	public String refreshDomain(String domain) {
		return refresh("domain", domain);
	}

	public String refreshIp(String ip) {
		return refresh("ip", ip);
	}

	public String refreshReportType(String period) {
		return refresh("type", period);
	}

	public void setValue(String key, String value) {
		map().put(key, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		boolean first = true;

		sb.append(ID).append('=');

		for (Map.Entry<String, String> e : map().entrySet()) {
			if (first) {
				first = false;
			} else {
				sb.append(',');
			}

			sb.append(e.getKey());
			sb.append(':');
			sb.append(e.getValue());
		}

		return sb.toString();
	}
}
