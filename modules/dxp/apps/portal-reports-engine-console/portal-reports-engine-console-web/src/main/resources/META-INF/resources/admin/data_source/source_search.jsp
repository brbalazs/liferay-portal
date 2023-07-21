<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
SourceDisplayTerms displayTerms = new SourceDisplayTerms(renderRequest);
%>

<liferay-ui:search-toggle
	buttonLabel="search"
	displayTerms="<%= displayTerms %>"
	id="toggle_id_reports_source_search"
	markupView="lexicon"
>
	<aui:fieldset>
		<aui:input label="source-name" name="<%= SourceDisplayTerms.NAME %>" size="20" value="<%= displayTerms.getName() %>" />

		<aui:input label="jdbc-url" name="<%= SourceDisplayTerms.DRIVER_URL %>" size="20" value="<%= displayTerms.getDriverUrl() %>" />
	</aui:fieldset>
</liferay-ui:search-toggle>