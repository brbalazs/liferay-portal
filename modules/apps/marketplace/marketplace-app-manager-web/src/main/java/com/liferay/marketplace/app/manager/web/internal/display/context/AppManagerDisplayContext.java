/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.marketplace.app.manager.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Pei-Jung Lan
 */
public class AppManagerDisplayContext {

	public AppManagerDisplayContext(
		HttpServletRequest request, RenderResponse renderResponse) {

		_request = request;
		_renderResponse = renderResponse;
	}

	public List<NavigationItem> getModuleNavigationItems() {
		String pluginType = ParamUtil.getString(
			_request, "pluginType", "components");

		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(
							pluginType.equals("components"));
						navigationItem.setHref(_getViewModuleURL("components"));
						navigationItem.setLabel(
							LanguageUtil.get(_request, "components"));
					});

				add(
					navigationItem -> {
						navigationItem.setActive(pluginType.equals("portlets"));
						navigationItem.setHref(_getViewModuleURL("portlets"));
						navigationItem.setLabel(
							LanguageUtil.get(_request, "portlets"));
					});
			}
		};
	}

	public List<NavigationItem> getNavigationItems(String url, String label) {
		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						navigationItem.setActive(true);
						navigationItem.setHref(url);
						navigationItem.setLabel(
							LanguageUtil.get(_request, label));
					});
			}
		};
	}

	private String _getViewModuleURL(String pluginType) {
		String app = ParamUtil.getString(_request, "app");
		String moduleGroup = ParamUtil.getString(_request, "moduleGroup");
		String symbolicName = ParamUtil.getString(_request, "symbolicName");
		String version = ParamUtil.getString(_request, "version");

		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter("mvcPath", "/view_module.jsp");
		portletURL.setParameter("app", app);
		portletURL.setParameter("moduleGroup", moduleGroup);
		portletURL.setParameter("symbolicName", symbolicName);
		portletURL.setParameter("version", version);
		portletURL.setParameter("pluginType", pluginType);

		return portletURL.toString();
	}

	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;

}