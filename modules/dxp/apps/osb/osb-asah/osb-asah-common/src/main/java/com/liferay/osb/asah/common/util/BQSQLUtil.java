/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.BeanUtils;

/**
 * @author Marcellus Tavares
 */
public class BQSQLUtil {

	public static String createInsertStatement(Object entity) {
		StringBuilder sb = new StringBuilder();

		sb.append("INSERT INTO ");

		Class<?> clazz = entity.getClass();

		sb.append(clazz.getSimpleName());

		sb.append("(");

		List<Column> columns = _getColumns(entity);

		sb.append(String.join(", ", _getColumnNames(columns)));

		sb.append(") VALUES ");

		sb.append(_createInsertValues(columns));

		return sb.toString();
	}

	private static String _createInsertValues(List<Column> columns) {
		StringBuilder sb = new StringBuilder();

		sb.append("(");

		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);

			Object value = column.getValue();

			if (value instanceof Date) {
				sb.append(_getValueString((Date)value));
			}
			else if (value instanceof Collection) {
				sb.append(_getValueString((Collection)value));
			}
			else if (value instanceof String) {
				sb.append(_getValueString((String)value));
			}
			else {
				sb.append(value);
			}

			if ((i + 1) < columns.size()) {
				sb.append(", ");
			}
		}

		sb.append(")");

		return sb.toString();
	}

	private static List<String> _getColumnNames(List<Column> columnValues) {
		Stream<Column> stream = columnValues.stream();

		return stream.map(
			Column::getName
		).collect(
			Collectors.toList()
		);
	}

	private static List<Column> _getColumns(Object entity) {
		List<Column> columns = new ArrayList<>();

		PropertyDescriptor[] propertyDescriptors =
			BeanUtils.getPropertyDescriptors(entity.getClass());

		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method propertyReadMethod = propertyDescriptor.getReadMethod();

			if (propertyReadMethod == null) {
				continue;
			}

			BigQueryColumn bigQueryColumn = propertyReadMethod.getAnnotation(
				BigQueryColumn.class);

			if (bigQueryColumn == null) {
				continue;
			}

			try {
				Object value = propertyReadMethod.invoke(entity);

				if (value == null) {
					continue;
				}

				columns.add(new Column(propertyDescriptor.getName(), value));
			}
			catch (Exception exception) {
				_log.error(
					"Unable to read property " + propertyDescriptor.getName(),
					exception);
			}
		}

		Collections.sort(columns);

		return columns;
	}

	private static String _getValueString(Collection<?> values) {
		if (values.isEmpty()) {
			return "ARRAY<STRING>[]";
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");

		Iterator<?> iterator = values.iterator();

		while (iterator.hasNext()) {
			Object value = iterator.next();

			if (value instanceof Boolean || value instanceof Number) {
				sb.append(value);
			}
			else if (value instanceof Date) {
				sb.append(_getValueString((Date)value));
			}
			else if (value instanceof String) {
				sb.append(_getValueString((String)value));
			}
			else {
				sb.append(_createInsertValues(_getColumns(value)));
			}

			if (iterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append("]");

		return sb.toString();
	}

	private static String _getValueString(Date value) {
		if (value == null) {
			return null;
		}

		return "TIMESTAMP '" + DateUtil.toUTCString(value) + "'";
	}

	private static String _getValueString(String value) {
		if (value == null) {
			return null;
		}

		return "'" + value + "'";
	}

	private static final Log _log = LogFactory.getLog(BQSQLUtil.class);

	private static class Column implements Comparable<Column> {

		public Column(String name, Object value) {
			_name = name;
			_value = value;
		}

		@Override
		public int compareTo(Column column) {
			return _name.compareTo(column._name);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Column)) {
				return false;
			}

			Column column = (Column)obj;

			if (Objects.equals(_name, column._name) &&
				Objects.equals(_value, column._value)) {

				return true;
			}

			return false;
		}

		public String getName() {
			return _name;
		}

		public Object getValue() {
			return _value;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_name, _value);
		}

		private final String _name;
		private final Object _value;

	}

}