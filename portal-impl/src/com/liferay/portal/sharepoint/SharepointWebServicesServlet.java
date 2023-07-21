/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.sharepoint;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.servlet.HttpHeaders;
import com.liferay.portal.kernel.servlet.ServletResponseUtil;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.StringBundler;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bruno Farache
 */
public class SharepointWebServicesServlet extends HttpServlet {

	@Override
	protected void doPost(
		HttpServletRequest request, HttpServletResponse response) {

		if (_log.isInfoEnabled()) {
			_log.info(
				StringBundler.concat(
					request.getHeader(HttpHeaders.USER_AGENT), " ",
					request.getMethod(), " ", request.getRequestURI()));
		}

		try {
			String uri = request.getRequestURI();

			if (uri.equals("/_vti_bin/webs.asmx")) {
				vtiBinWebsAsmx(request, response);
			}
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	protected void vtiBinWebsAsmx(
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		StringBundler sb = new StringBundler(12);

		String url = StringBundler.concat(
			"http://", request.getLocalAddr(), ":",
			String.valueOf(request.getServerPort()), "/sharepoint");

		sb.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"");
		sb.append("http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<SOAP-ENV:Header/>");
		sb.append("<SOAP-ENV:Body>");
		sb.append("<WebUrlFromPageUrlResponse xmlns=\"");
		sb.append("http://schemas.microsoft.com/sharepoint/soap/\">");
		sb.append("<WebUrlFromPageUrlResult>");
		sb.append(url);
		sb.append("</WebUrlFromPageUrlResult>");
		sb.append("</WebUrlFromPageUrlResponse>");
		sb.append("</SOAP-ENV:Body>");
		sb.append("</SOAP-ENV:Envelope>");

		response.setContentType(ContentTypes.TEXT_XML_UTF8);

		ServletResponseUtil.write(response, sb.toString());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		SharepointWebServicesServlet.class);

}