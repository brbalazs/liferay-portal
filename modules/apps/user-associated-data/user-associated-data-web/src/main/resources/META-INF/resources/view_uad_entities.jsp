<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
ViewUADEntitiesDisplay viewUADEntitiesDisplay = (ViewUADEntitiesDisplay)request.getAttribute(UADWebKeys.VIEW_UAD_ENTITIES_DISPLAY);

SearchContainer<UADEntity> uadEntitySearchContainer = viewUADEntitiesDisplay.getSearchContainer();

ViewUADEntitiesManagementToolbarDisplayContext viewUADEntitiesManagementToolbarDisplayContext = new ViewUADEntitiesManagementToolbarDisplayContext(liferayPortletRequest, liferayPortletResponse, request, uadEntitySearchContainer);

portletDisplay.setShowBackIcon(true);

PortletURL backURL = renderResponse.createRenderURL();

backURL.setParameter("mvcRenderCommandName", "/view_uad_applications_summary");
backURL.setParameter("p_u_i_d", String.valueOf(selectedUser.getUserId()));

portletDisplay.setURLBack(backURL.toString());

renderResponse.setTitle(StringBundler.concat(selectedUser.getFullName(), " - ", LanguageUtil.get(request, "personal-data-erasure"), " - ", viewUADEntitiesDisplay.getTypeName()));
%>

<clay:navigation-bar
	navigationItems="<%= viewUADEntitiesDisplay.getNavigationItems() %>"
/>

<clay:management-toolbar
	displayContext="<%= viewUADEntitiesManagementToolbarDisplayContext %>"
/>

<aui:form method="post" name="viewUADEntitiesFm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="p_u_i_d" type="hidden" value="<%= String.valueOf(selectedUser.getUserId()) %>" />
	<aui:input name="uadRegistryKey" type="hidden" value="<%= viewUADEntitiesDisplay.getUADRegistryKey() %>" />
	<aui:input name="primaryKeys" type="hidden" />

	<div class="closed container-fluid container-fluid-max-xl sidenav-container sidenav-right" id="<portlet:namespace />infoPanelId">
		<liferay-portlet:resourceURL copyCurrentRenderParameters="<%= true %>" id="/info_panel" var="entityTypeSidebarURL" />

		<liferay-frontend:sidebar-panel
			resourceURL="<%= entityTypeSidebarURL %>"
			searchContainerId="UADEntities"
		>
			<liferay-util:include page="/info_panel.jsp" servletContext="<%= application %>" />
		</liferay-frontend:sidebar-panel>

		<div class="sidenav-content">
			<liferay-ui:search-container
				searchContainer="<%= uadEntitySearchContainer %>"
			>
				<liferay-ui:search-container-row
					className="com.liferay.user.associated.data.web.internal.display.UADEntity"
					escapedModel="<%= true %>"
					keyProperty="primaryKey"
					modelVar="uadEntity"
				>

					<%
					List<KeyValuePair> columnEntries = uadEntity.getColumnEntries();

					for (KeyValuePair columnEntry : columnEntries) {
					%>

						<liferay-ui:search-container-column-text
							cssClass='<%= columnEntry.equals(columnEntries.get(0)) ? "table-cell-expand table-list-title" : "table-cell-expand" %>'
							href="<%= uadEntity.getEditURL() %>"
							name="<%= columnEntry.getKey() %>"
							value="<%= StringUtil.shorten(columnEntry.getValue(), 200) %>"
						/>

					<%
					}
					%>

					<liferay-ui:search-container-column-jsp
						cssClass="entry-action-column"
						path="/uad_entity_action.jsp"
					/>
				</liferay-ui:search-container-row>

				<liferay-ui:search-iterator
					markupView="lexicon"
				/>
			</liferay-ui:search-container>
		</div>
	</div>
</aui:form>

<aui:script>
	function <portlet:namespace />doAnonymizeMultiple() {
		<portlet:namespace />doMultiple(
			'<portlet:actionURL name="/anonymize_uad_entities" />',
			'<liferay-ui:message key="are-you-sure-you-want-to-anonymize-the-selected-items" />'
		);
	}

	function <portlet:namespace />doDeleteMultiple() {
		<portlet:namespace />doMultiple(
			'<portlet:actionURL name="/delete_uad_entities" />',
			'<liferay-ui:message key="are-you-sure-you-want-to-delete-the-selected-items" />'
		);
	}

	function <portlet:namespace />doMultiple(actionURL, message) {
		var form = document.getElementById('<portlet:namespace />viewUADEntitiesFm');

		if (form) {
			var primaryKeys = form.querySelector('#<portlet:namespace />primaryKeys');

			if (primaryKeys) {
				primaryKeys.setAttribute(
					'value',
					Liferay.Util.listCheckedExcept(form, '<portlet:namespace />allRowIds')
				);
			}
		}

		<portlet:namespace />confirmAction('viewUADEntitiesFm', actionURL, message);
	}
</aui:script>

<%@ include file="/action/confirm_action_js.jspf" %>