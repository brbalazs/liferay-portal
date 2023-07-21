<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
FragmentCollectionsDisplayContext fragmentCollectionsDisplayContext = new FragmentCollectionsDisplayContext(renderRequest, renderResponse, request);
%>

<clay:management-toolbar
	clearResultsURL="<%= fragmentCollectionsDisplayContext.getClearResultsURL() %>"
	filterDropdownItems="<%= fragmentCollectionsDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= fragmentCollectionsDisplayContext.getTotalItems() %>"
	searchActionURL="<%= fragmentCollectionsDisplayContext.getSearchActionURL() %>"
	searchContainerId="fragmentCollections"
	searchFormName="searchFm"
	sortingOrder="<%= fragmentCollectionsDisplayContext.getOrderByType() %>"
	sortingURL="<%= fragmentCollectionsDisplayContext.getSortingURL() %>"
/>

<aui:form cssClass="container-fluid-1280" name="fm">
	<liferay-ui:search-container
		id="fragmentCollections"
		searchContainer="<%= fragmentCollectionsDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.fragment.model.FragmentCollection"
			keyProperty="fragmentCollectionId"
			modelVar="fragmentCollection"
		>
			<liferay-ui:search-container-column-text
				name="name"
				truncate="<%= true %>"
				value="<%= HtmlUtil.escape(fragmentCollection.getName()) %>"
			/>

			<liferay-ui:search-container-column-date
				name="create-date"
				property="createDate"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="list"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script use="liferay-search-container">
	var searchContainer = Liferay.SearchContainer.get('<portlet:namespace />fragmentCollections');

	searchContainer.on(
		'rowToggled',
		function(event) {
			Liferay.Util.getOpener().Liferay.fire(
				'<%= HtmlUtil.escapeJS(fragmentCollectionsDisplayContext.getEventName()) %>',
				{
					data: event.elements.allSelectedElements.getDOMNodes()
				}
			);
		}
	);
</aui:script>