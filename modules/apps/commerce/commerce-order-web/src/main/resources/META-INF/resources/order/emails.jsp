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
CommerceOrderEditDisplayContext commerceOrderEditDisplayContext = (CommerceOrderEditDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<commerce-ui:panel
	bodyClasses="p-0"
	title='<%= LanguageUtil.get(request, "emails") %>'
>

	<%
	Map<String, String> contextParams = new HashMap<>();

	contextParams.put("commerceOrderId", String.valueOf(commerceOrderEditDisplayContext.getCommerceOrderId()));
	%>

	<commerce-ui:dataset-display
		contextParams="<%= contextParams %>"
		dataProviderKey="<%= CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_NOTIFICATIONS %>"
		id="<%= CommerceOrderDataSetConstants.COMMERCE_DATA_SET_KEY_NOTIFICATIONS %>"
		itemsPerPage="<%= 10 %>"
		namespace="<%= renderResponse.getNamespace() %>"
		pageNumber="<%= 1 %>"
		portletURL="<%= commerceOrderEditDisplayContext.getCommerceNotificationQueueEntriesPortletURL() %>"
	/>
</commerce-ui:panel>

<div id="<portlet:namespace />side-panel-root"></div>
<div id="<portlet:namespace />side-panel-wrapper"></div>

<aui:script require="commerce-frontend-js/js/side_panel/entry.es as SidePanel">
	sidePanel.default(
		'<portlet:namespace />sidePanel',
		'<portlet:namespace />side-panel-root',
		{
			spritemap:
				'<%= themeDisplay.getPathThemeImages() + "/clay/icons.svg" %>',
			topAnchorSelector: '.commerce-header',
			wrapperSelector: '#<portlet:namespace />side-panel-wrapper'
		}
	);
</aui:script>