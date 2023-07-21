<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String portletNamespace = PortalUtil.getPortletNamespace(ProductNavigationControlMenuPortletKeys.PRODUCT_NAVIGATION_CONTROL_MENU);

PortletURL addPanelURL = PortletURLFactoryUtil.create(request, ProductNavigationControlMenuPortletKeys.PRODUCT_NAVIGATION_CONTROL_MENU, PortletRequest.RENDER_PHASE);

addPanelURL.setParameter("mvcPath", "/add_panel.jsp");
addPanelURL.setParameter("stateMaximized", String.valueOf(themeDisplay.isStateMaximized()));
addPanelURL.setWindowState(LiferayWindowState.EXCLUSIVE);
%>

<li class="control-menu-nav-item">
	<a aria-label="<%= LanguageUtil.get(request, "add") %>" class="control-menu-icon lfr-portal-tooltip product-menu-toggle sidenav-toggler" data-content="body" data-open-class="open-admin-panel" data-qa-id="add" data-target="#<%= portletNamespace %>addPanelId" data-title="<%= LanguageUtil.get(request, "add") %>" data-toggle="sidenav" data-type="fixed-push" data-type-mobile="fixed" data-url="<%= addPanelURL.toString() %>" href="javascript:;" id="<%= portletNamespace %>addToggleId">
		<aui:icon cssClass="icon-monospaced" image="plus" markupView="lexicon" />
	</a>
</li>