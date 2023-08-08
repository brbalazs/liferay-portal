/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Shinn Lok
 */
public class StringUtil {

	public static String get(Object value) {
		return get(value, "");
	}

	public static String get(Object value, String defaultValue) {
		if (value == null) {
			return defaultValue;
		}

		return String.valueOf(value);
	}

	public static boolean isNull(String s) {
		if ((s == null) || s.equalsIgnoreCase("null")) {
			return true;
		}

		return false;
	}

	public static boolean isQuoted(String s) {
		if (StringUtils.isBlank(s) || (s.length() == 1)) {
			return false;
		}

		if (((s.charAt(0) == '\'') && (s.charAt(s.length() - 1) == '\'')) ||
			((s.charAt(0) == '"') && (s.charAt(s.length() - 1) == '"'))) {

			return true;
		}

		return false;
	}

	public static String replace(String s, String oldSub, String newSub) {
		if (s == null) {
			return null;
		}

		if ((oldSub == null) || oldSub.equals("")) {
			return s;
		}

		if (newSub == null) {
			newSub = "";
		}

		int y = s.indexOf(oldSub);

		if (y >= 0) {
			StringBuilder sb = new StringBuilder();

			int length = oldSub.length();
			int x = 0;

			while (x <= y) {
				sb.append(s.substring(x, y));
				sb.append(newSub);

				x = y + length;

				y = s.indexOf(oldSub, x);
			}

			sb.append(s.substring(x));

			return sb.toString();
		}

		return s;
	}

	public static String replace(String s, String[] oldSubs, String[] newSubs) {
		if ((s == null) || (oldSubs == null) || (newSubs == null)) {
			return null;
		}

		if (oldSubs.length != newSubs.length) {
			return s;
		}

		for (int i = 0; i < oldSubs.length; i++) {
			s = replace(s, oldSubs[i], newSubs[i]);
		}

		return s;
	}

	public static Object toObject(String string) {
		if (string.startsWith("'") && string.endsWith("'")) {
			return unquoteAndDecodeInnerQuotes(string);
		}

		if (string.equalsIgnoreCase("false")) {
			return false;
		}

		if (string.equalsIgnoreCase("null")) {
			return null;
		}

		if (string.equalsIgnoreCase("true")) {
			return true;
		}

		if (string.startsWith("[") && string.endsWith("]")) {
			return new JSONArray(string);
		}

		if (string.startsWith("{") && string.endsWith("}")) {
			return new JSONObject(string);
		}

		if (NumberUtils.isCreatable(string)) {
			if (string.contains(".")) {
				return Double.valueOf(string);
			}

			return Long.valueOf(string);
		}

		throw new IllegalArgumentException(
			"Unknown object " + string + " used in filter");
	}

	public static String unquote(String s) {
		if (StringUtils.isBlank(s) || (s.length() == 1)) {
			return s;
		}

		if (isQuoted(s)) {
			return s.substring(1, s.length() - 1);
		}

		return s;
	}

	public static String unquoteAndDecodeInnerQuotes(String s) {
		if (StringUtils.isBlank(s)) {
			return s;
		}

		s = unquote(s);

		return s.replace("''", "'");
	}

}