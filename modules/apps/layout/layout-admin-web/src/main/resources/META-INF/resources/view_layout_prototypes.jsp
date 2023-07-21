<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<clay:navigation-bar
	inverted="<%= true %>"
	navigationItems="<%= layoutsAdminDisplayContext.getNavigationItems() %>"
/>

<clay:management-toolbar
	actionDropdownItems="<%= layoutPrototypeDisplayContext.getActionDropdownItems() %>"
	componentId="layoutPrototypeManagementToolbar"
	creationMenu="<%= layoutPrototypeDisplayContext.isShowAddButton() ? layoutPrototypeDisplayContext.getCreationMenu() : null %>"
	disabled="<%= layoutPrototypeDisplayContext.isDisabledManagementBar() %>"
	filterDropdownItems="<%= layoutPrototypeDisplayContext.getFilterDropdownItems() %>"
	itemsTotal="<%= layoutPrototypeDisplayContext.getTotalItems() %>"
	searchContainerId="layoutPrototype"
	showSearch="<%= false %>"
	sortingOrder="<%= layoutPrototypeDisplayContext.getOrderByType() %>"
	sortingURL="<%= layoutPrototypeDisplayContext.getSortingURL() %>"
/>

<portlet:actionURL name="/layout_prototype/delete_layout_prototype" var="deleteLayoutPrototypesURL">
	<portlet:param name="redirect" value="<%= currentURL %>" />
</portlet:actionURL>

<aui:form action="<%= deleteLayoutPrototypesURL %>" cssClass="container-fluid-1280" name="fm">
	<liferay-ui:error embed="<%= false %>" exception="<%= RequiredLayoutPrototypeException.class %>" message="you-cannot-delete-page-templates-that-are-used-by-a-page" />

	<liferay-ui:search-container
		searchContainer="<%= layoutPrototypeDisplayContext.getSearchContainer() %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.layout.page.template.model.LayoutPageTemplateEntry"
			cssClass="selectable"
			escapedModel="<%= true %>"
			keyProperty="layoutPrototypeId"
			modelVar="layoutPageTemplateEntry"
		>

			<%
			LayoutPrototype layoutPrototype = LayoutPrototypeServiceUtil.getLayoutPrototype(layoutPageTemplateEntry.getLayoutPrototypeId());

			Group layoutPrototypeGroup = layoutPrototype.getGroup();

			row.setCssClass("entry-card lfr-asset-item");

			request.setAttribute(LayoutAdminWebKeys.LAYOUT_PROTOTYPE, layoutPrototype);
			%>

			<liferay-ui:search-container-column-text>
				<liferay-frontend:icon-vertical-card
					actionJsp="/layout_prototype_action.jsp"
					actionJspServletContext="<%= application %>"
					cssClass="entry-display-style"
					icon="page-template"
					resultRow="<%= row %>"
					rowChecker="<%= searchContainer.getRowChecker() %>"
					title="<%= layoutPrototype.getName(locale) %>"
					url="<%= layoutPrototypeGroup.getDisplayURL(themeDisplay, true) %>"
				>
					<liferay-frontend:vertical-card-header>

						<%
						Date createDate = layoutPrototype.getModifiedDate();
						%>

						<label class="text-default">
							<liferay-ui:message arguments="<%= LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - createDate.getTime(), true) %>" key="created-x-ago" />
						</label>
					</liferay-frontend:vertical-card-header>

					<liferay-frontend:vertical-card-footer>
						<label class="text-default">
							<c:choose>
								<c:when test="<%= layoutPrototype.isActive() %>">
									<liferay-ui:message key="active" />
								</c:when>
								<c:otherwise>
									<liferay-ui:message key="not-active" />
								</c:otherwise>
							</c:choose>
						</label>
					</liferay-frontend:vertical-card-footer>
				</liferay-frontend:icon-vertical-card>
			</liferay-ui:search-container-column-text>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			displayStyle="icon"
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</aui:form>

<aui:script sandbox="<%= true %>">
	var deleteSelectedLayoutPrototypes = function() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-delete-this" />')) {
			submitForm($(document.<portlet:namespace />fm));
		}
	}

	var ACTIONS = {
		'deleteSelectedLayoutPrototypes': deleteSelectedLayoutPrototypes
	};

	Liferay.componentReady('layoutPrototypeManagementToolbar').then(
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
		}
	);
</aui:script>