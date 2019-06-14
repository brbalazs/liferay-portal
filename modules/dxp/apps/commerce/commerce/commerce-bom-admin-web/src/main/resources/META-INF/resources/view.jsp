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
CommerceBOMAdminDisplayContext commerceBOMAdminDisplayContext = (CommerceBOMAdminDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<liferay-site-navigation:breadcrumb
	breadcrumbEntries="<%= commerceBOMAdminDisplayContext.getPortletBreadcrumbEntries(commerceBOMAdminDisplayContext.getCommerceBOMFolder()) %>"
/>

<portlet:actionURL name="editCommerceBOMFolder" var="editCommerceBOMFolderActionURL" />

<div class="container-fluid-1280" id="<portlet:namespace />commerceBOMFolderContainer">
	<aui:form action="<%= editCommerceBOMFolderActionURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="deleteCommerceBOMFolderIds" type="hidden" />

		<liferay-ui:search-container
			id="commerceBOMFolders"
			searchContainer="<%= commerceBOMAdminDisplayContext.getSearchContainer() %>"
		>
			<liferay-ui:search-container-row
				className="com.liferay.commerce.bom.model.CommerceBOMFolder"
				cssClass="entry-display-style"
				keyProperty="commerceBOMFolderId"
				modelVar="commerceBOMFolder"
			>

				<%
				PortletURL rowURL = commerceBOMAdminDisplayContext.getPortletURL();

				rowURL.setParameter("commerceBOMFolderId", String.valueOf(commerceBOMFolder.getCommerceBOMFolderId()));
				%>

				<liferay-ui:search-container-column-text
					cssClass="important table-cell-content"
					href="<%= rowURL %>"
					property="name"
				/>

				<liferay-ui:search-container-column-jsp
					cssClass="entry-action-column"
					path="bom_folder_action.jsp"
				/>
			</liferay-ui:search-container-row>

			<liferay-ui:search-iterator
				displayStyle="list"
				markupView="lexicon"
			/>
		</liferay-ui:search-container>
	</aui:form>
</div>