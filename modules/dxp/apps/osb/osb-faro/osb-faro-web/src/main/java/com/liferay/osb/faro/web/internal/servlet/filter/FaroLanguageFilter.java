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

package com.liferay.osb.faro.web.internal.servlet.filter;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.servlet.BaseFilter;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.WebKeys;

import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Matthew Kong
 */
@Component(
	immediate = true,
	property = {
		"before-filter=URL Rewrite Filter", "dispatcher=FORWARD",
		"dispatcher=REQUEST", "servlet-context-name=",
		"servlet-filter-name=Faro Language Filter", "url-pattern=/*"
	},
	service = Filter.class
)
public class FaroLanguageFilter extends BaseFilter {

	@Override
	protected Log getLog() {
		return _log;
	}

	@Override
	protected void processFilter(
			HttpServletRequest httpServletRequest,
			HttpServletResponse httpServletResponse, FilterChain filterChain)
		throws Exception {

		User user = _portal.getUser(httpServletRequest);

		if (user != null) {
			Locale locale = user.getLocale();

			httpServletRequest.setAttribute(
				WebKeys.I18N_LANGUAGE_CODE, LanguageUtil.getLanguageId(locale));
			httpServletRequest.setAttribute(
				WebKeys.I18N_LANGUAGE_ID, LanguageUtil.getLanguageId(locale));
			httpServletRequest.setAttribute(
				WebKeys.I18N_PATH, StringPool.SLASH + locale.toLanguageTag());

			HttpSession session = httpServletRequest.getSession();

			session.setAttribute(WebKeys.LOCALE, locale);
		}

		filterChain.doFilter(httpServletRequest, httpServletResponse);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FaroLanguageFilter.class);

	@Reference
	private Portal _portal;

}