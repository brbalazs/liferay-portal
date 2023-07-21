<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
Collection<DynamicInclude> dynamicIncludes = (Collection)request.getAttribute(PortalSettingsWebKeys.AUTHENTICATION_DYNAMIC_INCLUDES);

String[] tabsNames = {"general"};

tabsNames = ArrayUtil.append(tabsNames, PropsValues.COMPANY_SETTINGS_FORM_AUTHENTICATION);

tabsNames = ArrayUtil.append(tabsNames, (String)request.getAttribute(PortalSettingsWebKeys.AUTHENTICATION_TABS_NAMES));
%>

<liferay-ui:error-marker
	key="<%= WebKeys.ERROR_SECTION %>"
	value="authentication"
/>

<h3><liferay-ui:message key="authentication" /></h3>

<liferay-ui:tabs
	names="<%= StringUtil.merge(tabsNames) %>"
	refresh="<%= false %>"
>
	<liferay-ui:section>
		<liferay-util:include page="/authentication/general.jsp" portletId="<%= portletDisplay.getRootPortletId() %>" />
	</liferay-ui:section>

	<%
	for (String section : PropsValues.COMPANY_SETTINGS_FORM_AUTHENTICATION) {
	%>

		<liferay-ui:section>
			<liferay-util:include page='<%= "/html/portlet/portal_settings/authentication/" + _getSectionJsp(section) + ".jsp" %>' portletId="<%= PortletKeys.PORTAL %>" />
		</liferay-ui:section>

	<%
	}

	for (DynamicInclude dynamicInclude : dynamicIncludes) {
	%>

		<liferay-ui:section>

			<%
			dynamicInclude.include(request, PipingServletResponse.createPipingServletResponse(pageContext), null);
			%>

		</liferay-ui:section>

	<%
	}
	%>

</liferay-ui:tabs>

<%!
private String _getSectionJsp(String name) {
	return TextFormatter.format(name, TextFormatter.N);
}
%>