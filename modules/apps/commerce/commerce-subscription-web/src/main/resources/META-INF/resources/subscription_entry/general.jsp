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
CommerceSubscriptionEntryDisplayContext commerceSubscriptionEntryDisplayContext = (CommerceSubscriptionEntryDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

java.util.Map<String, String> contextParams = new java.util.HashMap<>();

CommerceSubscriptionEntry commerceSubscriptionEntry = commerceSubscriptionEntryDisplayContext.getCommerceSubscriptionEntry();

contextParams.put("commerceSubscriptionEntryId", String.valueOf(commerceSubscriptionEntry.getCommerceSubscriptionEntryId()));

String defaultCPSubscriptionType = StringPool.BLANK;

List<CPSubscriptionType> cpSubscriptionTypes = commerceSubscriptionEntryDisplayContext.getCPSubscriptionTypes();

if (!cpSubscriptionTypes.isEmpty()) {
	CPSubscriptionType firstCPSubscriptionType = cpSubscriptionTypes.get(0);

	defaultCPSubscriptionType = firstCPSubscriptionType.getName();
}

String subscriptionType = BeanParamUtil.getString(commerceSubscriptionEntry, request, "subscriptionType", defaultCPSubscriptionType);
long maxSubscriptionCycles = BeanParamUtil.getLong(commerceSubscriptionEntry, request, "maxSubscriptionCycles");
int subscriptionStatus = BeanParamUtil.getInteger(commerceSubscriptionEntry, request, "subscriptionStatus");

boolean finiteSubscription = false;

if (maxSubscriptionCycles > 0) {
	finiteSubscription = true;
}

CPSubscriptionType currentSubscriptionType = null;
%>

<div class="row">
	<div class="col-12 mb-4">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "reference-order") %>'
		>

		<div class="row">
			<div class="col-md-4">
				<commerce-ui:info-box
					elementClasses="py-3"
					title='<%= LanguageUtil.get(request, "order-id") %>'
				>
					<span>
						<a href="<%= commerceSubscriptionEntryDisplayContext.getEditCommerceOrderURL(0) %>">
							<%= commerceSubscriptionEntryDisplayContext.getCommerceOrderId() %>
						</a>
					</span>
				</commerce-ui:info-box>
			</div>

			<div class="col-md-4">
				<commerce-ui:info-box
					elementClasses="py-3"
					title='<%= LanguageUtil.get(request, "payment-method") %>'
				>
					<img url="<%= commerceSubscriptionEntryDisplayContext.getOrderPaymentMethodImage() %>" />

					<span><%= commerceSubscriptionEntryDisplayContext.getOrderPaymentMethodName() %></span>
				</commerce-ui:info-box>
			</div>

			<div class="col-md-4">
				<commerce-ui:info-box
					elementClasses="py-3"
					title='<%= LanguageUtil.get(request, "payment-status") %>'
				>
					<span><%= commerceSubscriptionEntryDisplayContext.getOrderPaymentStatus() %></span>
				</commerce-ui:info-box>
			</div>
		</div>
		</commerce-ui:panel>
	</div>
</div>

