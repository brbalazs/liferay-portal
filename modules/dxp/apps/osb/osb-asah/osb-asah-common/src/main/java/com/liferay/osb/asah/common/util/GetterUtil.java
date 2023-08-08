/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.google.cloud.bigquery.FieldValue;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Marcellus Tavares
 */
public class GetterUtil {

	public static BigDecimal getBigDecimal(Object object) {
		if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			return fieldValue.getNumericValue();
		}

		if (object instanceof Double) {
			return BigDecimal.valueOf((Double)object);
		}
		else if (object instanceof Integer) {
			return new BigDecimal((Integer)object);
		}
		else if (object instanceof String) {
			return new BigDecimal((String)object);
		}

		return (BigDecimal)object;
	}

	public static String getDateString(Object object) {
		LocalDateTime localDateTime = null;

		if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			localDateTime = LocalDateTime.parse(fieldValue.getStringValue());
		}
		else if (object instanceof String) {
			localDateTime = LocalDateTime.parse((String)object);
		}
		else {
			OffsetDateTime offsetDateTime = (OffsetDateTime)object;

			localDateTime = offsetDateTime.toLocalDateTime();
		}

		return localDateTime.toString();
	}

	public static int getInteger(Object object) {
		if (object instanceof BigDecimal) {
			BigDecimal bigDecimal = (BigDecimal)object;

			return bigDecimal.intValue();
		}
		else if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			Long longFieldValue = fieldValue.getLongValue();

			return longFieldValue.intValue();
		}
		else if (object instanceof Long) {
			Long longFieldValue = (Long)object;

			return longFieldValue.intValue();
		}
		else if (object instanceof String) {
			return Integer.parseInt((String)object);
		}

		return (int)object;
	}

	public static Number getNumber(Object object) {
		if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			return fieldValue.getNumericValue();
		}

		if (object instanceof String) {
			return NumberUtils.createNumber((String)object);
		}

		return (Number)object;
	}

	public static Object getObject(Object object) {
		if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			return fieldValue.getValue();
		}

		return object;
	}

	public static String getString(Object object) {
		if (object == null) {
			return "";
		}

		if (object instanceof FieldValue) {
			FieldValue fieldValue = (FieldValue)object;

			if (fieldValue.getValue() == null) {
				return "";
			}

			return fieldValue.getStringValue();
		}

		return String.valueOf(object);
	}

}