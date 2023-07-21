/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Resource;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.model.impl.ResourceImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Manuel de la Peña
 */
public class ResourcePermissionLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();
	}

	@Test
	public void testShouldFailIfFirstResourceIsNotIndividual()
		throws Exception {

		List<Resource> resources = new ArrayList<>();

		Resource firstResource = new ResourceImpl();

		firstResource.setScope(ResourceConstants.SCOPE_GROUP);

		resources.add(firstResource);

		Resource lastResource = new ResourceImpl();

		lastResource.setScope(ResourceConstants.SCOPE_COMPANY);

		resources.add(lastResource);

		long[] roleIds = new long[1];

		Role guestRole = RoleLocalServiceUtil.getRole(
			_group.getCompanyId(), RoleConstants.GUEST);

		roleIds[0] = guestRole.getRoleId();

		try {
			ResourcePermissionLocalServiceUtil.hasResourcePermission(
				resources, roleIds, ActionKeys.VIEW);
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"The first resource must be an individual scope",
				iae.getMessage());
		}
	}

	@Test
	public void testShouldFailIfLastResourceIsNotCompany() throws Exception {
		List<Resource> resources = new ArrayList<>();

		Resource firstResource = new ResourceImpl();

		firstResource.setScope(ResourceConstants.SCOPE_INDIVIDUAL);

		resources.add(firstResource);

		Resource lastResource = new ResourceImpl();

		lastResource.setScope(ResourceConstants.SCOPE_GROUP);

		resources.add(lastResource);

		long[] roleIds = new long[1];

		Role guestRole = RoleLocalServiceUtil.getRole(
			_group.getCompanyId(), RoleConstants.GUEST);

		roleIds[0] = guestRole.getRoleId();

		try {
			ResourcePermissionLocalServiceUtil.hasResourcePermission(
				resources, roleIds, ActionKeys.VIEW);
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"The last resource must be a company scope", iae.getMessage());
		}
	}

	@Test
	public void testShouldFailIfResourcesIsLessThanTwo() throws Exception {
		List<Resource> resources = new ArrayList<>();

		resources.add(new ResourceImpl());

		long[] roleIds = new long[1];

		Role guestRole = RoleLocalServiceUtil.getRole(
			_group.getCompanyId(), RoleConstants.GUEST);

		roleIds[0] = guestRole.getRoleId();

		try {
			ResourcePermissionLocalServiceUtil.hasResourcePermission(
				resources, roleIds, ActionKeys.VIEW);
		}
		catch (IllegalArgumentException iae) {
			Assert.assertEquals(
				"The list of resources must contain at least two values",
				iae.getMessage());
		}
	}

	@DeleteAfterTestRun
	private Group _group;

}