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
CommerceAccountUserDisplayContext commerceAccountUserDisplayContext = (CommerceAccountUserDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceAccount commerceAccount = commerceAccountUserDisplayContext.getCurrentCommerceAccount();
User selectedUser = commerceAccountUserDisplayContext.getSelectedUser();
PortletURL portletURL = commerceAccountUserDisplayContext.getPortletURL();

portletURL.setParameter("mvcRenderCommandName", "viewCommerceAccountUser");
%>

<portlet:renderURL var="editCommerceAccountURL">
	<portlet:param name="mvcRenderCommandName" value="editCommerceAccountUser" />
	<portlet:param name="commerceAccountId" value="<%= String.valueOf(commerceAccount.getCommerceAccountId()) %>" />
	<portlet:param name="userId" value="<%= String.valueOf(selectedUser.getUserId()) %>" />
	<portlet:param name='<%= PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL" %>' value="<%= portletURL.toString() %>" />
</portlet:renderURL>

<div class="details-header">
	<section class="details-header__section details-header__primary">
		<div class="details-header__main-data">
			<div class="details-header__avatar">
				<img alt="avatar" src="https://via.placeholder.com/120" />
			</div>

			<div class="details-header__name">
				<%= selectedUser.getFullName() %>
			</div>

			<div class="details-header__email">
				<%= selectedUser.getEmailAddress() %>
			</div>
		</div>

		<div class="details-header__action">
			<aui:button cssClass="minium-button minium-button--big minium-button--outline" href="<%= editCommerceAccountURL %>" value='<%= LanguageUtil.get(request, "edit-user") %>' />
		</div>
	</section>
</div>