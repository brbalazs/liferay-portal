/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFactory;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.internal.DDMExpressionFunctionRegistry;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.util.stream.Stream;

/**
 * @author Leonardo Barros
 */
public class AllFunction implements DDMExpressionFunction {

	public AllFunction(
		DDMExpressionFactory ddmExpressionFactory,
		DDMExpressionFunctionRegistry ddmExpressionFunctionRegistry) {

		_ddmExpressionFactory = ddmExpressionFactory;
		_ddmExpressionFunctionRegistry = ddmExpressionFunctionRegistry;
	}

	@Override
	public Object evaluate(Object... parameters) {
		if ((parameters == null) || (parameters.length < 1)) {
			throw new IllegalArgumentException(
				"At least one parameter is expected");
		}

		if (parameters.length == 1) {
			return false;
		}

		String expression = String.valueOf(parameters[0]);

		if (!expression.contains("#value#")) {
			return false;
		}

		Object[] values = null;

		if (isArray(parameters[1])) {
			values = (Object[])parameters[1];

			if (values.length == 0) {
				return false;
			}
		}
		else {
			values = new Object[] {parameters[1]};
		}

		return Stream.of(
			values
		).allMatch(
			value -> accept(expression, value)
		);
	}

	protected boolean accept(String expression, Object value) {
		expression = expression.replace("#value#", String.valueOf(value));

		try {
			DDMExpression<Boolean> ddmExpression =
				_ddmExpressionFactory.createBooleanDDMExpression(expression);

			_ddmExpressionFunctionRegistry.applyDDMExpressionFunctions(
				ddmExpression);

			return ddmExpression.evaluate();
		}
		catch (DDMExpressionException ddmee) {
			if (_log.isDebugEnabled()) {
				_log.debug(ddmee, ddmee);
			}
		}

		return false;
	}

	protected boolean isArray(Object parameter) {
		Class<?> clazz = parameter.getClass();

		return clazz.isArray();
	}

	private static final Log _log = LogFactoryUtil.getLog(AllFunction.class);

	private final DDMExpressionFactory _ddmExpressionFactory;
	private final DDMExpressionFunctionRegistry _ddmExpressionFunctionRegistry;

}