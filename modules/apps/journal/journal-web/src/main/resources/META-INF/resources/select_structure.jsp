<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
JournalSelectDDMStructureDisplayContext journalSelectDDMStructureDisplayContext = new JournalSelectDDMStructureDisplayContext(renderRequest, renderResponse);

SearchContainer<DDMStructure> structureSearch = journalSelectDDMStructureDisplayContext.getStructureSearch();
%>

<clay:management-toolbar
	clearResultsURL="<%= journalSelectDDMStructureDisplayContext.getClearResultsURL() %>"
	disabled="<%= journalSelectDDMStructureDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= journalSelectDDMStructureDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= journalSelectDDMStructureDisplayContext.getTotalItems() %>"
	searchActionURL="<%= journalSelectDDMStructureDisplayContext.getSearchActionURL() %>"
	searchFormName="searchForm"
	selectable="<%= false %>"
	sortingOrder="<%= journalSelectDDMStructureDisplayContext.getOrderByType() %>"
	sortingURL="<%= journalSelectDDMStructureDisplayContext.getSortingURL() %>"
/>

<aui:form cssClass="container-fluid-1280" method="post" name="selectStructureFm">
	<liferay-ui:search-container
		searchContainer="<%= structureSearch %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.dynamic.data.mapping.model.DDMStructure"
			keyProperty="structureId"
			modelVar="structure"
		>
			<liferay-ui:search-container-column-text
				name="id"
				value="<%= String.valueOf(structure.getStructureId()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="name"
			>
				<c:choose>
					<c:when test="<%= structure.getStructureId() != journalSelectDDMStructureDisplayContext.getClassPK() %>">
						<aui:a
							cssClass="selector-button"
							data='<%=
								HashMapBuilder.<String, Object>put(
									"ddmstructureid", structure.getStructureId()
								).put(
									"ddmstructurekey", structure.getStructureKey()
								).put(
									"name", HtmlUtil.escape(structure.getName(locale))
								).build()
							%>'
							href="javascript:;"
						>
							<%= HtmlUtil.escape(structure.getUnambiguousName(structureSearch.getResults(), themeDisplay.getScopeGroupId(), locale)) %>
						</aui:a>
					</c:when>
					<c:otherwise>
						<%= HtmlUtil.escape(structure.getUnambiguousName(structureSearch.getResults(), themeDisplay.getScopeGroupId(), locale)) %>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="description"
				truncate="<% true %>"
				value="<%= HtmlUtil.escape(structure.getDescription(locale)) %>"
			/>

			<liferay-ui:search-container-column-date
				name="modified-date"
				value="<%= structure.getModifiedDate() %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.selectEntityHandler('#<portlet:namespace />selectStructureFm', '<%= HtmlUtil.escapeJS(journalSelectDDMStructureDisplayContext.getEventName()) %>');
</aui:script>