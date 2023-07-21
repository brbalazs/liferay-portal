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
public class MinFunctionTest {

	@Test
	public void testEvaluateMin1() throws Exception {
		MinFunction minFunction = new MinFunction();

		Assert.assertEquals(2.0, minFunction.evaluate(3, 2, 5, 6));
	}

	@Test
	public void testEvaluateMin2() throws Exception {
		MinFunction minFunction = new MinFunction();

		Assert.assertEquals(1.0, minFunction.evaluate(4, 3, 2, 1));
	}

	@Test
	public void testEvaluateMin3() throws Exception {
		MinFunction minFunction = new MinFunction();

		Assert.assertEquals(5.0, minFunction.evaluate(5, 6, 7, 8));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumber() throws Exception {
		MinFunction minFunction = new MinFunction();

		minFunction.evaluate(1, "invalid", 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumberOfParameters() throws Exception {
		MinFunction minFunction = new MinFunction();

		minFunction.evaluate(1);
	}

}