<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Organization organization = (Organization)row.getObject();
%>

<c:if test="<%= GroupPermissionUtil.contains(permissionChecker, siteMembershipsDisplayContext.getGroupId(), ActionKeys.ASSIGN_MEMBERS) %>">
	<portlet:actionURL name="deleteGroupOrganizations" var="deleteGroupOrganizationsURL">
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="groupId" value="<%= String.valueOf(siteMembershipsDisplayContext.getGroupId()) %>" />
		<portlet:param name="removeOrganizationId" value="<%= String.valueOf(organization.getOrganizationId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		icon="times-circle"
		linkCssClass="table-action-link"
		message="remove-membership"
		showIcon="<%= true %>"
		url="<%= deleteGroupOrganizationsURL %>"
	/>
</c:if>