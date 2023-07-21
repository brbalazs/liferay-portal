/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.format;

import com.liferay.portal.kernel.format.bundle.phonenumberformatutil.TestPhoneNumberFormat;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class PhoneNumberFormatUtilTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.phonenumberformatutil"));

	@Test
	public void testFormat() {
		Assert.assertEquals(
			TestPhoneNumberFormat.FORMATTED,
			PhoneNumberFormatUtil.format(TestPhoneNumberFormat.UNFORMATTED));
	}

	@Test
	public void testGetPhoneNumberFormat() {
		PhoneNumberFormat phoneNumberFormat =
			PhoneNumberFormatUtil.getPhoneNumberFormat();

		Class<?> clazz = phoneNumberFormat.getClass();

		Assert.assertEquals(
			TestPhoneNumberFormat.class.getName(), clazz.getName());
	}

	@Test
	public void testStrip() {
		Assert.assertEquals(
			TestPhoneNumberFormat.UNFORMATTED,
			PhoneNumberFormatUtil.strip(TestPhoneNumberFormat.FORMATTED));
	}

	@Test
	public void testValidate() {
		Assert.assertTrue(
			PhoneNumberFormatUtil.validate(TestPhoneNumberFormat.FORMATTED));
		Assert.assertFalse(
			PhoneNumberFormatUtil.validate(TestPhoneNumberFormat.UNFORMATTED));
	}

}