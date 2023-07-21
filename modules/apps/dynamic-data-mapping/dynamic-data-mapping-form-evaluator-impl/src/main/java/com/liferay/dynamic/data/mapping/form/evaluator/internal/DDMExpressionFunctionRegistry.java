/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class DDMExpressionFunctionRegistry {

	public void applyDDMExpressionFunctions(DDMExpression<?> ddmExpression) {
		for (Map.Entry<String, DDMExpressionFunction> entry :
				_ddmExpressionFunctionsMap.entrySet()) {

			ddmExpression.setDDMExpressionFunction(
				entry.getKey(), entry.getValue());
		}
	}

	public void registerDDMExpressionFunction(
		String functionName, DDMExpressionFunction ddmExpressionFunction) {

		_ddmExpressionFunctionsMap.put(functionName, ddmExpressionFunction);
	}

	private final Map<String, DDMExpressionFunction>
		_ddmExpressionFunctionsMap = new HashMap<>();

}