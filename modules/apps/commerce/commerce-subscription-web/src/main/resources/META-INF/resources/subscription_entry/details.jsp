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
String redirect = ParamUtil.getString(request, "redirect");

CommerceSubscriptionEntryDisplayContext commerceSubscriptionEntryDisplayContext = (CommerceSubscriptionEntryDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceSubscriptionEntry commerceSubscriptionEntry = commerceSubscriptionEntryDisplayContext.getCommerceSubscriptionEntry();

String defaultCPSubscriptionType = StringPool.BLANK;

List<CPSubscriptionType> cpSubscriptionTypes = commerceSubscriptionEntryDisplayContext.getCPSubscriptionTypes();

if (!cpSubscriptionTypes.isEmpty()) {
	CPSubscriptionType firstCPSubscriptionType = cpSubscriptionTypes.get(0);

	defaultCPSubscriptionType = firstCPSubscriptionType.getName();
}

int subscriptionLength = BeanParamUtil.getInteger(commerceSubscriptionEntry, request, "subscriptionLength", 1);
String subscriptionType = BeanParamUtil.getString(commerceSubscriptionEntry, request, "subscriptionType", defaultCPSubscriptionType);
long maxSubscriptionCycles = BeanParamUtil.getLong(commerceSubscriptionEntry, request, "maxSubscriptionCycles");
int subscriptionStatus = BeanParamUtil.getInteger(commerceSubscriptionEntry, request, "subscriptionStatus");

String defaultCPSubscriptionTypeLabel = StringPool.BLANK;

CPSubscriptionType cpSubscriptionType = commerceSubscriptionEntryDisplayContext.getCPSubscriptionType(subscriptionType);

if (cpSubscriptionType != null) {
	defaultCPSubscriptionTypeLabel = cpSubscriptionType.getLabel(locale);
}

CPSubscriptionTypeJSPContributor cpSubscriptionTypeJSPContributor = commerceSubscriptionEntryDisplayContext.getCPSubscriptionTypeJSPContributor(subscriptionType);

boolean finiteSubscription = false;

if (maxSubscriptionCycles > 0) {
	finiteSubscription = true;
}
%>

<commerce-ui:header
		actions="<%= commerceOrderEditDisplayContext.getHeaderActionModels() %>"
		assignerModalUrl="/assigner/modal/url"
		bean="<%= commerceOrder %>"
		dropdownItems="<%= commerceOrderEditDisplayContext.getDropdownItems() %>"
		externalReferenceCode="123asd"
		externalReferenceCodeEditUrl="/external/reference/code/edit/url"
		model="<%= CommerceOrder.class %>"
		thumbnailUrl="<%= commerceOrderEditDisplayContext.getCommerceAccountThumbnailURL() %>"
		title="<%= headerTitle %>"
/>

<div id="<portlet:namespace />editOrderContainer">
	<liferay-frontend:screen-navigation
			fullContainerCssClass="col-12 pt-4"
			key="<%= CommerceOrderScreenNavigationConstants.SCREEN_NAVIGATION_KEY_COMMERCE_ORDER_GENERAL %>"
			modelBean="<%= commerceOrder %>"
			portletURL="<%= currentURLObj %>"
	/>
</div>
