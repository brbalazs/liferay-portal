/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public class JumpPageFunctionTest {

	@Test
	public void testEvaluate() {
		Map<Integer, Integer> pageFlow = new HashMap<>();

		JumpPageFunction jumpPageFunction = new JumpPageFunction(pageFlow);

		Object result = jumpPageFunction.evaluate(1.0, 4.0);

		Assert.assertTrue((boolean)result);

		Assert.assertEquals(4, (int)pageFlow.get(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		JumpPageFunction jumpPageFunction = new JumpPageFunction(null);

		jumpPageFunction.evaluate();
	}

}