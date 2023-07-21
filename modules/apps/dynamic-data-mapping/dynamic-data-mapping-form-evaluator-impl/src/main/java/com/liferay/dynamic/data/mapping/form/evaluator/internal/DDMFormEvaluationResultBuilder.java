/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormEvaluationResult;
import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.petra.string.StringBundler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Rafael Praxedes
 */
public class DDMFormEvaluationResultBuilder {

	public static DDMFormEvaluationResult build(
		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults,
		Set<Integer> disabledPagesIndexes) {

		DDMFormEvaluationResult ddmFormEvaluationResult =
			new DDMFormEvaluationResult();

		ddmFormEvaluationResult.setDDMFormFieldEvaluationResults(
			ddmFormFieldEvaluationResults);
		ddmFormEvaluationResult.setDDMFormFieldEvaluationResultsMap(
			createDDMFormFieldEvaluationResultsMap(
				ddmFormFieldEvaluationResults));

		ddmFormEvaluationResult.setDisabledPagesIndexes(disabledPagesIndexes);

		return ddmFormEvaluationResult;
	}

	protected static Map<String, DDMFormFieldEvaluationResult>
		createDDMFormFieldEvaluationResultsMap(
			List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults) {

		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultsMap = new HashMap<>();

		populateDDMFormFieldEvaluationResultsMap(
			ddmFormFieldEvaluationResults, ddmFormFieldEvaluationResultsMap);

		return ddmFormFieldEvaluationResultsMap;
	}

	protected static void populateDDMFormFieldEvaluationResultsMap(
		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults,
		Map<String, DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultsMap) {

		for (DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult :
				ddmFormFieldEvaluationResults) {

			String key = StringBundler.concat(
				ddmFormFieldEvaluationResult.getName(), "_INSTANCE_",
				ddmFormFieldEvaluationResult.getInstanceId());

			ddmFormFieldEvaluationResultsMap.put(
				key, ddmFormFieldEvaluationResult);

			populateDDMFormFieldEvaluationResultsMap(
				ddmFormFieldEvaluationResult.
					getNestedDDMFormFieldEvaluationResults(),
				ddmFormFieldEvaluationResultsMap);
		}
	}

}