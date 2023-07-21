<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/social_bookmark/init.jsp" %>

<%
String twitterDisplayStyle = "none";

if (displayStyle.equals("horizontal") || displayStyle.equals("vertical")) {
	twitterDisplayStyle = displayStyle;
}
%>

<a class="twitter-share-button" data-count="<%= twitterDisplayStyle %>" data-lang="<%= locale.getDisplayLanguage() %>" data-text="<%= HtmlUtil.escapeAttribute(title) %>" data-url="<%= url %>" href="http://twitter.com/share"><liferay-ui:message key="tweet" /></a>

<liferay-util:html-bottom
	outputKey="twitter"
>
	<script src="<%= HttpUtil.getProtocol(request) %>://platform.twitter.com/widgets.js" type="text/javascript"></script>
</liferay-util:html-bottom>