/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.security.auth.FullNameValidator;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class FullNameValidatorFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.fullnamevalidatorfactory"));

	@Test
	public void testValidate() {
		FullNameValidator fullNameValidator =
			FullNameValidatorFactory.getInstance();

		Assert.assertTrue(
			fullNameValidator.validate(1, "Brian", "middleName", "lastName"));
		Assert.assertFalse(
			fullNameValidator.validate(2, "Brian", "middleName", "lastName"));
		Assert.assertFalse(
			fullNameValidator.validate(1, "Peter", "middleName", "lastName"));
	}

}