<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
Long companyId = ParamUtil.getLong(request, "companyId");
String themeId = ParamUtil.getString(request, "themeId");

Theme selTheme = null;

Layout selLayout = layoutsAdminDisplayContext.getSelLayout();

if (Validator.isNotNull(themeId) && Validator.isNotNull(companyId)) {
	selTheme = ThemeLocalServiceUtil.getTheme(companyId, themeId);
}
else if (selLayout == null) {
	selTheme = selLayout.getTheme();
}

PluginPackage selPluginPackage = selTheme.getPluginPackage();
%>

<h5 class="text-default"><liferay-ui:message key="current-theme" /></h5>

<div class="card-horizontal main-content-card">
	<div class="card-row card-row-padded">
		<aui:row>
			<aui:col span="<%= 2 %>">
				<img alt="<%= HtmlUtil.escapeAttribute(selTheme.getName()) %>" class="img-thumbnail theme-screenshot" src="<%= themeDisplay.getCDNBaseURL() %><%= HtmlUtil.escapeAttribute(selTheme.getStaticResourcePath()) %><%= HtmlUtil.escapeAttribute(selTheme.getImagesPath()) %>/thumbnail.png" title="<%= HtmlUtil.escapeAttribute(selTheme.getName()) %>" />
			</aui:col>

			<aui:col span="<%= 10 %>">
				<c:if test="<%= (selPluginPackage != null) && Validator.isNotNull(selPluginPackage.getName()) %>">
					<h4><liferay-ui:message key="name" /></h4>

					<p class="text-default">
						<%= HtmlUtil.escape(selPluginPackage.getName()) %>
					</p>
				</c:if>

				<c:if test="<%= (selPluginPackage != null) && Validator.isNotNull(selPluginPackage.getAuthor()) %>">
					<h4><liferay-ui:message key="author" /></h4>

					<p class="text-default">
						<aui:a href="<%= HtmlUtil.escapeHREF(selPluginPackage.getPageURL()) %>" target="_blank"><%= HtmlUtil.escape(selPluginPackage.getAuthor()) %></aui:a>
					</p>
				</c:if>
			</aui:col>
		</aui:row>

		<c:if test="<%= (selPluginPackage != null) && Validator.isNotNull(selPluginPackage.getShortDescription()) %>">
			<h4><liferay-ui:message key="description" /></h4>

			<p class="text-default">
				<%= HtmlUtil.escape(selPluginPackage.getShortDescription()) %>
			</p>
		</c:if>

		<%
		ColorScheme selColorScheme = selLayout.getColorScheme();

		List<ColorScheme> colorSchemes = selTheme.getColorSchemes();
		%>

		<c:if test="<%= !colorSchemes.isEmpty() && (selColorScheme != null) %>">
			<h4><liferay-ui:message key="color-scheme" /></h4>

			<img alt="" class="img-thumbnail theme-screenshot" src="<%= themeDisplay.getCDNBaseURL() %><%= HtmlUtil.escapeAttribute(selTheme.getStaticResourcePath()) %><%= HtmlUtil.escapeAttribute(selColorScheme.getColorSchemeThumbnailPath()) %>/thumbnail.png" title="<%= HtmlUtil.escapeAttribute(selColorScheme.getName()) %>" />
		</c:if>

		<%
		Map<String, ThemeSetting> configurableSettings = selTheme.getConfigurableSettings();
		%>

		<c:if test="<%= !configurableSettings.isEmpty() %>">
			<h4><liferay-ui:message key="settings" /></h4>

			<%
			LayoutSet selLayoutSet = layoutsAdminDisplayContext.getSelLayoutSet();

			for (String name : configurableSettings.keySet()) {
			%>

				<p class="text-default"><liferay-ui:message key="<%= HtmlUtil.escape(name) %>" />: <%= HtmlUtil.escape(selLayoutSet.getThemeSetting(name, "regular")) %></p>

			<%
			}
			%>

		</c:if>
	</div>
</div>