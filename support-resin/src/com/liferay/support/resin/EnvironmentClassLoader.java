/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.support.resin;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;

import java.util.ArrayList;

/**
 * @author Raymond Augé
 */
public class EnvironmentClassLoader
	extends com.caucho.loader.EnvironmentClassLoader {

	public EnvironmentClassLoader(ClassLoader classLoader, String id) {
		super(classLoader, id);

		_id = id;
	}

	@Override
	public ArrayList<Permission> getPermissions() {
		if (_SECURITY_ENABLED && (_id != null) && _id.startsWith("web-app:") &&
			!_id.endsWith("/ROOT")) {

			return new ArrayList<>();
		}

		return super.getPermissions();
	}

	@Override
	protected PermissionCollection getPermissions(CodeSource codeSource) {
		if (_SECURITY_ENABLED && (_id != null) && _id.startsWith("web-app:") &&
			!_id.endsWith("/ROOT")) {

			return new Permissions();
		}

		return super.getPermissions(codeSource);
	}

	private static final boolean _SECURITY_ENABLED =
		System.getSecurityManager() != null;

	private final String _id;

}