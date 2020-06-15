<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
CPDefinitionsDisplayContext cpDefinitionsDisplayContext = (CPDefinitionsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CPDefinition cpDefinition = cpDefinitionsDisplayContext.getCPDefinition();

List<CommerceCatalog> commerceCatalogs = cpDefinitionsDisplayContext.getCommerceCatalogs();
%>

<commerce-ui:modal-content>
	<aui:form cssClass="container-fluid-1280 p-0" method="post" name="duplicatefm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "apiSubmit(this.form);" %>' useNamespace="<%= false %>">
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

		<aui:input name="name" type="text" value='<%= LanguageUtil.format(locale, "copy-of-x", cpDefinition.getName(languageId)) %>' />

		<aui:select label="catalog" name="catalogId" required="<%= true %>">
			<c:if test="<%= !commerceCatalogs.isEmpty() %>">

				<%
				for (CommerceCatalog commerceCatalog : commerceCatalogs) {
				%>

					<aui:option data-languageId="<%= commerceCatalog.getCatalogDefaultLanguageId() %>" label="<%= commerceCatalog.getName() %>" selected="<%= (cpDefinition == null) ? (commerceCatalogs.size() == 1) : cpDefinitionsDisplayContext.isSelectedCatalog(commerceCatalog) %>" value="<%= commerceCatalog.getCommerceCatalogId() %>" />

				<%
				}
				%>

			</c:if>
		</aui:select>
	</aui:form>

	<portlet:renderURL var="editProductDefinitionURL">
		<portlet:param name="mvcRenderCommandName" value="editProductDefinition" />
	</portlet:renderURL>

	<aui:script require="commerce-frontend-js/utilities/eventsDefinitions as events, commerce-frontend-js/utilities/forms/index as FormUtils">
		Liferay.provide(
			window,
			'<portlet:namespace/>apiSubmit',
			function(form) {
				var name = document.getElementById('name').value;

				var API_URL =
					'/o/headless-commerce-admin-catalog/v1.0/products/<%= cpDefinition.getCProductId() %>/clone?catalogId=' +
					document.getElementById('catalogId').value;

				FormUtils.apiSubmit(form, API_URL)
					.then(function(payload) {
						var headers = new Headers({
							Accept: 'application/json',
							'Content-Type': 'application/json',
							'x-csrf-token': Liferay.authToken
						});

						fetch(
							'/o/headless-commerce-admin-catalog/v1.0/products/' +
								payload.productId,
							{
								body: JSON.stringify({
									active: payload.active,
									catalogId: payload.catalogId,
									name: {
										<%= locale %>: name
									},
									productType: payload.productType
								}),
								credentials: 'include',
								headers: headers,
								method: 'patch'
							}
						).then(function() {
							var redirectURL = new Liferay.PortletURL.createURL(
								'<%= editProductDefinitionURL %>'
							);

							redirectURL.setParameter('cpDefinitionId', payload.id);
							redirectURL.setParameter(
								'p_p_state',
								'<%= LiferayWindowState.MAXIMIZED.toString() %>'
							);

							window.parent.Liferay.fire(events.CLOSE_MODAL, {
								redirectURL: redirectURL.toString(),
								successNotification: {
									showSuccessNotification: true,
									message:
										'<liferay-ui:message key="your-request-completed-successfully" />'
								}
							});
						});
					})
					.catch(function() {
						window.parent.Liferay.fire(events.IS_LOADING_MODAL, {
							isLoading: false
						});

						new Liferay.Notification({
							closeable: true,
							delay: {
								hide: 5000,
								show: 0
							},
							duration: 500,
							message:
								'<liferay-ui:message key="an-unexpected-error-occurred" />',
							render: true,
							title: '<liferay-ui:message key="danger" />',
							type: 'danger'
						});
					});
			},
			['liferay-portlet-url']
		);
	</aui:script>
</commerce-ui:modal-content>