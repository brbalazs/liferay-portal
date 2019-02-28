/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.theme.minium.full.site.initializer.internal;

import com.liferay.commerce.theme.minium.api.SiteInitializerDependencyResolver;
import com.liferay.commerce.theme.minium.api.SiteInitializerDependencyResolverThreadLocal;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.site.exception.InitializationException;
import com.liferay.site.initializer.SiteInitializer;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 */
@Component(
	immediate = true,
	property = "site.initializer.key=" + MiniumFullSiteInitializer.KEY,
	service = SiteInitializer.class
)
public class MiniumFullSiteInitializer implements SiteInitializer {

	public static final String KEY = "minium-full-initializer";

	@Override
	public String getDescription(Locale locale) {
		return _siteInitializer.getDescription(locale);
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return "Minium Demo";
	}

	@Override
	public String getThumbnailSrc() {
		return _servletContext.getContextPath() + "/images/thumbnail.png";
	}

	@Override
	public void initialize(long groupId) throws InitializationException {
		try {
			SiteInitializerDependencyResolverThreadLocal.
				setSiteInitializerDependencyResolver(
					_fullSiteInitializerDependencyResolver);

			_siteInitializer.initialize(groupId);
		}
		catch (InitializationException ie) {
			throw ie;
		}
		catch (Exception e) {
			_log.error(e, e);

			throw new InitializationException(e);
		}
	}

	@Override
	public boolean isActive(long companyId) {
		return _siteInitializer.isActive(companyId);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MiniumFullSiteInitializer.class);

	@Reference(target = "(site.initializer.key=minium-full-initializer)")
	private SiteInitializerDependencyResolver
		_fullSiteInitializerDependencyResolver;

	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.commerce.theme.minium.full.site.initializer)"
	)
	private ServletContext _servletContext;

	@Reference(target = "(site.initializer.key=minium-initializer)")
	private SiteInitializer _siteInitializer;

}