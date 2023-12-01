/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.staging.test.util;

import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.Field;

/**
 * @author Lily Chi
 */
public class PropsValuesReplacer implements AutoCloseable {

	public PropsValuesReplacer(String name, Object value) throws Exception {
		Field propsKeysField = ReflectionUtil.getDeclaredField(
			PropsKeys.class, name);

		_propsKeysName = (String)propsKeysField.get(null);

		_propsKeysOldValue = PropsUtil.get(_propsKeysName);

		PropsUtil.set(_propsKeysName, String.valueOf(value));

		_propsValuesField = ReflectionUtil.getDeclaredField(
			PropsValues.class, name);

		_propsValuesOldValue = _propsValuesField.get(null);

		_propsValuesField.set(null, value);
	}

	@Override
	public void close() throws Exception {
		PropsUtil.set(_propsKeysName, _propsKeysOldValue);

		_propsValuesField.set(null, _propsValuesOldValue);
	}

	private final String _propsKeysName;
	private final String _propsKeysOldValue;
	private final Field _propsValuesField;
	private final Object _propsValuesOldValue;

}