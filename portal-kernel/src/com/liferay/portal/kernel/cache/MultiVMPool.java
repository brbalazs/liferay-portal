/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.cache;

import aQute.bnd.annotation.ProviderType;

import java.io.Serializable;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 */
@ProviderType
public interface MultiVMPool {

	public void clear();

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCache(String)}
	 */
	@Deprecated
	public PortalCache<? extends Serializable, ? extends Serializable> getCache(
		String portalCacheName);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCache(String, boolean)}
	 */
	@Deprecated
	public PortalCache<? extends Serializable, ? extends Serializable> getCache(
		String portalCacheName, boolean blocking);

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #getPortalCacheManager()}
	 */
	@Deprecated
	public PortalCacheManager<? extends Serializable, ? extends Serializable>
		getCacheManager();

	public PortalCache<? extends Serializable, ? extends Serializable>
		getPortalCache(String portalCacheName);

	public PortalCache<? extends Serializable, ? extends Serializable>
		getPortalCache(String portalCacheName, boolean blocking);

	public PortalCache<? extends Serializable, ? extends Serializable>
		getPortalCache(String portalCacheName, boolean blocking, boolean mvcc);

	public PortalCacheManager<? extends Serializable, ? extends Serializable>
		getPortalCacheManager();

	/**
	 * @deprecated As of Wilberforce (7.0.x), replaced by {@link
	 *             #removePortalCache(String)}
	 */
	@Deprecated
	public void removeCache(String portalCacheName);

	public void removePortalCache(String portalCacheName);

}