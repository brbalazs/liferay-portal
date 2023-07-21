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
public class IsDecimalFunctionTest {

	@Test
	public void testEvaluateFalse() throws Exception {
		IsDecimalFunction isDecimalFunction = new IsDecimalFunction();

		Assert.assertFalse((Boolean)isDecimalFunction.evaluate("simple text"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		IsDecimalFunction isDecimalFunction = new IsDecimalFunction();

		isDecimalFunction.evaluate("test", "test2");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		IsDecimalFunction isDecimalFunction = new IsDecimalFunction();

		Assert.assertTrue((Boolean)isDecimalFunction.evaluate("3"));
		Assert.assertTrue((Boolean)isDecimalFunction.evaluate("4.76"));
		Assert.assertTrue((Boolean)isDecimalFunction.evaluate("-50.67"));
	}

}