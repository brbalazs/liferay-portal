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
%>

<commerce-ui:table
	dataProviderKey="commerceAccountUsers"
	itemPerPage="<%= 5 %>"
	namespace="<%= renderResponse.getNamespace() %>"
	pageNumber="1"
	portletURL="<%= commerceAccountMembersDisplayContext.getPortletURL() %>"
	tableName="commerceAccountUsers"
/>

<div class="minium-frame__cta is-visible">
	<aui:button cssClass="js-invite-user minium-button minium-button--big" onClick='<%= renderResponse.getNamespace() + "openUserInvitationModal();" %>' value="invite-user" />
</div>

<commerce-ui:user-invitation-modal
	componentId="userInvitationModal"
/>

<aui:script>

	Liferay.provide(
		window,
		'<portlet:namespace />openUserInvitationModal',
		function(evt) {
			const userInvitationModal = Liferay.component('userInvitationModal');
			userInvitationModal.open();
		}
	);

</aui:script>