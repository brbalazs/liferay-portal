<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>
<%@ page
import="com.liferay.commerce.product.content.search.web.internal.display.context.CPSortDisplayContext"
import="com.liferay.commerce.product.content.search.web.internal.constants.CPSearchResultsConstants"
%>

<%@ include file="/init.jsp" %>

<%
	CPSortDisplayContext cpSortDisplayContext = (CPSortDisplayContext)request.getAttribute(
		WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<%
String redirect = ParamUtil.getString(request, "redirect");
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>" var="configurationRenderURL" />

<aui:form action="<%= configurationActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<div class="portlet-configuration-body-content">
		<div class="container-fluid container-fluid-max-xl">
			<div class="sheet">
				<div class="panel-group panel-group-flush">
					<aui:select id="selectDefaultSorting" label="select-default-sorting" name="preferences--selectDefaultSorting--"
								value="<%= cpSortDisplayContext.selectedDefaultSorting() %>">

						<%
							for (String sortType : CPSearchResultsConstants.SORT_OPTIONS) {
						%>

						<aui:option label="<%= sortType %>" selected="<%= cpSortDisplayContext.selectedDefaultSorting().equalsIgnoreCase(sortType) %>" />

						<%
							}
						%>

					</aui:select>
				</div>
			</div>
		</div>
	</div>

	<aui:button-row>
		<aui:button cssClass="btn-lg" name="submitButton" type="submit" value="save" />
	</aui:button-row>
</aui:form>