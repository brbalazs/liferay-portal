/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.staging.processes.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.NavigationItemList;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import java.util.List;

import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Péter Alius
 */
public class StagingProcessesWebDisplayContext {

	public StagingProcessesWebDisplayContext(
		RenderResponse renderResponse, HttpServletRequest request) {

		_renderResponse = renderResponse;
		_request = request;
	}

	public List<NavigationItem> getNavigationItems() {
		return new NavigationItemList() {
			{
				add(
					navigationItem -> {
						String activeTab = ParamUtil.getString(
							_request, "tabs1", "processes");

						navigationItem.setActive(activeTab.equals("processes"));

						navigationItem.setHref(
							_renderResponse.createRenderURL(), "tabs1",
							"processes");
						navigationItem.setLabel(
							LanguageUtil.get(_request, "processes"));
					});

				add(
					navigationItem -> {
						String activeTab = ParamUtil.getString(
							_request, "tabs1", "processes");

						navigationItem.setActive(activeTab.equals("scheduled"));

						navigationItem.setHref(
							_renderResponse.createRenderURL(), "tabs1",
							"scheduled");
						navigationItem.setLabel(
							LanguageUtil.get(_request, "scheduled"));
					});
			}
		};
	}

	private final RenderResponse _renderResponse;
	private final HttpServletRequest _request;

}