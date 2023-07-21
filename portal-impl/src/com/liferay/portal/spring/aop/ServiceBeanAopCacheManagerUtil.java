/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.spring.aop;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Shuyang Zhou
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
public class ServiceBeanAopCacheManagerUtil {

	public static void registerServiceBeanAopCacheManager(
		ServiceBeanAopCacheManager serviceBeanAopCacheManager) {

		_serviceBeanAopCacheManagers.add(serviceBeanAopCacheManager);
	}

	public static void reset() {
		for (ServiceBeanAopCacheManager serviceBeanAopCacheManager :
				_serviceBeanAopCacheManagers) {

			serviceBeanAopCacheManager.reset();
		}
	}

	public static void unregisterServiceBeanAopCacheManager(
		ServiceBeanAopCacheManager serviceBeanAopCacheManager) {

		_serviceBeanAopCacheManagers.remove(serviceBeanAopCacheManager);
	}

	private static final List<ServiceBeanAopCacheManager>
		_serviceBeanAopCacheManagers = new CopyOnWriteArrayList<>();

}