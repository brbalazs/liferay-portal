/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.soy.internal;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.template.TemplateResource;

import java.util.HashSet;
import java.util.List;

/**
 * @author Bruno Basto
 */
public class SoyTofuCacheHandler {

	public SoyTofuCacheHandler(
		PortalCache<HashSet<TemplateResource>, SoyTofuCacheBag> portalCache) {

		_portalCache = portalCache;
	}

	public SoyTofuCacheBag add(
		List<TemplateResource> templateResources, SoyFileSet soyFileSet,
		SoyTofu soyTofu) {

		HashSet<TemplateResource> key = getKeySet(templateResources);

		SoyTofuCacheBag soyTofuCacheBag = new SoyTofuCacheBag(
			soyFileSet, soyTofu);

		_portalCache.put(key, soyTofuCacheBag);

		return soyTofuCacheBag;
	}

	public SoyTofuCacheBag get(List<TemplateResource> templateResources) {
		HashSet<TemplateResource> key = getKeySet(templateResources);

		return _portalCache.get(key);
	}

	public SoyTofu getSoyTofu(List<TemplateResource> templateResources) {
		SoyTofuCacheBag soyTofuCacheBag = get(templateResources);

		return soyTofuCacheBag.getSoyTofu();
	}

	public void removeIfAny(List<TemplateResource> templateResources) {
		for (TemplateResource templateResource : templateResources) {
			for (HashSet<TemplateResource> key : _portalCache.getKeys()) {
				if (key.contains(templateResource)) {
					_portalCache.remove(key);
				}
			}
		}
	}

	protected HashSet<TemplateResource> getKeySet(
		List<TemplateResource> templateResources) {

		return new HashSet<>(templateResources);
	}

	private final PortalCache<HashSet<TemplateResource>, SoyTofuCacheBag>
		_portalCache;

}