<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
SiteTeamsDisplayContext siteTeamsDisplayContext = new SiteTeamsDisplayContext(renderRequest, renderResponse, request);
%>

<clay:management-toolbar
	actionDropdownItems="<%= siteTeamsDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= siteTeamsDisplayContext.getClearResultsURL() %>"
	componentId="teamsManagementToolbar"
	creationMenu="<%= siteTeamsDisplayContext.isShowAddButton() ? siteTeamsDisplayContext.getCreationMenu() : null %>"
	disabled="<%= siteTeamsDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= siteTeamsDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= siteTeamsDisplayContext.getTotalItems() %>"
	searchActionURL="<%= siteTeamsDisplayContext.getSearchActionURL() %>"
	searchContainerId="teams"
	searchFormName="searchFm"
	showSearch="<%= siteTeamsDisplayContext.isSearchEnabled() %>"
	sortingOrder="<%= siteTeamsDisplayContext.getOrderByType() %>"
	sortingURL="<%= siteTeamsDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= siteTeamsDisplayContext.getViewTypeItems() %>"
/>

<portlet:actionURL name="deleteTeams" var="deleteTeamsURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= deleteTeamsURL %>" cssClass="container-fluid-1280" name="fm">
	<liferay-ui:search-container
		searchContainer="<%= siteTeamsDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Team"
			cssClass="selectable"
			escapedModel="<%= true %>"
			keyProperty="teamId"
			modelVar="team"
		>

			<%
			PortletURL rowURL = null;

			if (TeamPermissionUtil.contains(permissionChecker, team, ActionKeys.ASSIGN_MEMBERS)) {
				rowURL = renderResponse.createRenderURL();

				rowURL.setParameter("mvcPath", "/edit_team_assignments.jsp");
				rowURL.setParameter("teamId", String.valueOf(team.getTeamId()));
			}
			%>

			<c:choose>
				<c:when test="<%= siteTeamsDisplayContext.isDescriptiveView() %>">
					<liferay-ui:search-container-column-icon
						icon="users"
						toggleRowChecker="<%= true %>"
					/>

					<liferay-ui:search-container-column-text
						colspan="<%= 2 %>"
					>
						<h5>
							<aui:a href="<%= (rowURL != null) ? rowURL.toString() : null %>"><%= team.getName() %></aui:a>
						</h5>

						<h6 class="text-default">
							<span><%= team.getDescription() %></span>
						</h6>
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-jsp
						path="/team_action.jsp"
					/>
				</c:when>
				<c:when test="<%= siteTeamsDisplayContext.isIconView() %>">

					<%
					row.setCssClass("entry-card lfr-asset-item");
					%>

					<liferay-ui:search-container-column-text>
						<liferay-frontend:icon-vertical-card
							actionJsp="/team_action.jsp"
							actionJspServletContext="<%= application %>"
							cssClass="entry-display-style"
							icon="users"
							resultRow="<%= row %>"
							rowChecker="<%= searchContainer.getRowChecker() %>"
							subtitle="<%= team.getDescription() %>"
							title="<%= team.getName() %>"
							url="<%= (rowURL != null) ? rowURL.toString() : null %>"
						/>
					</liferay-ui:search-container-column-text>
				</c:when>
				<c:when test="<%= siteTeamsDisplayContext.isListView() %>">
					<liferay-ui:search-container-column-text
						cssClass="table-cell-expand-small table-cell-minw-200 table-title"
						href="<%= rowURL %>"
						name="name"
						property="name"
					/>

					<liferay-ui:search-container-column-text
						cssClass="table-cell-expand table-cell-minw-300"
						href="<%= rowURL %>"
						name="description"
						property="description"
					/>

					<liferay-ui:search-container-column-jsp
						path="/team_action.jsp"
					/>
				</c:when>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= siteTeamsDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script sandbox="<%= true %>">
	var deleteSelectedTeams = function() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			submitForm($(document.<portlet:namespace />fm));
		}
	}

	var ACTIONS = {
		'deleteSelectedTeams': deleteSelectedTeams
	};

	Liferay.componentReady('teamsManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				'actionItemClicked',
				function(event) {
					var itemData = event.data.item.data;

					if (itemData && itemData.action && ACTIONS[itemData.action]) {
						ACTIONS[itemData.action]();
					}
				}
			);
		}
	);
</aui:script>