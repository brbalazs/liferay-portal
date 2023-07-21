<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/card/init.jsp" %>

<%
String colorCssClass = (String)request.getAttribute("liferay-frontend:card:colorCssClass");
String footer = (String)request.getAttribute("liferay-frontend:card:footer");
String header = (String)request.getAttribute("liferay-frontend:card:header");
String onClick = (String)request.getAttribute("liferay-frontend:card:onClick");
String portraitURL = (String)request.getAttribute("liferay-frontend:card:portraitURL");
String subtitle = (String)request.getAttribute("liferay-frontend:card:subtitle");
String title = (String)request.getAttribute("liferay-frontend:card:title");
String userInitials = (String)request.getAttribute("liferay-frontend:card:userInitials");
%>