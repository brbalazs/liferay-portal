<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/message_boards/init.jsp" %>

<%
long categoryId = GetterUtil.getLong(request.getAttribute("view.jsp-categoryId"));

MBCategoryDisplay categoryDisplay = new MBCategoryDisplay(scopeGroupId, categoryId);
%>

<liferay-ui:search-container
	searchContainer='<%= (SearchContainer)request.getAttribute("view.jsp-categoryEntriesSearchContainer") %>'
>
	<liferay-ui:search-container-row
		className="com.liferay.message.boards.model.MBCategory"
		escapedModel="<%= true %>"
		keyProperty="categoryId"
		modelVar="category"
	>

		<%
		row.setPrimaryKey(String.valueOf(category.getCategoryId()));
		%>

		<liferay-portlet:renderURL varImpl="rowURL">
			<portlet:param name="mvcRenderCommandName" value="/message_boards/view_category" />
			<portlet:param name="mbCategoryId" value="<%= String.valueOf(category.getCategoryId()) %>" />
		</liferay-portlet:renderURL>

		<liferay-ui:search-container-column-icon
			icon="folder"
			toggleRowChecker="<%= true %>"
		/>

		<liferay-ui:search-container-column-text
			colspan="<%= 2 %>"
		>
			<h4>
				<aui:a href="<%= rowURL.toString() %>">
					<%= category.getName() %>
				</aui:a>
			</h4>

			<h5 class="text-default">
				<%= category.getDescription() %>
			</h5>

			<%
			int subcategoriesCount = categoryDisplay.getSubcategoriesCount(category);
			int threadsCount = categoryDisplay.getSubcategoriesThreadsCount(category);
			%>

			<span class="h6 text-default">
				<liferay-ui:message arguments="<%= subcategoriesCount %>" key='<%= (subcategoriesCount == 1) ? "x-subcategory" : "x-subcategories" %>' />
			</span>
			<span class="h6 text-default">
				<liferay-ui:message arguments="<%= threadsCount %>" key='<%= (threadsCount == 1) ? "x-thread" : "x-threads" %>' />
			</span>
		</liferay-ui:search-container-column-text>

		<liferay-ui:search-container-column-jsp
			path="/message_boards/category_action.jsp"
		/>
	</liferay-ui:search-container-row>

	<liferay-ui:search-iterator
		displayStyle="descriptive"
		markupView="lexicon"
		resultRowSplitter="<%= new MBResultRowSplitter() %>"
	/>
</liferay-ui:search-container>