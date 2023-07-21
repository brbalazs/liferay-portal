/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.admin.util;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.model.ResourceConstants;
import com.liferay.portal.kernel.model.ResourcePermission;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.RoleConstants;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.service.GroupLocalServiceUtil;
import com.liferay.portal.kernel.service.ResourcePermissionLocalServiceUtil;
import com.liferay.portal.kernel.service.RoleLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.List;

import javax.portlet.ActionRequest;

/**
 * @author Raymond Augé
 */
public class CleanUpPermissionsUtil {

	public static void cleanUpAddToPagePermissions(ActionRequest actionRequest)
		throws Exception {

		long companyId = PortalUtil.getCompanyId(actionRequest);

		Role role = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.GUEST);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), false);

		role = RoleLocalServiceUtil.getRole(
			companyId, RoleConstants.POWER_USER);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), true);

		role = RoleLocalServiceUtil.getRole(companyId, RoleConstants.USER);

		_cleanUpAddToPagePermissions(companyId, role.getRoleId(), false);
	}

	private static void _cleanUpAddToPagePermissions(
			long companyId, long roleId, boolean limitScope)
		throws Exception {

		List<ResourcePermission> roleResourcePermissions =
			ResourcePermissionLocalServiceUtil.getRoleResourcePermissions(
				roleId);

		Group userPersonalSite = GroupLocalServiceUtil.getGroup(
			companyId, GroupConstants.USER_PERSONAL_SITE);

		String groupIdString = String.valueOf(userPersonalSite.getGroupId());

		for (ResourcePermission resourcePermission : roleResourcePermissions) {
			if (!resourcePermission.hasActionId(ActionKeys.ADD_TO_PAGE)) {
				continue;
			}

			ResourcePermissionLocalServiceUtil.removeResourcePermission(
				companyId, resourcePermission.getName(),
				resourcePermission.getScope(), resourcePermission.getPrimKey(),
				roleId, ActionKeys.ADD_TO_PAGE);

			if (!limitScope) {
				continue;
			}

			ResourcePermissionLocalServiceUtil.addResourcePermission(
				companyId, resourcePermission.getName(),
				ResourceConstants.SCOPE_GROUP, groupIdString, roleId,
				ActionKeys.ADD_TO_PAGE);
		}
	}

}