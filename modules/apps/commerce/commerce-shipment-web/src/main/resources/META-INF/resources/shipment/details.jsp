<%@ page import="com.liferay.portal.kernel.util.Validator" %><%--
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
CommerceContext commerceContext = (CommerceContext)request.getAttribute(CommerceWebKeys.COMMERCE_CONTEXT);

CommerceShipmentItemDisplayContext commerceShipmentItemDisplayContext = (CommerceShipmentItemDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceShipment commerceShipment = commerceShipmentItemDisplayContext.getCommerceShipment();
long commerceShipmentId = commerceShipmentItemDisplayContext.getCommerceShipmentId();

long commerceCountryId = 0;
long commerceRegionId = 0;

CommerceAddress commerceAddress = commerceShipment.fetchCommerceAddress();

if (commerceAddress != null) {
	commerceCountryId = commerceAddress.getCommerceCountryId();
	commerceRegionId = commerceAddress.getCommerceRegionId();
}


String headerTitle = commerceShipmentId.toString();

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

<aui:alert closeable="<%= false %>" cssClass="mt-0" type="warning">
	<liferay-ui:message key="<%-- TODO define label key at page refresh --%>" />
</aui:alert>

<commerce-ui:header
	actions="<%-- TODO implement = commerceShipmentItemDisplayContext.getHeaderActionModels() --%>"
	assignerModalUrl="/assigner/modal/url"
	bean="<%= commerceShipment %>"
	dropdownItems="<%= commerceOrderEditDisplayContext.getDropdownItems() %>"
	externalReferenceCode="123asd"
	externalReferenceCodeEditUrl="/external/reference/code/edit/url"
	model="<%= CommerceShipment.class %>"
	thumbnailUrl="<%= commerce.getCommerceAccountThumbnailURL() %>"
	title="<%= headerTitle %>"
/>

<div id="<portlet:namespace />editShipmentContainer">
	<liferay-frontend:screen-navigation
		fullContainerCssClass="col-12 pt-4"
		key="<%-- TODO implement = CommerceShipmentScreenNavigationConstants.SUMMARY --%>"
		modelBean="<%= commerceShipment %>"
		portletURL="<%= currentURLObj %>"
	/>
</div>
