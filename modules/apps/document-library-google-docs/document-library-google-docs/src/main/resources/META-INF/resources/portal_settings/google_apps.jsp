<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/portal_settings/init.jsp" %>

<h3><liferay-ui:message key="google-apps" /></h3>

<%
PortletPreferences companyPortletPreferences = PrefsPropsUtil.getPreferences(company.getCompanyId());
%>

<aui:fieldset>
	<aui:input label='<%= ResourceBundleUtil.getString(resourceBundle, "google-apps-api-key") %>' localizeLabel="<%= false %>" name="settings--googleAppsAPIKey--" type="text" value='<%= PrefsParamUtil.getString(companyPortletPreferences, request, "googleAppsAPIKey") %>' />

	<aui:input label='<%= ResourceBundleUtil.getString(resourceBundle, "google-client-id") %>' localizeLabel="<%= false %>" name="settings--googleClientId--" type="text" value='<%= PrefsParamUtil.getString(companyPortletPreferences, request, "googleClientId") %>' />
</aui:fieldset>