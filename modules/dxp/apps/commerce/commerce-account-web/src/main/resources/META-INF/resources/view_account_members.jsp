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
CommerceAccountDisplayContext commerceAccountDisplayContext = (CommerceAccountDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<commerce-ui:table
	dataProviderKey="commerceAccountUsers"
	itemPerPage="<%= 5 %>"
	namespace="<%= renderResponse.getNamespace() %>"
	pageNumber="1"
	portletURL="<%= commerceAccountDisplayContext.getPortletURL() %>"
	tableName="commerceAccountUsers"
/>

<div class="minium-frame__cta is-visible">
	<aui:button cssClass="js-invite-user minium-button minium-button--big" onClick='<%= renderResponse.getNamespace() + "openUserInvitationModal();" %>' value="invite-user" />
	<aui:button cssClass="js-invite-user minium-button minium-button--big" onClick='<%= renderResponse.getNamespace() + "openUserRolesModal();" %>' value="user-roles" />
</div>

<commerce-ui:user-invitation-modal
	componentId="userInvitationModal"
/>

<commerce-ui:user-roles-modal
	componentId="userRolesModal"
/>

<portlet:actionURL name="inviteUser" var="inviteUserActionURL" />

<aui:form action="<%= inviteUserActionURL %>" method="post" name="inviteUserFm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.ASSIGN %>" />
	<aui:input name="commerceAccountId" type="hidden" value="<%= commerceAccountDisplayContext.getCurrentCommerceAccountId() %>" />
	<aui:input name="userIds" type="hidden" />
	<aui:input name="emailAddresses" type="hidden" />
</aui:form>

<aui:script>

	Liferay.provide(
		window,
		'<portlet:namespace />openUserInvitationModal',
		function(evt) {
			const userInvitationModal = Liferay.component('userInvitationModal');
			userInvitationModal.open();
		}
	);

	Liferay.provide(
		window,
		'<portlet:namespace />openUserRolesModal',
		function(evt) {
			const userRolesModal = Liferay.component('userRolesModal');
			userRolesModal.open();
		}
	);

	Liferay.componentReady('userInvitationModal').then(
		function(userInvitationModal) {
			userInvitationModal.on(
				'inviteUserToAccount',
				function(users) {
					let existingUsersIds = users.filter(
						function(el) {
							el.userId
						}
					).map(
						function(usr) {
							usr.userId
						}
					).join(',');

					let newUsersEmails = users.filter(
						function(el) {
							!el.userId
						}
					).map(
						function(usr) {
							usr.email
						}
					).join(',');

					document.querySelector('#<portlet:namespace />userIds').value = existingUsersIds;
					document.querySelector('#<portlet:namespace />emailAddresses').value = newUsersEmails;

					userInvitationModal.close();

					submitForm(document.<portlet:namespace />inviteUserFm);
				}
			);
		}
	);

</aui:script>