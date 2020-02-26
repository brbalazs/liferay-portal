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
	CommerceInventoryDisplayContext commerceInventoryDisplayContext = (CommerceInventoryDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	String skuCode = commerceInventoryDisplayContext.getSku();

	java.util.Map<String, String> contextParams = new java.util.HashMap<>();

	contextParams.put("sku", skuCode);
%>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			title='<%= LanguageUtil.get(request, "inventory-warehouse") %>'
		>
			<commerce-ui:dataset-display
				clayCreationMenu="<%= commerceInventoryDisplayContext.getWarehousesClayCreationMenu() %>"
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_WAREHOUSES %>"
				id="<%= CommerceInventoryDataSetConstants.COMMERCE_DATA_SET_KEY_INVENTORY_WAREHOUSES %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= commerceInventoryDisplayContext.getPortletURL() %>"
				style="stacked"
			/>
		</commerce-ui:panel>
	</div>
</div>

<%--
<div id="<portlet:namespace />side-panel-root"></div>
<div id="<portlet:namespace />side-panel-wrapper"></div>

<aui:script require="commerce-frontend-js/components/side_panel/entry.es as sidePanel">
	sidePanel.default(
		'<portlet:namespace />sidePanel',
		'<portlet:namespace />side-panel-root',
		{
			portalWrapperId: '<portlet:namespace />side-panel-wrapper',
			spritemap:
				'<%= themeDisplay.getPathThemeImages() + "/clay/icons.svg" %>',
			topAnchorSelector: '.commerce-header'
		}
	);
</aui:script>--%>