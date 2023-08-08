/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.cache;

import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

/**
 * @author Inácio Nery
 */
public class OSBAsahCacheResolver implements CacheResolver {

	public OSBAsahCacheResolver(CacheManager cacheManager) {
		_cacheManager = cacheManager;
	}

	@Override
	public Collection<Cache> resolveCaches(
		CacheOperationInvocationContext<?> cacheOperationInvocationContext) {

		Collection<Cache> caches = new ArrayList<>();

		Object target = cacheOperationInvocationContext.getTarget();

		StringBuilder sb = new StringBuilder();

		sb.append(ProjectIdThreadLocal.getProjectId());
		sb.append("#");

		Class<?> clazz = OSBAsahCacheUtil.extractTargetClass(target);

		sb.append(StringUtils.replace(clazz.getName(), "Custom", ""));

		caches.add(_cacheManager.getCache(sb.toString()));

		return caches;
	}

	private final CacheManager _cacheManager;

}