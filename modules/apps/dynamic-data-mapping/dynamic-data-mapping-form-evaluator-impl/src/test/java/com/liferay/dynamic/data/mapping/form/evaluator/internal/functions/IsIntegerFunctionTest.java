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
public class IsIntegerFunctionTest {

	@Test
	public void testEvaluateFalse() throws Exception {
		IsIntegerFunction isIntegerFunction = new IsIntegerFunction();

		Assert.assertFalse((Boolean)isIntegerFunction.evaluate("simple text"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		IsIntegerFunction isIntegerFunction = new IsIntegerFunction();

		isIntegerFunction.evaluate("test", "test2");
	}

	@Test
	public void testEvaluateTrue() throws Exception {
		IsIntegerFunction isIntegerFunction = new IsIntegerFunction();

		Assert.assertTrue((Boolean)isIntegerFunction.evaluate("3"));
		Assert.assertTrue((Boolean)isIntegerFunction.evaluate("-50"));
	}

}