<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
SelectSiteRolesDisplayContext selectSiteRolesDisplayContext = new SelectSiteRolesDisplayContext(request, renderRequest, renderResponse);
%>

<clay:management-toolbar
	clearResultsURL="<%= selectSiteRolesDisplayContext.getClearResultsURL() %>"
	componentId="siteRolesManagementToolbar"
	disabled="<%= selectSiteRolesDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= selectSiteRolesDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= selectSiteRolesDisplayContext.getTotalItems() %>"
	searchActionURL="<%= selectSiteRolesDisplayContext.getSearchActionURL() %>"
	searchContainerId="siteRoles"
	searchFormName="searchFm"
	selectable="<%= false %>"
	showSearch="<%= selectSiteRolesDisplayContext.isShowSearch() %>"
	sortingOrder="<%= selectSiteRolesDisplayContext.getOrderByType() %>"
	sortingURL="<%= selectSiteRolesDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= selectSiteRolesDisplayContext.getViewTypeItems() %>"
/>

<aui:form cssClass="container-fluid-1280 portlet-site-memberships-assign-site-roles" name="fm">
	<liferay-ui:search-container
		id="siteRoles"
		searchContainer="<%= selectSiteRolesDisplayContext.getRoleSearchSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.portal.kernel.model.Role"
			escapedModel="<%= true %>"
			keyProperty="roleId"
			modelVar="role"
		>

			<%
			Map<String, Object> data = new HashMap<String, Object>();

			data.put("id", role.getRoleId());
			%>

			<c:choose>
				<c:when test='<%= Objects.equals(selectSiteRolesDisplayContext.getDisplayStyle(), "icon") %>'>

					<%
					row.setCssClass("entry-card lfr-asset-item");
					%>

					<liferay-ui:search-container-column-text>
						<liferay-frontend:icon-vertical-card
							cssClass="selector-button"
							data="<%= data %>"
							icon="users"
							resultRow="<%= row %>"
							subtitle="<%= LanguageUtil.get(request, role.getTypeLabel()) %>"
							title="<%= HtmlUtil.escape(role.getTitle(locale)) %>"
						/>
					</liferay-ui:search-container-column-text>
				</c:when>
				<c:when test='<%= Objects.equals(selectSiteRolesDisplayContext.getDisplayStyle(), "descriptive") %>'>
					<liferay-ui:search-container-column-text
						colspan="<%= 2 %>"
					>
						<h5>
							<aui:a cssClass="selector-button" data="<%= data %>" href="javascript:;">
								<%= HtmlUtil.escape(role.getTitle(locale)) %>
							</aui:a>
						</h5>

						<h6 class="text-default">
							<span><%= HtmlUtil.escape(role.getDescription(locale)) %></span>
						</h6>

						<h6 class="text-default">
							<%= LanguageUtil.get(request, role.getTypeLabel()) %>
						</h6>
					</liferay-ui:search-container-column-text>
				</c:when>
				<c:when test='<%= Objects.equals(selectSiteRolesDisplayContext.getDisplayStyle(), "list") %>'>
					<liferay-ui:search-container-column-text
						cssClass="table-cell-content"
						name="title"
						truncate="<%= true %>"
					>
						<aui:a cssClass="selector-button" data="<%= data %>" href="javascript:;">
							<%= HtmlUtil.escape(role.getTitle(locale)) %>
						</aui:a>
					</liferay-ui:search-container-column-text>

					<liferay-ui:search-container-column-text
						cssClass="table-cell-content"
						name="description"
						value="<%= HtmlUtil.escape(role.getDescription(locale)) %>"
					/>

					<liferay-ui:search-container-column-text
						name="type"
						value="<%= LanguageUtil.get(request, role.getTypeLabel()) %>"
					/>
				</c:when>
			</c:choose>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="<%= selectSiteRolesDisplayContext.getDisplayStyle() %>"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.selectEntityHandler('#<portlet:namespace />fm', '<%= HtmlUtil.escapeJS(selectSiteRolesDisplayContext.getEventName()) %>');
</aui:script>