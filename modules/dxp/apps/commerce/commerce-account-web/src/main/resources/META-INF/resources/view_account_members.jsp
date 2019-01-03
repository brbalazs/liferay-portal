<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
CommerceAccountMembersDisplayContext commerceAccountMembersDisplayContext = (CommerceAccountMembersDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceAccount commerceAccount = commerceAccountMembersDisplayContext.getCurrentCommerceAccount();
%>

<aui:form action="<%= String.valueOf(commerceAccountMembersDisplayContext.getPortletURL()) %>" method="post" name="searchFm">
	<liferay-frontend:management-bar
		includeCheckBox="<%= true %>"
		searchContainerId="users"
	>
		<liferay-frontend:management-bar-buttons>
			<liferay-frontend:management-bar-button
				href='<%= "javascript:" + renderResponse.getNamespace() + "toggleFilter(false);" %>'
				iconCssClass="icon-filter"
				id="filterButton"
				label="filter"
			/>

			<liferay-frontend:add-menu
				inline="<%= true %>"
			>
				<liferay-frontend:add-menu-item
					title='<%= LanguageUtil.get(request, "invite-user") %>'
					type="<%= AddMenuKeys.AddMenuType.PRIMARY %>"
					url="<%= commerceAccountMembersDisplayContext.getInviteUserHref() %>"
				/>
			</liferay-frontend:add-menu>
		</liferay-frontend:management-bar-buttons>

		<liferay-frontend:management-bar-filters>
			<li>
				<liferay-portlet:renderURLParams varImpl="searchURL" />

				<liferay-ui:input-search
					markupView="lexicon"
				/>
			</li>
		</liferay-frontend:management-bar-filters>

		<liferay-frontend:management-bar-action-buttons>
			<liferay-frontend:management-bar-button
				href='<%= "javascript:" + renderResponse.getNamespace() + "removeUsers();" %>'
				icon="times"
				label="delete"
			/>
		</liferay-frontend:management-bar-action-buttons>
	</liferay-frontend:management-bar>

	<div class="form-group-autofit hide" id="<portlet:namespace />filterSettings">
		<div class="form-group-item">
			<aui:button cssClass="btn-outline-borderless btn-outline-primary" type="submit" value="apply-filters" />
		</div>
	</div>
</aui:form>

<portlet:actionURL name="inviteUser" var="inviteUserActionURL" />

<aui:form action="<%= inviteUserActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.REMOVE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="commerceAccountId" type="hidden" value="<%= commerceAccount.getCommerceAccountId() %>" />
	<aui:input name="removeUserIds" type="hidden" />

	<div class="container-fluid-1280">
		<commerce-ui:table
			dataProviderKey="commerce-account-users"
			itemPerPage="<%= 5 %>"
			namespace="<%= renderResponse.getNamespace() %>"
			pageNumber="1"
			portletURL="<%= commerceAccountMembersDisplayContext.getPortletURL() %>"
			tableName="commerce-account-users"
		/>
	</div>
</aui:form>

<aui:script>
	function <portlet:namespace />inviteUser(uri) {
		Liferay.Util.openWindow(
			{
				dialog: {
					centered: true,
					destroyOnClose: true,
					height: 600,
					modal: true,
					width: 700
				},
				dialogIframe: {
					bodyCssClass: 'dialog-with-footer'
				},
				id: 'inviteUserDialog',
				title: '<liferay-ui:message key="invite-users" />',
				uri: uri
			}
		);
	}

	function <portlet:namespace />removeUsers() {
		if (confirm('<liferay-ui:message key="are-you-sure-you-want-to-remove-the-selected-users" />')) {
			var form = AUI.$(document.<portlet:namespace />fm);

			form.fm('removeUserIds').val(Liferay.Util.listCheckedExcept(form, '<portlet:namespace />allRowIds'));

			submitForm(form);
		}
	}

	Liferay.provide(
		window,
		'<portlet:namespace />closePopup',
		function(dialogId) {
			var dialog = Liferay.Util.Window.getById(dialogId);

			dialog.destroy();
		},
		['liferay-util-window']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />refreshPortlet',
		function() {
			location.href = '<%= currentURL %>';
		},
		['aui-dialog', 'aui-dialog-iframe']
	);

	Liferay.provide(
		window,
		'<portlet:namespace />toggleFilter',
		function(state) {
			var A = AUI();

			var filterButton = A.one('#<portlet:namespace />filterButton');
			var filterSettings = A.one('#<portlet:namespace />filterSettings');

			if (filterButton && filterSettings) {
				filterButton.toggleClass('active');

				filterSettings.toggle();
			}
		},
		['aui-base']
	);
</aui:script>