/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.util.Validator;

import org.osgi.service.component.annotations.Component;

/**
 * @author Leonardo Barros
 */
@Component(
	immediate = true, property = "ddm.form.evaluator.function.name=concat",
	service = DDMExpressionFunction.class
)
public class ConcatFunction implements DDMExpressionFunction {

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length < 2) {
			throw new IllegalArgumentException(
				"Two or more parameters are expected");
		}

		StringBundler sb = new StringBundler(parameters.length);

		for (Object parameter : parameters) {
			String string = (String)parameter;

			if (Validator.isNull(string)) {
				continue;
			}

			sb.append(string);
		}

		return sb.toString();
	}

}