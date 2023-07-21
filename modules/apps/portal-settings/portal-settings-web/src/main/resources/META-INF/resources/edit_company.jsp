<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", "/portal_settings/view");

request.setAttribute("addresses.className", Account.class.getName());
request.setAttribute("emailAddresses.className", Account.class.getName());
request.setAttribute("phones.className", Account.class.getName());
request.setAttribute("websites.className", Account.class.getName());

request.setAttribute("addresses.classPK", company.getAccountId());
request.setAttribute("emailAddresses.classPK", company.getAccountId());
request.setAttribute("phones.classPK", company.getAccountId());
request.setAttribute("websites.classPK", company.getAccountId());
%>

<portlet:actionURL name="/portal_settings/edit_company" var="editCompanyURL" />

<aui:form action="<%= editCompanyURL %>" cssClass="container-fluid-1280" method="post" name="fm" onSubmit='<%= "event.preventDefault(); " + renderResponse.getNamespace() + "saveCompany();" %>'>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />

	<liferay-util:buffer
		var="htmlTop"
	>
		<div class="company-info">
			<p class="float-container">
				<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="logo" />" class="company-logo" src="<%= themeDisplay.getPathImage() %>/company_logo?img_id=<%= company.getLogoId() %>&t=<%= WebServerServletTokenUtil.getToken(company.getLogoId()) %>" /><br />

				<span class="company-name"><%= HtmlUtil.escape(company.getName()) %></span>
			</p>
		</div>
	</liferay-util:buffer>

	<liferay-ui:form-navigator
		formModelBean="<%= company %>"
		htmlTop="<%= htmlTop %>"
		id="<%= FormNavigatorConstants.FORM_NAVIGATOR_ID_COMPANY_SETTINGS %>"
		markupView="lexicon"
		showButtons="<%= RoleLocalServiceUtil.hasUserRole(user.getUserId(), company.getCompanyId(), RoleConstants.ADMINISTRATOR, true) %>"
	/>
</aui:form>

<aui:script>
	function <portlet:namespace />saveCompany() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= Constants.CMD %>').val('<%= Constants.UPDATE %>');

		<portlet:namespace />saveLocales();

		submitForm(form);
	}

	function <portlet:namespace />saveLocales() {
		var form = AUI.$(document.<portlet:namespace />fm);

		form.fm('<%= PropsKeys.LOCALES %>').val(Liferay.Util.listSelect(form.fm('currentLanguageIds')));
	}
</aui:script>