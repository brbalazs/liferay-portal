/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.cache;

import com.liferay.portal.kernel.spring.osgi.OSGiBeanProperties;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 * @deprecated As of Judson (7.1.x), with no direct replacement
 */
@Deprecated
@OSGiBeanProperties(service = SingleVMPoolUtil.class)
public class SingleVMPoolUtil {

	public static void clear() {
		_singleVMPool.clear();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCache(String)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName) {

		return getPortalCache(portalCacheName);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCache(String, boolean)}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCache<K, V> getCache(
		String portalCacheName, boolean blocking) {

		return getPortalCache(portalCacheName, blocking);
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCacheManager()}
	 */
	@Deprecated
	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getCacheManager() {

		return getPortalCacheManager();
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName) {

		return (PortalCache<K, V>)_singleVMPool.getPortalCache(portalCacheName);
	}

	public static <K extends Serializable, V> PortalCache<K, V> getPortalCache(
		String portalCacheName, boolean blocking) {

		return (PortalCache<K, V>)_singleVMPool.getPortalCache(
			portalCacheName, blocking);
	}

	public static <K extends Serializable, V> PortalCacheManager<K, V>
		getPortalCacheManager() {

		return (PortalCacheManager<K, V>)_singleVMPool.getPortalCacheManager();
	}

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #removePortalCache(String)}
	 */
	@Deprecated
	public static void removeCache(String portalCacheName) {
		removePortalCache(portalCacheName);
	}

	public static void removePortalCache(String portalCacheName) {
		_singleVMPool.removePortalCache(portalCacheName);
	}

	private static volatile SingleVMPool _singleVMPool =
		ServiceProxyFactory.newServiceTrackedInstance(
			SingleVMPool.class, SingleVMPoolUtil.class, "_singleVMPool", true);

}