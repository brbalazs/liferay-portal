<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/admin/init.jsp" %>

<clay:management-toolbar
	actionDropdownItems="<%= ddmFormAdminDisplayContext.getActionItemsDropdownItems() %>"
	clearResultsURL="<%= ddmFormAdminDisplayContext.getClearResultsURL() %>"
	componentId="ddmFormManagementToolbar"
	creationMenu="<%= ddmFormAdminDisplayContext.getCreationMenu() %>"
	disabled="<%= ddmFormAdminDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= ddmFormAdminDisplayContext.getFilterItemsDropdownItems() %>"
	itemsTotal="<%= ddmFormAdminDisplayContext.getTotalItems() %>"
	namespace="<%= renderResponse.getNamespace() %>"
	searchActionURL="<%= ddmFormAdminDisplayContext.getSearchActionURL() %>"
	searchContainerId="<%= ddmFormAdminDisplayContext.getSearchContainerId() %>"
	searchFormName="fm1"
	sortingOrder="<%= ddmFormAdminDisplayContext.getOrderByType() %>"
	sortingURL="<%= ddmFormAdminDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= ddmFormAdminDisplayContext.getViewTypesItems() %>"
/>

<aui:script sandbox="<%= true %>">
	var deleteFormInstances = function() {
		if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") %>')) {
			var form = AUI.$(document.<portlet:namespace />searchContainerForm);

			var searchContainer = AUI.$('#<portlet:namespace /><%= ddmFormAdminDisplayContext.getSearchContainerId() %>', form);

			form.attr('method', 'post');
			form.fm('deleteFormInstanceIds').val(Liferay.Util.listCheckedExcept(searchContainer, '<portlet:namespace />allRowIds'));

			submitForm(form, '<portlet:actionURL name="deleteFormInstance"><portlet:param name="mvcPath" value="/admin/view.jsp" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
		}
	}

	var deleteStructures = function() {
		if (confirm('<%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-this") %>')) {
			var form = AUI.$(document.<portlet:namespace />searchContainerForm);

			var searchContainer = AUI.$('#<portlet:namespace /><%= ddmFormAdminDisplayContext.getSearchContainerId() %>', form);

			form.attr('method', 'post');
			form.fm('deleteStructureIds').val(Liferay.Util.listCheckedExcept(searchContainer, '<portlet:namespace />allRowIds'));

			submitForm(form, '<portlet:actionURL name="deleteStructure"><portlet:param name="mvcPath" value="/admin/view.jsp" /><portlet:param name="currentTab" value="element-set" /><portlet:param name="redirect" value="<%= currentURL %>" /></portlet:actionURL>');
		}
	}

	var ACTIONS = {
		'deleteFormInstances': deleteFormInstances,
		'deleteStructures': deleteStructures
	};

	Liferay.componentReady('ddmFormManagementToolbar').then(
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