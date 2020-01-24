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
	CommerceCatalogDisplayContext commerceCatalogDisplayContext = (CommerceCatalogDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

	PortletURL portletURL = commerceCatalogDisplayContext.getPortletURL();

	portletURL.setParameter("searchContainerId", "commerceCatalogs");
%>

<div class="row">
	<div class="col-8">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "details")%>'
		>
			<!--
				AUI FORM inputs and switches
			-->

		</commerce-ui:panel>
	</div>

	<div class="col-4">
		<commerce-ui:panel
			elementClasses="flex-fill h-100"
			title='<%= LanguageUtil.get(request, "default-catalog-image")%>'>

			<div class="row">
				<div class="col-12 h-100">
					<img src="<%-- TODO get --%>" alt="<%= LanguageUtil.get(request, "default-catalog-image") %>" />
				</div>
			</div>
		</commerce-ui:panel>
	</div>
</div>

<div class="row">
	<div class="col-8">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "details")%>'
		>
			<!--
			AUI FORM inputs and switches
			-->

		</commerce-ui:panel>
	</div>

	<div class="col-4">
		<commerce-ui:panel
			elementClasses="flex-fill h-100"
			title='<%= LanguageUtil.get(request, "default-catalog-image")%>'>

			<div class="row">
				<div class="col-12 h-100">
					<img src="<%-- TODO get --%>" alt="<%= LanguageUtil.get(request, "default-catalog-image") %>" />
				</div>
			</div>
		</commerce-ui:panel>
	</div>
</div>