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

CommerceSubscriptionEntry commerceSubscriptionEntry = commerceSubscriptionEntryDisplayContext.getCommerceSubscriptionEntry();
int orderPaymentStatus = commerceSubscriptionEntryDisplayContext.getOrderPaymentStatus();

Map<String, String> contextParams = new HashMap<>();

contextParams.put("commerceSubscriptionEntryId", String.valueOf(commerceSubscriptionEntry.getCommerceSubscriptionEntryId()));

List<CPSubscriptionType> cpSubscriptionTypes = commerceSubscriptionEntryDisplayContext.getCPSubscriptionTypes();

String defaultCPSubscriptionType = StringPool.BLANK;

if (!cpSubscriptionTypes.isEmpty()) {
	CPSubscriptionType firstCPSubscriptionType = cpSubscriptionTypes.get(0);

	defaultCPSubscriptionType = firstCPSubscriptionType.getName();
}

int subscriptionLength = BeanParamUtil.getInteger(commerceSubscriptionEntry, request, "subscriptionLength", 1);
String subscriptionType = BeanParamUtil.getString(commerceSubscriptionEntry, request, "subscriptionType", defaultCPSubscriptionType);
long maxSubscriptionCycles = BeanParamUtil.getLong(commerceSubscriptionEntry, request, "maxSubscriptionCycles");

String defaultCPSubscriptionTypeLabel = StringPool.BLANK;

CPSubscriptionType cpSubscriptionType = commerceSubscriptionEntryDisplayContext.getCPSubscriptionType(subscriptionType);

if (cpSubscriptionType != null) {
	defaultCPSubscriptionTypeLabel = cpSubscriptionType.getLabel(locale);
}

CPSubscriptionTypeJSPContributor cpSubscriptionTypeJSPContributor = commerceSubscriptionEntryDisplayContext.getCPSubscriptionTypeJSPContributor(subscriptionType);

boolean ending = false;

if (maxSubscriptionCycles > 0) {
	ending = true;
}
%>

<commerce-ui:panel
	elementClasses="flex-fill"
	title='<%= LanguageUtil.get(request, "reference-order") %>'
>

<div class="row">
	<div class="col-3">
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

	<div class="col-3">
		<commerce-ui:info-box
			elementClasses="py-3"
			title='<%= LanguageUtil.get(request, "payment-method") %>'
		>
			<img url="<%= commerceSubscriptionEntryDisplayContext.getOrderPaymentMethodImage() %>" />

			<span><%= commerceSubscriptionEntryDisplayContext.getOrderPaymentMethodName() %></span>
		</commerce-ui:info-box>
	</div>

	<div class="col-3">
		<commerce-ui:info-box
			elementClasses="py-3"
			title='<%= LanguageUtil.get(request, "payment-status") %>'
		>
			<clay:label
				label="<%= LanguageUtil.get(request, CommerceOrderPaymentConstants.getOrderPaymentStatusLabel(orderPaymentStatus)) %>"
				style="<%= CommerceOrderPaymentConstants.getOrderPaymentLabelStyle(orderPaymentStatus) %>"
			/>
		</commerce-ui:info-box>
	</div>

	<div class="col-3">
		<commerce-ui:info-box
			elementClasses="py-3"
			title='<%= LanguageUtil.get(request, "start-date") %>'
		>
			<span><%= commerceSubscriptionEntryDisplayContext.getCommerceSubscriptionEntryStartDate() %></span>
		</commerce-ui:info-box>
	</div>
</div>
</commerce-ui:panel>

<portlet:actionURL name="editCommerceSubscriptionEntry" var="editCommerceSubscriptionEntryActionURL" />

