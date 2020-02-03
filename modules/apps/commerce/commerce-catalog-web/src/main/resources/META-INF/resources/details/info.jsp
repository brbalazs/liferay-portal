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
	CommerceCatalog commerceCatalog = commerceCatalogDisplayContext.getCommerceCatalog();
	List<CommerceCurrency> commerceCurrencies = commerceCatalogDisplayContext.getCommerceCurrencies();

	boolean isViewOnly = false;

	if (commerceCatalog != null) {
		isViewOnly = !commerceCatalogDisplayContext.hasPermission(commerceCatalog.getCommerceCatalogId(), ActionKeys.UPDATE) || commerceCatalog.isSystem();
	}

	portletURL.setParameter("searchContainerId", "commerceCatalogs");
%>

<portlet:actionURL name="editCommerceCatalog" var="editCommerceCatalogActionURL" />

<div class="row">
	<div class="col-8">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "details")%>'
		>
			<div class="col-12 lfr-form-content">
				<aui:form action="<%= editCommerceCatalogActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
					<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commerceCatalog == null) ? Constants.ADD : Constants.UPDATE %>" />
					<aui:input name="redirect" type="hidden" value="<%= backURL %>" />
					<aui:input name="commerceCatalogId" type="hidden" value="<%= (commerceCatalog == null) ? 0 : commerceCatalog.getCommerceCatalogId() %>" />

					<aui:fieldset>
						<aui:input bean="<%= commerceCatalog %>" disabled="<%= isViewOnly %>" model="<%= CommerceCatalog.class %>" name="name" required="<%= true %>" />

						<aui:select disabled="<%= isViewOnly %>" helpMessage="the-default-language-for-the-content-within-this-catalog" label="default-catalog-language" name="catalogDefaultLanguageId" required="<%= true %>" title="language">

							<%
								String catalogDefaultLanguageId = themeDisplay.getLanguageId();

								if (commerceCatalog != null) {
									catalogDefaultLanguageId = commerceCatalog.getCatalogDefaultLanguageId();
								}

								Set<Locale> siteAvailableLocales = LanguageUtil.getAvailableLocales(themeDisplay.getScopeGroupId());

								for (Locale siteAvailableLocale : siteAvailableLocales) {
							%>

							<aui:option label="<%= siteAvailableLocale.getDisplayName(locale) %>" lang="<%= LocaleUtil.toW3cLanguageId(siteAvailableLocale) %>" selected="<%= catalogDefaultLanguageId.equals(LanguageUtil.getLanguageId(siteAvailableLocale)) %>" value="<%= LocaleUtil.toLanguageId(siteAvailableLocale) %>" />

							<%
								}
							%>

						</aui:select>

						<aui:select disabled="<%= isViewOnly %>" label="currency" name="commerceCurrencyCode" required="<%= true %>" title="currency">

							<%
								for (CommerceCurrency commerceCurrency : commerceCurrencies) {
									String commerceCurrencyCode = commerceCurrency.getCode();
							%>

							<aui:option label="<%= commerceCurrency.getName(locale) %>" selected="<%= (commerceCatalog == null) ? commerceCurrency.isPrimary() : commerceCurrencyCode.equals(commerceCatalog.getCommerceCurrencyCode()) %>" value="<%= commerceCurrencyCode %>" />

							<%
								}
							%>

						</aui:select>

						<div class="row">
							<div class="col-6">
								<aui:input label="group-accounts-filter" name="groupAccountsFilter" type="toggle-switch" inlineLabel="true" localized="true" />
							</div>
							<div class="col-6">
								<aui:input label="channel-filter" name="ChannelFilter" type="toggle-switch" inlineLabel="true" localized="true"/>
							</div>
						</div>
					</aui:fieldset>
				</aui:form>
			</div>
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

<!-- div class="row">
	<div class="col-8">
		<commerce-ui:panel
			elementClasses="flex-fill"
			title='<%= LanguageUtil.get(request, "revenue")%>'
		>

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
</div -->