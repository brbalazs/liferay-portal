/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Geyson Silva
 * @author André Miranda
 */
public class QueryUtil {

	public static String escapeKeywords(String keywords) {
		IntStream intStream = keywords.codePoints();

		return intStream.mapToObj(
			c -> (char)c
		).map(
			c -> {
				if (_CHARACTERS_TO_BE_ESCAPED_IN_QUERY_STRING.indexOf(c) >= 0) {
					return "\\" + c;
				}

				return String.valueOf(c);
			}
		).collect(
			Collectors.joining()
		);
	}

	private static final String _CHARACTERS_TO_BE_ESCAPED_IN_QUERY_STRING =
		"_+-=&&||><!(){}[]^\"~*?:\\/";

}