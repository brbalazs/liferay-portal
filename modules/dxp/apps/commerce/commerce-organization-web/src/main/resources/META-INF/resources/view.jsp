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

request.setAttribute("view.jsp-filterPerOrganization", false);
%>

<div class="commerce-organization-container" id="<portlet:namespace />entriesContainer">
	<commerce-ui:table
		dataProviderKey="organizations"
		filter="<%= commerceOrganizationDisplayContext.getOrganizationFilter() %>"
		itemPerPage="<%= 5 %>"
		namespace="<%= renderResponse.getNamespace() %>"
		pageNumber="1"
		portletURL="<%= commerceOrganizationDisplayContext.getPortletURL() %>"
		tableName="organizations"
	/>
</div>

<c:if test="<%= commerceOrganizationDisplayContext.hasAddOrganizationPermissions() %>">
	<div class="commerce-cta is-visible">
		<aui:button cssClass="commerce-button commerce-button--big js-invite-user" onClick='<%= renderResponse.getNamespace() + "openAddOrganizationModal();" %>' value="add-organization" />
	</div>

	<portlet:actionURL name="editCommerceOrganization" var="editCommerceOrganizationActionURL" />

	<aui:form action="<%= editCommerceOrganizationActionURL %>" method="post" name="organizationFm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
		<aui:input name="name" type="hidden" />
	</aui:form>

	<commerce-ui:add-organizations-modal
		componentId="addOrganizationModal"
	/>

	<aui:script>
		Liferay.provide(
			window,
			'<portlet:namespace />openAddOrganizationModal',
			function(evt) {
				const addOrganizationModal = Liferay.component('addOrganizationModal');

				addOrganizationModal.open();
			}
		);

		Liferay.componentReady('addOrganizationModal').then(
			function(addOrganizationModal) {
				addOrganizationModal.on(
					'AddOrganizationModalSave',
					function(event) {
						document.querySelector('#<portlet:namespace />name').value = event.organizationName;

						addOrganizationModal.close();

						submitForm(document.<portlet:namespace />organizationFm);
					}
				);
			}
		);
	</aui:script>
</c:if>