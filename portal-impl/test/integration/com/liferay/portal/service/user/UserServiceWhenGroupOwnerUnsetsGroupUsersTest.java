/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
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
public class UserServiceWhenGroupOwnerUnsetsGroupUsersTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_organization = OrganizationTestUtil.addOrganization(true);

		_group = GroupTestUtil.addGroup();

		_groupOwnerUser = UserTestUtil.addGroupOwnerUser(_group);

		_organizationGroupUser = UserTestUtil.addGroupOwnerUser(
			_organization.getGroup());
	}

	@Test
	public void testShouldUnsetGroupAdmin() throws Exception {
		User groupAdminUser = UserTestUtil.addGroupAdminUser(_group);

		try {
			UserServiceTestUtil.unsetGroupUsers(
				_group.getGroupId(), _groupOwnerUser, groupAdminUser);

			Assert.assertFalse(
				UserLocalServiceUtil.hasGroupUser(
					_group.getGroupId(), groupAdminUser.getUserId()));
		}
		finally {
			UserLocalServiceUtil.deleteUser(groupAdminUser);
		}
	}

	@Test
	public void testShouldUnsetGroupOwner() throws Exception {
		User groupOwnerUser = UserTestUtil.addGroupOwnerUser(_group);

		try {
			UserServiceTestUtil.unsetGroupUsers(
				_group.getGroupId(), _groupOwnerUser, groupOwnerUser);

			Assert.assertFalse(
				UserLocalServiceUtil.hasGroupUser(
					_group.getGroupId(), groupOwnerUser.getUserId()));
		}
		finally {
			UserLocalServiceUtil.deleteUser(groupOwnerUser);
		}
	}

	@Test
	public void testShouldUnsetOrganizationAdmin() throws Exception {
		User organizationAdminUser = UserTestUtil.addOrganizationAdminUser(
			_organization);

		try {
			UserServiceTestUtil.unsetOrganizationUsers(
				_organization.getOrganizationId(), _organizationGroupUser,
				organizationAdminUser);

			Assert.assertTrue(
				UserLocalServiceUtil.hasOrganizationUser(
					_organization.getOrganizationId(),
					organizationAdminUser.getUserId()));
		}
		finally {
			UserLocalServiceUtil.deleteUser(organizationAdminUser);
		}
	}

	@Test
	public void testShouldUnsetOrganizationOwner() throws Exception {
		User organizationOwnerUser = UserTestUtil.addOrganizationOwnerUser(
			_organization);

		try {
			UserServiceTestUtil.unsetOrganizationUsers(
				_organization.getOrganizationId(), _organizationGroupUser,
				organizationOwnerUser);

			Assert.assertTrue(
				UserLocalServiceUtil.hasOrganizationUser(
					_organization.getOrganizationId(),
					organizationOwnerUser.getUserId()));
		}
		finally {
			UserLocalServiceUtil.deleteUser(organizationOwnerUser);
		}
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private User _groupOwnerUser;

	@DeleteAfterTestRun
	private Organization _organization;

	@DeleteAfterTestRun
	private User _organizationGroupUser;

}