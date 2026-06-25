/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.dsr.site.initializer.internal.security.permission.resource;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.portal.kernel.security.permission.ActionKeys;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermission;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionFactory;
import com.liferay.portal.kernel.security.permission.resource.ModelResourcePermissionLogic;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.site.dsr.site.initializer.util.DSRRoomUtil;

import java.util.Set;
import java.util.function.Consumer;

import org.osgi.service.component.annotations.Component;

/**
 * @author Balazs Breier
 */
@Component(
	property = "model.class.name=com.liferay.document.library.kernel.model.DLFileEntry",
	service = ModelResourcePermissionFactory.ModelResourcePermissionConfigurator.class
)
public class DSRDLFileEntryModelResourcePermissionConfigurator
	implements ModelResourcePermissionFactory.
				   ModelResourcePermissionConfigurator<DLFileEntry> {

	@Override
	public void configureModelResourcePermissionLogics(
		ModelResourcePermission<DLFileEntry> modelResourcePermission,
		Consumer<ModelResourcePermissionLogic<DLFileEntry>> consumer) {

		consumer.accept(
			(permissionChecker, name, dlFileEntry, actionId) -> {
				if (_delegableActionIds.contains(actionId)) {
					return null;
				}

				if (DSRRoomUtil.isReadOnly(
						dlFileEntry.getGroupId(), permissionChecker)) {

					return false;
				}

				return null;
			});
	}

	private static final Set<String> _delegableActionIds = SetUtil.fromArray(
		ActionKeys.ACCESS, ActionKeys.VIEW);

}