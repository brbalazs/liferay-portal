<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<%
GoogleAuthorizationConfiguration googleAuthorizationConfiguration = ConfigurationProviderUtil.getConfiguration(GoogleAuthorizationConfiguration.class, new ParameterMapSettingsLocator(request.getParameterMap(), PortalSettingsGoogleConstants.FORM_PARAMETER_NAMESPACE, new CompanyServiceSettingsLocator(company.getCompanyId(), GoogleConstants.SERVICE_NAME)));
%>

<liferay-ui:error key="googleClientIdInvalid" message="the-google-client-id-is-invalid" />
<liferay-ui:error key="googleClientSecretInvalid" message="the-google-client-secret-is-invalid" />

<aui:fieldset>
	<aui:input id='<%= PortalUtil.generateRandomKey(request, "portal_settings_authentication_google") %>' name="<%= ActionRequest.ACTION_NAME %>" type="hidden" value="/portal_settings/google" />

	<aui:input label="enabled" name='<%= PortalSettingsGoogleConstants.FORM_PARAMETER_NAMESPACE + "enabled" %>' type="checkbox" value="<%= googleAuthorizationConfiguration.enabled() %>" />

	<aui:input label="google-client-id" name='<%= PortalSettingsGoogleConstants.FORM_PARAMETER_NAMESPACE + "clientId" %>' type="text" value="<%= googleAuthorizationConfiguration.clientId() %>" wrapperCssClass="lfr-input-text-container" />

	<aui:input label="google-client-secret" name='<%= PortalSettingsGoogleConstants.FORM_PARAMETER_NAMESPACE + "clientSecret" %>' type="text" value="<%= googleAuthorizationConfiguration.clientSecret() %>" wrapperCssClass="lfr-input-text-container" />

	<aui:button-row>
		<portlet:actionURL name="/portal_settings/google_delete" var="resetValuesURL">
			<portlet:param name="tabs1" value="google" />
		</portlet:actionURL>

		<%
		String taglibOnClick = "if (confirm('" + UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-reset-the-configured-values") + "')) {submitForm(document.hrefFm, '" + resetValuesURL.toString() + "');}";
		%>

		<aui:button onClick="<%= taglibOnClick %>" value="reset-values" />
	</aui:button-row>
</aui:fieldset>