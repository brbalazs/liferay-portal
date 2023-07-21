/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.portal.json.JSONFactoryImpl;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.util.KeyValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(MockitoJUnitRunner.class)
public class CallFunctionTest {

	@Test
	public void testAutoSelectOption() {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult("field0", "1");

		ddmFormFieldEvaluationResult.setRequired(true);

		ddmFormFieldEvaluationResults.put(
			"field0", Arrays.asList(ddmFormFieldEvaluationResult));

		CallFunction callFunction = new CallFunction(
			null, ddmFormFieldEvaluationResults, null, _jsonFactory);

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		keyValuePairs.add(new KeyValuePair("key_1", "value_1"));

		callFunction.setDDMFormFieldOptions("field0", keyValuePairs);

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		jsonArray.put("key_1");

		Object value = ddmFormFieldEvaluationResult.getValue();

		Assert.assertEquals(jsonArray.toString(), value.toString());
	}

	@Test
	public void testGetFieldValueFromJSONArray() {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult("field0", "1");

		JSONArray jsonArray = _jsonFactory.createJSONArray();

		jsonArray.put("test");

		ddmFormFieldEvaluationResult.setValue(jsonArray);

		ddmFormFieldEvaluationResults.put(
			"field0", Arrays.asList(ddmFormFieldEvaluationResult));

		CallFunction callFunction = new CallFunction(
			null, ddmFormFieldEvaluationResults, null, _jsonFactory);

		Assert.assertEquals(
			"test", callFunction.getDDMFormFieldValue("field0"));
	}

	@Test
	public void testGetFieldValueFromString() {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult("field0", "1");

		ddmFormFieldEvaluationResult.setValue("test");

		ddmFormFieldEvaluationResults.put(
			"field0", Arrays.asList(ddmFormFieldEvaluationResult));

		CallFunction callFunction = new CallFunction(
			null, ddmFormFieldEvaluationResults, null, _jsonFactory);

		Assert.assertEquals(
			"test", callFunction.getDDMFormFieldValue("field0"));
	}

	@Test
	public void testNotAutoSelectOption() throws Exception {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult =
			new DDMFormFieldEvaluationResult("field0", "1");

		ddmFormFieldEvaluationResults.put(
			"field0", Arrays.asList(ddmFormFieldEvaluationResult));

		CallFunction callFunction = new CallFunction(
			null, ddmFormFieldEvaluationResults, null, _jsonFactory);

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		keyValuePairs.add(new KeyValuePair("key_1", "value_1"));
		keyValuePairs.add(new KeyValuePair("key_2", "value_2"));

		callFunction.setDDMFormFieldOptions("field0", keyValuePairs);

		Assert.assertNull(ddmFormFieldEvaluationResult.getValue());
	}

	@Test
	public void testSetDDMFormFieldOptionsRepeatableFields() {
		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResults = new HashMap<>();

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			new DDMFormFieldEvaluationResult("field0", "1");

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult2 =
			new DDMFormFieldEvaluationResult("field0", "2");

		ddmFormFieldEvaluationResults.put(
			"field0",
			Arrays.asList(
				ddmFormFieldEvaluationResult1, ddmFormFieldEvaluationResult2));

		CallFunction callFunction = new CallFunction(
			null, ddmFormFieldEvaluationResults, null, _jsonFactory);

		List<KeyValuePair> keyValuePairs = new ArrayList<>();

		keyValuePairs.add(new KeyValuePair("key_1", "value_1"));
		keyValuePairs.add(new KeyValuePair("key_2", "value_2"));

		callFunction.setDDMFormFieldOptions("field0", keyValuePairs);

		Assert.assertEquals(
			keyValuePairs,
			ddmFormFieldEvaluationResult1.getProperty("options"));
		Assert.assertEquals(
			keyValuePairs,
			ddmFormFieldEvaluationResult2.getProperty("options"));
	}

	private final JSONFactory _jsonFactory = new JSONFactoryImpl();

}