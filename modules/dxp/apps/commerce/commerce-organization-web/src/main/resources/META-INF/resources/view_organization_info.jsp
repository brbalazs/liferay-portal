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
CommerceOrganizationDisplayContext commerceOrganizationDisplayContext = (CommerceOrganizationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

long organizationId = commerceOrganizationDisplayContext.getOrganizationId();
%>

<div class="container-fluid-1280">
	<commerce-ui:table
		dataProviderKey="<%= CommerceOrganizationClayTable.NAME %>"
		filter="<%= commerceOrganizationDisplayContext.getOrganizationFilter() %>"
		itemPerPage="<%= 5 %>"
		namespace="<%= renderResponse.getNamespace() %>"
		pageNumber="1"
		portletURL="<%= commerceOrganizationDisplayContext.getPortletURL() %>"
		tableName="<%= CommerceOrganizationClayTable.NAME %>"
	/>
</div>

<c:if test="<%= OrganizationPermissionUtil.contains(permissionChecker, organizationId, ActionKeys.UPDATE) %>">
	<div class="commerce-cta is-visible">
		<aui:button cssClass="commerce-button commerce-button--big js-invite-user" onClick='<%= renderResponse.getNamespace() + "openAddOrganizationsModal();" %>' value="add-organizations" />
	</div>

	<commerce-ui:add-organizations-modal
		componentId="addOrganizationsModal"
	/>

	<portlet:actionURL name="editCommerceOrganization" var="editCommerceOrganizationActionURL" />

	<aui:form action="<%= editCommerceOrganizationActionURL %>" method="post" name="commerceOrganizationFm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD_MULTIPLE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="organizationId" type="hidden" value="<%= organizationId %>" />
		<aui:input name="addOrganizationIds" type="hidden" />
	</aui:form>

	<aui:script>
		Liferay.provide(
			window,
			'<portlet:namespace />openAddOrganizationsModal',
			function(evt) {
				const addOrganizationsModal = Liferay.component('addOrganizationsModal');

				addOrganizationsModal.open();
			}
		);

		Liferay.provide(
			window,
			'deleteCommerceOrganization',
			function(id) {
				document.querySelector('#<portlet:namespace /><%= Constants.CMD %>').value = '<%= Constants.REMOVE %>';
				document.querySelector('#<portlet:namespace />organizationId').value = id;

				submitForm(document.<portlet:namespace />commerceOrganizationFm);
			}
		);

		Liferay.componentReady('addOrganizationsModal').then(
			function(addOrganizationsModal) {
				addOrganizationsModal.on(
					'addOrganization',
					function(event) {
						let orgIds = event.map(
							function(org) {
								return org.id
							}
						).join(',');

						document.querySelector('#<portlet:namespace />addOrganizationIds').value = orgIds;

						addOrganizationsModal.close();

						submitForm(document.<portlet:namespace />commerceOrganizationFm);
					}
				);
			}
		);

	</aui:script>
</c:if>