/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.lpkg.deployer.internal;

import com.liferay.petra.lang.CentralizedThreadLocal;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;

/**
 * @author Shuyang Zhou
 */
public class LPKGIndexValidatorThreadLocal {

	public static boolean isEnabled() {
		return _enabled.get();
	}

	public static void setEnabled(boolean enabled) {
		_enabled.set(enabled);
	}

	private static final ThreadLocal<Boolean> _enabled =
		new CentralizedThreadLocal<>(
			CompanyThreadLocal.class + "._enabled", () -> Boolean.TRUE);

}