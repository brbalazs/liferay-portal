/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutSet;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.VirtualHostLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.CompanyTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Michael Bowerman
 */
public class PortalImplLayoutFriendlyURLTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_company = CompanyTestUtil.addCompany();

		_group = GroupLocalServiceUtil.fetchGroup(
			_company.getCompanyId(), GroupConstants.GUEST);

		_layout = LayoutLocalServiceUtil.fetchDefaultLayout(
			_group.getGroupId(), false);
	}

	@Test
	public void testCompanyDefaultSiteVirtualHost() throws Exception {
		testLayoutFriendlyURL(
			_company.getVirtualHostname(), _layout.getFriendlyURL());
	}

	@Test
	public void testCompanyDefaultSiteVirtualHostWithLayoutSetVirtualHost()
		throws Exception {

		setLayoutSetVirtualHost();

		String expectedURL =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING +
				_group.getFriendlyURL() + _layout.getFriendlyURL();

		testLayoutFriendlyURL(_company.getVirtualHostname(), expectedURL);
	}

	@Test
	public void testLayoutSetVirtualHost() throws Exception {
		String layoutSetVirtualHost = setLayoutSetVirtualHost();

		testLayoutFriendlyURL(layoutSetVirtualHost, _layout.getFriendlyURL());
	}

	@Test
	public void testLocalhost() throws Exception {
		String expectedURL =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING +
				_group.getFriendlyURL() + _layout.getFriendlyURL();

		testLayoutFriendlyURL("localhost", expectedURL);
	}

	@Test
	public void testLocalhostWithLayoutSetVirtualHost() throws Exception {
		setLayoutSetVirtualHost();

		String expectedURL =
			PropsValues.LAYOUT_FRIENDLY_URL_PUBLIC_SERVLET_MAPPING +
				_group.getFriendlyURL() + _layout.getFriendlyURL();

		testLayoutFriendlyURL("localhost", expectedURL);
	}

	protected String setLayoutSetVirtualHost() {
		LayoutSet layoutSet = _group.getPublicLayoutSet();

		String hostname =
			RandomTestUtil.randomString() + "." +
				RandomTestUtil.randomString(3);

		VirtualHostLocalServiceUtil.updateVirtualHost(
			_company.getCompanyId(), layoutSet.getLayoutSetId(), hostname);

		return hostname;
	}

	protected void testLayoutFriendlyURL(
			String virtualHostname, String expectedURL)
		throws Exception {

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(_company);
		themeDisplay.setLayout(_layout);
		themeDisplay.setLayoutSet(_layout.getLayoutSet());
		themeDisplay.setPortalDomain(virtualHostname);
		themeDisplay.setServerName(virtualHostname);
		themeDisplay.setServerPort(8080);
		themeDisplay.setSiteGroupId(_group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());

		Assert.assertEquals(
			expectedURL, PortalUtil.getLayoutFriendlyURL(themeDisplay));
	}

	@DeleteAfterTestRun
	private Company _company;

	private Group _group;
	private Layout _layout;

}