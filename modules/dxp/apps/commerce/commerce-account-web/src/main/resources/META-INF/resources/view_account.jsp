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

<div class="details-header">
	<section class="details-header__section details-header__primary">
		<div class="details-header__main-data">
			<div class="details-header__avatar">
				<img src="https://via.placeholder.com/120" alt="avatar" />
			</div>
			<div class="details-header__name">
				<%= commerceAccount.getName() %>
			</div>
			<div class="details-header__email">
				<%= commerceAccount.getEmail() %>
			</div>
		</div>
		<div class="details-header__info-wrapper">
			<div class="details-header__label">
				Address
			</div>
			<div class="details-header__value">
				PO Box 467<br />
				New York (NY) 10002
			</div>
		</div>
		<div class="details-header__action">
			<aui:button cssClass="minium-button minium-button--big minium-button--outline" href="<%= editCommerceAccountURL %>" value="edit-account" />
		</div>
	</section>
	<section class="details-header__section details-header__secondary">
		<div class="details-header__info-wrapper">
			<div class="details-header__label">
				Vat Number
			</div>
			<div class="details-header__value">
				123456789123456
			</div>
		</div>
		<div class="details-header__info-wrapper">
			<div class="details-header__label">
				Custmer Id
			</div>
			<div class="details-header__value">
				65479123
			</div>
		</div>
	</section>      

	<liferay-frontend:screen-navigation
		containerCssClass="p-0"
		context="<%= commerceAccount %>"
		key="<%= CommerceAccountScreenNavigationConstants.SCREEN_NAVIGATION_KEY %>"
		portletURL="<%= currentURLObj %>"
	/>  
</div>   