/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.Map;
import java.util.function.BiFunction;

/**
 * @author Rachael Koestartyo
 */
public class MapUtil {

	public static <T> T get(Map<String, Object> map, String key) {
		return (T)map.get(key);
	}

	public static long getLong(Map<String, ?> map, String key) {
		return getLong(map, key, 0);
	}

	public static long getLong(
		Map<String, ?> map, String key, long defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof Long) {
			return (long)value;
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return _get(array[0], defaultValue);
		}

		return _get(String.valueOf(value), defaultValue);
	}

	public static String getString(Map<String, ?> map, String key) {
		return getString(map, key, null);
	}

	public static String getString(
		Map<String, ?> map, String key, String defaultValue) {

		Object value = map.get(key);

		if (value == null) {
			return defaultValue;
		}

		if (value instanceof String) {
			return _get((String)value, defaultValue);
		}

		if (value instanceof String[]) {
			String[] array = (String[])value;

			if (array.length == 0) {
				return defaultValue;
			}

			return _get(array[0], defaultValue);
		}

		return String.valueOf(value);
	}

	@SafeVarargs
	public static <K, V> Map<K, V> merge(
		BiFunction<? super V, ? super V, ? extends V> remappingFunction,
		Map<K, V>... maps) {

		Map<K, V> map = maps[0];

		for (int i = 1; i < maps.length; i++) {
			Map<K, V> curMap = maps[i];

			for (Map.Entry<K, V> entry : curMap.entrySet()) {
				map.merge(entry.getKey(), entry.getValue(), remappingFunction);
			}
		}

		return map;
	}

	private static long _get(String value, long defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return _parseLong(value.trim(), defaultValue);
	}

	private static String _get(String value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		value = value.trim();

		if (value.contains("\r")) {
			value = value.replace("\r\n", "\n");
		}

		return value;
	}

	private static long _parseLong(String value, long defaultValue) {
		int length = value.length();

		if (length <= 0) {
			return defaultValue;
		}

		int pos = 0;
		long limit = -Long.MAX_VALUE;
		boolean negative = false;

		char c = value.charAt(0);

		if (c < '0') {
			if (c == '-') {
				limit = Long.MIN_VALUE;
				negative = true;
			}
			else if (c != '+') {
				return defaultValue;
			}

			if (length == 1) {
				return defaultValue;
			}

			pos++;
		}

		long smallLimit = limit / 10;

		long result = 0;

		while (pos < length) {
			if (result < smallLimit) {
				return defaultValue;
			}

			c = value.charAt(pos++);

			if ((c < '0') || (c > '9')) {
				return defaultValue;
			}

			int number = c - '0';

			result *= 10;

			if (result < (limit + number)) {
				return defaultValue;
			}

			result -= number;
		}

		if (negative) {
			return result;
		}

		return -result;
	}

}