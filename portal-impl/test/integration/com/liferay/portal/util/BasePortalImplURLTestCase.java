/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.util;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.service.CompanyLocalServiceUtil;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.util.test.LayoutTestUtil;

import org.junit.Before;

/**
 * @author Vilmos Papp
 * @author Akos Thurzo
 */
public abstract class BasePortalImplURLTestCase {

	@Before
	public void setUp() throws Exception {
		company = CompanyLocalServiceUtil.getCompany(
			TestPropsValues.getCompanyId());

		long controlPanelPlid = PortalUtil.getControlPanelPlid(
			company.getCompanyId());

		controlPanelLayout = LayoutLocalServiceUtil.getLayout(controlPanelPlid);

		group = GroupTestUtil.addGroup();

		privateLayout = LayoutTestUtil.addLayout(group, true);
		publicLayout = LayoutTestUtil.addLayout(group);
	}

	protected ThemeDisplay initThemeDisplay(
			Company company, Group group, Layout layout,
			String companyVirtualHostname)
		throws Exception {

		return initThemeDisplay(
			company, group, layout, companyVirtualHostname,
			companyVirtualHostname);
	}

	protected ThemeDisplay initThemeDisplay(
			Company company, Group group, Layout layout,
			String companyVirtualHostname, String serverName)
		throws Exception {

		company.setVirtualHostname(companyVirtualHostname);

		ThemeDisplay themeDisplay = new ThemeDisplay();

		themeDisplay.setCompany(company);
		themeDisplay.setI18nLanguageId(StringPool.BLANK);
		themeDisplay.setLayout(layout);
		themeDisplay.setLayoutSet(layout.getLayoutSet());
		themeDisplay.setSecure(false);
		themeDisplay.setServerName(serverName);
		themeDisplay.setServerPort(8080);
		themeDisplay.setSiteGroupId(group.getGroupId());
		themeDisplay.setUser(TestPropsValues.getUser());
		themeDisplay.setWidget(false);

		return themeDisplay;
	}

	protected static final String LOCALHOST = "localhost";

	protected static final String VIRTUAL_HOSTNAME = "test.com";

	protected Company company;
	protected Layout controlPanelLayout;

	@DeleteAfterTestRun
	protected Group group;

	protected Layout privateLayout;
	protected Layout publicLayout;

}