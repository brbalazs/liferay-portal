<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/entries/init.jsp" %>

<%
PortletURL editLayoutURL = PortletURLFactoryUtil.create(request, LayoutAdminPortletKeys.GROUP_PAGES, GroupControlPanelLayoutUtil.getGroupControlPanelLayout(layout.getGroup()), PortletRequest.RENDER_PHASE);

editLayoutURL.setParameter("mvcPath", "/edit_content_layout.jsp");
editLayoutURL.setParameter("redirect", PortalUtil.getCurrentURL(request));
editLayoutURL.setParameter("groupId", String.valueOf(layout.getGroupId()));
editLayoutURL.setParameter("selPlid", String.valueOf(layout.getPlid()));

((LiferayPortletURL)editLayoutURL).setRefererPlid(layout.getPlid());
%>

<li class="control-menu-nav-item">
	<a aria-label="<%= LanguageUtil.get(request, "edit") %>" class="control-menu-icon lfr-portal-tooltip product-menu-toggle sidenav-toggler" data-title="<%= LanguageUtil.get(request, "edit") %>" href="<%= editLayoutURL.toString() %>">
		<aui:icon cssClass="icon-monospaced" image="pencil" markupView="lexicon" />
	</a>
</li>