/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.UnicodeProperties;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.List;

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
public class UserServiceWhenAddingUserWithDefaultSitesEnabledTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		UnicodeProperties properties = new UnicodeProperties();

		properties.put(
			PropsKeys.ADMIN_DEFAULT_GROUP_NAMES, _group.getDescriptiveName());

		_organization = OrganizationTestUtil.addOrganization(true);

		Group organizationGroup = _organization.getGroup();

		properties.put(
			PropsKeys.ADMIN_DEFAULT_ORGANIZATION_GROUP_NAMES,
			organizationGroup.getDescriptiveName());

		CompanyLocalServiceUtil.updatePreferences(
			_group.getCompanyId(), properties);

		UnicodeProperties typeSettingsProperties =
			_group.getTypeSettingsProperties();

		_siteRole = RoleTestUtil.addRole(RoleConstants.TYPE_SITE);

		typeSettingsProperties.put(
			"defaultSiteRoleIds", String.valueOf(_siteRole.getRoleId()));

		GroupLocalServiceUtil.updateGroup(
			_group.getGroupId(), typeSettingsProperties.toString());

		_user = UserTestUtil.addUser();
	}

	@Test
	public void testShouldInheritDefaultOrganizationSiteMembership() {
		Group organizationGroup = _organization.getGroup();

		long organizationGroupId = organizationGroup.getGroupId();

		Assert.assertTrue(
			ArrayUtil.contains(_user.getGroupIds(), organizationGroupId));
	}

	@Test
	public void testShouldInheritDefaultSiteRolesFromDefaultSite()
		throws Exception {

		Assert.assertTrue(
			ArrayUtil.contains(_user.getGroupIds(), _group.getGroupId()));

		List<UserGroupRole> userGroupRoles =
			UserGroupRoleLocalServiceUtil.getUserGroupRoles(
				_user.getUserId(), _group.getGroupId());

		Assert.assertEquals(
			userGroupRoles.toString(), 1, userGroupRoles.size());

		UserGroupRole userGroupRole = userGroupRoles.get(0);

		Assert.assertEquals(_siteRole.getRoleId(), userGroupRole.getRoleId());
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private Organization _organization;

	@DeleteAfterTestRun
	private Role _siteRole;

	@DeleteAfterTestRun
	private User _user;

}