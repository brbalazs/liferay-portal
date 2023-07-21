<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
UserRolesDisplayContext userRolesDisplayContext = new UserRolesDisplayContext(request, renderRequest, renderResponse);
%>

<clay:management-toolbar
	clearResultsURL="<%= userRolesDisplayContext.getClearResultsURL() %>"
	componentId="userGroupRoleRoleManagementToolbar"
	disabled="<%= userRolesDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= userRolesDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= userRolesDisplayContext.getTotalItems() %>"
	searchActionURL="<%= userRolesDisplayContext.getSearchActionURL() %>"
	searchContainerId="userGroupRoleRole"
	searchFormName="searchFm"
	showSearch="<%= userRolesDisplayContext.isShowSearch() %>"
	sortingOrder="<%= userRolesDisplayContext.getOrderByType() %>"
	sortingURL="<%= userRolesDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= userRolesDisplayContext.getViewTypeItems() %>"
/>

<aui:form cssClass="container-fluid-1280 portlet-site-memberships-assign-site-roles" name="fm">
	<liferay-ui:membership-policy-error />

	<liferay-ui:search-container
		id="userGroupRoleRole"
		searchContainer="<%= userRolesDisplayContext.getRoleSearchSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Role"
			keyProperty="roleId"
			modelVar="role"
		>

			<%
			String displayStyle = userRolesDisplayContext.getDisplayStyle();
			%>

			<%@ include file="/role_columns.jspf" %>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= userRolesDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script use="liferay-search-container">
	var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />userGroupRoleRole');

	searchContainer.on(
		'rowToggled',
		function(event) {
			Liferay.Util.getOpener().Liferay.fire(
				'<%= HtmlUtil.escapeJS(userRolesDisplayContext.getEventName()) %>',
				{
					data: event.elements.allSelectedElements.getDOMNodes()
				}
			);
		}
	);
</aui:script>