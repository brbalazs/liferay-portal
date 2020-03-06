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
CommerceTaxFixedRateAddressRelsDisplayContext commerceTaxFixedRateAddressRelsDisplayContext = (CommerceTaxFixedRateAddressRelsDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceTaxFixedRateAddressRel commerceTaxFixedRateAddressRel = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceTaxFixedRateAddressRel();

long commerceCountryId = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceCountryId();
long commerceRegionId = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceRegionId();
long commerceTaxMethodId = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceTaxMethodId();

long commerceTaxFixedRateAddressRelId = 0;

if (commerceTaxFixedRateAddressRel != null) {
	commerceTaxFixedRateAddressRelId = commerceTaxFixedRateAddressRel.getCommerceTaxFixedRateAddressRelId();
}
%>

<commerce-ui:side-panel-content>
	<portlet:actionURL name="editCommerceTaxFixedRateAddressRel" var="editCommerceTaxFixedRateAddressRelActionURL" />

	<aui:form action="<%= editCommerceTaxFixedRateAddressRelActionURL %>" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commerceTaxFixedRateAddressRel == null) ? Constants.ADD : Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceTaxFixedRateAddressRelId" type="hidden" value="<%= commerceTaxFixedRateAddressRelId %>" />
		<aui:input name="commerceTaxMethodId" type="hidden" value="<%= commerceTaxMethodId %>" />

		<aui:model-context bean="<%= commerceTaxFixedRateAddressRel %>" model="<%= CommerceTaxFixedRateAddressRel.class %>" />

		<commerce-ui:panel>
			<div class="row">
				<div class="col-md-6">
					<aui:select disabled="<%= commerceTaxFixedRateAddressRel != null %>" label="tax-category" name="CPTaxCategoryId">

						<%
						List<CPTaxCategory> cpTaxCategories = commerceTaxFixedRateAddressRelsDisplayContext.getAvailableCPTaxCategories();

						for (CPTaxCategory cpTaxCategory : cpTaxCategories) {
						%>

							<aui:option label="<%= HtmlUtil.escape(cpTaxCategory.getName(languageId)) %>" value="<%= cpTaxCategory.getCPTaxCategoryId() %>" />

						<%
						}
						%>

					</aui:select>
				</div>

				<div class="col-md-6">
					<aui:input name="rate" suffix="<%= commerceTaxFixedRateAddressRelsDisplayContext.getCommerceCurrencyCode() %>" />
				</div>
			</div>

			<div class="row">
				<div class="col-md-4">
					<aui:select label="country" name="commerceCountryId" showEmptyOption="<%= true %>">

						<%
						List<CommerceCountry> commerceCountries = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceCountries();

						for (CommerceCountry commerceCountry : commerceCountries) {
						%>

							<aui:option label="<%= commerceCountry.getName(languageId) %>" selected="<%= (commerceTaxFixedRateAddressRel != null) && (commerceTaxFixedRateAddressRel.getCommerceCountryId() == commerceCountry.getCommerceCountryId()) %>" value="<%= commerceCountry.getCommerceCountryId() %>" />

						<%
						}
						%>

					</aui:select>
				</div>

				<div class="col-md-4">
					<aui:select label="region" name="commerceRegionId" showEmptyOption="<%= true %>">

						<%
						List<CommerceRegion> commerceRegions = commerceTaxFixedRateAddressRelsDisplayContext.getCommerceRegions();

						for (CommerceRegion commerceRegion : commerceRegions) {
						%>

							<aui:option label="<%= commerceRegion.getName() %>" selected="<%= (commerceTaxFixedRateAddressRel != null) && (commerceTaxFixedRateAddressRel.getCommerceRegionId() == commerceRegion.getCommerceRegionId()) %>" value="<%= commerceRegion.getCommerceRegionId() %>" />

						<%
						}
						%>

					</aui:select>
				</div>

				<div class="col-md-4">
					<aui:input name="zip" />
				</div>
			</div>
		</commerce-ui:panel>

		<aui:button-row>
			<aui:button cssClass="btn-lg" type="submit" />
		</aui:button-row>
	</aui:form>
</commerce-ui:side-panel-content>

<aui:script use="aui-base,liferay-dynamic-select">
	new Liferay.DynamicSelect([
		{
			select: '<portlet:namespace />commerceCountryId',
			selectData: function(callback) {
				Liferay.Service(
					'/commerce.commercecountry/get-commerce-countries',
					{
						companyId: <%= company.getCompanyId() %>,
						active: true
					},
					callback
				);
			},
			selectDesc: 'nameCurrentValue',
			selectId: 'commerceCountryId',
			selectSort: '<%= true %>',
			selectVal: '<%= commerceCountryId %>'
		},
		{
			select: '<portlet:namespace />commerceRegionId',
			selectData: function(callback, selectKey) {
				Liferay.Service(
					'/commerce.commerceregion/get-commerce-regions',
					{
						commerceCountryId: Number(selectKey),
						active: true
					},
					callback
				);
			},
			selectDesc: 'name',
			selectId: 'commerceRegionId',
			selectVal: '<%= commerceRegionId %>'
		}
	]);
</aui:script>