/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * @author Rachael Koestartyo
 */
public class MatcherUtil {

	public static String getGroupByPattern() {
		return StringEscapeUtils.unescapeJava(_groupByPattern.toString());
	}

	public static Matcher getMatcher(String apply) {
		return _groupByPattern.matcher(apply);
	}

	private static final Pattern _groupByPattern = Pattern.compile(
		"groupby\\(\\((?<groupByField>[^)]+)\\)\\)" +
			"(/contains\\(\\((?<containsField>[^)]+)\\)\\))?");

}