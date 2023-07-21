/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.cache.MultiVMPool;
import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.SingleVMPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.template.StringTemplateResource;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceCache;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.ProxyFactory;

/**
 * @author Tina Tian
 */
public abstract class BaseTemplateResourceCache
	implements TemplateResourceCache {

	public void clear() {
		if (!isEnabled()) {
			return;
		}

		_multiVMPortalCache.removeAll();
		_singleVMPortalCache.removeAll();
	}

	public TemplateResource getTemplateResource(String templateId) {
		if (!isEnabled()) {
			return null;
		}

		TemplateResource templateResource = _singleVMPortalCache.get(
			templateId);

		if (templateResource == null) {
			templateResource = _multiVMPortalCache.get(templateId);
		}

		if ((templateResource != null) &&
			(templateResource != DUMMY_TEMPLATE_RESOURCE) &&
			(_modificationCheckInterval > 0)) {

			long expireTime =
				templateResource.getLastModified() + _modificationCheckInterval;

			if (System.currentTimeMillis() > expireTime) {
				remove(templateId);

				templateResource = null;

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Remove expired template resource " + templateId);
				}
			}
		}

		return templateResource;
	}

	public boolean isEnabled() {
		if (_modificationCheckInterval == 0) {
			return false;
		}

		return true;
	}

	public void put(String templateId, TemplateResource templateResource) {
		if (!isEnabled()) {
			return;
		}

		if (templateResource == null) {
			_singleVMPortalCache.put(templateId, DUMMY_TEMPLATE_RESOURCE);
		}
		else if (templateResource instanceof URLTemplateResource) {
			_singleVMPortalCache.put(
				templateId, new CacheTemplateResource(templateResource));
		}
		else if (templateResource instanceof CacheTemplateResource ||
				 templateResource instanceof StringTemplateResource) {

			_multiVMPortalCache.put(templateId, templateResource);
		}
		else {
			_multiVMPortalCache.put(
				templateId, new CacheTemplateResource(templateResource));
		}
	}

	public void remove(String templateId) {
		if (!isEnabled()) {
			return;
		}

		_multiVMPortalCache.remove(templateId);
		_singleVMPortalCache.remove(templateId);
	}

	public void setSecondLevelPortalCache(
		PortalCache<TemplateResource, ?> portalCache) {

		if (!isEnabled()) {
			return;
		}

		if (_templateResourcePortalCacheListener != null) {
			_multiVMPortalCache.unregisterPortalCacheListener(
				_templateResourcePortalCacheListener);
			_singleVMPortalCache.unregisterPortalCacheListener(
				_templateResourcePortalCacheListener);
		}

		_templateResourcePortalCacheListener =
			new TemplateResourcePortalCacheListener(portalCache);

		_multiVMPortalCache.registerPortalCacheListener(
			_templateResourcePortalCacheListener);
		_singleVMPortalCache.registerPortalCacheListener(
			_templateResourcePortalCacheListener);
	}

	protected void destroy() {
		if (!isEnabled()) {
			return;
		}

		_multiVMPool.removePortalCache(_portalCacheName);
		_singleVMPool.removePortalCache(_portalCacheName);
	}

	protected void init(
		long modificationCheckInterval, MultiVMPool multiVMPool,
		SingleVMPool singleVMPool, String portalCacheName) {

		_modificationCheckInterval = modificationCheckInterval;
		_multiVMPool = multiVMPool;
		_singleVMPool = singleVMPool;
		_portalCacheName = portalCacheName;

		if (isEnabled()) {
			_multiVMPortalCache =
				(PortalCache<String, TemplateResource>)
					multiVMPool.getPortalCache(portalCacheName);
			_singleVMPortalCache =
				(PortalCache<String, TemplateResource>)
					singleVMPool.getPortalCache(portalCacheName);
		}
	}

	protected static final TemplateResource DUMMY_TEMPLATE_RESOURCE =
		ProxyFactory.newDummyInstance(TemplateResource.class);

	private static final Log _log = LogFactoryUtil.getLog(
		BaseTemplateResourceCache.class);

	private long _modificationCheckInterval;
	private MultiVMPool _multiVMPool;
	private PortalCache<String, TemplateResource> _multiVMPortalCache;
	private String _portalCacheName;
	private SingleVMPool _singleVMPool;
	private PortalCache<String, TemplateResource> _singleVMPortalCache;
	private TemplateResourcePortalCacheListener
		_templateResourcePortalCacheListener;

}