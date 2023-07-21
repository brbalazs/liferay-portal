<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
EditSiteTeamAssignmentsDisplayContext editSiteTeamAssignmentsDisplayContext = new EditSiteTeamAssignmentsDisplayContext(renderRequest, renderResponse, request);

String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	PortletURL portletURL = renderResponse.createRenderURL();

	redirect = portletURL.toString();
}

portletDisplay.setShowBackIcon(true);
portletDisplay.setURLBack(redirect);

renderResponse.setTitle(editSiteTeamAssignmentsDisplayContext.getTeamName());
%>

<c:choose>
	<c:when test='<%= Objects.equals(editSiteTeamAssignmentsDisplayContext.getTabs1(), "users") %>'>
		<liferay-util:include page="/edit_team_assignments_users.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:when test='<%= Objects.equals(editSiteTeamAssignmentsDisplayContext.getTabs1(), "user-groups") %>'>
		<liferay-util:include page="/edit_team_assignments_user_groups.jsp" servletContext="<%= application %>" />
	</c:when>
</c:choose>

<%
Team team = editSiteTeamAssignmentsDisplayContext.getTeam();

Group group = GroupLocalServiceUtil.getGroup(team.getGroupId());

if (group != null) {
	group = StagingUtil.getLiveGroup(group.getGroupId());
}

Organization organization = null;

if (group.isOrganization()) {
	organization = OrganizationLocalServiceUtil.getOrganization(group.getOrganizationId());
}

if (group.isOrganization()) {
	UsersAdminUtil.addPortletBreadcrumbEntries(organization, request, renderResponse);
}
else {
	PortalUtil.addPortletBreadcrumbEntry(request, group.getDescriptiveName(locale), null);
}

PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "manage-teams"), redirect);
PortalUtil.addPortletBreadcrumbEntry(request, editSiteTeamAssignmentsDisplayContext.getTeamName(), null);
PortalUtil.addPortletBreadcrumbEntry(request, LanguageUtil.get(request, "assign-members"), currentURL);
%>