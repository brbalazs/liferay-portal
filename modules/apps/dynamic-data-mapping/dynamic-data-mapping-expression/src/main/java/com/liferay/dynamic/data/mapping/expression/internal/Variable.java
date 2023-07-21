/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression.internal;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
public class Variable {

	public Variable(String name) {
		_name = name;
	}

	public String getExpressionString() {
		return _expressionString;
	}

	public String getName() {
		return _name;
	}

	public Object getValue() {
		return _value;
	}

	public void setExpressionString(String expressionString) {
		_expressionString = expressionString;
	}

	public void setValue(Object value) {
		_value = value;
	}

	private String _expressionString;
	private final String _name;
	private Object _value;

}