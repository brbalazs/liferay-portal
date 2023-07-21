<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String portletBackURL = (String)request.getAttribute(ProductNavigationControlMenuWebKeys.PORTLET_BACK_URL);
%>

<li class="control-menu-nav-item">
	<a class="back-url-link control-menu-icon" href="<%= HtmlUtil.escapeHREF(portletBackURL) %>">
		<aui:icon cssClass="icon-monospaced" image="angle-left" markupView="lexicon" />
	</a>
</li>