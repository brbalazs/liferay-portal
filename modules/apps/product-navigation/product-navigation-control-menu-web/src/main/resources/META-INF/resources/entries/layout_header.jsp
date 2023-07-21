<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
LayoutTypeController layoutTypeController = LayoutTypeControllerTracker.getLayoutTypeController(layout.getType());

ResourceBundle layoutTypeResourceBundle = ResourceBundleUtil.getBundle("content.Language", locale, layoutTypeController.getClass());
%>

<li class="align-items-center control-menu-nav-item control-menu-nav-item-content">
	<span class="control-menu-level-1-heading truncate-text" data-qa-id="headerTitle"><%= HtmlUtil.escape(layout.getName(locale)) %></span>&nbsp;<span class="text-muted truncate-text">(<%= LanguageUtil.get(request, layoutTypeResourceBundle, "layout.types." + layout.getType()) %>)</span>
</li>