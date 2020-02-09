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
CPInstanceCommercePriceEntryDisplayContext cpInstanceCommercePriceEntryDisplayContext = (CPInstanceCommercePriceEntryDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommercePriceEntry commercePriceEntry = cpInstanceCommercePriceEntryDisplayContext.getCommercePriceEntry();
long cpDefinitionId = cpInstanceCommercePriceEntryDisplayContext.getCPDefinitionId();
long cpInstanceId = cpInstanceCommercePriceEntryDisplayContext.getCPInstanceId();

CommercePriceList commercePriceList = commercePriceEntry.getCommercePriceList();
long commercePriceEntryId = commercePriceEntry.getCommercePriceEntryId();

String instancePriceEntryToolbarItem = ParamUtil.getString(request, "instancePriceEntryToolbarItem", "view-price-entry-details");

String title = commercePriceList.getName();
%>

<commerce-ui:side-panel-content
	title="<%= title %>"
>
	<%@ include file="/instance_price_entry_navbar.jspf" %>

	<portlet:actionURL name="editCPInstanceCommercePriceEntry" var="editCommercePriceEntryActionURL" />

	<aui:form action="<%= editCommercePriceEntryActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commercePriceEntryId" type="hidden" value="<%= commercePriceEntryId %>" />
		<aui:input name="cpInstanceId" type="hidden" value="<%= cpInstanceId %>" />

		<aui:model-context bean="<%= commercePriceEntry %>" model="<%= CommercePriceEntry.class %>" />

		<div class="row">
			<div class="col-12">
				<%@ include file="/price_entry/details.jspf" %>

				<%@ include file="/price_entry/custom_fields.jspf" %>
			</div>
		</div>

		<aui:button-row cssClass="price-entry-button-row">
			<aui:button cssClass="btn-lg" type="submit" />

			<aui:button cssClass="btn-lg" type="cancel" />
		</aui:button-row>
	</aui:form>
</commerce-ui:side-panel-content>