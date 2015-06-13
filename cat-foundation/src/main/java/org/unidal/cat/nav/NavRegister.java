package org.unidal.cat.nav;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.unidal.helper.Splitters;

public class NavRegister {
	private static final String NAME = "_r_";

	private Map<String, String> m_map = new LinkedHashMap<String, String>();

	public NavRegister(HttpServletRequest req) {
		String encoded = req.getParameter(NAME);

		if (encoded != null && encoded.length() > 0) {
			Splitters.by(',', ':').trim().split(encoded, m_map);
		}
	}

	public String getValue(String key) {
		return m_map.get(key);
	}

	public boolean hasValue(String key) {
		return m_map.containsKey(key);
	}

	public void setValue(String key, String value) {
		m_map.put(key, value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(1024);
		boolean first = true;

		sb.append(NAME).append('=');

		for (Map.Entry<String, String> e : m_map.entrySet()) {
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

	public String refreshValue(String key, String newValue) {
		if (newValue == null) {
			return m_map.get(key);
		} else {
			// override the value
			m_map.put(key, newValue);

			return newValue;
		}
	}
}
