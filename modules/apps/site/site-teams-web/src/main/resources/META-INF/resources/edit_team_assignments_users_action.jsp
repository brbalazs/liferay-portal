<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
long teamId = ParamUtil.getLong(request, "teamId");

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

User user2 = (User)row.getObject();
%>

<portlet:actionURL name="deleteTeamUsers" var="deleteTeamUsersURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
	<portlet:param name="teamId" value="<%= String.valueOf(teamId) %>" />
	<portlet:param name="removeUserId" value="<%= String.valueOf(user2.getUserId()) %>" />
</portlet:actionURL>

<liferay-ui:icon-delete
	icon="times-circle"
	linkCssClass="icon-monospaced text-default"
	message="delete"
	showIcon="<%= true %>"
	url="<%= deleteTeamUsersURL %>"
/>