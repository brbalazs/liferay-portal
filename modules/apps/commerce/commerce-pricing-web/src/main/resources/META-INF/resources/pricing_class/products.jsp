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
CommercePricingClassCPDefinitionDisplayContext commercePricingClassCPDefinitionDisplayContext = (CommercePricingClassCPDefinitionDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommercePricingClass commercePricingClass = commercePricingClassCPDefinitionDisplayContext.getCommercePricingClass();

long commercePricingClassId = commercePricingClass.getCommercePricingClassId();

	boolean hasPermission = commercePricingClassCPDefinitionDisplayContext.hasPermission();
%>

<c:if test="<%= hasPermission %>">
	<div class="container mt-3">
		<div class="row">
			<div class="col-12">
				<div id="item-finder-root"></div>

				<aui:script require="commerce-frontend-js/components/item_finder/entry as itemFinder, commerce-frontend-js/utilities/slugify as slugify, commerce-frontend-js/utilities/eventsDefinitions as events">
					var headers = new Headers({
						Accept: 'application/json',
						'Content-Type': 'application/json',
						'x-csrf-token': Liferay.authToken
					});

					var id = <%= commercePricingClass.getCommercePricingClassId() %>;
					var pricingClassExternalReferenceCode =
						'<%= commercePricingClass.getExternalReferenceCode() %>';

					function selectItem(product) {
						return fetch(
							'/o/headless-commerce-admin-catalog/v1.0/product-groups/' +
								id +
								'/product-group-products/',
							{
								body: JSON.stringify({
									productExternalReferenceCode: product.externalReferenceCode,
									productId: product.id,
									productGroupExternalReferenceCode: pricingClassExternalReferenceCode,
									productGroupId: id
								}),
								credentials: 'include',
								headers: headers,
								method: 'POST'
							}
						).then(function(response) {
							debugger;
							if (!response.ok) {
								return response.json().then(function(data) {
									return Promise.reject(data.errorDescription);
								});
							}

							Liferay.fire(events.UPDATE_DATASET_DISPLAY, {
								id:
									'<%= CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS %>'
							});
							return null;
						});
					}

					function getSelectedItems() {
						return Promise.resolve([]);
					}

					itemFinder.default('itemFinder', 'item-finder-root', {
						apiUrl: '/o/headless-commerce-admin-catalog/v1.0/products',
						getSelectedItems: getSelectedItems,
						inputPlaceholder: '<%= LanguageUtil.get(request, "find-a-product") %>',
						itemSelectedMessage: '<%= LanguageUtil.get(request, "product-selected") %>',
						linkedDatasetsId: [
							'<%= CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS %>'
						],
						itemCreation: false,
						itemsKey: 'id',
						onItemSelected: selectItem,
						pageSize: 10,
						panelHeaderLabel: '<%= LanguageUtil.get(request, "add-products") %>',
						portletId: '<%= portletDisplay.getRootPortletId() %>',
						schema: [
							{
								fieldName: ['name', 'LANG']
							},
							{
								fieldName: 'productId'
							}
						],
						spritemap: '<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg',
						titleLabel: '<%= LanguageUtil.get(request, "add-existing-product") %>'
					});
				</aui:script>
			</div>

			<div class="col-12">
				<commerce-ui:panel
					bodyClasses="p-0"
					title='<%= LanguageUtil.get(request, "products") %>'
				>

					<%
					Map<String, String> contextParams = new HashMap<>();

					contextParams.put("commercePricingClassId", String.valueOf(commercePricingClassId));
					%>

					<commerce-ui:dataset-display
						contextParams="<%= contextParams %>"
						dataProviderKey="<%= CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS %>"
						formId="fm"
						id="<%= CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS %>"
						itemsPerPage="<%= 10 %>"
						namespace="<%= renderResponse.getNamespace() %>"
						pageNumber="<%= 1 %>"
						portletURL="<%= currentURLObj %>"
						showManagementBar="<%= false %>"
					/>
				</commerce-ui:panel>
			</div>
		</div>
	</div>
</c:if>