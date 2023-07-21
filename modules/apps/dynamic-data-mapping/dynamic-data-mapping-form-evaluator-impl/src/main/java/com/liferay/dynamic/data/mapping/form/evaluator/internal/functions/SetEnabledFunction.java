/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;

import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public class SetEnabledFunction extends SetPropertyFunction {

	public SetEnabledFunction(
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults) {

		super(ddmFormFieldEvaluationResults, "readOnly");
	}

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 2) {
			throw new IllegalArgumentException("Two parameters are expected");
		}

		String fieldName = parameters[0].toString();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults =
			getDDMFormFieldEvaluationResults(fieldName);

		boolean propertyValue = (Boolean)parameters[1];

		for (DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult :
				ddmFormFieldEvaluationResults) {

			ddmFormFieldEvaluationResult.setReadOnly(!propertyValue);
		}

		return true;
	}

}