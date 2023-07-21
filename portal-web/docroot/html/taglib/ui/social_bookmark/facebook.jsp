<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/social_bookmark/init.jsp" %>

<%
String facebookDisplayStyle = "button_count";

if (displayStyle.equals("simple")) {
	facebookDisplayStyle = "standard";
}
else if (displayStyle.equals("vertical")) {
	facebookDisplayStyle = "box_count";
}
%>

<liferay-util:html-bottom
	outputKey="taglib_ui_social_bookmark_facebook"
>
	<script src="<%= HttpUtil.getProtocol(request) %>://connect.facebook.net/<%= locale.getLanguage() %>_<%= locale.getCountry() %>/all.js#xfbml=1" type="text/javascript"></script>
</liferay-util:html-bottom>

<div id="fb-root"></div>

<div class="fb-like" data-font="" data-height="<%= (facebookDisplayStyle.equals("standard") || facebookDisplayStyle.equals("button_count")) ? 20 : StringPool.BLANK %>" data-href="<%= url %>" data-layout="<%= facebookDisplayStyle %>" data-send="false" data-show_faces="true">
</div>