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
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Time;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;
import java.io.Writer;

import java.util.Map;
import java.util.Objects;

import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;

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

		ThemeDisplay themeDisplay = (ThemeDisplay)servletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PermissionChecker permissionChecker =
			themeDisplay.getPermissionChecker();

		Map<String, String> licenseProperties =
			LicenseManagerUtil.getLicenseProperties(_productId);

		int licenseState = LicenseManagerUtil.getLicenseState(_productId);

		if ((licenseState == LicenseManager.STATE_OVERLOAD) &&
			Objects.equals("virtual-cluster", licenseProperties.get("type"))) {

			Writer writer = servletResponse.getWriter();

			if (permissionChecker.isOmniadmin()) {
				writer.write(
					_getAdminMessage(
						licenseProperties,
						"it has exceeded the maximum number of cluster nodes " +
							GetterUtil.getInteger(
								licenseProperties.get("maxClusterNodes")),
						servletRequest));
			}
			else {
				StringBundler sb = new StringBundler(5);

				sb.append("<div class=\"alert alert-danger\">The activation ");
				sb.append("key for ");
				sb.append(licenseProperties.get("productEntryName"));
				sb.append(" has exceeded the maximum number of cluster ");
				sb.append("nodes. Please contact your administrator.");

				writer.write(sb.toString());
			}

			return;
		}

		long expirationDate = GetterUtil.getLong(
			licenseProperties.get("expirationDate"));

		long expirationDays =
			(expirationDate - System.currentTimeMillis()) / Time.DAY;

		if (licenseState == LicenseManager.STATE_EXPIRED) {
			Writer writer = servletResponse.getWriter();

			if (permissionChecker.isOmniadmin()) {
				writer.write(
					_getExpirationMessage(
						licenseProperties, expirationDays, servletRequest));
			}
			else {
				StringBundler sb = new StringBundler(4);

				sb.append("<div class=\"alert alert-danger\">The activation ");
				sb.append("key for ");
				sb.append(licenseProperties.get("productEntryName"));
				sb.append(" has expired. Please contact your administrator.");

				writer.write(sb.toString());
			}

			return;
		}

		filterChain.doFilter(servletRequest, servletResponse);

		if (!permissionChecker.isOmniadmin()) {
			return;
		}

		long startDate = GetterUtil.getLong(licenseProperties.get("startDate"));

		long lifetimeDays = (expirationDate - startDate) / Time.DAY;

		if (((lifetimeDays == 30) && (expirationDays < 7)) ||
			((lifetimeDays > 30) && (expirationDays < 30))) {

			Writer writer = servletResponse.getWriter();

			writer.write(
				_getExpirationMessage(
					licenseProperties, expirationDays, servletRequest));
		}
	}

	@Override
	public void init(FilterConfig filterConfig) {
	}

	private String _getAdminMessage(
		Map<String, String> licenseProperties, String reason,
		ServletRequest servletRequest) {

		StringBundler sb = new StringBundler(8);

		sb.append("<div class=\"alert alert-danger\">Update your ");
		sb.append("<a class=\"alert-link\" href=\"");

		PortletURL portletURL = PortalUtil.getControlPanelPortletURL(
			(PortletRequest)servletRequest.getAttribute(
				JavaConstants.JAVAX_PORTLET_REQUEST),
			"com_liferay_license_manager_web_portlet_LicenseManagerPortlet",
			PortletRequest.RENDER_PHASE);

		sb.append(portletURL.toString());

		sb.append("\">activation key for ");
		sb.append(licenseProperties.get("productEntryName"));
		sb.append("</a>, ");
		sb.append(reason);
		sb.append("</div>");

		return sb.toString();
	}

	private String _getExpirationMessage(
		Map<String, String> licenseProperties, long expirationDays,
		ServletRequest servletRequest) {

		StringBundler sb = new StringBundler(6);

		sb.append("it ");

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

		return _getAdminMessage(
			licenseProperties, sb.toString(), servletRequest);
	}

	private final String _productId;

}