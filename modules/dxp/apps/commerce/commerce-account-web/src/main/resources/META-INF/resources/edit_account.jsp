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
CommerceAccountDisplayContext commerceAccountDisplayContext = (CommerceAccountDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceAccount commerceAccount = commerceAccountDisplayContext.getCurrentCommerceAccount();
%>

<portlet:actionURL name="editCommerceAccount" var="editCommerceAccountActionURL" />

<aui:form action="<%= editCommerceAccountActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commerceAccount == null) ? Constants.ADD : Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="commerceAccountIdId" type="hidden" value="<%= (commerceAccount == null) ? 0 : commerceAccount.getCommerceAccountId() %>" />

	<liferay-ui:error-marker
		key="<%= WebKeys.ERROR_SECTION %>"
		value="details"
	/>

	<aui:model-context bean="<%= commerceAccount %>" model="<%= CommerceAccount.class %>" />

	<div class="row">
		<aui:fieldset cssClass="col-md-6">
			<liferay-ui:error exception="<%= CommerceAccountNameException.class %>" message="please-enter-a-valid-name" />
			<liferay-ui:error exception="<%= DuplicateCommerceAccountException.class %>" message="the-account-name-is-already-taken" />

			<aui:input autoFocus="<%= windowState.equals(WindowState.MAXIMIZED) %>" name="name" />

			<aui:input name="email" />

			<aui:input checked="<%= (commerceAccount == null) ? true : commerceAccount.isActive() %>" name="active" type="toggle-switch" />
		</aui:fieldset>

		<aui:fieldset cssClass="col-md-6">
			<div>
				<c:if test="<%= commerceAccount != null %>">

					<%
					long logoId = commerceAccount.getLogoId();

					UserFileUploadsConfiguration userFileUploadsConfiguration = commerceAccountDisplayContext.getUserFileUploadsConfiguration();
					%>

					<liferay-ui:logo-selector
						currentLogoURL='<%= themeDisplay.getPathImage() + "/organization_logo?img_id=" + logoId + "&t=" + WebServerServletTokenUtil.getToken(logoId) %>'
						defaultLogo="<%= logoId == 0 %>"
						defaultLogoURL='<%= themeDisplay.getPathImage() + "/organization_logo?img_id=0" %>'
						logoDisplaySelector=".organization-logo"
						maxFileSize="<%= userFileUploadsConfiguration.imageMaxSize() %>"
						tempImageFileName="<%= String.valueOf(themeDisplay.getScopeGroupId()) %>"
					/>
				</c:if>
			</div>
		</aui:fieldset>

		<aui:button-row>
			<aui:button type="submit" />
		</aui:button-row>
	</div>
</aui:form>

<liferay-frontend:screen-navigation
	containerCssClass="col-md-10"
	context="<%= commerceAccount %>"
	key="<%= CommerceAccountScreenNavigationConstants.SCREEN_NAVIGATION_KEY %>"
	navCssClass="col-md-2"
	portletURL="<%= currentURLObj %>"
/>