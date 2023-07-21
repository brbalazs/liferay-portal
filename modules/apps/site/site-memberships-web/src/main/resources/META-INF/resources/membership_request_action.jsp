<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

MembershipRequest membershipRequest = (MembershipRequest)row.getObject();
%>

<c:if test="<%= (membershipRequest.getStatusId() == MembershipRequestConstants.STATUS_PENDING) && GroupPermissionUtil.contains(permissionChecker, themeDisplay.getScopeGroup(), ActionKeys.ASSIGN_MEMBERS) %>">
	<portlet:renderURL var="replyRequestURL">
		<portlet:param name="mvcPath" value="/reply_membership_request.jsp" />
		<portlet:param name="p_u_i_d" value="<%= String.valueOf(membershipRequest.getUserId()) %>" />
		<portlet:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
		<portlet:param name="membershipRequestId" value="<%= String.valueOf(membershipRequest.getMembershipRequestId()) %>" />
	</portlet:renderURL>

	<liferay-ui:icon
		icon="reply"
		linkCssClass="icon-monospaced text-default"
		markupView="lexicon"
		message="reply"
		url="<%= replyRequestURL %>"
	/>
</c:if>