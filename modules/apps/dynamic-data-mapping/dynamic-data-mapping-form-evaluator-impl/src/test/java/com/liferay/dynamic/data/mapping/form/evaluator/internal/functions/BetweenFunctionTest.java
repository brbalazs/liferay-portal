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
public class BetweenFunctionTest {

	@Test
	public void testEvaluateFalse1() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(1, 2, 3);

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(10, 2, 9);

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse3() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(7, 7, 5);

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse4() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(6, 4, 4);

		Assert.assertFalse(result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalidArguments1() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		betweenFunction.evaluate(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalidArguments2() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		betweenFunction.evaluate(1, "2", "3");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalidArguments3() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		betweenFunction.evaluate(9, null, 10);
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(3, 2, 5);

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(4, 4, 4);

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue3() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(7, 4, 7);

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue4() throws Exception {
		BetweenFunction betweenFunction = new BetweenFunction();

		Boolean result = (Boolean)betweenFunction.evaluate(9, 9, 10);

		Assert.assertTrue(result);
	}

}