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

CommerceAccount commerceAccount = commerceAccountDisplayContext.getCurrentCommerceAccount();
%>

<portlet:renderURL var="editCommerceAccountURL">
	<portlet:param name="mvcRenderCommandName" value="editCommerceAccount" />
	<portlet:param name="commerceAccountId" value="<%= String.valueOf(commerceAccount.getCommerceAccountId()) %>" />
</portlet:renderURL>

<section class="details-header__section">
	<div class="row">
		<div class="col-lg-4 u-vac">
			<img alt="<%= commerceAccount.getName() %>" src="<%= commerceAccountDisplayContext.getLogo(commerceAccount) %>">
		</div>

		<div class="col-lg-4 mt-4 mt-lg-0 u-vac">
			<aui:button href="<%= editCommerceAccountURL %>" value="edit-account" />
		</div>
	</div>
</section>

<liferay-frontend:screen-navigation
	containerCssClass="col-md-10"
	context="<%= commerceAccount %>"
	key="<%= CommerceAccountScreenNavigationConstants.SCREEN_NAVIGATION_KEY %>"
	navCssClass="col-md-2"
	portletURL="<%= currentURLObj %>"
/>