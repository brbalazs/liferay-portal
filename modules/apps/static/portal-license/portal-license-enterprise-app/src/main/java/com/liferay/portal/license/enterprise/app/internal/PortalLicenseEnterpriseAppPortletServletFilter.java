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

package com.liferay.portal.license.enterprise.app.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.license.util.LicenseManager;
import com.liferay.portal.kernel.license.util.LicenseManagerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.Writer;

import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Dante Wang
 */
public class PortalLicenseEnterpriseAppPortletServletFilter implements Filter {

	public PortalLicenseEnterpriseAppPortletServletFilter(String productId) {
		_productId = productId;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(
			ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain)
		throws IOException, ServletException {

		Map<String, String> licenseProperties =
			LicenseManagerUtil.getLicenseProperties(_productId);

		long expirationDate = GetterUtil.getLong(
			licenseProperties.get("expirationDate"));

		long expirationDays =
			(expirationDate - System.currentTimeMillis()) / Time.DAY;

		if (LicenseManagerUtil.getLicenseState(_productId) ==
				LicenseManager.STATE_EXPIRED) {

			servletRequest.setAttribute(
				"ERROR_MESSAGE",
				StringBundler.concat(
					"Your license for product ", _productId, " expired ",
					expirationDays * -1, " day(s) ago"));

			servletRequest.setAttribute(
				WebKeys.PORTLET_CONTENT_JSP, "/portal/license.jsp");

			return;
		}

		filterChain.doFilter(servletRequest, servletResponse);

		long startDate = GetterUtil.getLong(licenseProperties.get("startDate"));

		long lifetimeDays = (expirationDate - startDate) / Time.DAY;

		if (((lifetimeDays == 30) && (expirationDays < 7)) ||
			((lifetimeDays > 30) && (expirationDays < 30))) {

			StringBundler sb = new StringBundler(11);

			sb.append("<div class=\"alert alert-danger\">Update your ");
			sb.append("<a class=\"alert-link\" href=\"");
			sb.append(PortalUtil.getPathMain());
			sb.append("/portal/license\">activation key for ");
			sb.append(_productId);
			sb.append("</a>, it ");

			if (expirationDays <= 0) {
				sb.append("has been expired for ");

				expirationDays = expirationDays * -1;
			}
			else {
				sb.append("will expire in ");
			}

			sb.append(expirationDays);
			sb.append(" day");

			if (expirationDays > 1) {
				sb.append("s");
			}

			sb.append("</div>");

			Writer writer = servletResponse.getWriter();

			writer.write(sb.toString());
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	private final String _productId;

}