/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.data.provider;

/**
 * @author Marcellus Tavares
 */
public class DDMDataProviderResponseOutput {

	public static DDMDataProviderResponseOutput of(
		String name, String type, Object value) {

		return new DDMDataProviderResponseOutput(name, type, value);
	}

	public String getName() {
		return _name;
	}

	public String getType() {
		return _type;
	}

	public <T> T getValue(Class<T> valueType) {
		return valueType.cast(_value);
	}

	private DDMDataProviderResponseOutput(
		String name, String type, Object value) {

		_name = name;
		_type = type;
		_value = value;
	}

	private final String _name;
	private final String _type;
	private final Object _value;

}