/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression.internal;

import com.liferay.dynamic.data.mapping.expression.DDMExpression;
import com.liferay.dynamic.data.mapping.expression.DDMExpressionException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class LogicalEvaluationTest {

	@Test
	public void testAndExpression() throws Exception {
		Assert.assertTrue(evaluate("true and true"));
		Assert.assertFalse(evaluate("true AND false"));
		Assert.assertFalse(evaluate("false && true"));
		Assert.assertFalse(evaluate("false & false"));
	}

	@Test
	public void testCombinedExpression() throws Exception {
		boolean expected = false;

		if (true && (false || true) && (false || false)) {
			expected = true;
		}

		Assert.assertEquals(
			expected, evaluate("true && (false || true) && (false || false)"));

		expected = ((true && (2 > 1)) || (4.0 < 3)) && (((2 + 1) > 5) || true);

		Assert.assertEquals(
			expected,
			evaluate(
				"((true && 2 > 1) || (4.0 < 3)) && ((2 + 1) > 5 || true)"));
	}

	@Test
	public void testDifferentTypesExpression() throws Exception {
		Assert.assertTrue(evaluate("true != 1"));
		Assert.assertFalse(evaluate("true == \"true\""));
	}

	@Test
	public void testEqualsExpression() throws Exception {
		Assert.assertTrue(evaluate("true = true"));
		Assert.assertTrue(evaluate("1 = 1.0"));
		Assert.assertTrue(evaluate("\"Joe\" == \"Joe\""));
		Assert.assertFalse(evaluate("true == FALSE"));
	}

	@Test
	public void testFalseConstant() throws Exception {
		Assert.assertFalse(evaluate("false"));
		Assert.assertFalse(evaluate("FALSE"));
	}

	@Test(expected = DDMExpressionException.class)
	public void testIncompatibleTypesExpression1() throws Exception {
		Assert.assertTrue(evaluate("true > 1"));
	}

	@Test(expected = DDMExpressionException.class)
	public void testIncompatibleTypesExpression2() throws Exception {
		Assert.assertTrue(evaluate("false >= 1"));
	}

	@Test(expected = DDMExpressionException.class)
	public void testIncompatibleTypesExpression3() throws Exception {
		Assert.assertTrue(evaluate("false < \"Joe\""));
	}

	@Test(expected = DDMExpressionException.class)
	public void testIncompatibleTypesExpression4() throws Exception {
		Assert.assertTrue(evaluate("false <= 1.5"));
	}

	@Test
	public void testNotEqualsExpression() throws Exception {
		Assert.assertFalse(evaluate("true != true"));
		Assert.assertFalse(evaluate("1 <> 1"));
		Assert.assertFalse(evaluate("\"Joe\" <> \"Joe\""));
		Assert.assertTrue(evaluate("true <> false"));
	}

	@Test
	public void testNotExpression() throws Exception {
		Assert.assertTrue(evaluate("not false"));
		Assert.assertFalse(evaluate("not true"));
		Assert.assertFalse(evaluate("NOT(TRUE)"));
		Assert.assertTrue(evaluate("NOT(FALSE)"));
	}

	@Test
	public void testOrExpression() throws Exception {
		Assert.assertTrue(evaluate("true or true"));
		Assert.assertTrue(evaluate("true OR false"));
		Assert.assertTrue(evaluate("false || true"));
		Assert.assertFalse(evaluate("false | false"));
	}

	@Test
	public void testTrueConstant() throws Exception {
		Assert.assertTrue(evaluate("true"));
		Assert.assertTrue(evaluate("TRUE"));
	}

	protected boolean evaluate(String expressionString) throws Exception {
		DDMExpression<Boolean> ddmExpression = new DDMExpressionImpl<>(
			expressionString, Boolean.class);

		return ddmExpression.evaluate();
	}

}