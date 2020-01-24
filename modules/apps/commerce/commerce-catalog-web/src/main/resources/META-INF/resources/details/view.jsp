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
	// TODO CommerceCatalogsDisplayContext

	String headerTitle = null;

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
	actions="<%-- TOOO implement displayContext = .getHeaderActionModels() --%>"
	assignerModalUrl="/assigner/modal/url"
	bean="<%!-- TODO implement = commerceCatalogs --%>"
	dropdownItems="<%-- TOOO implement displayContext = .getHeaderActionModels() --%>"
	externalReferenceCode="123asd"
	externalReferenceCodeEditUrl="/external/reference/code/edit/url"
	model="<%!-- TODO implement = commerceCatalogs --%>"
	thumbnailUrl="<%-- TOOO implement displayContext = .getHeaderActionModels() --%>"
	title="<%= headerTitle %>"
/>

<div id="<portlet:namespace />editOrderContainer">
	<liferay-frontend:screen-navigation
		fullContainerCssClass="col-12 pt-4"
		key="<%-- TODO implement screen navigation constants %>"
		modelBean="<%!-- TODO implement = commerceCatalogs --%>"
		portletURL="<%= currentURLObj %>"
	/>
</div>

<aui:alert closeable="<%= false %>" cssClass="mt-3" type="warning">
	<liferay-ui:message key="this-site-does-not-have-a-channel" />
</aui:alert>