<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String displayStyle = ParamUtil.getString(request, "displayStyle", "descriptive");
long groupId = ParamUtil.getLong(request, "groupId");
String navigation = ParamUtil.getString(request, "navigation", "all");
String orderByCol = ParamUtil.getString(request, "orderByCol");
String orderByType = ParamUtil.getString(request, "orderByType");
boolean privateLayout = ParamUtil.getBoolean(request, "privateLayout");
String searchContainerId = ParamUtil.getString(request, "searchContainerId");
%>

<div id="<portlet:namespace />importProcessesSearchContainer">
	<liferay-util:include page="/toolbar.jsp" servletContext="<%= application %>">
		<liferay-util:param name="mvcRenderCommandName" value="importLayoutsView" />
		<liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
		<liferay-util:param name="privateLayout" value="<%= String.valueOf(privateLayout) %>" />
		<liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
		<liferay-util:param name="navigation" value="<%= navigation %>" />
		<liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
		<liferay-util:param name="orderByType" value="<%= orderByType %>" />
		<liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
	</liferay-util:include>

	<div class="container-fluid-1280" id="<portlet:namespace />processesContainer">
		<liferay-util:include page="/import/processes_list/import_layouts_processes.jsp" servletContext="<%= application %>">
			<liferay-util:param name="groupId" value="<%= String.valueOf(groupId) %>" />
			<liferay-util:param name="privateLayout" value="<%= String.valueOf(privateLayout) %>" />
			<liferay-util:param name="validate" value="<%= String.valueOf(Boolean.TRUE) %>" />
			<liferay-util:param name="displayStyle" value="<%= displayStyle %>" />
			<liferay-util:param name="navigation" value="<%= navigation %>" />
			<liferay-util:param name="orderByCol" value="<%= orderByCol %>" />
			<liferay-util:param name="orderByType" value="<%= orderByType %>" />
			<liferay-util:param name="searchContainerId" value="<%= searchContainerId %>" />
		</liferay-util:include>
	</div>
</div>