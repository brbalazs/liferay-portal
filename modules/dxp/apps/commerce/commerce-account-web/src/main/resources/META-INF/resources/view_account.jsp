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
CommerceAddress commerceAddress = commerceAccountDisplayContext.getDefaultBillingCommerceAddress();

PortletURL portletURL = commerceAccountDisplayContext.getPortletURL();

portletURL.setParameter("mvcRenderCommandName", "viewCommerceAccount");
%>

<portlet:renderURL var="editCommerceAccountURL">
	<portlet:param name="mvcRenderCommandName" value="editCommerceAccount" />
	<portlet:param name="commerceAccountId" value="<%= String.valueOf(commerceAccount.getCommerceAccountId()) %>" />
	<portlet:param name='<%= PortletQName.PUBLIC_RENDER_PARAMETER_NAMESPACE + "backURL" %>' value="<%= portletURL.toString() %>" />
</portlet:renderURL>

<div class="details-header">
	<section class="details-header__section details-header__primary">
		<div class="details-header__main-data">
			<div class="details-header__avatar">
				<img alt="avatar" src="<%= commerceAccountDisplayContext.getLogo(commerceAccount) %>" />
			</div>

			<div class="details-header__name">
				<%= commerceAccount.getName() %>
			</div>

			<div class="details-header__email">
				<%= commerceAccount.getEmail() %>
			</div>
		</div>

		<c:if test="<%= commerceAddress != null %>">
			<div class="details-header__info-wrapper">
				<div class="details-header__label">
					<liferay-ui:message key="address" />
				</div>

				<div class="details-header__value">
					<%= commerceAddress.getStreet1() %><br />
					<%= commerceAddress.getCity() + StringPool.SPACE + commerceAddress.getZip() %>
				</div>
			</div>
		</c:if>

		<c:if test="<%= commerceAccountDisplayContext.hasEditCommerceAccountPermissions(commerceAccount.getCommerceAccountId()) %>">
			<div class="details-header__action">
				<aui:button cssClass="minium-button minium-button--big minium-button--outline" href="<%= editCommerceAccountURL %>" value="edit-account" />
			</div>
		</c:if>
	</section>

	<section class="details-header__section details-header__secondary">
		<div class="details-header__info-wrapper">
			<div class="details-header__label">
				<liferay-ui:message key="vat-number" />
			</div>

			<div class="details-header__value">
				<%= commerceAccount.getTaxId() %>
			</div>
		</div>

		<div class="details-header__info-wrapper">
			<div class="details-header__label">
				<liferay-ui:message key="customer-id" />
			</div>

			<div class="details-header__value">
				<%= commerceAccount.getCommerceAccountId() %>
			</div>
		</div>
	</section>

	<liferay-frontend:screen-navigation
		containerCssClass="p-0"
		context="<%= commerceAccount %>"
		key="<%= CommerceAccountScreenNavigationConstants.SCREEN_NAVIGATION_KEY %>"
		portletURL="<%= portletURL %>"
	/>
</div>