/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet;

import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.FriendlyURLMapper;
import com.liferay.portal.kernel.portlet.FriendlyURLMapperTracker;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.model.impl.PortletImpl;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.SyntheticBundleRule;
import com.liferay.portlet.bundle.friendlyurlmappertrackerimpl.TestFriendlyURLMapper;
import com.liferay.portlet.internal.FriendlyURLMapperTrackerImpl;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Philip Jones
 */
public class FriendlyURLMapperTrackerImplTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			new SyntheticBundleRule("bundle.friendlyurlmappertrackerimpl"));

	@Test
	public void testGetFriendlyURLMapper() throws Exception {
		Portlet portlet = new PortletImpl();

		portlet.setPortletClass(MVCPortlet.class.getName());
		portlet.setPortletId("FriendlyURLMapperTrackerImplTest");

		FriendlyURLMapperTracker friendlyURLMapperTracker =
			new FriendlyURLMapperTrackerImpl(portlet);

		FriendlyURLMapper friendlyURLMapper =
			friendlyURLMapperTracker.getFriendlyURLMapper();

		Class<?> clazz = friendlyURLMapper.getClass();

		Assert.assertEquals(
			TestFriendlyURLMapper.class.getName(), clazz.getName());
	}

}