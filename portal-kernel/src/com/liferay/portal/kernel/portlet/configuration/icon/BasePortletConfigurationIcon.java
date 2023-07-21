/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.portlet.configuration.icon;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Eudaldo Alonso
 */
public abstract class BasePortletConfigurationIcon
	implements PortletConfigurationIcon {

	@Override
	public String getAlt() {
		return null;
	}

	@Override
	public String getAriaRole() {
		return null;
	}

	@Override
	public String getCssClass() {
		return null;
	}

	@Override
	public Map<String, Object> getData() {
		return null;
	}

	@Override
	public String getIconCssClass() {
		return null;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getImage() {
		return null;
	}

	@Override
	public String getImageHover() {
		return null;
	}

	@Override
	public String getLang() {
		return null;
	}

	@Override
	public String getLinkCssClass() {
		return null;
	}

	@Override
	public String getMessage(PortletRequest portletRequest) {
		return null;
	}

	@Override
	public String getMethod() {
		return null;
	}

	@Override
	public String getOnClick(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return null;
	}

	public ResourceBundle getResourceBundle(Locale locale) {
		return PortalUtil.getResourceBundle(locale);
	}

	@Override
	public String getSrc() {
		return null;
	}

	@Override
	public String getSrcHover() {
		return null;
	}

	@Override
	public String getTarget() {
		return null;
	}

	@Override
	public String getURL(
		PortletRequest portletRequest, PortletResponse portletResponse) {

		return null;
	}

	@Override
	public double getWeight() {
		return 0;
	}

	/**
	 * @throws IOException
	 */
	@Override
	public boolean include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		return false;
	}

	@Override
	public boolean isLabel() {
		return false;
	}

	@Override
	public boolean isToolTip() {
		return false;
	}

	@Override
	public boolean isUseDialog() {
		return false;
	}

	protected Locale getLocale(PortletRequest portletRequest) {
		ThemeDisplay themeDisplay = (ThemeDisplay)portletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		return themeDisplay.getLocale();
	}

}