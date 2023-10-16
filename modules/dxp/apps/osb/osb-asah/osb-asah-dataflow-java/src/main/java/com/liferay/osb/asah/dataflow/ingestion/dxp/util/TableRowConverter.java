/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.dxp.util;

import com.google.api.services.bigquery.model.TableRow;

import com.liferay.osb.asah.dataflow.common.ObjectMapperUtil;

import java.lang.reflect.Field;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Riccardo Ferrari
 */
public class TableRowConverter {

	public static TableRow asTableRow(Object object) {
		if (object == null) {
			return null;
		}

		TableRow tableRow = new TableRow();

		Class<?> objectClass = object.getClass();

		for (Field field : objectClass.getFields()) {
			try {
				Object fieldValue = _getFieldValue(field, object);

				if (fieldValue != null) {
					tableRow.set(field.getName(), fieldValue);
				}
			}
			catch (Exception exception) {
				_logger.error(
					"Unable to convert field " + field.getName(), exception);
			}
		}

		return tableRow;
	}

	private static Object _getFieldValue(Field field, Object object)
		throws IllegalAccessException {

		Object value = field.get(object);

		Class<?> fieldTypeClass = field.getType();

		String fieldTypeClassName = fieldTypeClass.getCanonicalName();

		if (fieldTypeClass.isPrimitive() ||
			fieldTypeClassName.startsWith("java.lang")) {

			if (value instanceof Boolean) {
				return Boolean.toString((Boolean)value);
			}
			else if (value instanceof Double) {
				return String.valueOf((Double)value);
			}
			else if (value instanceof Integer) {
				return String.valueOf((Integer)value);
			}
			else if (value instanceof Long) {
				return String.valueOf((Long)value);
			}
			else if (value instanceof Object[]) {
				return Arrays.toString((Object[])value);
			}

			return value;
		}
		else if (fieldTypeClassName.equals("java.util.List")) {
			return _getFieldValues((List)value);
		}
		else if (fieldTypeClassName.equals("java.util.Map")) {
			return ObjectMapperUtil.writeValueAsString(value);
		}
		else if (fieldTypeClassName.startsWith(_PACKAGE_NAME)) {
			return asTableRow(value);
		}

		throw new IllegalStateException(
			"Unknown field type class name " + fieldTypeClassName);
	}

	private static Object _getFieldValues(List<?> values) {
		if ((values == null) || values.isEmpty()) {
			return Collections.<TableRow>emptyList();
		}

		Object listElement = values.get(0);

		String listElementClassName = _getObjectClassName(listElement);

		if (listElementClassName.startsWith(_PACKAGE_NAME)) {
			Stream<?> valuesStream = values.stream();

			return valuesStream.map(
				TableRowConverter::asTableRow
			).collect(
				Collectors.toList()
			);
		}
		else if (listElement instanceof Map) {
			Stream<?> valuesStream = values.stream();

			return valuesStream.map(
				ObjectMapperUtil::writeValueAsString
			).collect(
				Collectors.toList()
			);
		}

		return values;
	}

	private static String _getObjectClassName(Object object) {
		Class<?> clazz = object.getClass();

		if (clazz.isAnonymousClass()) {
			clazz = clazz.getSuperclass();
		}

		return clazz.getName();
	}

	private static final String _PACKAGE_NAME =
		"com.liferay.osb.asah.dataflow.ingestion.dxp.entity";

	private static final Logger _logger = LoggerFactory.getLogger(
		TableRowConverter.class);

}