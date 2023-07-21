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
public class IsEmailAddressFunctionTest {

	@Test
	public void testEvaluateFalse1() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertFalse(
			(Boolean)isEmailAddressFunction.evaluate("simple text"));
	}

	@Test
	public void testEvaluateFalse2() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertFalse(
			(Boolean)isEmailAddressFunction.evaluate(
				"simple text1, simple text 2"));
	}

	@Test
	public void testEvaluateFalse3() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertFalse(
			(Boolean)isEmailAddressFunction.evaluate(
				"simple text1, test@liferay.com"));
	}

	@Test
	public void testEvaluateFalse4() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertFalse(
			(Boolean)isEmailAddressFunction.evaluate(
				"test@liferay.com, simple text1"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEvaluateInvalid() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		isEmailAddressFunction.evaluate("test", "test2");
	}

	@Test
	public void testEvaluateTrue1() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertTrue(
			(Boolean)isEmailAddressFunction.evaluate("test@liferay.com"));
	}

	@Test
	public void testEvaluateTrue2() throws Exception {
		IsEmailAddressFunction isEmailAddressFunction =
			new IsEmailAddressFunction();

		Assert.assertTrue(
			(Boolean)isEmailAddressFunction.evaluate(
				"test@liferay.com, test@liferay.com"));
	}

}