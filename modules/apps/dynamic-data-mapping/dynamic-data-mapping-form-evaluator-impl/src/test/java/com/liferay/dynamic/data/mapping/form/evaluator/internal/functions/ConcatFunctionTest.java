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
public class ConcatFunctionTest {

	@Test
	public void testConcatConstants() throws Exception {
		ConcatFunction concatFunction = new ConcatFunction();

		Assert.assertEquals(
			"hello world!", concatFunction.evaluate("hello ", "world", "!"));
	}

	@Test
	public void testConcatNull() throws Exception {
		ConcatFunction concatFunction = new ConcatFunction();

		Assert.assertEquals("test", concatFunction.evaluate("test", null));
	}

	@Test
	public void testConcatNullWithConstant() throws Exception {
		ConcatFunction concatFunction = new ConcatFunction();

		Assert.assertEquals("test", concatFunction.evaluate(null, "test"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidNumberOfParameters() throws Exception {
		ConcatFunction concatFunction = new ConcatFunction();

		concatFunction.evaluate("invalid");
	}

}