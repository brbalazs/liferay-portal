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

<%@ include file="../init.jsp" %>

<%

	CommerceCatalogDisplayContext commerceCatalogDisplayContext = (CommerceCatalogDisplayContext) request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	CommerceCatalog commerceCatalog = commerceCatalogDisplayContext.getCommerceCatalog();

	String headerTitle = null;

	if (commerceCatalog != null) {
		headerTitle = commerceCatalog.getName();
	}

	portletDisplay.setShowBackIcon(true);

	if (redirect == null) {
		portletDisplay.setURLBack(String.valueOf(renderResponse.createRenderURL()));
	}
	else {
		portletDisplay.setURLBack(redirect);
	}
%>

<commerce-ui:header
	actions="<%= commerceCatalogDisplayContext.getHeaderActionModels() %>"
	assignerModalUrl="/assigner/modal/url"
	bean="<%= commerceCatalog %>"
	dropdownItems="<%= commerceCatalogDisplayContext.getDropdownItems() %>"
	externalReferenceCode="123asd"
	externalReferenceCodeEditUrl="/external/reference/code/edit/url"
	model="<%= commerceCatalog %>"
	thumbnailUrl="<%-- TOOO it would be nice to have the proper Default Catalog Image --%>"
	title="<%= headerTitle %>"
/>

<div id="<portlet:namespace />editCatalogContainer">
	<liferay-frontend:screen-navigation
		fullContainerCssClass="col-12 pt-4"
		key="<%= CommerceCatalogScreenNavigationConstants.SCREEN_NAVIGATION_KEY_COMMERCE_CATALOG_GENERAL %>"
		modelBean="<%= CommerceCatalog.class %>"
		portletURL="<%= currentURLObj %>"
	/>
</div>

<aui:alert closeable="<%= false %>" cssClass="mt-3" type="warning">
	<liferay-ui:message key="this-site-does-not-have-a-channel" />
</aui:alert>