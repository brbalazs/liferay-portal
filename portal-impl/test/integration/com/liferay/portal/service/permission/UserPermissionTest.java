/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.permission;

import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.permission.UserPermission;
import com.liferay.portal.kernel.service.permission.UserPermissionUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Pei-Jung Lan
 */
public class UserPermissionTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_user = UserTestUtil.addUser();
	}

	@Test
	public void testContainsPermissionsActionId() throws Exception {
		_user = UserTestUtil.addUser();
		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		_userLocalService.addRoleUser(_role.getRoleId(), _user);

		PermissionChecker permissionChecker = _permissionCheckerFactory.create(
			_user);

		Assert.assertFalse(
			_userPermission.contains(
				permissionChecker, _user.getUserId(), null,
				ActionKeys.PERMISSIONS));

		RoleTestUtil.addResourcePermission(
			_role, User.class.getName(), ResourceConstants.SCOPE_COMPANY,
			String.valueOf(_user.getCompanyId()), ActionKeys.PERMISSIONS);

		Assert.assertTrue(
			_userPermission.contains(
				permissionChecker, _user.getUserId(), null,
				ActionKeys.PERMISSIONS));
	}

	@Test
	public void testContainsViewActionId() throws Exception {
		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		RoleTestUtil.addResourcePermission(
			_role, User.class.getName(), ResourceConstants.SCOPE_COMPANY,
			String.valueOf(_user.getCompanyId()), ActionKeys.VIEW);

		UserLocalServiceUtil.addRoleUser(_role.getRoleId(), _user);

		PermissionChecker permissionChecker =
			PermissionCheckerFactoryUtil.create(_user);

		Assert.assertTrue(
			UserPermissionUtil.contains(
				permissionChecker, ResourceConstants.PRIMKEY_DNE,
				ActionKeys.VIEW));
	}

	@Test
	public void testOrganizationManageUsersPermission() throws Exception {
		_user = UserTestUtil.addUser();

		_user2 = UserTestUtil.addUser();

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_REGULAR);

		RoleTestUtil.addResourcePermission(
			_role, Organization.class.getName(),
			ResourceConstants.SCOPE_COMPANY,
			String.valueOf(_user.getCompanyId()), ActionKeys.MANAGE_USERS);

		_userLocalService.addRoleUser(_role.getRoleId(), _user);

		PermissionChecker permissionChecker = _permissionCheckerFactory.create(
			_user);

		Assert.assertFalse(
			_userPermission.contains(
				permissionChecker, _user2.getUserId(), ActionKeys.UPDATE));

		_organization = OrganizationTestUtil.addOrganization();

		_userLocalService.addOrganizationUser(
			_organization.getOrganizationId(), _user2.getUserId());

		Assert.assertTrue(
			_userPermission.contains(
				permissionChecker, _user2.getUserId(), ActionKeys.UPDATE));
	}

	@DeleteAfterTestRun
	private Organization _organization;

	@Inject
	private PermissionCheckerFactory _permissionCheckerFactory;

	@DeleteAfterTestRun
	private Role _role;

	@DeleteAfterTestRun
	private User _user;

	@DeleteAfterTestRun
	private User _user2;

	@Inject
	private UserLocalService _userLocalService;

	@Inject
	private UserPermission _userPermission;

}