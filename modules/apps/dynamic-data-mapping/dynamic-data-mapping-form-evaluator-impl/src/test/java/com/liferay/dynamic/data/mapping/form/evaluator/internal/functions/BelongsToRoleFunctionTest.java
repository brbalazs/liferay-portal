/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.dynamic.data.mapping.form.evaluator.internal.functions;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author Leonardo Barros
 */
@RunWith(MockitoJUnitRunner.class)
public class BelongsToRoleFunctionTest {

	@Before
	public void setUp() throws Exception {
		setPortalUtil();
		setRole();
	}

	@Test
	public void testEvaluateFalseWithOrganizationalRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasSiteRole();

		Assert.assertFalse(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role2"));
	}

	@Test
	public void testEvaluateFalseWithRegularRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasRegularRole();

		Assert.assertFalse(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role2"));
	}

	@Test
	public void testEvaluateFalseWithSiteRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasSiteRole();

		Assert.assertFalse(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role2"));
	}

	@Test
	public void testEvaluateTrueWithOrganizationalRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasSiteRole();

		Assert.assertTrue(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role1", "Role2"));
	}

	@Test
	public void testEvaluateTrueWithRegularRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasRegularRole();

		Assert.assertTrue(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role1", "Role2"));
	}

	@Test
	public void testEvaluateTrueWithSiteRole() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			_request, 1, _roleLocalService, _userGroupRoleLocalService,
			_userLocalService);

		mockHasSiteRole();

		Assert.assertTrue(
			(boolean)belongsToRoleFunction.evaluate("Role0", "Role1", "Role2"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArgument() throws Exception {
		BelongsToRoleFunction belongsToRoleFunction = new BelongsToRoleFunction(
			null, 0, null, null, null);

		belongsToRoleFunction.evaluate();
	}

	protected void mockHasRegularRole() throws Exception {
		Mockito.when(
			_role.getType()
		).thenReturn(
			RoleConstants.TYPE_REGULAR
		);

		Mockito.when(
			_userLocalService.hasRoleUser(
				Matchers.anyLong(), Matchers.eq("Role1"), Matchers.anyLong(),
				Matchers.eq(true))
		).thenReturn(
			true
		);
	}

	protected void mockHasSiteRole() throws Exception {
		Mockito.when(
			_role.getType()
		).thenReturn(
			RoleConstants.TYPE_SITE
		);

		Mockito.when(
			_userGroupRoleLocalService.hasUserGroupRole(
				Matchers.anyLong(), Matchers.anyLong(), Matchers.eq("Role1"),
				Matchers.eq(true))
		).thenReturn(
			true
		);
	}

	protected void setPortalUtil() throws Exception {
		PortalUtil portalUtil = new PortalUtil();

		Portal portal = Mockito.mock(Portal.class);

		Mockito.when(
			portal.getUser(_request)
		).thenReturn(
			_user
		);

		Mockito.when(
			portal.getCompany(_request)
		).thenReturn(
			_company
		);

		portalUtil.setPortal(portal);
	}

	protected void setRole() throws Exception {
		Mockito.when(
			_roleLocalService.fetchRole(
				Matchers.anyLong(), Matchers.anyString())
		).thenReturn(
			_role
		);
	}

	@Mock
	private Company _company;

	@Mock
	private HttpServletRequest _request;

	@Mock
	private Role _role;

	@Mock
	private RoleLocalService _roleLocalService;

	@Mock
	private User _user;

	@Mock
	private UserGroupRoleLocalService _userGroupRoleLocalService;

	@Mock
	private UserLocalService _userLocalService;

}