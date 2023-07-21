<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
DefinitionDisplayTerms displayTerms = new DefinitionDisplayTerms(renderRequest);
%>

<liferay-ui:search-toggle
	buttonLabel="search"
	displayTerms="<%= displayTerms %>"
	id="toggle_id_reports_definition_search"
	markupView="lexicon"
>
	<aui:fieldset>
		<aui:input name="<%= DefinitionDisplayTerms.DEFINITION_NAME %>" size="20" value="<%= displayTerms.getDefinitionName() %>" />

		<aui:select label="data-source-name" name="<%= DefinitionDisplayTerms.SOURCE_ID %>">
			<aui:option label="all" />
			<aui:option label="<%= ReportDataSourceType.PORTAL %>" value="0" />

			<%
			List<Source> sources = SourceServiceUtil.getSources(themeDisplay.getSiteGroupId(), null, null, false, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

			for (Source source : sources) {
			%>

				<aui:option label="<%= HtmlUtil.escape(source.getName(locale)) %>" value="<%= source.getSourceId() %>" />

			<%
			}
			%>

		</aui:select>

		<aui:input name="<%= DefinitionDisplayTerms.DESCRIPTION %>" size="20" value="<%= displayTerms.getDescription() %>" />

		<aui:input label="template" name="<%= DefinitionDisplayTerms.REPORT_NAME %>" size="20" value="<%= displayTerms.getReportName() %>" />
	</aui:fieldset>
</liferay-ui:search-toggle>