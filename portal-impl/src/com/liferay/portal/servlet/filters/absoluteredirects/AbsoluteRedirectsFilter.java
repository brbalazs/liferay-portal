/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet.filters.absoluteredirects;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.servlet.TryFilter;
import com.liferay.portal.kernel.servlet.WrapHttpServletResponseFilter;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.servlet.filters.BasePortalFilter;
import com.liferay.portal.util.PortalInstances;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * This filter is used to ensure that all redirects are absolute. It should not
 * be disabled because it also sets the company ID in the request so that
 * subsequent calls in the thread have the company ID properly set. This filter
 * should also always be the first filter in the list of filters.
 * </p>
 *
 * @author Minhchau Dang
 * @author Brian Wing Shun Chan
 */
public class AbsoluteRedirectsFilter
	extends BasePortalFilter
	implements TryFilter, WrapHttpServletResponseFilter {

	@Override
	public Object doFilterTry(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		if (request.getCharacterEncoding() == null) {
			request.setCharacterEncoding(StringPool.UTF8);
		}

		//response.setContentType(ContentTypes.TEXT_HTML_UTF8);

		// Company id needs to always be called here so that it's properly set
		// in subsequent calls

		long companyId = PortalInstances.getCompanyId(request);

		if (_log.isDebugEnabled()) {
			_log.debug("Company id " + companyId);
		}

		PortalUtil.getCurrentCompleteURL(request);
		PortalUtil.getCurrentURL(request);

		HttpSession session = request.getSession();

		Boolean httpsInitial = (Boolean)session.getAttribute(
			WebKeys.HTTPS_INITIAL);

		if (httpsInitial == null) {
			httpsInitial = Boolean.valueOf(request.isSecure());

			session.setAttribute(WebKeys.HTTPS_INITIAL, httpsInitial);

			if (_log.isDebugEnabled()) {
				_log.debug("Setting httpsInitial to " + httpsInitial);
			}
		}

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			request);

		ServiceContextThreadLocal.pushServiceContext(serviceContext);

		return null;
	}

	@Override
	public HttpServletResponse getWrappedHttpServletResponse(
		HttpServletRequest request, HttpServletResponse response) {

		return new AbsoluteRedirectsResponse(request, response);
	}

	@Override
	public boolean isFilterEnabled() {
		return _FILTER_ENABLED;
	}

	private static final boolean _FILTER_ENABLED = true;

	private static final Log _log = LogFactoryUtil.getLog(
		AbsoluteRedirectsFilter.class);

}