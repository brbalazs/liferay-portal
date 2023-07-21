<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<clay:management-toolbar
	clearResultsURL="<%= fragmentItemSelectorViewDisplayContext.getClearResultsURL() %>"
	componentId="fragmentItemSelectorFragmentCollectionsManagementToolbar"
	filterDropdownItems="<%= fragmentItemSelectorViewDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= fragmentItemSelectorViewDisplayContext.getFragmentCollectionsTotalItems() %>"
	searchActionURL="<%= fragmentItemSelectorViewDisplayContext.getSearchActionURL() %>"
	searchFormName="searchFm"
	selectable="<%= false %>"
	sortingOrder="<%= fragmentItemSelectorViewDisplayContext.getOrderByType() %>"
	sortingURL="<%= fragmentItemSelectorViewDisplayContext.getSortingURL() %>"
/>

<aui:form cssClass="container-fluid-1280" name="fm">
	<liferay-ui:search-container
		searchContainer="<%= fragmentItemSelectorViewDisplayContext.getFragmentCollectionsSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.fragment.model.FragmentCollection"
			keyProperty="fragmentCollectionId"
			modelVar="fragmentCollection"
		>

			<%
			PortletURL rowURL = fragmentItemSelectorViewDisplayContext.getPortletURL();

			rowURL.setParameter("fragmentCollectionId", String.valueOf(fragmentCollection.getFragmentCollectionId()));

			row.setCssClass("entry-card lfr-asset-folder");
			%>

			<liferay-ui:search-container-column-text>
				<liferay-ui:search-container-column-text
					colspan="<%= 2 %>"
				>
					<liferay-frontend:horizontal-card
						resultRow="<%= row %>"
						text="<%= HtmlUtil.escape(fragmentCollection.getName()) %>"
						url="<%= rowURL.toString() %>"
					>
						<liferay-frontend:horizontal-card-col>
							<liferay-frontend:horizontal-card-icon
								icon="documents-and-media"
							/>
						</liferay-frontend:horizontal-card-col>
					</liferay-frontend:horizontal-card>
				</liferay-ui:search-container-column-text>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="icon"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>