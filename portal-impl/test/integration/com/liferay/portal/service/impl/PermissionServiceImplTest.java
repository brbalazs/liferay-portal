/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.PermissionService;
import com.liferay.portal.kernel.service.PermissionServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portal.util.test.AtomicState;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class PermissionServiceImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.permissionserviceimpl"));

	@BeforeClass
	public static void setUpClass() {
		_atomicState = new AtomicState();
	}

	@AfterClass
	public static void tearDownClass() {
		_atomicState.close();
	}

	@Test
	public void testCheckPermission1() throws PortalException {
		PermissionService permissionService =
			PermissionServiceUtil.getService();

		_atomicState.reset();

		permissionService.checkPermission(0, "PermissionServiceImplTest", 0);

		Assert.assertTrue(_atomicState.isSet());
	}

	@Test
	public void testCheckPermission2() throws PortalException {
		PermissionService permissionService =
			PermissionServiceUtil.getService();

		_atomicState.reset();

		permissionService.checkPermission(0, "PermissionServiceImplTest", null);

		Assert.assertTrue(_atomicState.isSet());
	}

	private static AtomicState _atomicState;

}