/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression;

import aQute.bnd.annotation.ProviderType;

/**
 * @author Marcellus Tavares
 */
@ProviderType
public interface DDMExpressionFactory {

	public DDMExpression<Boolean> createBooleanDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<Double> createDoubleDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<Float> createFloatDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<Integer> createIntegerDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<Long> createLongDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<Number> createNumberDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

	public DDMExpression<String> createStringDDMExpression(
			String ddmExpressionString)
		throws DDMExpressionException;

}