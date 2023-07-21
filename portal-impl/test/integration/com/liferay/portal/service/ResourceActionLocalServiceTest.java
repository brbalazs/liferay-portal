/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.ResourceAction;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Michael Bowerman
 */
public class ResourceActionLocalServiceTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() {
		List<String> actionIds = new ArrayList<>(3);

		actionIds.add(ActionKeys.VIEW);
		actionIds.add(_ACTION_ID_1);
		actionIds.add(_ACTION_ID_2);

		ResourceActionLocalServiceUtil.checkResourceActions(_NAME, actionIds);

		actionIds.set(2, _ACTION_ID_3);

		ResourceActionLocalServiceUtil.deleteResourceAction(
			ResourceActionLocalServiceUtil.fetchResourceAction(
				_NAME, _ACTION_ID_2));

		ResourceActionLocalServiceUtil.checkResourceActions(_NAME, actionIds);
	}

	@After
	public void tearDown() {
		List<ResourceAction> resourceActions =
			ResourceActionLocalServiceUtil.getResourceActions(_NAME);

		for (ResourceAction resourceAction : resourceActions) {
			ResourceActionLocalServiceUtil.deleteResourceAction(resourceAction);
		}
	}

	@Test(expected = SystemException.class)
	public void testAddMoreThan64Actions() {
		List<String> actionIds = new ArrayList<>(65);

		actionIds.add(ActionKeys.VIEW);

		for (int i = 0; i < 64; i++) {
			actionIds.add("actionId" + i);
		}

		ResourceActionLocalServiceUtil.checkResourceActions(_NAME, actionIds);
	}

	@Test
	public void testFirstAvailableBitwiseValueGetsGenerated()
		throws PortalException {

		ResourceAction resourceAction =
			ResourceActionLocalServiceUtil.getResourceAction(
				_NAME, ActionKeys.VIEW);

		Assert.assertEquals(1L, resourceAction.getBitwiseValue());

		resourceAction = ResourceActionLocalServiceUtil.getResourceAction(
			_NAME, _ACTION_ID_1);

		Assert.assertEquals(2L, resourceAction.getBitwiseValue());

		resourceAction = ResourceActionLocalServiceUtil.getResourceAction(
			_NAME, _ACTION_ID_3);

		Assert.assertEquals(4L, resourceAction.getBitwiseValue());
	}

	@Test
	public void testViewActionBitwiseValue() throws PortalException {
		ResourceAction viewResourceAction =
			ResourceActionLocalServiceUtil.getResourceAction(
				_NAME, ActionKeys.VIEW);

		Assert.assertEquals(1L, viewResourceAction.getBitwiseValue());
	}

	private static final String _ACTION_ID_1 = "actionId1";

	private static final String _ACTION_ID_2 = "actionId2";

	private static final String _ACTION_ID_3 = "actionId3";

	private static final String _NAME =
		ResourceActionLocalServiceTest.class.getName();

}