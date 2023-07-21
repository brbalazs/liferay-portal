<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/dynamic_include/init.jsp" %>

<%
FacebookConnectConfiguration facebookConnectConfiguration = ConfigurationProviderUtil.getConfiguration(FacebookConnectConfiguration.class, new ParameterMapSettingsLocator(request.getParameterMap(), PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE, new CompanyServiceSettingsLocator(company.getCompanyId(), FacebookConnectConstants.SERVICE_NAME)));

boolean authEnabled = facebookConnectConfiguration.enabled();
boolean verifiedAccountRequired = facebookConnectConfiguration.verifiedAccountRequired();
String appId = facebookConnectConfiguration.appId();

String appSecret = facebookConnectConfiguration.appSecret();

if (Validator.isNotNull(appSecret)) {
	appSecret = Portal.TEMP_OBFUSCATION_VALUE;
}

String graphURL = facebookConnectConfiguration.graphURL();
String oauthAuthURL = facebookConnectConfiguration.oauthAuthURL();
String oauthTokenURL = facebookConnectConfiguration.oauthTokenURL();
String oauthRedirectURL = facebookConnectConfiguration.oauthRedirectURL();
%>

<liferay-ui:error key="facebookConnectGraphURLInvalid" message="the-facebook-connect-graph-url-is-invalid" />
<liferay-ui:error key="facebookConnectOauthAuthURLInvalid" message="the-facebook-connect-oauth-auth-url-is-invalid" />
<liferay-ui:error key="facebookConnectOauthRedirectURLInvalid" message="the-facebook-connect-oauth-redirect-url-is-invalid" />
<liferay-ui:error key="facebookConnectOauthTokenURLInvalid" message="the-facebook-connect-oauth-token-url-is-invalid" />

<aui:fieldset>
	<aui:input id='<%= PortalUtil.generateRandomKey(request, "portal_settings_authentication_facebook") %>' name="<%= ActionRequest.ACTION_NAME %>" type="hidden" value="/portal_settings/facebook_connect" />

	<aui:input label="enabled" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "enabled" %>' type="checkbox" value="<%= authEnabled %>" />

	<aui:input label="require-verified-account" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "verifiedAccountRequired" %>' type="checkbox" value="<%= verifiedAccountRequired %>" />

	<aui:input cssClass="lfr-input-text-container" label="application-id" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "appId" %>' type="text" value="<%= appId %>" />

	<aui:input cssClass="lfr-input-text-container" label="application-secret" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "appSecret" %>' type="password" value="<%= appSecret %>" />

	<aui:input cssClass="lfr-input-text-container" label="graph-url" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "graphURL" %>' type="text" value="<%= graphURL %>" />

	<aui:input cssClass="lfr-input-text-container" label="oauth-authentication-url" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "oauthAuthURL" %>' type="text" value="<%= oauthAuthURL %>" />

	<aui:input cssClass="lfr-input-text-container" label="oauth-token-url" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "oauthTokenURL" %>' type="text" value="<%= oauthTokenURL %>" />

	<aui:input cssClass="lfr-input-text-container" label="oauth-redirect-url" name='<%= PortalSettingsFacebookConnectConstants.FORM_PARAMETER_NAMESPACE + "oauthRedirectURL" %>' type="text" value="<%= oauthRedirectURL %>" />

	<aui:button-row>
		<portlet:actionURL name="/portal_settings/facebook_connect_delete" var="resetValuesURL">
			<portlet:param name="tabs1" value="facebook_connect" />
		</portlet:actionURL>

		<%
		String taglibOnClick = "if (confirm('" + UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-reset-the-configured-values") + "')) {submitForm(document.hrefFm, '" + resetValuesURL.toString() + "');}";
		%>

		<aui:button onClick="<%= taglibOnClick %>" value="reset-values" />
	</aui:button-row>
</aui:fieldset>