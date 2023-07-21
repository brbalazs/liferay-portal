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
public class MaxFunctionTest {

	@Test
	public void testEvaluateMax1() throws Exception {
		MaxFunction maxFunction = new MaxFunction();

		Assert.assertEquals(6.0, maxFunction.evaluate(3, 2, 5, 6));
	}

	@Test
	public void testEvaluateMax2() throws Exception {
		MaxFunction maxFunction = new MaxFunction();

		Assert.assertEquals(4.0, maxFunction.evaluate(4, 3, 2, 1));
	}

	@Test
	public void testEvaluateMax3() throws Exception {
		MaxFunction maxFunction = new MaxFunction();

		Assert.assertEquals(8.0, maxFunction.evaluate(5, 6, 7, 8));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumber() throws Exception {
		MaxFunction maxFunction = new MaxFunction();

		maxFunction.evaluate(1, "invalid", 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumberOfParameters() throws Exception {
		MaxFunction maxFunction = new MaxFunction();

		maxFunction.evaluate(1);
	}

}