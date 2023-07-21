/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.expression.DDMExpressionFunction;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;

import java.util.List;
import java.util.Map;

/**
 * @author Leonardo Barros
 */
public abstract class BaseDDMFormRuleFunction implements DDMExpressionFunction {

	public BaseDDMFormRuleFunction(
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResultsMap) {

		this.ddmFormFieldEvaluationResultsMap =
			ddmFormFieldEvaluationResultsMap;
	}

	protected List<DDMFormFieldEvaluationResult>
		getDDMFormFieldEvaluationResults(String ddmFormFieldName) {

		if (!ddmFormFieldEvaluationResultsMap.containsKey(ddmFormFieldName)) {
			throw new IllegalArgumentException("Invalid field name");
		}

		return ddmFormFieldEvaluationResultsMap.get(ddmFormFieldName);
	}

	protected final Map<String, List<DDMFormFieldEvaluationResult>>
		ddmFormFieldEvaluationResultsMap;

}