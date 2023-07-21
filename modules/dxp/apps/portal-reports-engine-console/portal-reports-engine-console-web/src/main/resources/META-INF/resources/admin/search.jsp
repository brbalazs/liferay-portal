<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<c:choose>
	<c:when test="<%= hasAddSourcePermission && reportsEngineDisplayContext.isSourcesTabSelected() %>">
		<liferay-ui:search-form
			page="/admin/data_source/source_search.jsp"
			servletContext="<%= application %>"
		/>
	</c:when>
	<c:when test="<%= hasAddDefinitionPermission && reportsEngineDisplayContext.isDefinitionsTabSelected() %>">
		<liferay-ui:search-form
			page="/admin/definition/definition_search.jsp"
			servletContext="<%= application %>"
		/>
	</c:when>
	<c:otherwise>
		<liferay-ui:search-form
			page="/admin/report/entry_search.jsp"
			servletContext="<%= application %>"
		/>
	</c:otherwise>
</c:choose>