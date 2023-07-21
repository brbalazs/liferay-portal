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
	immediate = true, property = "ddm.form.evaluator.function.name=max",
	service = DDMExpressionFunction.class
)
public class MaxFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length < 2) {
			throw new IllegalArgumentException(
				"Two or more parameters are expected");
		}

		double max = Double.MIN_VALUE;

		for (Object parameter : parameters) {
			if (!Number.class.isInstance(parameter)) {
				throw new IllegalArgumentException(
					"The parameters should be numbers");
			}

			double parameterDouble = ((Number)parameter).doubleValue();

			if (parameterDouble > max) {
				max = parameterDouble;
			}
		}

		return max;
	}

}