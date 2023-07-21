/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.service.impl.bundle.permissionserviceimpl;

import com.liferay.portal.kernel.security.permission.BaseModelPermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionChecker;

import java.util.concurrent.atomic.AtomicBoolean;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Philip Jones
 */
@Component(
	immediate = true,
	property = {
		"model.class.name=PermissionServiceImplTest",
		"service.ranking:Integer=" + Integer.MAX_VALUE
	},
	service = BaseModelPermissionChecker.class
)
public class TestBaseModelPermissionChecker
	implements BaseModelPermissionChecker {

	@Override
	public void checkBaseModel(
		PermissionChecker permissionChecker, long groupId, long primaryKey,
		String actionId) {

		_atomicBoolean.set(Boolean.TRUE);
	}

	@Reference(target = "(test=AtomicState)")
	protected void setAtomicBoolean(AtomicBoolean atomicBoolean) {
		_atomicBoolean = atomicBoolean;
	}

	private AtomicBoolean _atomicBoolean;

}