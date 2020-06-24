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

<commerce-ui:modal-content
	title='<%= LanguageUtil.get(request, "create-new-product") %>'
>
	<aui:form cssClass="container-fluid-1280" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "apiSubmit();" %>'>
		<aui:input name="locale" type="hidden" value="<%= locale %>" />

		<aui:input autoFocus="<%= true %>" name="name" required="<%= true %>" type="text" />

		<label class="control-label" for="catalogId"><%= LanguageUtil.get(request, "catalog") %></label>

		<div id="autocomplete-root"></div>
	</aui:form>

	<portlet:renderURL var="editProductDefinitionURL">
		<portlet:param name="mvcRenderCommandName" value="editProductDefinition" />
	</portlet:renderURL>

	<aui:script require="commerce-frontend-js/components/autocomplete/entry as autocomplete, commerce-frontend-js/utilities/eventsDefinitions as events, commerce-frontend-js/utilities/modals/index as ModalUtils, commerce-frontend-js/ServiceProvider/index as ServiceProvider">
		Liferay.provide(
			window,
			'<portlet:namespace/>apiSubmit',
			function() {
				const AdminCatalogResource = ServiceProvider.default.AdminCatalogAPI(
					'v1'
				);

				const productData = {
					active: true,
					catalogId: document.getElementById('<portlet:namespace />catalogId')
						.value,
					name: {
						<%= locale %>: document.getElementById(
							'<portlet:namespace />name'
						).value
					},
					productType:
						'<%= ParamUtil.getString(request, "productTypeName") %>'
				};

				ModalUtils.isSubmitting();

				AdminCatalogResource.createProduct(productData)
					.then(function(cpDefinition) {
						const redirectURL = new Liferay.PortletURL.createURL(
							'<%= editProductDefinitionURL %>'
						);

						redirectURL.setParameter(
							'p_p_state',
							'<%= LiferayWindowState.MAXIMIZED.toString() %>'
						);

						redirectURL.setParameter('cpDefinitionId', cpDefinition.id);

						ModalUtils.closeAndRedirect(redirectURL);
					})
					.catch(ModalUtils.onSubmitFail);
			},
			['liferay-portlet-url']
		);

		autocomplete.default('autocomplete', 'autocomplete-root', {
			apiUrl: '/o/headless-commerce-admin-catalog/v1.0/catalogs',
			inputId: '<portlet:namespace />catalogId',
			inputName: '<%= renderResponse.getNamespace() %>catalogId',
			itemsKey: 'id',
			itemsLabel: 'name'
		});

		Liferay.on(events.AUTOCOMPLETE_VALUE_UPDATED, function(e) {
			if (e.value) {
				AdminCatalogResource.getCatalogById(e.value).then(function(catalog) {
					document.getElementById('<portlet:namespace />locale').value =
						catalog.defaultLanguageId;
				});
			}
		});
	</aui:script>
</commerce-ui:modal-content>