/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueAccessor;
import com.liferay.dynamic.data.mapping.model.DDMFormField;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Leonardo Barros
 */
public class GetValueFunction extends BaseDDMFormRuleFunction {

	public GetValueFunction(
		Map<String, DDMFormField> ddmFormFields,
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResultsMap,
		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker) {

		super(ddmFormFieldEvaluationResultsMap);

		_ddmFormFields = ddmFormFields;
		_ddmFormFieldTypeServicesTracker = ddmFormFieldTypeServicesTracker;
	}

	@Override
	public Object evaluate(Object... parameters) {
		if (parameters.length != 1) {
			throw new IllegalArgumentException("One parameter is expected");
		}

		String ddmFormFieldName = parameters[0].toString();

		List<DDMFormFieldEvaluationResult> ddmFormFieldEvaluationResults =
			getDDMFormFieldEvaluationResults(ddmFormFieldName);

		Stream<DDMFormFieldEvaluationResult>
			ddmFormFieldEvaluationResultStream =
				ddmFormFieldEvaluationResults.stream();

		Stream<Object> valueStream = ddmFormFieldEvaluationResultStream.map(
			result -> map(ddmFormFieldName, result.getValue()));

		Set<Object> valuesSet = valueStream.collect(Collectors.toSet());

		Object[] values = valuesSet.toArray();

		if (values.length == 1) {
			return values[0];
		}

		return values;
	}

	protected Object map(String ddmFormFieldName, Object value) {
		DDMFormField ddmFormField = _ddmFormFields.get(ddmFormFieldName);

		if (ddmFormField == null) {
			return value;
		}

		DDMFormFieldValueAccessor<?> ddmFormFieldValueAccessor =
			_ddmFormFieldTypeServicesTracker.getDDMFormFieldValueAccessor(
				ddmFormField.getType());

		if (ddmFormFieldValueAccessor == null) {
			return value;
		}

		return ddmFormFieldValueAccessor.map(value);
	}

	private final Map<String, DDMFormField> _ddmFormFields;
	private final DDMFormFieldTypeServicesTracker
		_ddmFormFieldTypeServicesTracker;

}