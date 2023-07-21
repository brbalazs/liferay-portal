<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<%
OpenIdConnectConfiguration openIdConnectConfiguration = ConfigurationProviderUtil.getConfiguration(OpenIdConnectConfiguration.class, new ParameterMapSettingsLocator(request.getParameterMap(), PortalSettingsOpenIdConnectConstants.FORM_PARAMETER_NAMESPACE, new CompanyServiceSettingsLocator(company.getCompanyId(), OpenIdConnectConstants.SERVICE_NAME)));
%>

<aui:input id='<%= PortalUtil.generateRandomKey(request, "portal_settings_authentication_openid_connect") %>' name="<%= ActionRequest.ACTION_NAME %>" type="hidden" value="/portal_settings/openid_connect" />

<aui:fieldset>
	<aui:input label="enabled" name='<%= PortalSettingsOpenIdConnectConstants.FORM_PARAMETER_NAMESPACE + "enabled" %>' type="checkbox" value="<%= openIdConnectConfiguration.enabled() %>" />

	<aui:button-row>
		<portlet:actionURL name="/portal_settings/openid_connect_delete" var="resetValuesURL">
			<portlet:param name="tabs1" value="openid-connect" />
		</portlet:actionURL>

		<%
		String taglibOnClick = "if (confirm('" + UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-reset-the-configured-values") + "')) {submitForm(document.hrefFm, '" + resetValuesURL.toString() + "');}";
		%>

		<aui:button onClick="<%= taglibOnClick %>" value="reset-values" />
	</aui:button-row>
</aui:fieldset>