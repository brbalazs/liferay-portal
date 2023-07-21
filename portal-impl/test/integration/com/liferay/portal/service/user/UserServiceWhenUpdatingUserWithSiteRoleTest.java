/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.user;

import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Organization;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.UserGroupRole;
import com.liferay.portal.kernel.service.OrganizationLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.UserGroupRoleLocalServiceUtil;
import com.liferay.portal.kernel.service.UserServiceUtil;
import com.liferay.portal.kernel.service.persistence.UserGroupRolePK;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.OrganizationTestUtil;
import com.liferay.portal.kernel.test.util.RoleTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerTestRule;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Pei-Jung Lan
 */
public class UserServiceWhenUpdatingUserWithSiteRoleTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerTestRule.INSTANCE);

	@Test
	public void testShouldAssignSiteRoleForInheritedSite() throws Exception {
		_organization = OrganizationTestUtil.addOrganization();

		_group = GroupTestUtil.addGroup();

		OrganizationLocalServiceUtil.addGroupOrganization(
			_group.getGroupId(), _organization);

		_user = UserTestUtil.addUser();

		OrganizationLocalServiceUtil.addUserOrganization(
			_user.getUserId(), _organization);

		_role = RoleTestUtil.addRole(RoleConstants.TYPE_SITE);

		UserGroupRolePK userGroupRolePK = new UserGroupRolePK(
			_user.getUserId(), _group.getGroupId(), _role.getRoleId());

		UserGroupRole userGroupRole =
			UserGroupRoleLocalServiceUtil.createUserGroupRole(userGroupRolePK);

		_user = _updateUser(_user, Collections.singletonList(userGroupRole));

		Assert.assertTrue(
			UserGroupRoleLocalServiceUtil.hasUserGroupRole(
				_user.getUserId(), _group.getGroupId(), _role.getRoleId()));
	}

	private User _updateUser(User user, List<UserGroupRole> userGroupRoles)
		throws Exception {

		Contact contact = user.getContact();

		Calendar birthdayCal = CalendarFactoryUtil.getCalendar();

		birthdayCal.setTime(contact.getBirthday());

		int birthdayMonth = birthdayCal.get(Calendar.MONTH);
		int birthdayDay = birthdayCal.get(Calendar.DATE);
		int birthdayYear = birthdayCal.get(Calendar.YEAR);

		return UserServiceUtil.updateUser(
			user.getUserId(), user.getPassword(), null, null,
			user.isPasswordReset(), null, null, user.getScreenName(),
			user.getEmailAddress(), user.getFacebookId(), user.getOpenId(),
			user.getLanguageId(), user.getTimeZoneId(), user.getGreeting(),
			user.getComments(), user.getFirstName(), user.getMiddleName(),
			user.getLastName(), contact.getPrefixId(), contact.getSuffixId(),
			user.isMale(), birthdayMonth, birthdayDay, birthdayYear,
			contact.getSmsSn(), contact.getFacebookSn(), contact.getJabberSn(),
			contact.getSkypeSn(), contact.getTwitterSn(), user.getJobTitle(),
			null, null, null, userGroupRoles, null, new ServiceContext());
	}

	@DeleteAfterTestRun
	private Group _group;

	@DeleteAfterTestRun
	private Organization _organization;

	@DeleteAfterTestRun
	private Role _role;

	@DeleteAfterTestRun
	private User _user;

}