<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %><%--
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
CommerceShipmentDisplayContext commerceShipmentDisplayContext = (CommerceShipmentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

PortletURL portletURL = commerceShipmentDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "commerceShipments");

request.setAttribute("view.jsp-portletURL", portletURL);
%>

<c:if test="<%= commerceShipmentDisplayContext.hasManageCommerceShipmentsPermission() %>">

	<liferay-portlet:renderURL var="createNewShipmentURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcRenderCommandName" value="editCommerceShipment" /> <%-- TODO taken from the old redirect --%>
	</liferay-portlet:renderURL>

	<%-- TODO this is not needed currenly, 'cause the modal is triggered from the BE configuration towards the dataset display --%>
	<commerce-ui:modal
		id="create-new-shipment-modal"
		refreshPageOnClose="<%= true %>"
		size="lg"
		url="<%-- TODO createNewShipmentURL --%>"
	/>

	<%-- TODO Access modal via creationMenuItem that has a type: modal, label
	"create new shipments" and modal url for the
	iframe to load-up in the modal --%>

	<div id="<portlet:namespace />editShipmentContainer" class="row">
		<div class="col-12">
		<commerce-ui:dataset-display
			contextParams=""
			dataProviderKey="<%-- TODO CommerceShipmentsClayTable.NAME - implement --%>"
			id="<%= CommerceShipmentsClayTable.NAME %>"
			itemsPerPage="<%= 10 %>"
			namespace="<%= renderResponse.getNamespace() %>"
			pageNumber="<%= 1 %>"
			portletURL="<%= portletURL %>"
			style="fluid"
		/>
		</div>
	</div>
</c:if>