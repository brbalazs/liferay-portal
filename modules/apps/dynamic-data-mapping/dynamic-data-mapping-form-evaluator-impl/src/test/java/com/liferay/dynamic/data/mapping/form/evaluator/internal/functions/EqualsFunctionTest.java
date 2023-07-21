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
public class EqualsFunctionTest {

	@Test
	public void testEvaluateFalse1() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		Boolean result = (Boolean)equalsFunction.evaluate(null, "not equals");

		Assert.assertFalse(result);
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		Boolean result = (Boolean)equalsFunction.evaluate("text", null);

		Assert.assertFalse(result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		equalsFunction.evaluate("test");
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		Boolean result = (Boolean)equalsFunction.evaluate(
			"simple text", "simple text");

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		Boolean result = (Boolean)equalsFunction.evaluate(2, 2);

		Assert.assertTrue(result);
	}

	@Test
	public void testEvaluateTrue3() throws Exception {
		EqualsFunction equalsFunction = new EqualsFunction();

		Boolean result = (Boolean)equalsFunction.evaluate(1, "1");

		Assert.assertTrue(result);
	}

}