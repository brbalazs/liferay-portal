/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.admin.web.internal.portlet;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.Portlet;
import com.liferay.portal.kernel.portlet.BaseControlPanelEntry;
import com.liferay.portal.kernel.portlet.ControlPanelEntry;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.util.PropsValues;
import com.liferay.site.admin.web.internal.constants.SiteAdminPortletKeys;

import java.util.LinkedHashMap;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Jorge Ferrer
 * @author Sergio González
 * @author Miguel Pastor
 */
@Component(
	immediate = true,
	property = "javax.portlet.name=" + SiteAdminPortletKeys.SITE_ADMIN,
	service = ControlPanelEntry.class
)
public class SiteAdminControlPanelEntry extends BaseControlPanelEntry {

	@Override
	protected boolean hasPermissionImplicitlyGranted(
			PermissionChecker permissionChecker, Group group, Portlet portlet)
		throws Exception {

		if (PropsValues.SITES_CONTROL_PANEL_MEMBERS_VISIBLE) {
			LinkedHashMap<String, Object> groupParams = new LinkedHashMap<>();

			groupParams.put("site", Boolean.TRUE);
			groupParams.put("usersGroups", permissionChecker.getUserId());

			int count = _groupLocalService.searchCount(
				permissionChecker.getCompanyId(), null, null, groupParams);

			if (count > 0) {
				return true;
			}
		}

		return super.hasPermissionImplicitlyGranted(
			permissionChecker, group, portlet);
	}

	@Reference(unbind = "-")
	protected void setGroupLocalService(GroupLocalService groupLocalService) {
		_groupLocalService = groupLocalService;
	}

	private GroupLocalService _groupLocalService;

}