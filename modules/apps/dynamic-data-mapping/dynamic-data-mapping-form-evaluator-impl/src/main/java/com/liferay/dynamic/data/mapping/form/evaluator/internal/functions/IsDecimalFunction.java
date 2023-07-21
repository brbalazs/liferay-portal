/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import org.apache.commons.lang.math.NumberUtils;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=isDecimal",
	service = DDMExpressionFunction.class
)
public class IsDecimalFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 1) {
			throw new IllegalArgumentException("One parameter is expected");
		}

		Double value = NumberUtils.toDouble(
			parameters[0].toString(), Double.NaN);

		return !value.isNaN();
	}

}