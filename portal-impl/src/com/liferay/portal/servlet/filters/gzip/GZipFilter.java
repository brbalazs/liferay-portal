/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.servlet.filters.gzip;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.BrowserSnifferUtil;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.ServerDetector;
import com.liferay.portal.servlet.filters.BasePortalFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 */
public class GZipFilter extends BasePortalFilter {

	public static final String SKIP_FILTER =
		GZipFilter.class.getName() + "#SKIP_FILTER";

	public GZipFilter() {

		// The compression filter will work on JBoss, Jetty, JOnAS, OC4J,
		// Tomcat, WebLogic, and WebSphere, but may break on other servers

		boolean filterEnabled = false;

		if (super.isFilterEnabled() &&
			(ServerDetector.isJBoss() || ServerDetector.isJetty() ||
			 ServerDetector.isJOnAS() || ServerDetector.isOC4J() ||
			 ServerDetector.isTomcat() || ServerDetector.isWebLogic() ||
			 ServerDetector.isWebSphere())) {

			filterEnabled = true;
		}

		_filterEnabled = filterEnabled;
	}

	@Override
	public boolean isFilterEnabled() {
		return _filterEnabled;
	}

	@Override
	public boolean isFilterEnabled(
		HttpServletRequest request, HttpServletResponse response) {

		if (isCompress(request) && !isInclude(request) &&
			BrowserSnifferUtil.acceptsGzip(request) &&
			!isAlreadyFiltered(request)) {

			return true;
		}

		return false;
	}

	protected boolean isAlreadyFiltered(HttpServletRequest request) {
		if (request.getAttribute(SKIP_FILTER) != null) {
			return true;
		}

		return false;
	}

	protected boolean isCompress(HttpServletRequest request) {
		if (ParamUtil.getBoolean(request, _COMPRESS, true)) {
			return true;
		}

		return false;
	}

	protected boolean isInclude(HttpServletRequest request) {
		String uri = (String)request.getAttribute(
			JavaConstants.JAVAX_SERVLET_INCLUDE_REQUEST_URI);

		if (uri == null) {
			return false;
		}

		return true;
	}

	@Override
	protected void processFilter(
			HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
		throws Exception {

		if (_log.isDebugEnabled()) {
			String completeURL = HttpUtil.getCompleteURL(request);

			_log.debug("Compressing " + completeURL);
		}

		request.setAttribute(SKIP_FILTER, Boolean.TRUE);

		GZipResponse gZipResponse = new GZipResponse(response);

		processFilter(
			GZipFilter.class.getName(), request, gZipResponse, filterChain);

		gZipResponse.finishResponse();
	}

	private static final String _COMPRESS = "compress";

	private static final Log _log = LogFactoryUtil.getLog(GZipFilter.class);

	private final boolean _filterEnabled;

}