<aui:form>
<div class="row">
	<div class="col-12 mb-4">
	<commerce-ui:panel
		bodyClasses="p-0"
		collapsed="<%= false %>"
		collapseLabel="paymentSubscriptionEnabledLabel"
		collapseSwitchName="paymentSubscriptionEnabledSwitch"
		title='<%= LanguageUtil.get(request, "payment-subscription") %>'
	>
		<aui:fieldset cssClass="col-12">
			<h3><%= LanguageUtil.get(request, "info") %></h3>

			<hr />

			<div class="row">
				<div class="col-md-6">
					<div>
						<aui:select name="subscriptionStatus">

							<%
								for (int curSubscriptionStatus : CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUSES) {
							%>

							<aui:option data-label="<%= CommerceSubscriptionEntryConstants.getSubscriptionStatusLabel(curSubscriptionStatus) %>" label="<%= CommerceSubscriptionEntryConstants.getSubscriptionStatusLabel(curSubscriptionStatus) %>" selected="<%= subscriptionStatus == curSubscriptionStatus %>" value="<%= curSubscriptionStatus %>" />

							<%
								}
							%>

						</aui:select>
					</div>

					<div><aui:input checked="<%= finiteSubscription ? false : true %>" name="neverEnds" type="toggle-switch" /></div>
				</div>

				<div class="col-md-6">
					<div>
						<span><%= LanguageUtil.get(request, "start-date") %></span>

						<%
							java.util.Calendar startCalendar = Calendar.getInstance();
							startCalendar.setTime(commerceSubscriptionEntry.getStartDate());
						%>

						<liferay-ui:input-date
							dayParam="startDateDay"
							dayValue="<%= startCalendar.get(Calendar.DATE) %>"
							disabled="<%= false %>"
							firstDayOfWeek="<%= startCalendar.getFirstDayOfWeek() - 1 %>"
							monthParam="startDateMonth"
							monthValue="<%= startCalendar.get(Calendar.MONTH) %>"
							name="startDate"
							yearParam="startDateYear"
							yearValue="<%= startCalendar.get(Calendar.YEAR) %>"
						/>
					</div>

					<div>
						<span><%= LanguageUtil.get(request, "end-date") %></span>

						<%
							java.util.Calendar endCalendar = commerceSubscriptionEntryDisplayContext.getSubscriptionEndDate();
						%>

						<liferay-ui:input-date
							dayParam="endDateDay"
							dayValue="<%= endCalendar.get(Calendar.DATE) %>"
							disabled="<%= false %>"
							firstDayOfWeek="<%= endCalendar.getFirstDayOfWeek() - 1 %>"
							monthParam="endDateMonth"
							monthValue="<%= endCalendar.get(Calendar.MONTH) %>"
							name="endDate"
							yearParam="endDateYear"
							yearValue="<%= endCalendar.get(Calendar.YEAR) %>"
						/>
					</div>
				</div>
			</div>

			<h3><%= LanguageUtil.get(request, "payment") %></h3>

			<hr />

			<div class="row">
				<div class="col-md-6">
					<div>
					<aui:select name="subscriptionType" onChange='<%= renderResponse.getNamespace() + "selectSubscriptionType();" %>'>

						<%
							for (CPSubscriptionType curCPSubscriptionType : cpSubscriptionTypes) {
								if (subscriptionType.equals(curCPSubscriptionType.getName())) {
									currentSubscriptionType = curCPSubscriptionType;
								}
						%>

							<aui:option data-label="<%= curCPSubscriptionType.getLabel(locale) %>" label="<%= curCPSubscriptionType.getLabel(locale) %>" selected="<%= subscriptionType.equals(curCPSubscriptionType.getName()) %>" value="<%= curCPSubscriptionType.getName() %>" />

						<%
						}
						%>

					</aui:select>
					</div>

					<div>
						<aui:input name='<%= LanguageUtil.get(request, "subscription-length") %>' suffix="<%= currentSubscriptionType.getLabel(locale) %>" value="<%= commerceSubscriptionEntry.getSubscriptionLength() %>" />
					</div>
				</div>

				<div class="col-md-6">
					<div class="row">
						<div class="col-md-6">

							<%
								CommerceCurrency commerceCurrency = commerceSubscriptionEntryDisplayContext.getSubscriptionCurrency();
							%>

							<aui:input label='<%= LanguageUtil.get(request, "one-time-price") %>' name="oneTimePrice" suffix="<%= HtmlUtil.escape(commerceCurrency.getCode()) %>" type="text" value="<%= commerceCurrency.round(commerceSubscriptionEntryDisplayContext.getSubscriptionUnitPrice()) %>">
								<aui:validator name="number" />
							</aui:input>
						</div>

						<div class="col-md-6">
							<aui:input label='<%= LanguageUtil.get(request, "total-price") %>' name="totalPrice" suffix="<%= HtmlUtil.escape(commerceCurrency.getCode()) %>" type="text" value="<%= commerceCurrency.round(commerceSubscriptionEntryDisplayContext.getSubscriptionTotalPrice()) %>">
								<aui:validator name="number" />
							</aui:input>
						</div>
					</div>

					<div class="row">
						<div class="col-12">
							<aui:input name='<%= LanguageUtil.get(request, "max-subscription-cycles") %>' suffix='<%= LanguageUtil.get(request, "cycles") %>' value="<%= commerceSubscriptionEntry.getMaxSubscriptionCycles() %>" />
						</div>
					</div>
				</div>
			</div>
		</aui:fieldset>
		</commerce-ui:panel>
	</div>
</div>

<%--
<div class="row">
	<div class="col-12">
		collapsable
	<commerce-ui:panel
		bodyClasses="p-0"
		title='<%= LanguageUtil.get(request, "delivery-subscription") %>'
	>
				<!-- FORM 2 -->
			<div class="row">
				<div class="col-md-6">

				</div>

				<div class="col-md-6">

				</div>
			</div>

			<div class="row">
				<div class="col-md-6">

				</div>

				<div class="col-md-6">

				</div>
			</div>
		</commerce-ui:panel>
	</div>
	</div>
</div>
--%>

</aui:form>

<div class="row">
	<div class="col-12">
		<commerce-ui:panel
			bodyClasses="p-0"
			title='<%= LanguageUtil.get(request, "items") %>'
		>
		<commerce-ui:dataset-display
			contextParams="<%= contextParams %>"
			dataProviderKey="<%= CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_ORDER_ITEMS %>"
			id="<%= CommerceSubscriptionDataSetConstants.COMMERCE_DATA_SET_KEY_SUBSCRIPTION_ORDER_ITEMS %>"
			itemsPerPage="<%= 10 %>"
			namespace="<%= renderResponse.getNamespace() %>"
			pageNumber="<%= 1 %>"
			portletURL="<%= commerceSubscriptionEntryDisplayContext.getPortletURL() %>"
			style="stacked"
		/>
		</commerce-ui:panel>
	</div>
</div>