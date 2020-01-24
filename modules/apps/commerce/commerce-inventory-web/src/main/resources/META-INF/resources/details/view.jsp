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

<%-- TODO - Inventory Item Display Context - There doesn't seem to exist a specific one for the Inventory Section --%>

<%

String headerTitle = null; // TODO Set up logic to display the headerTitle

if (commerceOrder != null) {
	headerTitle = LanguageUtil.format(request, "order-x", commerceOrder.getCommerceOrderId());
}
else {
	headerTitle = LanguageUtil.get(request, "add-order");
}

portletDisplay.setShowBackIcon(true);

if (Validator.isNull(redirect)) {
	portletDisplay.setURLBack(String.valueOf(renderResponse.createRenderURL()));
}
else {
	portletDisplay.setURLBack(redirect);
}
%>

<commerce-ui:header
	actions="<%-- TODO implement displayContext = .getHeaderActionModels() --%>"
	assignerModalUrl="/assigner/modal/url"
	bean="<%-- TODO implement = commerceInventory --%>"
	dropdownItems="<%-- TODO implement displayContext .getDropdownItems() --%>"
	externalReferenceCode="123asd"
	externalReferenceCodeEditUrl="/external/reference/code/edit/url"
	model="<%-- CommerceInventory class model --%>"
	thumbnailUrl="<%-- TODO implement displayContext = .getCommerceAccountThumbnailURL() --%>"
	title="<%= headerTitle %>"
/>

<div id="<portlet:namespace />editInventoryItemContainer">
	<liferay-frontend:screen-navigation
		fullContainerCssClass="col-12 pt-4"
		key="<%-- TODO implement CommerceInventoryScreenNavigationConstants =  --%>"
		modelBean="<%= commerceOrder %>"
		portletURL="<%= currentURLObj %>"
	/>
</div>