/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.membershippolicy;

import com.liferay.portal.kernel.security.membershippolicy.OrganizationMembershipPolicy;
import com.liferay.portal.kernel.security.membershippolicy.OrganizationMembershipPolicyFactoryUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.security.membershippolicy.bundle.organizationmembershippolicyfactory.TestOrganizationMembershipPolicy;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Peter Fellwock
 */
public class OrganizationMembershipPolicyFactoryTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule(
				"bundle.organizationmembershippolicyfactory"));

	@Test
	public void testGetOrganizationMembershipPolicy() {
		OrganizationMembershipPolicy organizationMembershipPolicy =
			OrganizationMembershipPolicyFactoryUtil.
				getOrganizationMembershipPolicy();

		Class<?> clazz = organizationMembershipPolicy.getClass();

		Assert.assertEquals(
			TestOrganizationMembershipPolicy.class.getName(), clazz.getName());
	}

}