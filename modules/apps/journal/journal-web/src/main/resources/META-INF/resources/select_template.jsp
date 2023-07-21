<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
JournalSelectDDMTemplateDisplayContext journalSelectDDMTemplateDisplayContext = new JournalSelectDDMTemplateDisplayContext(renderRequest, renderResponse);
%>

<clay:management-toolbar
	clearResultsURL="<%= journalSelectDDMTemplateDisplayContext.getClearResultsURL() %>"
	disabled="<%= journalSelectDDMTemplateDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= journalSelectDDMTemplateDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= journalSelectDDMTemplateDisplayContext.getTotalItems() %>"
	searchActionURL="<%= journalSelectDDMTemplateDisplayContext.getSearchActionURL() %>"
	searchFormName="searchForm"
	selectable="<%= false %>"
	sortingOrder="<%= journalSelectDDMTemplateDisplayContext.getOrderByType() %>"
	sortingURL="<%= journalSelectDDMTemplateDisplayContext.getSortingURL() %>"
/>

<aui:form cssClass="container-fluid-1280" method="post" name="selectTemplateFm">
	<liferay-ui:search-container
		searchContainer="<%= journalSelectDDMTemplateDisplayContext.getTemplateSearch() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.dynamic.data.mapping.model.DDMTemplate"
			keyProperty="templateId"
			modelVar="template"
		>
			<liferay-ui:search-container-column-text
				name="id"
				value="<%= String.valueOf(template.getTemplateId()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="name"
			>
				<c:choose>
					<c:when test="<%= template.getTemplateId() != journalSelectDDMTemplateDisplayContext.getTemplateId() %>">

						<%
						Map<String, Object> data = new HashMap<>();

						data.put("ddmtemplateid", template.getTemplateId());
						data.put("ddmtemplatekey", template.getTemplateKey());
						data.put("description", template.getDescription(locale));
						data.put("imageurl", template.getTemplateImageURL(themeDisplay));
						data.put("name", template.getName(locale));
						%>

						<aui:a cssClass="selector-button" data="<%= data %>" href="javascript:;">
							<%= HtmlUtil.escape(template.getName(locale)) %>
						</aui:a>
					</c:when>
					<c:otherwise>
						<%= HtmlUtil.escape(template.getName(locale)) %>
					</c:otherwise>
				</c:choose>
			</liferay-ui:search-container-column-text>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-content"
				name="description"
				value="<%= HtmlUtil.escape(template.getDescription(locale)) %>"
			/>

			<liferay-ui:search-container-column-date
				name="modified-date"
				value="<%= template.getModifiedDate() %>"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script>
	Liferay.Util.selectEntityHandler('#<portlet:namespace />selectTemplateFm', '<%= HtmlUtil.escapeJS(journalSelectDDMTemplateDisplayContext.getEventName()) %>');
</aui:script>