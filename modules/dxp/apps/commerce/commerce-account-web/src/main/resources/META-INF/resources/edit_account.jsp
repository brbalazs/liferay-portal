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

<portlet:actionURL name="editCommerceAccount" var="editCommerceAccountActionURL" />

<aui:form action="<%= editCommerceAccountActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= (commerceAccount == null) ? Constants.ADD : Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="commerceAccountId" type="hidden" value="<%= (commerceAccount == null) ? 0 : commerceAccount.getCommerceAccountId() %>" />

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