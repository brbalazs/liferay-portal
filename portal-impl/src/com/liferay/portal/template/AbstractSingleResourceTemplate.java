/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.template;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.PortalCacheHelperUtil;
import com.liferay.portal.kernel.cache.PortalCacheManagerNames;
import com.liferay.portal.kernel.io.unsync.UnsyncStringWriter;
import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateException;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceCache;
import com.liferay.portal.kernel.template.URLTemplateResource;
import com.liferay.portal.kernel.util.StringBundler;

import java.io.Serializable;
import java.io.Writer;

import java.util.Map;

/**
 * @author Miroslav Ligas
 */
public abstract class AbstractSingleResourceTemplate extends AbstractTemplate {

	public AbstractSingleResourceTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, boolean restricted,
		TemplateResourceCache templateResourceCache) {

		super(
			errorTemplateResource, context, templateContextHelper, restricted);

		if (templateResource == null) {
			throw new IllegalArgumentException("Template resource is null");
		}

		this.templateResource = templateResource;

		if (templateResourceCache.isEnabled()) {
			cacheTemplateResource(templateResourceCache);
		}
	}

	/**
	 * @deprecated As of Mueller (7.2.x), replaced by {@link
	 *             #AbstractSingleResourceTemplate(TemplateResource,
	 *             TemplateResource, Map, TemplateContextHelper, String, long,
	 *             boolean)}}
	 */
	@Deprecated
	public AbstractSingleResourceTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, String templateManagerName,
		long interval) {

		this(
			templateResource, errorTemplateResource, context,
			templateContextHelper, templateManagerName, interval, false);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #AbstractSingleResourceTemplate(TemplateResource,
	 *             TemplateResource, Map, TemplateContextHelper, boolean,
	 *             TemplateResourceCache)}}
	 */
	@Deprecated
	public AbstractSingleResourceTemplate(
		TemplateResource templateResource,
		TemplateResource errorTemplateResource, Map<String, Object> context,
		TemplateContextHelper templateContextHelper, String templateManagerName,
		long interval, boolean restricted) {

		this(
			templateResource, errorTemplateResource, context,
			templateContextHelper, false, null);
	}

	@Override
	public void doProcessTemplate(Writer writer) throws Exception {
		UnsyncStringWriter unsyncStringWriter = new UnsyncStringWriter();

		put(TemplateConstants.WRITER, unsyncStringWriter);

		processTemplate(templateResource, unsyncStringWriter);

		StringBundler sb = unsyncStringWriter.getStringBundler();

		sb.writeTo(writer);
	}

	@Override
	public void processTemplate(Writer writer) throws TemplateException {
		if (errorTemplateResource == null) {
			try {
				processTemplate(templateResource, writer);

				return;
			}
			catch (Exception e) {
				throw new TemplateException(
					"Unable to process template " +
						templateResource.getTemplateId(),
					e);
			}
		}

		write(writer);
	}

	/**
	 * @deprecated As of Judson (7.1.x), replaced by {@link
	 *             #cacheTemplateResource(TemplateResourceCache)}}
	 */
	@Deprecated
	protected void cacheTemplateResource(String templateManagerName) {
	}

	protected void cacheTemplateResource(
		TemplateResourceCache templateResourceCache) {

		TemplateResource cachedTemplateResource =
			templateResourceCache.getTemplateResource(
				templateResource.getTemplateId());

		if ((cachedTemplateResource == null) ||
			!templateResource.equals(cachedTemplateResource)) {

			templateResourceCache.put(
				templateResource.getTemplateId(), templateResource);
		}

		if (errorTemplateResource == null) {
			return;
		}

		TemplateResource cachedErrorTemplateResource =
			templateResourceCache.getTemplateResource(
				errorTemplateResource.getTemplateId());

		if ((cachedErrorTemplateResource == null) ||
			!errorTemplateResource.equals(cachedErrorTemplateResource)) {

			templateResourceCache.put(
				errorTemplateResource.getTemplateId(), errorTemplateResource);
		}
	}

	/**
	 * @deprecated As of Judson (7.1.x), with no direct replacement
	 */
	@Deprecated
	protected PortalCache<String, Serializable> getPortalCache(
		TemplateResource templateResource, String portalCacheName) {

		if (!(templateResource instanceof CacheTemplateResource)) {
			return PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.MULTI_VM, portalCacheName);
		}

		CacheTemplateResource cacheTemplateResource =
			(CacheTemplateResource)templateResource;

		TemplateResource innerTemplateResource =
			cacheTemplateResource.getInnerTemplateResource();

		if (innerTemplateResource instanceof URLTemplateResource) {
			return PortalCacheHelperUtil.getPortalCache(
				PortalCacheManagerNames.SINGLE_VM, portalCacheName);
		}

		return PortalCacheHelperUtil.getPortalCache(
			PortalCacheManagerNames.MULTI_VM, portalCacheName);
	}

	protected abstract void processTemplate(
			TemplateResource templateResource, Writer writer)
		throws Exception;

	protected TemplateResource templateResource;

}