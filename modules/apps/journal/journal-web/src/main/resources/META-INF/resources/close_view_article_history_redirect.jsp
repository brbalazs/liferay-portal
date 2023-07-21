<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String referringPortletResource = ParamUtil.getString(request, "referringPortletResource");

Portlet selPortlet = PortletLocalServiceUtil.getPortletById(company.getCompanyId(), referringPortletResource);
%>

<aui:script>
	Liferay.fire('closeWindow', {
		id: '_<%= HtmlUtil.escapeJS(selPortlet.getPortletId()) %>_editAsset',
		portletAjaxable: <%= selPortlet.isAjaxable() %>,
		refresh: '<%= HtmlUtil.escapeJS(selPortlet.getPortletId()) %>'
	});
</aui:script>