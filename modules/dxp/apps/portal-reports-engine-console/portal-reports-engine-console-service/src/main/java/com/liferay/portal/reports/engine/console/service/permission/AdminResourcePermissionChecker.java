/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.reports.engine.console.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.BaseResourcePermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.ResourcePermissionChecker;
import com.liferay.portal.reports.engine.console.constants.ReportsEngineConsolePortletKeys;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Shin
 */
@Component(
	immediate = true,
	property = "resource.name=" + AdminResourcePermissionChecker.RESOURCE_NAME,
	service = ResourcePermissionChecker.class
)
public class AdminResourcePermissionChecker
	extends BaseResourcePermissionChecker {

	public static final String RESOURCE_NAME =
		"com.liferay.portal.reports.engine.console.admin";

	public static void check(
			PermissionChecker permissionChecker, long groupId, String actionId)
		throws PortalException {

		if (!contains(permissionChecker, groupId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, RESOURCE_NAME, groupId, actionId);
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker, long classPK, String actionId) {

		return contains(
			permissionChecker, RESOURCE_NAME,
			ReportsEngineConsolePortletKeys.REPORTS_ADMIN, classPK, actionId);
	}

	@Override
	public Boolean checkResource(
		PermissionChecker permissionChecker, long classPK, String actionId) {

		return contains(permissionChecker, classPK, actionId);
	}

}