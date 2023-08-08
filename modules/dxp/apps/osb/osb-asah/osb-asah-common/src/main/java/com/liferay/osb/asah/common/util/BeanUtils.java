/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.math.BigDecimal;

import java.sql.Array;
import java.sql.Timestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.jooq.JSON;

import org.json.JSONArray;
import org.json.JSONObject;

import org.postgresql.jdbc.PgArray;
import org.postgresql.util.PGobject;

import org.springframework.core.ResolvableType;
import org.springframework.data.relational.core.mapping.Column;

/**
 * @author Inácio Nery
 */
public class BeanUtils {

	public static void copyProperties(
		Map<String, Object> source, Object target) {

		PropertyDescriptor[] targetPropertyDescriptors =
			org.springframework.beans.BeanUtils.getPropertyDescriptors(
				target.getClass());

		for (PropertyDescriptor targetPropertyDescriptor :
				targetPropertyDescriptors) {

			String targetPropertyName = _getPropertyName(
				targetPropertyDescriptor);

			if (!source.containsKey(targetPropertyName)) {
				targetPropertyName = targetPropertyName.toLowerCase();
			}

			Object value = source.get(targetPropertyName);

			if ((value == null) || StringUtil.isNull(value.toString())) {
				continue;
			}

			_setTargetPropertyValue(
				targetPropertyName, value, targetPropertyDescriptor, target);
		}
	}

	public static void copyProperties(Object source, Object target) {
		copyProperties(_toSourceMap(source), target);
	}

	private static String _getPropertyName(
		PropertyDescriptor propertyDescriptor) {

		String propertyName = propertyDescriptor.getName();

		Method propertyReadMethod = propertyDescriptor.getReadMethod();

		if (propertyReadMethod == null) {
			return propertyName;
		}

		BigQueryColumn bigQueryColumn = propertyReadMethod.getAnnotation(
			BigQueryColumn.class);

		if ((bigQueryColumn != null) &&
			StringUtils.isNotBlank(bigQueryColumn.value())) {

			propertyName = bigQueryColumn.value();
		}

		Column column = propertyReadMethod.getAnnotation(Column.class);

		if (column != null) {
			propertyName = column.value();
		}

		return propertyName;
	}

