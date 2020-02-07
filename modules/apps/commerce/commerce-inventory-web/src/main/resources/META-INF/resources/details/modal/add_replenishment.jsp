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

String skuCode = commerceInventoryDisplayContext.getSkuCode();
%>

<portlet:actionURL name="addInventoryReplenishment" var="addInventoryReplenishmentActionURL" />

<div class="col-12 lfr-form-content">
	<aui:form action="<%= addInventoryReplenishmentActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ADD %>" />
		<aui:input name="sku" type="hidden" value="<%= skuCode %>" />

		<aui:fieldset>
			<aui:select label="warehouse" name="commerceInventoryWarehouseId" required="<%= true %>">

				<%
				List<CommerceInventoryWarehouse> commerceInventoryWarehouses = commerceInventoryDisplayContext.getCommerceInventoryWarehouses();

				for (CommerceInventoryWarehouse commerceInventoryWarehouse : commerceInventoryWarehouses) {
				%>

					<aui:option label="<%= commerceInventoryWarehouse.getName() %>" value="<%= commerceInventoryWarehouse.getCommerceInventoryWarehouseId() %>" />

				<%
				}
				%>

			</aui:select>

			<aui:input label="quantity" name="quantity" required="<%= true %>" type="text" />

			<%
			java.util.Calendar startCalendar = Calendar.getInstance();
			%>

			<liferay-ui:input-date
				dayParam="dateDay"
				dayValue="<%= startCalendar.get(Calendar.DATE) %>"
				disabled="<%= false %>"
				firstDayOfWeek="<%= startCalendar.getFirstDayOfWeek() - 1 %>"
				monthParam="dateMonth"
				monthValue="<%= startCalendar.get(Calendar.MONTH) %>"
				name="date"
				required="<%= true %>"
				yearParam="dateYear"
				yearValue="<%= startCalendar.get(Calendar.YEAR) %>"
			/>
		</aui:fieldset>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" />
		</aui:button-row>
	</aui:form>
</div>