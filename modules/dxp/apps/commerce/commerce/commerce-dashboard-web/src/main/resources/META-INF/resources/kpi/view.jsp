<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommerceDashboardKPIDisplayContext commerceDashboardKPIDisplayContext = (CommerceDashboardKPIDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<aui:row>
	<aui:col width="<%= 30 %>">
		<div class="kpi-value">
			<%= commerceDashboardKPIDisplayContext.getOrdersCount() %>
		</div>

		<div class="kpi-title">
			<liferay-ui:message key="number-of-orders" />
		</div>
	</aui:col>

	<aui:col width="<%= 30 %>">
		<div class="kpi-value">

			<%
			CommerceMoney averageOrderPrice = commerceDashboardKPIDisplayContext.getAverageOrderPrice();
			%>

			<%= HtmlUtil.escape(averageOrderPrice.format(locale)) %>
		</div>

		<div class="kpi-title">
			<liferay-ui:message key="average-order-size" />
		</div>
	</aui:col>

	<aui:col width="<%= 30 %>">
		<div class="kpi-value">

			<%
			CommerceMoney ordersTotal = commerceDashboardKPIDisplayContext.getOrdersTotal();
			%>

			<%= HtmlUtil.escape(ordersTotal.format(locale)) %>
		</div>

		<div class="kpi-title">
			<liferay-ui:message key="total-spent" />
		</div>
	</aui:col>
</aui:row>