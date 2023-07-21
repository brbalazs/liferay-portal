/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression;

import aQute.bnd.annotation.ProviderType;

import com.liferay.dynamic.data.mapping.expression.model.Expression;

import java.math.MathContext;

import java.util.Map;

/**
 * @author Miguel Angelo Caldas Gallindo
 */
@ProviderType
public interface DDMExpression<T> {

	public T evaluate() throws DDMExpressionException;

	public Expression getModel();

	public Map<String, VariableDependencies> getVariableDependenciesMap()
		throws DDMExpressionException;

	public void setBooleanVariableValue(
		String variableName, Boolean variableValue);

	public void setDDMExpressionFunction(
		String functionName, DDMExpressionFunction ddmExpressionFunction);

	public void setDoubleVariableValue(
		String variableName, Double variableValue);

	public void setExpressionStringVariableValue(
		String variableName, String variableValue);

	public void setFloatVariableValue(String variableName, Float variableValue);

	public void setIntegerVariableValue(
		String variableName, Integer variableValue);

	public void setLongVariableValue(String variableName, Long variableValue);

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	public void setMathContext(MathContext mathContext);

	public void setNumberVariableValue(
		String variableName, Number variableValue);

	public void setObjectVariableValue(
		String variableName, Object variableValue);

	public void setStringVariableValue(
			String variableName, String variableValue)
		throws DDMExpressionException;

}