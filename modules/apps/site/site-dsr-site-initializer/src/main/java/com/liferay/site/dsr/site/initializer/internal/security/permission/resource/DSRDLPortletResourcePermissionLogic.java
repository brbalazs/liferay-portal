/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.dsr.site.initializer.internal.security.permission.resource;

import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.resource.PortletResourcePermissionLogic;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portlet.documentlibrary.constants.DLConstants;
import com.liferay.site.dsr.site.initializer.util.DSRRoomUtil;

import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Balazs Breier
 */
@Component(
	property = "resource.name=" + DLConstants.RESOURCE_NAME,
	service = PortletResourcePermissionLogic.class
)
public class DSRDLPortletResourcePermissionLogic
	implements PortletResourcePermissionLogic {

	@Override
	public Boolean contains(
		PermissionChecker permissionChecker, String name, Group group,
		String actionId) {

		if (_delegableActionIds.contains(actionId)) {
			return null;
		}

		if (DSRRoomUtil.isReadOnly(group.getGroupId(), permissionChecker)) {
			return false;
		}

		return null;
	}

	private static final Set<String> _delegableActionIds = SetUtil.fromArray(
		ActionKeys.ACCESS, ActionKeys.ACCESS_IN_CONTROL_PANEL, ActionKeys.VIEW);

}