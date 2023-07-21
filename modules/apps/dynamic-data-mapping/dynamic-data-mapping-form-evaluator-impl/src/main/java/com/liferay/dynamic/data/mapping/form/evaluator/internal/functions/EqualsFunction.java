/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.portal.kernel.util.GetterUtil;

import org.apache.commons.lang.math.NumberUtils;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=equals",
	service = DDMExpressionFunction.class
)
public class EqualsFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		Object parameter1 = parameters[0];
		Object parameter2 = parameters[1];

		if ((parameter1 == null) || (parameter2 == null)) {
			return false;
		}

		if (NumberUtils.isNumber(parameter1.toString())) {
			parameter1 = GetterUtil.getDouble(parameter1);
		}

		if (NumberUtils.isNumber(parameter2.toString())) {
			parameter2 = GetterUtil.getDouble(parameter2);
		}

		return parameter1.equals(parameter2);
	}

}