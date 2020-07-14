<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
FaroAdminDisplayContext faroAdminDisplayContext = new FaroAdminDisplayContext(renderRequest, renderResponse, request);
%>

<clay:management-toolbar
	displayContext="<%= new FaroAdminManagementToolbarDisplayContext(renderResponse, liferayPortletRequest, liferayPortletResponse, request, faroAdminDisplayContext.getSearchContainer()) %>"
/>

<div class="closed container-fluid-1280 sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
	<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= false %>" id="/faro_admin/info_panel" var="sidebarPanelURL" />

	<liferay-frontend:sidebar-panel
		resourceURL="<%= sidebarPanelURL %>"
		searchContainerId="faro_admin"
	>
		<liferay-util:include page="" servletContext="<%= application %>" />
	</liferay-frontend:sidebar-panel>

	<div class="sidenav-content">
		<aui:form name="fm">
			<liferay-ui:search-container
				id="faro_admin"
				searchContainer="<%= faroAdminDisplayContext.getSearchContainer() %>"
			>
				<liferay-ui:search-container-row
					className="com.liferay.osb.faro.admin.web.internal.model.FaroProjectAdminDisplay"
					escapedModel="<%= true %>"
					keyProperty="faroProjectId"
					modelVar="faroProjectAdminDisplay"
					rowIdProperty="faroProjectId"
				>
					<liferay-ui:search-container-column-text
						orderable="<%= true %>"
						property="name"
					/>

					<liferay-ui:search-container-column-text
						property="owner"
					/>

					<liferay-ui:search-container-column-date
						orderable="<%= true %>"
						property="createDate"
					/>

					<liferay-ui:search-container-column-text
						property="subscriptionName"
					/>

					<liferay-ui:search-container-column-text
						property="individualsUsage"
					/>

					<liferay-ui:search-container-column-text
						property="pageViewsUsage"
					/>

					<liferay-ui:search-container-column-text
						property="version"
					/>

					<liferay-ui:search-container-column-text
						property="offline"
					/>

					<c:if test="<%= permissionChecker.isOmniadmin() %>">
						<liferay-ui:search-container-column-text>
							<clay:dropdown-actions
								dropdownItems="<%= faroAdminDisplayContext.getActionDropdownItems(faroProjectAdminDisplay) %>"
							/>
						</liferay-ui:search-container-column-text>
					</c:if>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
					searchResultCssClass="show-quick-actions-on-hover table table-autofit"
				/>
			</liferay-ui:search-container>
		</aui:form>
	</div>
</div>