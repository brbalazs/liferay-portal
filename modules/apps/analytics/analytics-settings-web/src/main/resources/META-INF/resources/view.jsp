<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "analytics-cloud-connection");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("tabs1", tabs1);
%>

<aui:nav-bar cssClass="collapse-basic-search" markupView="lexicon">
	<aui:nav cssClass="navbar-nav">

		<%
		PortletURL analyticsCloudConnectionURL = PortletURLUtil.clone(portletURL, renderResponse);

		analyticsCloudConnectionURL.setParameter("tabs1", "analytics-cloud-connection");
		%>

		<aui:nav-item href="<%= analyticsCloudConnectionURL.toString() %>" label="analytics-cloud-connection" selected='<%= tabs1.equals("analytics-cloud-connection") %>' />

		<%
		PortletURL syncedSitesURL = PortletURLUtil.clone(portletURL, renderResponse);

		syncedSitesURL.setParameter("tabs1", "synced-sites");
		%>

		<aui:nav-item href="<%= syncedSitesURL.toString() %>" label="synced-sites" selected='<%= tabs1.equals("synced-sites") %>' />

		<%
		PortletURL syncedContactDataURL = PortletURLUtil.clone(portletURL, renderResponse);

		syncedContactDataURL.setParameter("tabs1", "synced-contact-data");
		%>

		<aui:nav-item href="<%= syncedContactDataURL.toString() %>" label="synced-contact-data" selected='<%= tabs1.equals("synced-contact-data") %>' />
	</aui:nav>
</aui:nav-bar>

<c:choose>
	<c:when test='<%= tabs1.equals("analytics-cloud-connection") %>'>
		<liferay-util:include page="/edit_workspace_connection.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:when test='<%= tabs1.equals("synced-contact-data") %>'>
		<liferay-util:include page="/edit_synced_contacts_data.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/edit_synced_sites.jsp" servletContext="<%= application %>" />
	</c:otherwise>
</c:choose>