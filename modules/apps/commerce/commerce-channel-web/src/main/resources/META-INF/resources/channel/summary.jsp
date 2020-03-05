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
CommerceChannelDisplayContext commerceChannelDisplayContext = (CommerceChannelDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceChannel commerceChannel = commerceChannelDisplayContext.getCommerceChannel();
long commerceChannelId = commerceChannelDisplayContext.getCommerceChannelId();
String commerceCurrencyCode = commerceChannel.getCommerceCurrencyCode();
List<CommerceCurrency> commerceCurrencies = commerceChannelDisplayContext.getCommerceCurrencies();
%>

<portlet:actionURL name="editCommerceChannel" var="editCommerceChannelActionURL" />

<aui:form action="<%= editCommerceChannelActionURL %>" cssClass="m-0 p-0" method="post" name="channelFm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commerceChannel == null) ? Constants.ADD : Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURLObj %>" />
	<aui:input name="commerceChannelId" type="hidden" value="<%= commerceChannelId %>" />

	<aui:model-context bean="<%= commerceChannel %>" model="<%= CommerceChannel.class %>" />

	<div class="row">
		<div class="col-6">
			<commerce-ui:panel
				bodyClasses="flex-fill"
				title='<%= LanguageUtil.get(request, "details") %>'
			>
				<aui:input autoFocus="<%= true %>" name="name" />

				<aui:select label="currency" name="commerceCurrencyCode" required="<%= true %>" title="currency">

					<%
					for (CommerceCurrency commerceCurrency : commerceCurrencies) {
					%>

						<aui:option label="<%= commerceCurrency.getName(locale) %>" selected="<%= (commerceChannel == null) ? commerceCurrency.isPrimary() : commerceCurrencyCode.equals(commerceCurrency.getCode()) %>" value="<%= commerceCurrency.getCode() %>" />

					<%
					}
					%>

				</aui:select>

				<aui:select label="commerce-site-type" name="settings--commerceSiteType--">

					<%
					for (int commerceSiteType : CommerceAccountConstants.SITE_TYPES) {
					%>

						<aui:option label="<%= CommerceAccountConstants.getSiteTypeLabel(commerceSiteType) %>" selected="<%= commerceSiteType == commerceChannelDisplayContext.getCommerceSiteType() %>" value="<%= commerceSiteType %>" />

					<%
					}
					%>

				</aui:select>
			</commerce-ui:panel>
		</div>

		<div class="col-6">
			<commerce-ui:panel
				bodyClasses="flex-fill"
				title='<%= LanguageUtil.get(request, "orders") %>'
			>

				<%
				List<WorkflowDefinition> workflowDefinitions = commerceChannelDisplayContext.getActiveWorkflowDefinitions();
				%>

				<aui:fieldset>

					<%
					long typePK = CommerceOrderConstants.TYPE_PK_APPROVAL;
					String typePrefix = "approval";
					%>

					<%@ include file="/channel/workflow_definition.jspf" %>

					<%
					typePK = CommerceOrderConstants.TYPE_PK_FULFILLMENT;
					typePrefix = "fulfillment";
					%>

					<%@ include file="/channel/workflow_definition.jspf" %>
				</aui:fieldset>

				<aui:fieldset>
					<aui:input checked="<%= commerceChannelDisplayContext.isShowPurchaseOrderNumber() %>" helpMessage="configures-whether-purchase-order-number-is-shown-or-hidden-in-placed-and-pending-order-details" label="purchase-order-number" labelOff="hide" labelOn="show" name="settings--showPurchaseOrderNumber--" type="toggle-switch" />
				</aui:fieldset>
			</commerce-ui:panel>
		</div>
	</div>
</aui:form>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
		>

			<%
			java.util.Map<String, String> contextParams = new java.util.HashMap<>();

			contextParams.put("commerceChannelId", String.valueOf(commerceChannel.getCommerceChannelId()));
			%>

			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommerceChannelHealthCheckClayTable.NAME %>"
				id="<%= CommerceChannelHealthCheckClayTable.NAME %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= commerceChannelDisplayContext.getPortletURL() %>"
				showManagementBar="<%= false %>"
			/>
		</commerce-ui:panel>
	</div>
</div>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
			title='<%= LanguageUtil.get(request, "payment-methods") %>'
		>

			<%
			java.util.Map<String, String> contextParams = new java.util.HashMap<>();

			contextParams.put("commerceChannelId", String.valueOf(commerceChannel.getCommerceChannelId()));
			%>

			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommercePaymentMethodClayTable.NAME %>"
				id="<%= CommercePaymentMethodClayTable.NAME %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= commerceChannelDisplayContext.getPortletURL() %>"
				showManagementBar="<%= false %>"
			/>
		</commerce-ui:panel>
	</div>
</div>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
			title='<%= LanguageUtil.get(request, "shipping-methods") %>'
		>

			<%
			java.util.Map<String, String> contextParams = new java.util.HashMap<>();

			contextParams.put("commerceChannelId", String.valueOf(commerceChannel.getCommerceChannelId()));
			%>

			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommerceShippingMethodClayTable.NAME %>"
				id="<%= CommerceShippingMethodClayTable.NAME %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= commerceChannelDisplayContext.getPortletURL() %>"
				showManagementBar="<%= false %>"
			/>
		</commerce-ui:panel>
	</div>
</div>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
			title='<%= LanguageUtil.get(request, "tax-methods") %>'
		>

			<%
			java.util.Map<String, String> contextParams = new java.util.HashMap<>();

			contextParams.put("commerceChannelId", String.valueOf(commerceChannel.getCommerceChannelId()));
			%>

			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%= CommerceTaxMethodClayTable.NAME %>"
				id="<%= CommerceTaxMethodClayTable.NAME %>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%= commerceChannelDisplayContext.getPortletURL() %>"
				showManagementBar="<%= false %>"
			/>
		</commerce-ui:panel>
	</div>
</div>