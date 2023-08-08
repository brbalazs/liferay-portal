/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.test.util;

import com.liferay.osb.asah.backend.model.GetterAndSetterPair;
import com.liferay.petra.string.StringPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.function.Supplier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public abstract class BaseBeanTestCase<T> {

	@Test
	public void testEqualsAndHashCode() {
		T instance = newInstance();

		EqualsVerifier<? extends Object> equalsVerifier =
			EqualsVerifier.forClass(instance.getClass());

		equalsVerifier.suppress(
			Warning.NONFINAL_FIELDS, Warning.STRICT_INHERITANCE);

		equalsVerifier.verify();
	}

	@Test
	public void testGettersAndSetters() throws Exception {
		T instance = newInstance();

		Class<?> clazz = instance.getClass();

		Map<String, GetterAndSetterPair> getterAndSetterPairs =
			_getGetterSetterPairs();

		for (Map.Entry<String, GetterAndSetterPair> entry :
				getterAndSetterPairs.entrySet()) {

			GetterAndSetterPair getterSetterPair = entry.getValue();

			String objectName = entry.getKey();

			String firstChar = objectName.substring(0, 1);

			String fieldName =
				firstChar.toLowerCase() + objectName.substring(1);

			if (getterSetterPair.hasGetMethodAndSetMethod()) {
				Method setMethod = getterSetterPair.getSetMethod();

				Object object = _newInstance(
					setMethod.getParameterTypes()[0], fieldName);

				setMethod.invoke(instance, object);

				_invokeGetMethod(
					fieldName, getterSetterPair.getGetMethod(), instance,
					object);
			}
			else if (getterSetterPair.getGetMethod() != null) {
				Field field = null;

				try {
					field = clazz.getDeclaredField(
						StringPool.UNDERLINE + fieldName);
				}
				catch (Exception exception) {
					field = clazz.getDeclaredField(fieldName);
				}

				field.setAccessible(true);

				Method getMethod = getterSetterPair.getGetMethod();

				Object object = _newInstance(
					getMethod.getReturnType(), fieldName);

				field.set(instance, object);

				_invokeGetMethod(
					fieldName, getterSetterPair.getGetMethod(), instance,
					object);
			}
		}
	}

	protected BaseBeanTestCase() {
		this(null, null);
	}

	protected BaseBeanTestCase(
		Map<Class<?>, Supplier<?>> classSupliers,
		List<String> ignoredMethodNames) {

		if (classSupliers == null) {
			_classSuppliers = _defaultClassSuppliers;
		}
		else {
			_classSuppliers = new HashMap<Class<?>, Supplier<?>>() {
				{
					putAll(classSupliers);
					putAll(_defaultClassSuppliers);
				}
			};
		}

		_ignoredMethodNames = new HashSet<>();

		_ignoredMethodNames.add("getClass");

		if (ignoredMethodNames != null) {
			_ignoredMethodNames.addAll(ignoredMethodNames);
		}
	}

	protected abstract T newInstance();

	private Map<String, GetterAndSetterPair> _getGetterSetterPairs() {
		Map<String, GetterAndSetterPair> getterSetterPairs = new TreeMap<>();

		T instance = newInstance();

		Class<?> clazz = instance.getClass();

		for (Method method : clazz.getMethods()) {
			String methodName = method.getName();

			if (_ignoredMethodNames.contains(methodName)) {
				continue;
			}

			Parameter[] parameters = method.getParameters();

			if (methodName.startsWith("get") && (parameters.length == 0)) {
				String objectName = methodName.substring("get".length());

				GetterAndSetterPair getterSetterPair = getterSetterPairs.get(
					objectName);

				if (getterSetterPair == null) {
					getterSetterPair = new GetterAndSetterPair();

					getterSetterPairs.put(objectName, getterSetterPair);
				}

				getterSetterPair.setGetMethod(method);
			}
			else if (methodName.startsWith("is") && (parameters.length == 0)) {
				String objectName = methodName.substring("is".length());

				GetterAndSetterPair getterSetterPair = getterSetterPairs.get(
					objectName);

				if (getterSetterPair == null) {
					getterSetterPair = new GetterAndSetterPair();

					getterSetterPairs.put(objectName, getterSetterPair);
				}

				getterSetterPair.setGetMethod(method);
			}
			else if (methodName.startsWith("set") && (parameters.length == 1)) {
				String objectName = methodName.substring("set".length());

				GetterAndSetterPair getterSetterPair = getterSetterPairs.get(
					objectName);

				if (getterSetterPair == null) {
					getterSetterPair = new GetterAndSetterPair();

					getterSetterPairs.put(objectName, getterSetterPair);
				}

				getterSetterPair.setSetMethod(method);
			}
		}

		return getterSetterPairs;
	}

	private void _invokeGetMethod(
			String fieldName, Method getMethod, T instance,
			Object expectedObject)
		throws Exception {

		Object actualObject = getMethod.invoke(instance);

		Assertions.assertEquals(
			expectedObject, actualObject, fieldName + " is different");
	}

	private Object _newInstance(Class<?> clazz, String fieldName) {
		try {
			Supplier<?> supplier = _classSuppliers.get(clazz);

			if (supplier != null) {
				return supplier.get();
			}

			if (clazz.isEnum()) {
				return clazz.getEnumConstants()[0];
			}

			return clazz.newInstance();
		}
		catch (IllegalAccessException | InstantiationException exception) {
			throw new RuntimeException(
				"Unable to instantiate class for field " + fieldName,
				exception);
		}
	}

	private static final Map<Class<?>, Supplier<?>> _defaultClassSuppliers =
		new HashMap<Class<?>, Supplier<?>>() {
			{
				put(BigDecimal.class, () -> BigDecimal.ONE);
				put(Boolean.class, () -> Boolean.TRUE);
				put(Byte.class, () -> Byte.valueOf((byte)0));
				put(Character.class, () -> Character.valueOf((char)0));
				put(Date.class, Date::new);
				put(Double.class, () -> Double.valueOf(0.0));
				put(Float.class, () -> Float.valueOf(0.0F));
				put(Integer.class, () -> Integer.valueOf(0));
				put(List.class, Collections::emptyList);
				put(LocalDateTime.class, LocalDateTime::now);
				put(Long.class, () -> Long.valueOf(0));
				put(Map.class, Collections::emptyMap);
				put(Set.class, Collections::emptySet);
				put(Short.class, () -> Short.valueOf((short)0));
				put(SortedMap.class, Collections::emptySortedMap);
				put(SortedSet.class, Collections::emptySortedSet);
				put(boolean.class, () -> true);
				put(byte.class, () -> (byte)0);
				put(char.class, () -> (char)0);
				put(double.class, () -> 0.0D);
				put(float.class, () -> 0.0F);
				put(int.class, () -> 0);
				put(long.class, () -> 0L);
				put(short.class, () -> (short)0);
			}
		};

	private final Map<Class<?>, Supplier<?>> _classSuppliers;
	private final Set<String> _ignoredMethodNames;

}