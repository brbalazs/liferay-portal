<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
PortletURL portletURL = ddmDataProviderDisplayContext.getPortletURL();
%>

<clay:management-toolbar
	actionDropdownItems="<%= ddmDataProviderDisplayContext.getActionItemsDropdownItems() %>"
	clearResultsURL="<%= ddmDataProviderDisplayContext.getClearResultsURL() %>"
	componentId="ddmDataProviderManagementToolbar"
	creationMenu="<%= ddmDataProviderDisplayContext.getCreationMenu() %>"
	disabled="<%= ddmDataProviderDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= ddmDataProviderDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= ddmDataProviderDisplayContext.getTotalItems() %>"
	namespace="<%= renderResponse.getNamespace() %>"
	searchActionURL="<%= portletURL.toString() %>"
	searchContainerId="<%= ddmDataProviderDisplayContext.getSearchContainerId() %>"
	searchFormName="fm1"
	sortingOrder="<%= ddmDataProviderDisplayContext.getOrderByType() %>"
	sortingURL="<%= ddmDataProviderDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= ddmDataProviderDisplayContext.getViewTypesItems() %>"
/>

<aui:script>
	var deleteDataProviderInstances = function() {
		if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") %>')) {
			var form = AUI.$(document.<portlet:namespace />searchContainerForm);

			var searchContainer = AUI.$('#<portlet:namespace />dataProviderInstance', form);

			form.attr('method', 'post');
			form.fm('deleteDataProviderInstanceIds').val(Liferay.Util.listCheckedExcept(searchContainer, '<portlet:namespace />allRowIds'));

			submitForm(form, '<portlet:actionURL name="deleteDataProvider"><portlet:param name="mvcPath" value="/view.jsp" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
		}
	}
	var ACTIONS = {
		'deleteDataProviderInstances': deleteDataProviderInstances
	};

	Liferay.componentReady('ddmDataProviderManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				['actionItemClicked'],
				function(event) {
					var itemData = event.data.item.data;

					if (itemData && itemData.action && ACTIONS[itemData.action]) {
						ACTIONS[itemData.action]();
					}
				}
			);
		}
	);
</aui:script>