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
CPDefinitionSpecificationOptionValueDisplayContext cpDefinitionSpecificationOptionValueDisplayContext = (CPDefinitionSpecificationOptionValueDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CPDefinitionSpecificationOptionValue cpDefinitionSpecificationOptionValue = cpDefinitionSpecificationOptionValueDisplayContext.getCPDefinitionSpecificationOptionValue();

CPSpecificationOption cpSpecificationOption = cpDefinitionSpecificationOptionValue.getCPSpecificationOption();
%>

<portlet:actionURL name="editProductDefinitionSpecificationOptionValue" var="editProductDefinitionSpecificationOptionValueActionURL" />

<commerce-ui:side-panel-content
	title="<%= cpSpecificationOption.getTitle(locale) %>"
>
	<aui:form action="<%= editProductDefinitionSpecificationOptionValueActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="cpDefinitionSpecificationOptionValueId" type="hidden" value="<%= String.valueOf(cpDefinitionSpecificationOptionValue.getCPDefinitionSpecificationOptionValueId()) %>" />

		<div class="lfr-form-content">
			<liferay-ui:form-navigator
				backURL="<%= currentURL %>"
				formModelBean="<%= cpDefinitionSpecificationOptionValue %>"
				id="<%= CPDefinitionSpecificationOptionValueFormNavigatorConstants.FORM_NAVIGATOR_ID_COMMERCE_PRODUCT_DEFINITION_SPECIFICATION_OPTION_VALUE %>"
				markupView="lexicon"
			/>
		</div>
	</aui:form>
</commerce-ui:side-panel-content>