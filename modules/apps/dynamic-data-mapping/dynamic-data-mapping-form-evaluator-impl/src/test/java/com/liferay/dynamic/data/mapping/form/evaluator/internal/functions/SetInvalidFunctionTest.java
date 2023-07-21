/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.dynamic.data.mapping.form.evaluator.DDMFormFieldEvaluationResult;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class SetInvalidFunctionTest extends BaseDDMFormRuleFunctionTestCase {

	@Test
	public void testEvaluate() {
		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult1 =
			createDDMFormFieldEvaluationResult(
				"Field_1", "valid", RandomTestUtil.randomBoolean());

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult2 =
			createDDMFormFieldEvaluationResult(
				"Field_1", "valid", RandomTestUtil.randomBoolean());

		DDMFormFieldEvaluationResult ddmFormFieldEvaluationResult3 =
			createDDMFormFieldEvaluationResult("Field_2", "valid", true);

		Map<String, List<DDMFormFieldEvaluationResult>>
			ddmFormFieldEvaluationResultsMap =
				createDDMFormFieldEvaluationResultsMap(
					ddmFormFieldEvaluationResult1,
					ddmFormFieldEvaluationResult2,
					ddmFormFieldEvaluationResult3);

		SetInvalidFunction setInvalidFunction = new SetInvalidFunction(
			ddmFormFieldEvaluationResultsMap);

		setInvalidFunction.evaluate("Field_1", "Error Field 1");

		Assert.assertFalse(ddmFormFieldEvaluationResult1.isValid());
		Assert.assertEquals(
			"Error Field 1", ddmFormFieldEvaluationResult1.getErrorMessage());

		Assert.assertFalse(ddmFormFieldEvaluationResult2.isValid());
		Assert.assertEquals(
			"Error Field 1", ddmFormFieldEvaluationResult2.getErrorMessage());

		Assert.assertTrue(ddmFormFieldEvaluationResult3.isValid());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		SetInvalidFunction setInvalidFunction = new SetInvalidFunction(null);

		setInvalidFunction.evaluate("param1");
	}

}