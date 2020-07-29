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
CommercePriceEntryDisplayContext commercePriceEntryDisplayContext = (CommercePriceEntryDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommercePriceList commercePriceList = commercePriceEntryDisplayContext.getCommercePriceList();
long commercePriceListId = commercePriceEntryDisplayContext.getCommercePriceListId();
%>

<c:if test="<%= commercePriceEntryDisplayContext.hasPermission(commercePriceListId, ActionKeys.UPDATE) %>">
	<div class="row">
		<div class="col-12">
			<div id="item-finder-root"></div>

			<aui:script require="commerce-frontend-js/components/item_finder/entry as itemFinder, commerce-frontend-js/utilities/slugify as slugify, commerce-frontend-js/utilities/eventsDefinitions as events, commerce-frontend-js/ServiceProvider/index as ServiceProvider">
				const CommercePriceEntriesResource = ServiceProvider.default.AdminPricingAPI(
					'v2'
				);

				var id = <%= commercePriceListId %>;
				var priceListExternalReferenceCode =
					'<%= commercePriceList.getExternalReferenceCode() %>';

				function selectItem(sku) {
					var priceEntryData = {
						price: '0.0',
						priceListExternalReferenceCode: priceListExternalReferenceCode,
						priceListId: id,
						skuExternalReferenceCode: sku.externalReferenceCode,
						skuId: sku.id
					};

					return CommercePriceEntriesResource.addPriceEntry(id, priceEntryData)
						.then(function() {
							Liferay.fire(events.UPDATE_DATASET_DISPLAY, {
								id:
									'<%= CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_ENTRIES %>'
							});
						})
						.catch(function(error) {
							return Promise.reject(error);
						});
				}

				function getSelectedItems() {
					return Promise.resolve([]);
				}

				itemFinder.default('itemFinder', 'item-finder-root', {
					apiUrl: '/o/headless-commerce-admin-catalog/v1.0/skus',
					getSelectedItems: getSelectedItems,
					inputPlaceholder: '<%= LanguageUtil.get(request, "find-a-product") %>',
					itemSelectedMessage: '<%= LanguageUtil.get(request, "product-selected") %>',
					linkedDatasetsId: [
						'<%= CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_ENTRIES %>'
					],
					itemCreation: false,
					itemsKey: 'id',
					onItemSelected: selectItem,
					pageSize: 10,
					panelHeaderLabel: '<%= LanguageUtil.get(request, "add-products") %>',
					portletId: '<%= portletDisplay.getRootPortletId() %>',
					schema: [
						{
							fieldName: ['productName', 'LANG']
						}
					],
					spritemap: '<%= themeDisplay.getPathThemeImages() %>/lexicon/icons.svg',
					titleLabel: '<%= LanguageUtil.get(request, "add-existing-product") %>'
				});
			</aui:script>
		</div>

		<div class="col-12">

			<%
			Map<String, String> contextParams = new HashMap<>();

			contextParams.put("commercePriceListId", String.valueOf(commercePriceListId));
			%>

			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_ENTRIES %>"
				formId="fm"
				id="<%= CommercePricingDataSetConstants.COMMERCE_DATA_SET_KEY_PRICE_ENTRIES %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= currentURLObj %>"
			/>
		</div>
	</div>
</c:if>