/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.workflow.kaleo.designer.web.internal.permission;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion;
import com.liferay.portal.workflow.kaleo.service.KaleoDefinitionVersionLocalService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Marcellus Tavares
 */
@Component(
	immediate = true,
	property = "model.class.name=com.liferay.portal.workflow.kaleo.model.KaleoDefinitionVersion",
	service = BaseModelPermissionChecker.class
)
public class KaleoDefinitionVersionPermission
	implements BaseModelPermissionChecker {

	public static void check(
			PermissionChecker permissionChecker, long kaleoDefinitionVersionId,
			String actionId)
		throws PortalException {

		if (!contains(permissionChecker, kaleoDefinitionVersionId, actionId)) {
			throw new PrincipalException.MustHavePermission(
				permissionChecker, KaleoDefinitionVersion.class.getName(),
				kaleoDefinitionVersionId, actionId);
		}
	}

	public static boolean contains(
		PermissionChecker permissionChecker,
		KaleoDefinitionVersion kaleoDefinitionVersion, String actionId) {

		if (permissionChecker.hasOwnerPermission(
				kaleoDefinitionVersion.getCompanyId(),
				KaleoDefinitionVersion.class.getName(),
				kaleoDefinitionVersion.getKaleoDefinitionVersionId(),
				kaleoDefinitionVersion.getUserId(), actionId)) {

			return true;
		}

		return permissionChecker.hasPermission(
			kaleoDefinitionVersion.getGroupId(),
			KaleoDefinitionVersion.class.getName(),
			kaleoDefinitionVersion.getKaleoDefinitionVersionId(), actionId);
	}

	public static boolean contains(
			PermissionChecker permissionChecker, long kaleoDefinitionVersionId,
			String actionId)
		throws PortalException {

		KaleoDefinitionVersion kaleoDefinitionVersion =
			_kaleoDefinitionVersionLocalService.getKaleoDefinitionVersion(
				kaleoDefinitionVersionId);

		return contains(permissionChecker, kaleoDefinitionVersion, actionId);
	}

	public static boolean hasViewPermission(
		PermissionChecker permissionChecker,
		KaleoDefinitionVersion kaleoDefinitionVersion, long companyGroupId) {

		if (contains(
				permissionChecker, kaleoDefinitionVersion, ActionKeys.DELETE) ||
			contains(
				permissionChecker, kaleoDefinitionVersion,
				ActionKeys.PERMISSIONS) ||
			contains(
				permissionChecker, kaleoDefinitionVersion, ActionKeys.UPDATE) ||
			contains(
				permissionChecker, kaleoDefinitionVersion, ActionKeys.VIEW) ||
			KaleoDesignerPermission.contains(
				permissionChecker, companyGroupId, ActionKeys.VIEW)) {

			return true;
		}

		return false;
	}

	@Override
	public void checkBaseModel(
			PermissionChecker permissionChecker, long groupId, long primaryKey,
			String actionId)
		throws PortalException {

		check(permissionChecker, primaryKey, actionId);
	}

	@Reference(unbind = "-")
	protected void setKaleoDefinitionVersionLocalService(
		KaleoDefinitionVersionLocalService kaleoDefinitionVersionLocalService) {

		_kaleoDefinitionVersionLocalService =
			kaleoDefinitionVersionLocalService;
	}

	private static KaleoDefinitionVersionLocalService
		_kaleoDefinitionVersionLocalService;

}