/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Leonardo Barros
 */
public class ContainsFunctionTest {

	@Test
	public void testCaseInsensitiveComparison() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"Some test", "Test");

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateFalse1() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"another text", "not contains");

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			null, "not contains");

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse3() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"simple text", null);

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse4() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"text", "simple text");

		Assert.assertFalse(result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		containsFunction.evaluate("test");
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"another text", "another");

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		ContainsFunction containsFunction = new ContainsFunction();

		Boolean result = (Boolean)containsFunction.evaluate(
			"not contains 2", 2);

		Assert.assertTrue(result);
	}

}