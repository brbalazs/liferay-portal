<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<clay:management-toolbar
	actionDropdownItems="<%= journalDisplayContext.getActionDropdownItems() %>"
	clearResultsURL="<%= journalDisplayContext.getClearResultsURL() %>"
	componentId="journalWebManagementToolbar"
	creationMenu="<%= journalDisplayContext.getCreationMenu() %>"
	disabled="<%= journalDisplayContext.isDisabled() %>"
	filterDropdownItems="<%= journalDisplayContext.getFilterDropdownItems() %>"
	filterLabelItems="<%= journalDisplayContext.getFilterLabelItems() %>"
	infoPanelId="infoPanelId"
	itemsTotal="<%= journalDisplayContext.getTotalItems() %>"
	searchActionURL="<%= journalDisplayContext.getSearchActionURL() %>"
	searchContainerId='<%= ParamUtil.getString(request, "searchContainerId") %>'
	searchFormName="fm1"
	showCreationMenu="<%= journalDisplayContext.isShowAddButton() %>"
	showInfoButton="<%= journalDisplayContext.isShowInfoButton() %>"
	showSearch="<%= journalDisplayContext.isShowSearch() %>"
	sortingOrder="<%= journalDisplayContext.getOrderByType() %>"
	sortingURL="<%= journalDisplayContext.getSortingURL() %>"
	viewTypeItems="<%= journalDisplayContext.getViewTypeItems() %>"
/>

<aui:script sandbox="<%= true %>">
	var deleteEntries = function() {
		if (<%= trashHelper.isTrashEnabled(scopeGroupId) %> || confirm(' <%= UnicodeLanguageUtil.get(request, "are-you-sure-you-want-to-delete-the-selected-entries") %>')) {
			Liferay.fire(
				'<%= renderResponse.getNamespace() %>editEntry',
				{
					action: '<%= trashHelper.isTrashEnabled(scopeGroupId) ? "moveEntriesToTrash" : "deleteEntries" %>'
				}
			);
		}
	}

	var expireEntries = function() {
		Liferay.fire(
			'<portlet:namespace />editEntry',
			{
				action: 'expireEntries'
			}
		);
	};

	var moveEntries = function() {
		Liferay.fire(
			'<portlet:namespace />editEntry',
			{
				action: 'moveEntries'
			}
		);
	};

	<portlet:renderURL var="viewDDMStructureArticlesURL">
		<portlet:param name="navigation" value="structure" />
		<portlet:param name="folderId" value="<%= String.valueOf(JournalFolderConstants.DEFAULT_PARENT_FOLDER_ID) %>" />
		<portlet:param name="showEditActions" value="<%= String.valueOf(journalDisplayContext.isShowEditActions()) %>" />
	</portlet:renderURL>

	var openStructuresSelector = function() {
		Liferay.Util.selectEntity(
			{
				dialog: {
					constrain: true,
					modal: true
				},
				eventName: '<portlet:namespace />selectStructure',
				title: '<%= UnicodeLanguageUtil.get(request, "structures") %>',
				uri: '<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/select_structure.jsp" /></portlet:renderURL>'
			},
			function(event) {
				var uri = '<%= viewDDMStructureArticlesURL %>';

				uri = Liferay.Util.addParams('<portlet:namespace />ddmStructureKey=' + event.ddmstructurekey, uri);

				location.href = uri;
			}
		);
	}

	var openViewMoreStructuresSelector = function(event) {
		event.preventDefault();

		Liferay.Util.openWindow(
			{
				dialog: {
					after: {
						destroy: function(event) {
							if (event.target.get('destroyOnHide')) {
								window.location.reload();
							}
						}
					},
					destroyOnHide: true,
					modal: true
				},
				id: '<portlet:namespace />selectAddMenuItem',
				title: '<liferay-ui:message key="more" />',

				<portlet:renderURL var="viewMoreURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
					<portlet:param name="mvcPath" value="/view_more_menu_items.jsp" />
					<portlet:param name="folderId" value="<%= String.valueOf(journalDisplayContext.getFolderId()) %>" />
					<portlet:param name="eventName" value='<%= renderResponse.getNamespace() + "selectAddMenuItem" %>' />
				</portlet:renderURL>

				uri: '<%= viewMoreURL %>'
			}
		);
	}

	var ACTIONS = {
		'deleteEntries': deleteEntries,
		'expireEntries': expireEntries,
		'moveEntries': moveEntries,
		'openStructuresSelector': openStructuresSelector,
		'openViewMoreStructuresSelector': openViewMoreStructuresSelector
	};

	Liferay.componentReady('journalWebManagementToolbar').then(
		function(managementToolbar) {
			managementToolbar.on(
				['actionItemClicked', 'filterItemClicked'],
				function(event) {
					var itemData = event.data.item.data;

					if (itemData && itemData.action && ACTIONS[itemData.action]) {
						ACTIONS[itemData.action]();
					}
				}
			);

			managementToolbar.on('creationMenuMoreButtonClicked', openViewMoreStructuresSelector);
		}
	);

	<portlet:renderURL var="addArticleURL">
		<portlet:param name="mvcPath" value="/edit_article.jsp" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="groupId" value="<%= String.valueOf(scopeGroupId) %>" />
		<portlet:param name="folderId" value="<%= String.valueOf(journalDisplayContext.getFolderId()) %>" />
	</portlet:renderURL>

	Liferay.on(
		'<portlet:namespace />selectAddMenuItem',
		function(event) {
			const selectAddMenuItemWindow = Liferay.Util.Window.getById('<portlet:namespace />selectAddMenuItem');

			selectAddMenuItemWindow.set('destroyOnHide', false);

			Liferay.fire(
				'closeWindow',
				{
					id: '<portlet:namespace />selectAddMenuItem',
					redirect: Liferay.Util.addParams('<portlet:namespace />ddmStructureKey=' + event.ddmStructureKey, '<%= addArticleURL %>')
				}
			);
		}
	);
</aui:script>