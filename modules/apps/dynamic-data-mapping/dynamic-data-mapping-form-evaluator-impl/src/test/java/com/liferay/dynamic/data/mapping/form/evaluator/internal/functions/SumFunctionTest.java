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
public class SumFunctionTest {

	@Test
	public void testEvaluateArray1() throws Exception {
		SumFunction sumFunction = new SumFunction();

		Object parameters = new Integer[] {1, 2, 4};

		Assert.assertEquals(7, sumFunction.evaluate(parameters));
	}

	@Test
	public void testEvaluateArray2() throws Exception {
		SumFunction sumFunction = new SumFunction();

		Object parameters = new Double[] {3.8, 5D, 7D};

		Assert.assertEquals(15.8, sumFunction.evaluate(parameters));
	}

	@Test
	public void testEvaluateEquals1() throws Exception {
		SumFunction sumFunction = new SumFunction();

		Assert.assertEquals(5, sumFunction.evaluate(2, 3));
	}

	@Test
	public void testEvaluateEquals2() throws Exception {
		SumFunction sumFunction = new SumFunction();

		Assert.assertEquals(21.4D, sumFunction.evaluate(1, 13.4, 7));
	}

}