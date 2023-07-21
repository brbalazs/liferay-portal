/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true,
	property = {
		"ddm.form.evaluator.function.available.on.calculation.rule=true",
		"ddm.form.evaluator.function.name=sum"
	},
	service = DDMExpressionFunction.class
)
public class SumFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		Object[] values = null;

		if ((parameters.length == 1) && isArray(parameters[0])) {
			values = (Object[])parameters[0];
		}
		else {
			values = parameters;
		}

		double sum = 0;

		boolean integerSum = true;

		for (Object value : values) {
			if (!Number.class.isInstance(value)) {
				continue;
			}

			if (!Integer.class.isInstance(value)) {
				integerSum = false;
			}

			Number number = (Number)value;

			sum += number.doubleValue();
		}

		if (integerSum) {
			return (int)sum;
		}

		return sum;
	}

	protected boolean isArray(Object parameter) {
		Class<?> clazz = parameter.getClass();

		return clazz.isArray();
	}

}