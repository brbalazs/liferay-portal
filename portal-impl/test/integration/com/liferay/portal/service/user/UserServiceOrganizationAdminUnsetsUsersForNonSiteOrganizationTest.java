/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Brian Wing Shun Chan
 * @author José Manuel Navarro
 * @author Drew Brokke
 */
public class UserServiceOrganizationAdminUnsetsUsersForNonSiteOrganizationTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		_organizationAdminUser = UserTestUtil.addOrganizationAdminUser(
			_organization);

		_organizationOwnerUser = UserTestUtil.addOrganizationOwnerUser(
			_organization);
	}

	@Test
	public void testShouldUnsetOrganizationAdmin() throws Exception {
		User otherOrganizationAdminUser = UserTestUtil.addOrganizationAdminUser(
			_organization);

		try {
			UserServiceTestUtil.unsetOrganizationUsers(
				_organization.getOrganizationId(), _organizationAdminUser,
				otherOrganizationAdminUser);

			Assert.assertTrue(
				UserLocalServiceUtil.hasOrganizationUser(
					_organization.getOrganizationId(),
					otherOrganizationAdminUser.getUserId()));
		}
		finally {
			UserLocalServiceUtil.deleteUser(otherOrganizationAdminUser);
		}
	}

	@Test
	public void testShouldUnsetOrganizationOwner() throws Exception {
		UserServiceTestUtil.unsetOrganizationUsers(
			_organization.getOrganizationId(), _organizationAdminUser,
			_organizationOwnerUser);

		Assert.assertTrue(
			UserLocalServiceUtil.hasOrganizationUser(
				_organization.getOrganizationId(),
				_organizationOwnerUser.getUserId()));
	}

	@DeleteAfterTestRun
	private Organization _organization;

	@DeleteAfterTestRun
	private User _organizationAdminUser;

	@DeleteAfterTestRun
	private User _organizationOwnerUser;

}