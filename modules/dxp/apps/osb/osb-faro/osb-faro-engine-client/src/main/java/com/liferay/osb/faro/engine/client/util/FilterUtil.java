/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.faro.engine.client.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Matthew Kong
 */
public class FilterUtil {

	public static String getBlankFilter(String fieldName, String operator) {
		return fieldName.concat(
			operator
		).concat(
			StringPool.DOUBLE_APOSTROPHE
		);
	}

	public static String getFieldName(
		String fieldName, String fieldNameContext) {

		if (Validator.isNull(fieldNameContext)) {
			return fieldName;
		}

		return StringUtil.replace(
			fieldNameContext, CharPool.QUESTION, fieldName);
	}

	public static String getFilter(
		String fieldName, String operator, boolean useDoubleApostrophe,
		Object value) {

		if (value == null) {
			return null;
		}

		if (!(value instanceof Boolean) && !(value instanceof Long)) {
			String valueString = String.valueOf(value);

			if (Validator.isBlank(valueString)) {
				return null;
			}

			if (useDoubleApostrophe) {
				value = StringUtil.quote(
					valueString, StringPool.DOUBLE_APOSTROPHE);
			}
			else {
				value = StringUtil.quote(valueString, StringPool.APOSTROPHE);
			}
		}
		else if (value instanceof Date) {
			Date date = (Date)value;

			value = String.valueOf(date.toInstant());
		}
		else if (value instanceof List) {
			List<?> values = (List<?>)value;

			Stream<?> stream = values.stream();

			value = StringPool.OPEN_BRACKET.concat(
				stream.map(
					String::valueOf
				).collect(
					Collectors.joining(StringPool.COMMA)
				)
			).concat(
				StringPool.CLOSE_BRACKET
			);
		}

		if (FilterConstants.isStringFunction(operator)) {
			StringBundler sb = new StringBundler(6);

			sb.append(operator);
			sb.append(StringPool.OPEN_PARENTHESIS);
			sb.append(fieldName);
			sb.append(StringPool.COMMA);
			sb.append(value);
			sb.append(StringPool.CLOSE_PARENTHESIS);

			return sb.toString();
		}

		StringBundler sb = new StringBundler(3);

		sb.append(fieldName);
		sb.append(operator);
		sb.append(value);

		return sb.toString();
	}

	public static String getFilter(
		String fieldName, String operator, Object value) {

		return getFilter(fieldName, operator, false, value);
	}

	public static String getFilter(
		String fieldName, String fieldNameContext, String operator,
		String value) {

		return getFilter(
			getFieldName(fieldName, fieldNameContext), operator, value);
	}

	public static String getInterestFilter(
		String interestName, boolean interested) {

		if (Validator.isNull(interestName)) {
			return null;
		}

		StringBundler sb = new StringBundler(3);

		sb.append("interests.filter(filter='");

		FilterBuilder filterBuilder = new FilterBuilder();

		filterBuilder.addFilter(
			"name", FilterConstants.COMPARISON_OPERATOR_EQUALS, interestName,
			false, true, true);
		filterBuilder.addFilter(
			"score", FilterConstants.COMPARISON_OPERATOR_EQUALS, interested,
			false, true, true);

		sb.append(filterBuilder.build());

		sb.append("')");

		return sb.toString();
	}

	public static String getNullFilter(String fieldName, String operator) {
		return fieldName.concat(
			operator
		).concat(
			StringPool.NULL
		);
	}

	public static String negate(String filterString) {
		return "not".concat(
			StringPool.SPACE
		).concat(
			filterString
		);
	}

}