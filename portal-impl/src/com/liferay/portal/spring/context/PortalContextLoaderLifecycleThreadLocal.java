/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.context;

import com.liferay.petra.lang.CentralizedThreadLocal;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class PortalContextLoaderLifecycleThreadLocal {

	public static boolean isDestroying() {
		return _destroying.get();
	}

	public static boolean isInitializing() {
		return _initializing.get();
	}

	public static void setDestroying(boolean destroying) {
		_destroying.set(destroying);
	}

	public static void setInitializing(boolean initializing) {
		_initializing.set(initializing);
	}

	private static final ThreadLocal<Boolean> _destroying =
		new CentralizedThreadLocal<>(
			PortalContextLoaderLifecycleThreadLocal.class + "._destroying",
			() -> Boolean.FALSE, false);
	private static final ThreadLocal<Boolean> _initializing =
		new CentralizedThreadLocal<>(
			PortalContextLoaderLifecycleThreadLocal.class + "._initializing",
			() -> Boolean.FALSE, false);

}