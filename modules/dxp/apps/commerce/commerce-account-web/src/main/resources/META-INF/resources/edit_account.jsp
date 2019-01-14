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

String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);
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

	<section class="details-header__section">
		<div class="row">
			<div class="col-lg-4 u-vac">
				<aui:fieldset>
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
				</aui:fieldset>
			</div>

			<div class="col-lg-4 mt-4 mt-lg-0 u-vac">
				<aui:input inlineLabel="true" name="name" />
				<aui:input inlineLabel="true" name="email" wrapperCssClass="mb-0" />
			</div>
		</div>
	</section>

	<section class="details-header__section pb-0">
		<div class="row">
			<div class="col-lg-4">
				<aui:select inlineLabel="true" label="country" name="commerceCountryId" showEmptyOption="<%= true %>">
					<aui:option label="Test" value="1" />

					<aui:option label="Test 2" value="2" />
				</aui:select>
			</div>

			<div class="col-lg-4">
				<aui:select inlineLabel="true" label="region" name="commerceRegionId" showEmptyOption="<%= true %>">
					<aui:option label="Test" value="1" />

					<aui:option label="Test 2" value="2" />
				</aui:select>
			</div>

			<div class="col-lg-4">
				<aui:input inlineLabel="true" name="address" type="text" />
			</div>

			<div class="col-lg-4">
				<aui:input inlineLabel="true" label="zip" name="zipCode" type="text" />
			</div>

			<div class="col-lg-4">
				<aui:input inlineLabel="true" name="city" type="text" />
			</div>
		</div>
	</section>

	<div class="minium-frame__cta is-visible">
		<aui:button cssClass="minium-button minium-button--big minium-button--outline" href="<%= backURL %>" value="cancel" />
		<aui:button cssClass="minium-button minium-button--big" type="submit" />
	</div>
</aui:form>