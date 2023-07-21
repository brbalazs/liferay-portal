/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.wiki.web.internal.portlet.action;

import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.struts.FindStrutsAction;
import com.liferay.wiki.constants.WikiPortletKeys;
import com.liferay.wiki.model.WikiNode;
import com.liferay.wiki.model.WikiPageResource;
import com.liferay.wiki.service.WikiNodeLocalService;
import com.liferay.wiki.service.WikiPageResourceLocalService;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Samuel Kong
 */
@Component(property = "path=/wiki/find_page", service = StrutsAction.class)
public class FindPageAction extends FindStrutsAction {

	@Override
	protected long getGroupId(long primaryKey) throws Exception {
		WikiPageResource pageResource =
			_wikiPageResourceLocalService.getPageResource(primaryKey);

		WikiNode node = _wikiNodeLocalService.getNode(pageResource.getNodeId());

		return node.getGroupId();
	}

	@Override
	protected String getPrimaryKeyParameterName() {
		return "pageResourcePrimKey";
	}

	@Override
	protected String getStrutsAction(
		HttpServletRequest request, String portletId) {

		if (portletId.equals(WikiPortletKeys.WIKI) ||
			portletId.equals(WikiPortletKeys.WIKI_ADMIN)) {

			return "/wiki/view";
		}

		return "/wiki_display/view";
	}

	@Override
	protected String[] initPortletIds() {

		// Order is important. See LPS-23770.

		return new String[] {
			WikiPortletKeys.WIKI_ADMIN, WikiPortletKeys.WIKI,
			WikiPortletKeys.WIKI_DISPLAY
		};
	}

	@Override
	protected PortletURL processPortletURL(
			HttpServletRequest request, PortletURL portletURL)
		throws Exception {

		long pageResourcePrimKey = ParamUtil.getLong(
			request, getPrimaryKeyParameterName());

		WikiPageResource pageResource =
			_wikiPageResourceLocalService.getPageResource(pageResourcePrimKey);

		WikiNode node = _wikiNodeLocalService.getNode(pageResource.getNodeId());

		portletURL.setParameter("nodeName", node.getName());

		portletURL.setParameter("title", pageResource.getTitle());

		return portletURL;
	}

	@Reference(unbind = "-")
	protected void setWikiNodeLocalService(
		WikiNodeLocalService wikiNodeLocalService) {

		_wikiNodeLocalService = wikiNodeLocalService;
	}

	@Reference(unbind = "-")
	protected void setWikiPageResourceLocalService(
		WikiPageResourceLocalService wikiPageResourceLocalService) {

		_wikiPageResourceLocalService = wikiPageResourceLocalService;
	}

	private WikiNodeLocalService _wikiNodeLocalService;
	private WikiPageResourceLocalService _wikiPageResourceLocalService;

}