<aui:form action="<%= editCommerceSubscriptionEntryActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="commerceSubscriptionEntryId" type="hidden" value="<%= commerceSubscriptionEntryDisplayContext.getCommerceSubscriptionEntryId() %>" />

	<commerce-ui:panel
		title='<%= LanguageUtil.get(request, "payment-subscription") %>'
	>
		<commerce-ui:info-box
			elementClasses="py-3"
			title='<%= LanguageUtil.get(request, "info") %>'
		>
			<div class="row">
				<div class="col-6">
					<aui:select name="subscriptionStatus">

						<%
						for (int curSubscriptionStatus : CommerceSubscriptionEntryConstants.SUBSCRIPTION_STATUSES) {
						%>

							<aui:option data-label="<%= CommerceSubscriptionEntryConstants.getSubscriptionStatusLabel(curSubscriptionStatus) %>" label="<%= CommerceSubscriptionEntryConstants.getSubscriptionStatusLabel(curSubscriptionStatus) %>" selected="<%= commerceSubscriptionEntry.getSubscriptionStatus() == curSubscriptionStatus %>" value="<%= curSubscriptionStatus %>" />

						<%
						}
						%>

					</aui:select>

					<div class="never-ends-header">
						<aui:input checked="<%= ending ? false : true %>" name="neverEnds" type="toggle-switch" />
					</div>

					<div class="never-ends-content">
						<aui:input disabled="<%= ending ? false : true %>" helpMessage="max-subscription-cycles-help" label="end-after" name="maxSubscriptionCycles" suffix='<%= LanguageUtil.get(request, "cycles") %>' value="<%= String.valueOf(maxSubscriptionCycles) %>">
							<aui:validator name="digits" />

							<aui:validator errorMessage='<%= LanguageUtil.format(request, "please-enter-a-value-greater-than-or-equal-to-x", 1) %>' name="custom">
								function(val, fieldNode, ruleValue) {
									if (AUI.$('#<portlet:namespace />neverEnds')[0].checked) {
										return true;
									}

									if (parseInt(val, 10) > 0) {
										return true;
									}

									return false;
								}
							</aui:validator>
						</aui:input>
					</div>
				</div>

				<div class="col-6">
					<label for="<portlet:namespace />nextIterationDate"><%= LanguageUtil.get(request, "next-iteration-date") %></label>

					<%
					Date nextIterationDate = commerceSubscriptionEntry.getNextIterationDate();

					Calendar nextIterationCalendar = CalendarFactoryUtil.getCalendar(nextIterationDate.getTime(), user.getTimeZone());
					%>

					<liferay-ui:input-date
						dayParam="nextIterationDateDay"
						dayValue="<%= nextIterationCalendar.get(Calendar.DATE) %>"
						disabled="<%= false %>"
						firstDayOfWeek="<%= nextIterationCalendar.getFirstDayOfWeek() - 1 %>"
						monthParam="nextIterationDateMonth"
						monthValue="<%= nextIterationCalendar.get(Calendar.MONTH) %>"
						name="nextIterationDate"
						yearParam="nextIterationDateYear"
						yearValue="<%= nextIterationCalendar.get(Calendar.YEAR) %>"
					/>
				</div>
			</div>
		</commerce-ui:info-box>

		<commerce-ui:info-box
			elementClasses="py-3"
			title='<%= LanguageUtil.get(request, "payment") %>'
		>
			<div class="row">
				<div class="col-6">
					<aui:select name="subscriptionType" onChange='<%= renderResponse.getNamespace() + "selectSubscriptionType();" %>'>

						<%
						for (CPSubscriptionType curCPSubscriptionType : cpSubscriptionTypes) {
						%>

							<aui:option data-label="<%= curCPSubscriptionType.getLabel(locale) %>" label="<%= curCPSubscriptionType.getLabel(locale) %>" selected="<%= subscriptionType.equals(curCPSubscriptionType.getName()) %>" value="<%= curCPSubscriptionType.getName() %>" />

						<%
						}
						%>

					</aui:select>

					<%
					if (cpSubscriptionTypeJSPContributor != null) {
						cpSubscriptionTypeJSPContributor.render(commerceSubscriptionEntry, request, PipingServletResponse.createPipingServletResponse(pageContext));
					}
					%>

				</div>

				<div class="col-6">
					<div id="<portlet:namespace />cycleLengthContainer">
						<aui:input name="subscriptionLength" suffix="<%= defaultCPSubscriptionTypeLabel %>" value="<%= String.valueOf(subscriptionLength) %>">
							<aui:validator name="digits" />
							<aui:validator name="min">1</aui:validator>
						</aui:input>
					</div>
				</div>
			</div>
		</commerce-ui:info-box>
	</commerce-ui:panel>
</aui:form>

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

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />selectSubscriptionType',
		function() {
			var A = AUI();

			var subscriptionLength = A.one(
				'#<portlet:namespace />subscriptionLength'
			).val();
			var subscriptionType = A.one(
				'#<portlet:namespace />subscriptionType'
			).val();
			var maxSubscriptionCycles = A.one(
				'#<portlet:namespace />maxSubscriptionCycles'
			).val();

			var portletURL = new Liferay.PortletURL.createURL(
				'<%= currentURLObj %>'
			);

			portletURL.setParameter('subscriptionLength', subscriptionLength);
			portletURL.setParameter('subscriptionType', subscriptionType);
			portletURL.setParameter('maxSubscriptionCycles', maxSubscriptionCycles);

			window.location.replace(portletURL.toString());
		},
		['liferay-portlet-url']
	);
</aui:script>

<aui:script use="liferay-form">
	A.one('#<portlet:namespace />neverEnds').on('change', function(event) {
		var formValidator = Liferay.Form.get('<portlet:namespace />fm')
			.formValidator;

		formValidator.validateField('<portlet:namespace />maxSubscriptionCycles');
	});
</aui:script>

<aui:script use="aui-toggler">
	new A.Toggler({
		animated: true,
		content: '.never-ends-content',
		expanded: <%= ending %>,
		header: '#<portlet:namespace />neverEnds',
		on: {
			animatingChange: function(event) {
				var instance = this;

				if (!instance.get('expanded')) {
					A.one('#<portlet:namespace />maxSubscriptionCycles').attr(
						'disabled',
						false
					);
				} else {
					A.one('#<portlet:namespace />maxSubscriptionCycles').attr(
						'disabled',
						true
					);
				}
			}
		}
	});
</aui:script>