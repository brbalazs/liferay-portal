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
	immediate = true, property = "ddm.form.evaluator.function.name=between",
	service = DDMExpressionFunction.class
)
public class BetweenFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 3) {
			throw new IllegalArgumentException("Three parameters are expected");
		}

		if (!Number.class.isInstance(parameters[0]) ||
			!Number.class.isInstance(parameters[1]) ||
			!Number.class.isInstance(parameters[2])) {

			throw new IllegalArgumentException(
				"The parameters should be numbers");
		}

		Number parameter = (Number)parameters[0];

		Number minParameter = (Number)parameters[1];
		Number maxParameter = (Number)parameters[2];

		if ((parameter.doubleValue() >= minParameter.doubleValue()) &&
			(parameter.doubleValue() <= maxParameter.doubleValue())) {

			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}

}