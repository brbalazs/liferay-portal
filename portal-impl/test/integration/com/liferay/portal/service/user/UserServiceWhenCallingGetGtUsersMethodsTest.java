/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.petra.function.UnsafeFunction;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroup;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserGroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Brian Wing Shun Chan
 * @author José Manuel Navarro
 * @author Drew Brokke
 */
public class UserServiceWhenCallingGetGtUsersMethodsTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE);

	@Test
	public void testGetGtCompanyUsers() throws Exception {
		for (int i = 0; i < 10; i++) {
			_users.add(UserTestUtil.addUser());
		}

		int size = 5;

		_assert(
			size,
			gtUserId -> UserServiceUtil.getGtCompanyUsers(
				gtUserId, TestPropsValues.getCompanyId(), size));
	}

	@Test
	public void testGetGtOrganizationUsers() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		for (int i = 0; i < 10; i++) {
			_users.add(
				UserTestUtil.addOrganizationUser(
					_organization, RoleConstants.ORGANIZATION_USER));
		}

		int size = 5;

		_assert(
			size,
			gtUserId -> UserServiceUtil.getGtOrganizationUsers(
				gtUserId, _organization.getOrganizationId(), size));
	}

	@Test
	public void testGetGtUserGroupUsers() throws Exception {
		_userGroup = UserGroupTestUtil.addUserGroup();

		long[] userIds = new long[10];

		for (int i = 0; i < userIds.length; i++) {
			User user = UserTestUtil.addUser();

			_users.add(user);

			userIds[i] = user.getUserId();
		}

		UserLocalServiceUtil.setUserGroupUsers(
			_userGroup.getUserGroupId(), userIds);

		int size = 5;

		_assert(
			size,
			gtUserId -> UserServiceUtil.getGtUserGroupUsers(
				gtUserId, _userGroup.getUserGroupId(), size));
	}

	private void _assert(
			int size,
			UnsafeFunction<Long, List<User>, Exception> unsafeFunction)
		throws Exception {

		List<User> users = unsafeFunction.apply(0L);

		Assert.assertFalse(users.isEmpty());
		Assert.assertEquals(users.toString(), size, users.size());

		User lastUser = users.get(users.size() - 1);

		users = unsafeFunction.apply(lastUser.getUserId());

		Assert.assertFalse(users.isEmpty());
		Assert.assertEquals(users.toString(), size, users.size());

		long previousUserId = 0;

		for (User user : users) {
			long userId = user.getUserId();

			Assert.assertTrue(userId > lastUser.getUserId());
			Assert.assertTrue(userId > previousUserId);

			previousUserId = userId;
		}
	}

	@DeleteAfterTestRun
	private Organization _organization;

	@DeleteAfterTestRun
	private UserGroup _userGroup;

	@DeleteAfterTestRun
	private final List<User> _users = new ArrayList<>();

}