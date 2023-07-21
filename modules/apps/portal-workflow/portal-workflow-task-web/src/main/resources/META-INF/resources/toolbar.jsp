<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(renderRequest, "tabs1", "assigned-to-me");

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcPath", "/view.jsp");
portletURL.setParameter("tabs1", tabs1);
%>

<aui:nav-bar cssClass="collapse-basic-search" markupView="lexicon">
	<aui:nav cssClass="nav-bar-workflow nav-tabs nav-tabs-default">
		<portlet:renderURL var="viewAssignedToMeURL">
			<portlet:param name="mvcPath" value="/view.jsp" />
			<portlet:param name="tabs1" value="assigned-to-me" />
		</portlet:renderURL>

		<aui:nav-item href="<%= viewAssignedToMeURL %>" label="assigned-to-me" selected='<%= tabs1.equals("assigned-to-me") %>' />

		<portlet:renderURL var="viewAssignedToMyRolesURL">
			<portlet:param name="mvcPath" value="/view.jsp" />
			<portlet:param name="tabs1" value="assigned-to-my-roles" />
		</portlet:renderURL>

		<aui:nav-item href="<%= viewAssignedToMyRolesURL %>" label="assigned-to-my-roles" selected='<%= tabs1.equals("assigned-to-my-roles") %>' />
	</aui:nav>
</aui:nav-bar>

<clay:management-toolbar
	clearResultsURL="<%= workflowTaskDisplayContext.getClearResultsURL() %>"
	filterDropdownItems="<%= workflowTaskDisplayContext.getFilterOptions() %>"
	itemsTotal="<%= workflowTaskDisplayContext.getTotalItems() %>"
	namespace="<%= renderResponse.getNamespace() %>"
	searchActionURL="<%= workflowTaskDisplayContext.getSearchURL() %>"
	searchContainerId="workflowTasks"
	searchFormName="fm1"
	selectable="<%= false %>"
	sortingOrder="<%= workflowTaskDisplayContext.getOrderByType() %>"
	sortingURL="<%= workflowTaskDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= workflowTaskDisplayContext.getViewTypes() %>"
/>