	private static void _setTargetPropertyValue(
		String targetPropertyName, Object targetPropertyValue,
		PropertyDescriptor targetPropertyDescriptor, Object target) {

		Method targetPropertyWriteMethod =
			targetPropertyDescriptor.getWriteMethod();

		if (targetPropertyWriteMethod == null) {
			return;
		}

		ResolvableType targetPropertyResolvableType =
			ResolvableType.forMethodParameter(targetPropertyWriteMethod, 0);

		Class<?> targetPropertyClass =
			targetPropertyResolvableType.getRawClass();

		Class<?> targetPropertyValueClass = targetPropertyValue.getClass();

		try {
			if ((targetPropertyClass != null) && targetPropertyClass.isEnum()) {
				targetPropertyWriteMethod.invoke(
					target,
					Enum.valueOf(
						(Class<Enum>)targetPropertyClass,
						targetPropertyValue.toString()));
			}
			else {
				if ((targetPropertyClass != null) &&
					(targetPropertyValue instanceof BigDecimal)) {

					BigDecimal bigDecimal = (BigDecimal)targetPropertyValue;

					if (targetPropertyClass.isAssignableFrom(Double.class)) {
						targetPropertyValue = bigDecimal.doubleValue();
					}
					else if (targetPropertyClass.isAssignableFrom(
								Integer.class)) {

						targetPropertyValue = bigDecimal.intValue();
					}
					else if (targetPropertyClass.isAssignableFrom(Long.class)) {
						targetPropertyValue = bigDecimal.longValue();
					}
					else if (targetPropertyClass.isAssignableFrom(Set.class)) {
						targetPropertyValue = Collections.singleton(
							bigDecimal.longValue());
					}
				}
				else if ((targetPropertyClass != null) &&
						 (targetPropertyValue instanceof OffsetDateTime)) {

					OffsetDateTime offsetDateTime =
						(OffsetDateTime)targetPropertyValue;

					if (targetPropertyClass.isAssignableFrom(Date.class)) {
						targetPropertyValue = Date.from(
							offsetDateTime.toInstant());
					}
					else if (targetPropertyClass.isAssignableFrom(
								LocalDate.class)) {

						targetPropertyValue = offsetDateTime.toLocalDate();
					}
					else if (targetPropertyClass.isAssignableFrom(
								LocalDateTime.class)) {

						targetPropertyValue = offsetDateTime.toLocalDateTime();
					}
				}
				else if ((targetPropertyClass != null) &&
						 (targetPropertyValue instanceof Timestamp)) {

					Timestamp timestamp = (Timestamp)targetPropertyValue;

					LocalDateTime localDateTime = timestamp.toLocalDateTime();

					if (targetPropertyClass.isAssignableFrom(LocalDate.class)) {
						targetPropertyValue = localDateTime.toLocalDate();
					}
					else if (targetPropertyClass.isAssignableFrom(
								LocalDateTime.class)) {

						targetPropertyValue = localDateTime;
					}
				}
				else if (targetPropertyValue instanceof JSON ||
						 targetPropertyValue instanceof PGobject) {

					String json = String.valueOf(targetPropertyValue);

					if (json.startsWith("{")) {
						targetPropertyValue = new JSONObject(json);
					}
					else if (json.startsWith("[")) {
						targetPropertyValue = new JSONArray(json);
					}
				}

				if (targetPropertyValueClass.isArray() ||
					(targetPropertyValue instanceof Array) ||
					(targetPropertyValue instanceof ArrayList<?>)) {

					Class<?> rawClass =
						targetPropertyResolvableType.getRawClass();

					if (rawClass == null) {
						return;
					}

					Class<?> componentType = rawClass.getComponentType();

					if (componentType == null) {
						ResolvableType resolvableType =
							targetPropertyResolvableType.getGeneric(0);

						Class<?> clazz = resolvableType.getRawClass();

						if (clazz == null) {
							return;
						}

						if ((targetPropertyValue instanceof ArrayList<?>) &&
							targetPropertyClass.isAssignableFrom(List.class)) {

							ArrayList<?> targetPropertyValueArrayList =
								(ArrayList<?>)targetPropertyValue;

							if (!targetPropertyValueArrayList.isEmpty() &&
								(targetPropertyValueArrayList.get(0) instanceof
									Map)) {

								List<Object> records = new ArrayList<>();

								for (Map<String, Object> sourceMap :
										(ArrayList<Map<String, Object>>)
											targetPropertyValue) {

									Object object = clazz.newInstance();

									copyProperties(sourceMap, object);

									records.add(object);
								}

								targetPropertyValue = records;
							}
						}

						if ((targetPropertyValue instanceof ArrayList<?>) &&
							targetPropertyClass.isAssignableFrom(Set.class)) {

							if (StringUtils.equals(
									clazz.getName(), String.class.getName())) {

								targetPropertyValue = new LinkedHashSet<>(
									(ArrayList<String>)targetPropertyValue);
							}
							else if (StringUtils.equals(
										clazz.getName(),
										Long.class.getName())) {

								targetPropertyValue = new LinkedHashSet<>(
									(ArrayList<Long>)targetPropertyValue);
							}
						}

						if (targetPropertyValue instanceof PgArray) {
							PgArray pgArray = (PgArray)targetPropertyValue;

							targetPropertyValue = pgArray.getArray();
						}

						if (targetPropertyValueClass.isArray() ||
							(targetPropertyValue instanceof Array)) {

							if (StringUtils.equals(
									clazz.getName(), String.class.getName())) {

								targetPropertyValue = new LinkedHashSet<>(
									Arrays.asList(
										(String[])targetPropertyValue));
							}
							else if (StringUtils.equals(
										clazz.getName(),
										Long.class.getName())) {

								targetPropertyValue = new LinkedHashSet<>(
									Arrays.asList((Long[])targetPropertyValue));
							}
						}
					}
					else {
						if (targetPropertyClass.isAssignableFrom(
								BigDecimal[].class) &&
							(targetPropertyValue instanceof Float[])) {

							Stream<Float> stream = Arrays.stream(
								(Float[])targetPropertyValue);

							targetPropertyValue = stream.map(
								f -> {
									try {
										return new BigDecimal(f);
									}
									catch (NumberFormatException
												numberFormatException) {

										return BigDecimal.ZERO;
									}
								}
							).toArray(
								BigDecimal[]::new
							);
						}
					}
				}

				Class<?>[] classes =
					targetPropertyWriteMethod.getParameterTypes();

				if (classes[0] == BigDecimal.class) {
					targetPropertyValue = new BigDecimal(
						String.valueOf(targetPropertyValue));
				}
				else if (classes[0] == Double.class) {
					targetPropertyValue = Double.valueOf(
						String.valueOf(targetPropertyValue));
				}
				else if (classes[0] == Long.class) {
					targetPropertyValue = Long.valueOf(
						String.valueOf(targetPropertyValue));
				}

				targetPropertyWriteMethod.invoke(target, targetPropertyValue);
			}
		}
		catch (RuntimeException runtimeException) {
			throw runtimeException;
		}
		catch (Exception exception) {
			_log.error(
				"Unable to write property " + targetPropertyName, exception);
		}
	}

	private static Map<String, Object> _toSourceMap(Object source) {
		Map<String, Object> sourceMap = new HashMap<>();

		PropertyDescriptor[] sourcePropertyDescriptors =
			org.springframework.beans.BeanUtils.getPropertyDescriptors(
				source.getClass());

		for (PropertyDescriptor sourcePropertyDescriptor :
				sourcePropertyDescriptors) {

			Method sourcePropertyReadMethod =
				sourcePropertyDescriptor.getReadMethod();

			if (sourcePropertyReadMethod == null) {
				continue;
			}

			String sourcePropertyName = _getPropertyName(
				sourcePropertyDescriptor);

			sourcePropertyName = sourcePropertyName.toLowerCase();

			try {
				Object value = sourcePropertyReadMethod.invoke(source);

				if ((value == null) || StringUtil.isNull(value.toString())) {
					continue;
				}

				sourceMap.put(sourcePropertyName, value);
			}
			catch (Exception exception) {
				_log.error(
					"Unable to read property " + sourcePropertyName, exception);
			}
		}

		return sourceMap;
	}

	private static final Log _log = LogFactory.getLog(BeanUtils.class);

}