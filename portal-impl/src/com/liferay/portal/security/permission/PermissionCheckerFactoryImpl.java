/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.security.permission;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionCheckerFactory;
import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.util.PropsValues;

/**
 * @author Charles May
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
@OSGiBeanProperties(property = "service.ranking:Integer=-1")
public class PermissionCheckerFactoryImpl implements PermissionCheckerFactory {

	public PermissionCheckerFactoryImpl() throws Exception {
		Class<PermissionChecker> clazz =
			(Class<PermissionChecker>)Class.forName(
				PropsValues.PERMISSIONS_CHECKER);

		_permissionChecker = clazz.newInstance();
	}

	@Override
	public PermissionChecker create(User user) {
		PermissionChecker permissionChecker = _permissionChecker.clone();

		permissionChecker.init(user);

		return permissionChecker;
	}

	private final PermissionChecker _permissionChecker;

}