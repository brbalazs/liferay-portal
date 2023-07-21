/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.portal.kernel.util.Validator;

import java.util.stream.Stream;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=isEmpty",
	service = DDMExpressionFunction.class
)
public class IsEmptyFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters == null) {
			return true;
		}

		if ((parameters.length == 1) && isArray(parameters[0])) {
			Object[] values = (Object[])parameters[0];

			if (values.length == 0) {
				return true;
			}

			return !Stream.of(
				values
			).anyMatch(
				Validator::isNotNull
			);
		}

		return Validator.isNull(parameters[0]);
	}

	protected boolean isArray(Object parameter) {
		Class<?> clazz = parameter.getClass();

		return clazz.isArray();
	}

}