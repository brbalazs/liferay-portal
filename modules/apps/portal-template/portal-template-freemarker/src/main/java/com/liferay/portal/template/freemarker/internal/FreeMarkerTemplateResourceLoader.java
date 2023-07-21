/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.template.freemarker.internal;

import com.liferay.portal.kernel.template.TemplateConstants;
import com.liferay.portal.kernel.template.TemplateResource;
import com.liferay.portal.kernel.template.TemplateResourceLoader;
import com.liferay.portal.template.DefaultTemplateResourceLoader;
import com.liferay.portal.template.TemplateResourceParser;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Igor Spasic
 */
@Component(
	immediate = true,
	service = {
		FreeMarkerTemplateResourceLoader.class, TemplateResourceLoader.class
	}
)
public class FreeMarkerTemplateResourceLoader
	implements TemplateResourceLoader {

	@Override
	public void clearCache() {
		_defaultTemplateResourceLoader.clearCache();
	}

	@Override
	public void clearCache(String templateId) {
		_defaultTemplateResourceLoader.clearCache(templateId);
	}

	@Deactivate
	@Override
	public void destroy() {
		_defaultTemplateResourceLoader.destroy();
	}

	@Override
	public String getName() {
		return _defaultTemplateResourceLoader.getName();
	}

	@Override
	public TemplateResource getTemplateResource(String templateId) {
		return _defaultTemplateResourceLoader.getTemplateResource(templateId);
	}

	@Override
	public boolean hasTemplateResource(String templateId) {
		return _defaultTemplateResourceLoader.hasTemplateResource(templateId);
	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_defaultTemplateResourceLoader = new DefaultTemplateResourceLoader(
			TemplateConstants.LANG_TYPE_FTL, _templateResourceParsers,
			_freeMarkerTemplateResourceCache);
	}

	@Reference(
		cardinality = ReferenceCardinality.MULTIPLE,
		policy = ReferencePolicy.DYNAMIC,
		policyOption = ReferencePolicyOption.GREEDY,
		target = "(lang.type=" + TemplateConstants.LANG_TYPE_FTL + ")"
	)
	protected void setTemplateResourceParser(
		TemplateResourceParser templateResourceParser) {

		_templateResourceParsers.add(templateResourceParser);
	}

	protected void unsetTemplateResourceParser(
		TemplateResourceParser templateResourceParser) {

		_templateResourceParsers.remove(templateResourceParser);
	}

	private static volatile DefaultTemplateResourceLoader
		_defaultTemplateResourceLoader;

	@Reference
	private FreeMarkerTemplateResourceCache _freeMarkerTemplateResourceCache;

	private final Set<TemplateResourceParser> _templateResourceParsers =
		Collections.newSetFromMap(new ConcurrentHashMap<>());

}