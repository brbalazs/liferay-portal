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
public class IsEmptyFunctionTest {

	@Test
	public void testEvaluateFalse1() throws Exception {
		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate("test"));
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate(0));
	}

	@Test
	public void testEvaluateFalse3() throws Exception {
		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate(false));
	}

	@Test
	public void testEvaluateFalse4() throws Exception {
		Object parameters = new Integer[] {1, 2};

		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate(parameters));
	}

	@Test
	public void testEvaluateFalse5() throws Exception {
		Object parameters = new Double[] {3.0};

		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate(parameters));
	}

	@Test
	public void testEvaluateFalse6() throws Exception {
		Object parameters = new String[] {"", "test"};

		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertFalse((Boolean)isEmptyFunction.evaluate(parameters));
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertTrue((Boolean)isEmptyFunction.evaluate(""));
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertTrue((Boolean)isEmptyFunction.evaluate(null));
	}

	@Test
	public void testEvaluateTrue3() throws Exception {
		Object parameters = new String[] {"", ""};

		IsEmptyFunction isEmptyFunction = new IsEmptyFunction();

		Assert.assertTrue((Boolean)isEmptyFunction.evaluate(parameters));
	}

}