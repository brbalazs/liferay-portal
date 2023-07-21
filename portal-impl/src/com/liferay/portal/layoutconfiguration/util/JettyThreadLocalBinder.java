/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.layoutconfiguration.util;

import com.liferay.portal.kernel.util.DefaultThreadLocalBinder;
import com.liferay.portal.kernel.util.ServerDetector;

/**
 * @author Shuyang Zhou
 */
public class JettyThreadLocalBinder extends DefaultThreadLocalBinder {

	@Override
	public void afterPropertiesSet() throws Exception {
		if (!ServerDetector.isJetty()) {
			return;
		}

		Thread currentThread = Thread.currentThread();

		ClassLoader classLoader = currentThread.getContextClassLoader();

		classLoader = classLoader.getParent();

		setClassLoader(classLoader);

		super.afterPropertiesSet();

		ParallelRenderThreadLocalBinderUtil.setThreadLocalBinder(this);
	}

}