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
CommercePricingClassDisplayContext commercePricingClassDisplayContext = (CommercePricingClassDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommercePricingClass commercePricingClass = commercePricingClassDisplayContext.getCommercePricingClass();

long commercePricingClassId = commercePricingClass.getCommercePricingClassId();
boolean isViewOnly = !commercePricingClassDisplayContext.hasPermission();
%>

<liferay-portlet:renderURL var="editCommercePricingClassExternalReferenceCodeURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="editCommercePricingClassExternalReferenceCode" />
	<portlet:param name="commercePricingClassId" value="<%= String.valueOf(commercePricingClass.getCommercePricingClassId()) %>" />
</liferay-portlet:renderURL>

<commerce-ui:header
	actions="<%= commercePricingClassDisplayContext.getHeaderActionModels() %>"
	bean="<%= commercePricingClass %>"
	beanIdLabel="id"
	dropdownItems="<%= commercePricingClassDisplayContext.getDropdownItems() %>"
	externalReferenceCode="<%= commercePricingClass.getExternalReferenceCode() %>"
	externalReferenceCodeEditUrl="<%= editCommercePricingClassExternalReferenceCodeURL %>"
	model="<%= CommercePricingClass.class %>"
	title="<%= commercePricingClass.getTitle(locale) %>"
/>

<portlet:actionURL name="editCommercePricingClass" var="editCommercePricingClassActionURL" />

<div class="container mt-3">
	<aui:form action="<%= editCommercePricingClassActionURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commercePricingClass == null) ? Constants.ADD : Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= backURL %>" />
		<aui:input name="commercePricingClassId" type="hidden" value="<%= (commercePricingClass == null) ? 0 : commercePricingClass.getCommercePricingClassId() %>" />

		<div class="row">
			<div class="col-12">
				<commerce-ui:panel
					elementClasses="flex-fill"
					title='<%= LanguageUtil.get(request, "details") %>'
				>
					<div class="col-12 lfr-form-content">
						<aui:input bean="<%= commercePricingClass %>" disabled="<%= isViewOnly %>" model="<%= CommercePricingClass.class %>" name="title" required="<%= true %>" />

						<aui:input localized="<%= true %>" name="description" type="textarea" value="<%= commercePricingClass.getDescription(locale) %>" />
					</div>
				</commerce-ui:panel>
			</div>
		</div>

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
							'/o/headless-commerce-admin-catalog/v1.0/productGroups/' +
								id +
								'/productGroupProducts/',
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
						).then(function() {
							Liferay.fire(events.UPDATE_DATASET_DISPLAY, {
								id:
									'<%= CommercePricingClassDataSetConstants.COMMERCE_DATA_SET_KEY_PRICING_CLASS_PRODUCT_DEFINITIONS %>'
							});
							return null;
						});
					}

					function addNewItem(name) {
						var nameDefinition = {
							[themeDisplay.getLanguageId()]: name
						};

						if (themeDisplay.getLanguageId() !== themeDisplay.getDefaultLanguageId()) {
							nameDefinition[themeDisplay.getDefaultLanguageId()] = name;
						}

						return fetch('/o/headless-commerce-admin-catalog/v1.0/specifications', {
							body: JSON.stringify({
								key: slugify.default(encodeURIComponent(name)),
								title: nameDefinition
							}),
							credentials: 'include',
							headers: headers,
							method: 'POST'
						})
							.then(function(response) {
								if (response.ok) {
									return response.json();
								}

								return response.json().then(function(data) {
									return Promise.reject(data.message);
								});
							})
							.then(selectItem);
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
						itemsKey: 'id',
						onItemCreated: addNewItem,
						onItemSelected: selectItem,
						pageSize: 10,
						panelHeaderLabel: '<%= LanguageUtil.get(request, "add-products") %>',
						portletId: '<%= portletDisplay.getRootPortletId() %>',
						schema: {
							itemTitle: ['name', 'LANG']
						},
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
	</aui:form>
</div>