/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.events;

import com.liferay.portal.events.test.TestServicePreAction;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.util.GroupTestUtil;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.util.PropsValues;
import com.liferay.portal.util.test.LayoutTestUtil;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author Preston Crary
 */
public class ServicePreActionTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Before
	public void setUp() throws Exception {
		_group = GroupTestUtil.addGroup();

		LayoutTestUtil.addLayout(_group);

		LayoutTestUtil.addLayout(
			_group.getGroupId(), "Page not visible", false, null, false, true);

		_request.setRequestURI(PortalUtil.getPathMain() + "/portal/login");

		_request.setAttribute(
			WebKeys.VIRTUAL_HOST_LAYOUT_SET, _group.getPublicLayoutSet());
	}

	@Test
	public void testHiddenLayoutsVirtualHostLayoutCompositeWithNonexistentLayout()
		throws Exception {

		_request.setRequestURI("/nonexistent_page");

		long plid = getThemeDisplayPlid(true, false);

		ServicePreAction.LayoutComposite defaultLayoutComposite =
			TestServicePreAction.INSTANCE.getDefaultVirtualHostLayoutComposite(
				_request);

		defaultLayoutComposite =
			TestServicePreAction.INSTANCE.getViewableLayoutComposite(
				_request, _user, PermissionCheckerFactoryUtil.create(_user),
				defaultLayoutComposite, 0, false);

		Layout layout = defaultLayoutComposite.getLayout();

		List<Layout> layouts = defaultLayoutComposite.getLayouts();

		Assert.assertEquals(layout.getPlid(), plid);

		Assert.assertEquals(layouts.toString(), 1, layouts.size());
	}

	@Test
	public void testInitThemeDisplayPlidDefaultUserPersonalSiteLayoutComposite()
		throws Exception {

		long plid = getThemeDisplayPlid(false, true);

		ServicePreAction.LayoutComposite defaultLayoutComposite =
			TestServicePreAction.INSTANCE.
				getDefaultUserPersonalSiteLayoutComposite(_user);

		Layout layout = defaultLayoutComposite.getLayout();

		Assert.assertEquals(layout.getPlid(), plid);
	}

	@Test
	public void testInitThemeDisplayPlidDefaultUserSitesLayoutComposite()
		throws Exception {

		boolean publicLayoutsAutoCreate =
			PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_AUTO_CREATE;
		boolean privateLayoutsAutoCreate =
			PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_AUTO_CREATE;

		PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_AUTO_CREATE = false;
		PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_AUTO_CREATE = false;

		try {
			long plid = getThemeDisplayPlid(false, true);

			ServicePreAction.LayoutComposite defaultLayoutComposite =
				TestServicePreAction.INSTANCE.
					getDefaultUserSitesLayoutComposite(_user);

			Layout layout = defaultLayoutComposite.getLayout();

			Assert.assertEquals(layout.getPlid(), plid);
		}
		finally {
			PropsValues.LAYOUT_USER_PUBLIC_LAYOUTS_AUTO_CREATE =
				publicLayoutsAutoCreate;
			PropsValues.LAYOUT_USER_PRIVATE_LAYOUTS_AUTO_CREATE =
				privateLayoutsAutoCreate;
		}
	}

	@Test
	public void testInitThemeDisplayPlidGuestSiteLayoutComposite()
		throws Exception {

		long plid = getThemeDisplayPlid(false, false);

		ServicePreAction.LayoutComposite defaultLayoutComposite =
			TestServicePreAction.INSTANCE.getGuestSiteLayoutComposite(_user);

		Layout layout = defaultLayoutComposite.getLayout();

		Assert.assertEquals(layout.getPlid(), plid);
	}

	@Test
	public void testInitThemeDisplayPlidVirtualHostLayoutComposite()
		throws Exception {

		long plid = getThemeDisplayPlid(true, false);

		ServicePreAction.LayoutComposite defaultLayoutComposite =
			TestServicePreAction.INSTANCE.getDefaultVirtualHostLayoutComposite(
				_request);

		Layout layout = defaultLayoutComposite.getLayout();

		Assert.assertEquals(layout.getPlid(), plid);
	}

	protected long getThemeDisplayPlid(
			boolean hasGuestViewPermission, boolean signedIn)
		throws Exception {

		if (!hasGuestViewPermission) {
			Role role = RoleLocalServiceUtil.getRole(
				_group.getCompanyId(), RoleConstants.GUEST);

			ResourcePermissionLocalServiceUtil.removeResourcePermissions(
				_group.getCompanyId(), Layout.class.getName(),
				ResourceConstants.SCOPE_INDIVIDUAL, role.getRoleId(),
				ActionKeys.VIEW);
		}

		if (signedIn) {
			_user = UserTestUtil.addUser();
		}
		else {
			_user = PortalUtil.initUser(_request);
		}

		_request.setAttribute(WebKeys.USER, _user);

		ThemeDisplay themeDisplay =
			TestServicePreAction.INSTANCE.initThemeDisplay(_request, _response);

		return themeDisplay.getPlid();
	}

	@DeleteAfterTestRun
	private Group _group;

	private final MockHttpServletRequest _request =
		new MockHttpServletRequest();
	private final MockHttpServletResponse _response =
		new MockHttpServletResponse();
	private User _user;

}