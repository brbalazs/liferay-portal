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

package com.liferay.commerce.dashboard.web.internal.display.context;

import com.liferay.commerce.dashboard.web.internal.configuration.CommerceDashboardForecastPortletInstanceConfiguration;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.theme.PortletDisplay;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Riccardo Ferrari
 */
public class CommerceDashboardForecastDisplayContext {

	public CommerceDashboardForecastDisplayContext(
			HttpServletRequest httpServletRequest)
		throws PortalException {

		_httpServletrequest = httpServletRequest;

		_themeDisplay = (ThemeDisplay)httpServletRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		PortletDisplay portletDisplay = _themeDisplay.getPortletDisplay();

		_commerceDashboardForecastPortletInstanceConfiguration =
			portletDisplay.getPortletInstanceConfiguration(
				CommerceDashboardForecastPortletInstanceConfiguration.class);
	}

	public String getAssetCategoryIds() throws PortalException {
		return _commerceDashboardForecastPortletInstanceConfiguration.
			assetCategoryIds();
	}

	private final CommerceDashboardForecastPortletInstanceConfiguration
		_commerceDashboardForecastPortletInstanceConfiguration;
	private final HttpServletRequest _httpServletrequest;
	private final ThemeDisplay _themeDisplay;

}