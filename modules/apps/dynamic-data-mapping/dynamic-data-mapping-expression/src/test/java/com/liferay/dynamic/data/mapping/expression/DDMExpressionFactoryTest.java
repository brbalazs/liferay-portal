/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.expression;

import com.liferay.dynamic.data.mapping.expression.internal.DDMExpressionFactoryImpl;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Marcellus Tavares
 */
public class DDMExpressionFactoryTest {

	@Test
	public void testCreateBooleanExpression() throws Exception {
		DDMExpression<Boolean> ddmExpression =
			_ddmExpressionFactory.createBooleanDDMExpression("true");

		Assert.assertTrue(ddmExpression.evaluate());
	}

	@Test
	public void testCreateDoubleExpression() throws Exception {
		DDMExpression<Double> ddmExpression =
			_ddmExpressionFactory.createDoubleDDMExpression(".1");

		Assert.assertEquals(.1D, ddmExpression.evaluate(), .001);
	}

	@Test
	public void testCreateFloatExpression() throws Exception {
		DDMExpression<Float> ddmExpression =
			_ddmExpressionFactory.createFloatDDMExpression(".1");

		Assert.assertEquals(.1F, ddmExpression.evaluate(), .001);
	}

	@Test
	public void testCreateIntegerExpression() throws Exception {
		DDMExpression<Integer> ddmExpression =
			_ddmExpressionFactory.createIntegerDDMExpression("42");

		Assert.assertEquals(42, (int)ddmExpression.evaluate());
	}

	@Test
	public void testCreateLongExpression() throws Exception {
		DDMExpression<Long> ddmExpression =
			_ddmExpressionFactory.createLongDDMExpression("10000000000");

		Assert.assertEquals(10000000000L, (long)ddmExpression.evaluate());
	}

	@Test
	public void testCreateNumberExpression() throws Exception {
		DDMExpression<Number> ddmExpression =
			_ddmExpressionFactory.createNumberDDMExpression("1.1");

		Number result = ddmExpression.evaluate();

		Assert.assertEquals(1, result.intValue());
		Assert.assertEquals(1, result.longValue());
		Assert.assertEquals(1.1, result.doubleValue(), .01);
		Assert.assertEquals(1.1, result.floatValue(), .01);
	}

	@Test
	public void testCreateStringExpression1() throws Exception {
		DDMExpression<String> ddmExpression =
			_ddmExpressionFactory.createStringDDMExpression("\"Test\"");

		Assert.assertEquals("Test", ddmExpression.evaluate());
	}

	@Test
	public void testCreateStringExpression2() throws Exception {
		DDMExpression<String> ddmExpression =
			_ddmExpressionFactory.createStringDDMExpression("'Test'");

		Assert.assertEquals("Test", ddmExpression.evaluate());
	}

	private final DDMExpressionFactory _ddmExpressionFactory =
		new DDMExpressionFactoryImpl();

}