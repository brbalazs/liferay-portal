/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.settings.web.internal.servlet.taglib.ui;

import com.liferay.map.constants.MapProviderWebKeys;
import com.liferay.map.util.MapProviderHelperUtil;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorConstants;
import com.liferay.portal.kernel.servlet.taglib.ui.FormNavigatorEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Pei-Jung Lan
 * @author Philip Jones
 */
@Component(
	immediate = true, property = "form.navigator.entry.order:Integer=20",
	service = FormNavigatorEntry.class
)
public class CompanySettingsMapsFormNavigatorEntry
	extends BaseCompanySettingsFormNavigatorEntry {

	@Override
	public String getCategoryKey() {
		return FormNavigatorConstants.
			CATEGORY_KEY_COMPANY_SETTINGS_MISCELLANEOUS;
	}

	@Override
	public String getKey() {
		return "maps";
	}

	@Override
	public void include(
			HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		ThemeDisplay themeDisplay = (ThemeDisplay)request.getAttribute(
			WebKeys.THEME_DISPLAY);

		request.setAttribute(
			MapProviderWebKeys.MAP_PROVIDER_KEY,
			MapProviderHelperUtil.getMapProviderKey(
				themeDisplay.getCompanyId()));

		super.include(request, response);
	}

	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.portal.settings.web)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}

	@Override
	protected String getJspPath() {
		return "/maps.jsp";
	}

}