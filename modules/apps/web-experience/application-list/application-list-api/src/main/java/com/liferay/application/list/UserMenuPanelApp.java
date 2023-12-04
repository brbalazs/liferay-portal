/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.application.list;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.model.GroupConstants;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.service.GroupLocalService;

import org.osgi.service.component.annotations.Reference;

/**
 * @author Istvan Sajtos
 */
public abstract class UserMenuPanelApp extends BasePanelApp {

	@Override
	public boolean isShow(PermissionChecker permissionChecker, Group group)
		throws PortalException {

		if (!group.isControlPanel()) {
			group = groupLocalService.getGroup(
				group.getCompanyId(), GroupConstants.CONTROL_PANEL);
		}

		return super.isShow(permissionChecker, group);
	}

	@Reference
	protected GroupLocalService groupLocalService;

}