/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.security.auth.FullNameGenerator;
import com.liferay.portal.kernel.security.auth.FullNameGeneratorFactory;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class FullNameGeneratorFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.fullnamegeneratorfactory"));

	@Test
	public void testGetFullName() {
		FullNameGenerator fullNameGenerator =
			FullNameGeneratorFactory.getInstance();

		Assert.assertEquals(
			"John Stephen Piper",
			fullNameGenerator.getFullName("John", "Stephen", "Piper"));
	}

	@Test
	public void testGetLocalizedFullName() {
		FullNameGenerator fullNameGenerator =
			FullNameGeneratorFactory.getInstance();

		Assert.assertEquals(
			"Jacques",
			fullNameGenerator.getLocalizedFullName(
				"James", "middle", "lastname", LocaleUtil.FRENCH, 1, 1));

		Assert.assertNotEquals(
			"Jacques",
			fullNameGenerator.getLocalizedFullName(
				"Tom", "middle", "lastname", LocaleUtil.CHINESE, 1, 1));
	}

	@Test
	public void testSplitFullName() {
		FullNameGenerator fullNameGenerator =
			FullNameGeneratorFactory.getInstance();

		String[] splitFullName = fullNameGenerator.splitFullName(
			"John Stephen Piper");

		Assert.assertEquals(
			Arrays.toString(splitFullName), 3, splitFullName.length);
	}

}