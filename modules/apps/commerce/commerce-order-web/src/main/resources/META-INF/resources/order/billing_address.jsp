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
CommerceOrderEditDisplayContext commerceOrderEditDisplayContext = (CommerceOrderEditDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceOrder commerceOrder = commerceOrderEditDisplayContext.getCommerceOrder();
%>

<portlet:actionURL name="editCommerceOrder" var="editCommerceOrderBillingAddressActionURL" />

<commerce-ui:modal-content
	title='<%= LanguageUtil.get(request, "edit-billing-address") %>'
>
	<aui:form action="<%= editCommerceOrderBillingAddressActionURL %>" cssClass="container-fluid-1280 p-0" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="billingAddress" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceOrderId" type="hidden" value="<%= commerceOrder.getCommerceOrderId() %>" />

		<%
		Map<String, String> contextParams = new HashMap<>();

		contextParams.put("commerceOrderId", String.valueOf(commerceOrder.getCommerceOrderId()));
		%>

		<commerce-ui:dataset-display
			contextParams="<%= contextParams %>"
			dataProviderKey="<%= CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_BILLING_ADDRESSES %>"
			formId="fm"
			id="<%= CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_BILLING_ADDRESSES %>"
			itemsPerPage="<%= 10 %>"
			namespace="<%= renderResponse.getNamespace() %>"
			pageNumber="<%= 1 %>"
			portletURL="<%= currentURLObj %>"
			selectedItemsKey="addressId"
			selectionType="single"
		/>
	</aui:form>

	<liferay-portlet:renderURL var="addBillingAddressURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcRenderCommandName" value="addCommerceOrderBillingAddress" />
		<portlet:param name="commerceOrderId" value="<%= String.valueOf(commerceOrderEditDisplayContext.getCommerceOrderId()) %>" />
	</liferay-portlet:renderURL>

	<clay:link
		href="<%= addBillingAddressURL %>"
		label="add-new-address"
	/>
</commerce-ui:modal-content>