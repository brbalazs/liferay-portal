/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.cache;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.nio.intraband.proxy.annotation.Proxy;

import java.io.Serializable;

import java.net.URL;

import java.util.Set;

/**
 * @author Joseph Shum
 */
@ProviderType
public interface PortalCacheManager<K extends Serializable, V> {

	public static final String PORTAL_CACHE_MANAGER_NAME =
		"portal.cache.manager.name";

	public static final String PORTAL_CACHE_MANAGER_TYPE =
		"portal.cache.manager.type";

	@Proxy
	public void clearAll() throws PortalCacheException;

	public void destroy();

	public PortalCache<K, V> fetchPortalCache(String portalCacheName);

	public PortalCache<K, V> getPortalCache(String portalCacheName)
		throws PortalCacheException;

	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean blocking)
		throws PortalCacheException;

	public PortalCache<K, V> getPortalCache(
			String portalCacheName, boolean blocking, boolean mvcc)
		throws PortalCacheException;

	public Set<PortalCacheManagerListener> getPortalCacheManagerListeners();

	public String getPortalCacheManagerName();

	public boolean isClusterAware();

	@Proxy
	public void reconfigurePortalCaches(URL configurationURL);

	public boolean registerPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener);

	public void removePortalCache(String portalCacheName);

	public boolean unregisterPortalCacheManagerListener(
		PortalCacheManagerListener portalCacheManagerListener);

	public void unregisterPortalCacheManagerListeners();

}