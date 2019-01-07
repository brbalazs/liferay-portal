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
			
			<div class="col-lg-4 u-vac mt-4 mt-lg-0">
				<aui:input name="name" inlineLabel="true"/>
				<aui:input name="email" inlineLabel="true" wrapperCssClass="mb-0"/>
			</div>
		</div>
	</section>

	<section class="details-header__section pb-0">
		<div class="row">
			<div class="col-lg-4">
				<aui:select label="country" name="commerceCountryId" inlineLabel="true" showEmptyOption="<%= true %>">
					<aui:option 
						label="Test" 
						value="1" 
					/>
					<aui:option 
						label="Test 2" 
						value="2" 
					/>
				</aui:select>
			</div>
			<div class="col-lg-4">
				<aui:select label="region" name="commerceRegionId" inlineLabel="true" showEmptyOption="<%= true %>">
					
					<aui:option 
						label="Test" 
						value="1" 
					/>

					<aui:option 
						label="Test 2" 
						value="2" 
					/>

				</aui:select>
			</div>

			<div class="col-lg-4">
				<aui:input type="text" name="address" inlineLabel="true"/>
			</div>
			<div class="col-lg-4">
				<aui:input type="text" name="zipCode" inlineLabel="true"/>
			</div>
			<div class="col-lg-4">
				<aui:input type="text" name="city" inlineLabel="true"/>
			</div>
		</div>
	</section>

	<div class="minium-frame__cta is-visible">
		<button class="minium-button minium-button--big minium-button--outline">Cancel</button>
		<aui:button type="submit" cssClass="minium-button minium-button--big" />
	</div>

</aui:form>

<liferay-frontend:screen-navigation
	containerCssClass="col-md-10"
	context="<%= commerceAccount %>"
	key="<%= CommerceAccountScreenNavigationConstants.SCREEN_NAVIGATION_KEY %>"
	navCssClass="col-md-2"
	portletURL="<%= currentURLObj %>"
/>
