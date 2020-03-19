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
%>

<portlet:actionURL name="editCommerceInventoryWarehouseItem" var="editCommerceInventoryWarehouseItemActionURL" />

<commerce-ui:side-panel-content
	title='<%= LanguageUtil.get(request, "edit-inventory") %>'
>
	<commerce-ui:panel
		title='<%= LanguageUtil.get(request, "details") %>'
	>
		<aui:form action="<%= editCommerceInventoryWarehouseItemActionURL %>" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
			<aui:input name="commerceInventoryWarehouseItemId" type="hidden" value="<%= commerceInventoryDisplayContext.getCommerceInventoryWarehouseItemId() %>" />

			<aui:model-context bean="<%= commerceInventoryDisplayContext.getCommerceInventoryWarehouseItem() %>" model="<%= CommerceInventoryWarehouseItem.class %>" />

			<aui:input label="quantity-on-hand" name="quantity">
				<aui:validator name="min">1</aui:validator>
			</aui:input>

			<aui:input label="safety-stock-quantity" name="reservedQuantity">
				<aui:validator name="min">0</aui:validator>
			</aui:input>

			<aui:button-row>
				<aui:button cssClass="btn-lg" type="submit" value="save" />

				<aui:button cssClass="btn-lg" type="cancel" />
			</aui:button-row>
		</aui:form>
	</commerce-ui:panel>
</commerce-ui:side-panel-content>