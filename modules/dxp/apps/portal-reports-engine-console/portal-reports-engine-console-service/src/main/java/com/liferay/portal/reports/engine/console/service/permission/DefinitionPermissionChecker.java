/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.reports.engine.console.service.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.reports.engine.console.model.Definition;
import com.liferay.portal.reports.engine.console.service.DefinitionLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.portal.reports.engine.console.model.Definition",
	service = BaseModelPermissionChecker.class
)
public class DefinitionPermissionChecker implements BaseModelPermissionChecker {

	public static void check(
			PermissionChecker permissionChecker, Definition definition,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, definition, actionId)) {
			throw new PrincipalException();
		}
	}

	public static void check(
			PermissionChecker permissionChecker, long definitionId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, definitionId, actionId)) {
			throw new PrincipalException();
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker, Definition definition,
		String actionId) {

		if (permissionChecker.hasOwnerPermission(
				definition.getCompanyId(), Definition.class.getName(),
				definition.getDefinitionId(), definition.getUserId(),
				actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			definition.getGroupId(), Definition.class.getName(),
			definition.getDefinitionId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long definitionId,
			String actionId)
		throws PortalException {

		Definition definition = _definitionLocalService.getDefinition(
			definitionId);

		return contains(permissionChecker, definition, actionId);
	}

	@Override
	public void checkBaseModel(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException {

		check(permissionChecker, primaryKey, actionId);
	}

	@Reference(unbind = "-")
	protected void setDefinitionLocalService(
		DefinitionLocalService definitionLocalService) {

		_definitionLocalService = definitionLocalService;
	}

	private static DefinitionLocalService _definitionLocalService;

}