/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.security.auth.ScreenNameValidator;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Locale;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class ScreenNameValidatorFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.screennamevalidatorfactory"));

	@BeforeClass
	public static void setUpClass() throws Exception {
		_screenNameValidator = ScreenNameValidatorFactory.getInstance();
	}

	@Test
	public void testGetAUIValidatorJS() {
		Assert.assertEquals(
			"function() {return true;}",
			_screenNameValidator.getAUIValidatorJS());
	}

	@Test
	public void testGetDescription() {
		Locale locale = LocaleUtil.getDefault();

		Assert.assertEquals(
			locale.toString(), _screenNameValidator.getDescription(locale));
	}

	@Test
	public void testValidate() {
		Assert.assertTrue(_screenNameValidator.validate(1, "lftest"));
		Assert.assertFalse(_screenNameValidator.validate(2, "lftest"));
		Assert.assertFalse(_screenNameValidator.validate(1, "bob"));
	}

	private static ScreenNameValidator _screenNameValidator;

}