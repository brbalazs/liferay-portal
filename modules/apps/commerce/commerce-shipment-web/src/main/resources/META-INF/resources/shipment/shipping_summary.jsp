<%@ page import="com.liferay.portal.kernel.portlet.LiferayWindowState" %>
<%@ taglib prefix="commerce-ui" uri="http://alloy.liferay.com/tld/alloy" %>
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
CommerceShipmentItemDisplayContext commerceShipmentItemDisplayContext = (CommerceShipmentItemDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<liferay-portlet:renderURL var="editCourierDetailURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="editCourierDetail" />
	<%-- TODO add required parameters --%>
</liferay-portlet:renderURL>

<commerce-ui:modal
	id="edit-courier-modal"
	refreshPageOnClose="<%= true %>"
	size="lg"
	title='<%= LanguageUtil.get(request, "edit-courier-detail") %>'
	url="<%= editCourierDetailURL %>"
/>

<liferay-portlet:renderURL var="editBarcodeDetailURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
	<portlet:param name="mvcRenderCommandName" value="editBarcode" />
	<%-- TODO add required parameters --%>
</liferay-portlet:renderURL>

<commerce-ui:modal
	id="edit-barcode-modal"
	refreshPageOnClose="<%= true %>"
	size="lg"
	title='<%= LanguageUtil.get(request, "edit-barcode") %>'
	url="<%= editRequestedDeliveryDateURL %>"
/>

<div class="row">
	<div class="col-12 mb-4">
		<commerce-ui:step-tracker
			steps="<%-- TODO implement = commerceShipmentItemDisplayContext.getShipmentSteps() --%>"
		/>
	</div>

	<div class="col-12">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "info")%>'>

			<div class="row vertically-divided">
				<div class="col-md-4">
					<commerce-ui:info-box
						actionLabel='<%= LanguageUtil.get(request, "edit") %>'
						actionTargetId="edit-courier-modal"
						actionUrl="<%-- TODO - implement = editCourierURL --%>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "courier-detail") %>'
					>
						<p>
							<span class="text-muted">
								<liferay-ui:message key="shipping-id" />
							</span>
							<span class="text-muted">
								<%-- TODO - implement = shipping id link -- opens modal? --%>
							</span>
						</p>
					</commerce-ui:info-box>
				</div>

				<div class="col-md-4">
					<commerce-ui:info-box
						actionLabel='<%= LanguageUtil.get(request, "edit") %>'
						actionTargetId="edit-barcode-modal"
						actionUrl="<%-- TODO - implement = editBarcodeURL --%>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "barcode") %>'
					>
						<p>
							<span class="text-muted">
								<liferay-ui:message key="international-article-number" />
							</span>
							<span class="text-muted">
								<b><%-- TODO - implement = get international article number --%></b>
							</span>
						</p>
					</commerce-ui:info-box>
				</div>

				<div class="col-md-4">
					<commerce-ui:info-box
						actionLabel=""
						actionTargetId=""
						actionUrl="<%-- TODO - implement = changeDateAction --%>"
						elementClasses="py-3"
						title='<%= LanguageUtil.get(request, "select-date") %>'
					>
						<p>
							<%-- date picker input --%>
						</p>
					</commerce-ui:info-box>
				</div>
			</div>
		</commerce-ui:panel>
	</div>

	<div class="col-12">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "products")%>'
		>
			<commerce-ui:dataset-display
				contextParams="<%= contextParams %>"
				dataProviderKey="<%-- = TODO = implement commerce shipping products clay table .NAME --%>"
				id="<%-- = TODO = implement commerce shipping products clay table .NAME --%>"
				itemsPerPage="<%= 10 %>"
				namespace="<%= renderResponse.getNamespace() %>"
				pageNumber="<%= 1 %>"
				portletURL="<%-- TODO = commerceShipmentItemDisplayContext.getPortletURL() --%>"
				style="stacked"
			/>

			<%-- TODO add products via modal, to be configured --%>
		</commerce-ui:panel>
	</div>
</div>

<div id="<portlet:namespace />side-panel-root"></div>
<div id="<portlet:namespace />side-panel-wrapper"></div>

<aui:script require="commerce-frontend-js/components/side_panel/entry.es as sidePanel">
	sidePanel.default(
		"<portlet:namespace />sidePanel",
		"<portlet:namespace />side-panel-root",
		{
			portalWrapperId: "<portlet:namespace />side-panel-wrapper",
			spritemap: "<%= themeDisplay.getPathThemeImages() + "/clay/icons.svg" %>",
			topAnchorSelector: ".commerce-header"
		}
	);
</aui:script>