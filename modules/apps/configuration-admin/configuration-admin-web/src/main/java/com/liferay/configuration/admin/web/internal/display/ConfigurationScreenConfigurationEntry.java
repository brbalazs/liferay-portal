/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.configuration.admin.web.internal.display;

import com.liferay.configuration.admin.display.ConfigurationScreen;

import java.util.Locale;

import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Jorge Ferrer
 */
public class ConfigurationScreenConfigurationEntry
	implements ConfigurationEntry {

	public ConfigurationScreenConfigurationEntry(
		ConfigurationScreen configurationScreen, Locale locale) {

		_configurationScreen = configurationScreen;
		_locale = locale;
	}

	@Override
	public String getCategory() {
		return _configurationScreen.getCategoryKey();
	}

	@Override
	public String getEditURL(
		RenderRequest renderRequest, RenderResponse renderResponse) {

		PortletURL portletURL = renderResponse.createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "/view_configuration_screen");
		portletURL.setParameter(
			"configurationScreenKey", _configurationScreen.getKey());

		return portletURL.toString();
	}

	@Override
	public String getKey() {
		return _configurationScreen.getKey();
	}

	@Override
	public String getName() {
		return _configurationScreen.getName(_locale);
	}

	@Override
	public String getScope() {
		return _configurationScreen.getScope();
	}

	private final ConfigurationScreen _configurationScreen;
	private final Locale _locale;

}