<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
SocialBookmark socialBookmark = (SocialBookmark)request.getAttribute("liferay-social-bookmarks:bookmark:socialBookmark");
String title = GetterUtil.getString((String)request.getAttribute("liferay-social-bookmarks:bookmark:title"));
String url = GetterUtil.getString((String)request.getAttribute("liferay-social-bookmarks:bookmark:url"));

String icon = "../aui/google-plus-sign";
%>

<liferay-ui:icon
	image="<%= icon %>"
	label="<%= false %>"
	linkCssClass="btn btn-borderless btn-monospaced btn-outline-borderless btn-outline-secondary btn-sm"
	message="<%= socialBookmark.getName(locale) %>"
	method="get"
	src="<%= icon %>"
	url="<%= socialBookmark.getPostURL(title, url) %>"
/>