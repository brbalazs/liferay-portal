/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.portal.kernel.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marcellus Tavares
 */
public abstract class BaseDDMFormRuleFunctionTestCase {

	protected DDMFormFieldEvaluationResult createDDMFormFieldEvaluationResult(
		String fieldName, String propertyName, Object propertyValue) {

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult(
				fieldName, StringUtil.randomString());

		ddmFormFieldEvaluationResult.setProperty(propertyName, propertyValue);

		return ddmFormFieldEvaluationResult;
	}

	protected Map<String, List<DDMFormFieldEvaluationResult>>
		createDDMFormFieldEvaluationResultsMap(
			DDMFormFieldEvaluationResult... ddmFormFieldEvaluationResultArray) {

		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResultsMap = new HashMap<>();

		for (DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult :
				ddmFormFieldEvaluationResultArray) {

			List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults =
				ddmFormFieldEvaluationResultsMap.get(
					ddmFormFieldEvaluationResult.getName());

			if (ddmFormFieldEvaluationResults == null) {
				ddmFormFieldEvaluationResults = new ArrayList<>();

				ddmFormFieldEvaluationResultsMap.put(
					ddmFormFieldEvaluationResult.getName(),
					ddmFormFieldEvaluationResults);
			}

			ddmFormFieldEvaluationResults.add(ddmFormFieldEvaluationResult);
		}

		return ddmFormFieldEvaluationResultsMap;
	}

}