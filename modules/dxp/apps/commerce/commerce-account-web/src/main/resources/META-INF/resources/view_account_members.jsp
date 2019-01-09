<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
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

<div class="minium-frame__cta is-visible">
	<aui:button 
	cssClass="minium-button minium-button--big js-invite-user"
	onClick='<%= renderResponse.getNamespace() + "openUserInvitation();" %>'  value="invite-user" />
</div>

<portlet:actionURL name="inviteUser" var="inviteUserActionURL" />

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

<commerce-ui:user-invitation
	componentId="userInvitation"
/>

<aui:script>

	Liferay.provide(
		window,
		'<portlet:namespace />openUserInvitation',
		function(evt) {
			console.log(evt)
			const userInvitation = Liferay.component('userInvitation');
			console.log(userInvitation)
			userInvitation.open();
		}
	);

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

	Liferay.componentReady('userInvitation')
		.then(
			userInvitation => {
				return userInvitation.on(
					'userInvitationSave',
					(event) => {
						console.log(event)
					}
				);
			}
		);

</aui:script>