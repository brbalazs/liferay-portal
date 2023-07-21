/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.search.web.portlet.shared.search;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.web.search.request.SearchResponse;

import java.util.Optional;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;

/**
 * @author André de Oliveira
 */
@ProviderType
public interface PortletSharedSearchResponse extends SearchResponse {

	public Optional<String> getParameter(
		String name, RenderRequest renderRequest);

	public Optional<String[]> getParameterValues(
		String name, RenderRequest renderRequest);

	public Optional<PortletPreferences> getPortletPreferences(
		RenderRequest renderRequest);

	public ThemeDisplay getThemeDisplay(RenderRequest renderRequest);

}