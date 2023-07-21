<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/init.jsp" %>

<%
String displayStyle = GetterUtil.getString((String)request.getAttribute("liferay-ui:social-bookmark:displayStyle"));
String icon = (String)request.getAttribute("liferay-ui:social-bookmark:icon");
String type = (String)request.getAttribute("liferay-ui:social-bookmark:type");
String url = GetterUtil.getString((String)request.getAttribute("liferay-ui:social-bookmark:url"));
String title = GetterUtil.getString((String)request.getAttribute("liferay-ui:social-bookmark:title"));
String target = GetterUtil.getString((String)request.getAttribute("liferay-ui:social-bookmark:target"));
String postUrl = (String)request.getAttribute("liferay-ui:social-bookmark:postUrl");

if (Validator.isNull(displayStyle)) {
	String[] displayStyles = PropsUtil.getArray(PropsKeys.SOCIAL_BOOKMARK_DISPLAY_STYLES);

	displayStyle = displayStyles[0];
}

String messageKey = "social-bookmark-" + type;
%>