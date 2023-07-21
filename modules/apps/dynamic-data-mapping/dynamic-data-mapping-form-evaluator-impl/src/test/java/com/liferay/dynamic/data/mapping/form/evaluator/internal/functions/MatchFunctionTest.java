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
public class MatchFunctionTest {

	@Test
	public void testEvaluateFalse1() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		Assert.assertFalse((boolean)matchFunction.evaluate("texto", "[0-9]+"));
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		Assert.assertFalse((boolean)matchFunction.evaluate("123", "[a-z]+"));
	}

	@Test
	public void testEvaluateFalse3() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		Assert.assertFalse((boolean)matchFunction.evaluate("invalid*", "\\w+"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		matchFunction.evaluate("value");
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		Assert.assertTrue(
			(boolean)matchFunction.evaluate("Liferay123", "Liferay[0-9]{3}"));
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		MatchFunction matchFunction = new MatchFunction();

		Assert.assertTrue(
			(boolean)matchFunction.evaluate(
				"admin@liferay.com", "\\w+@liferay.com"));
	}

}