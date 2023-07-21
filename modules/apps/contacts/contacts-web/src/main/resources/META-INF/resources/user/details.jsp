<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
User selUser = (User)request.getAttribute("user.selUser");
%>

<aui:model-context bean="<%= selUser %>" model="<%= User.class %>" />

<h3><liferay-ui:message key="details" /></h3>

<aui:fieldset markupview="lexicon">
	<aui:input autocapitalize="off" autocorrect="off" disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "screenName") %>' name="screenName" type="text" />

	<aui:input bean="<%= user %>" disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "emailAddress") %>' model="<%= User.class %>" name="emailAddress" type="email">
		<c:if test="<%= PrefsPropsUtil.getBoolean(company.getCompanyId(), PropsKeys.USERS_EMAIL_ADDRESS_REQUIRED) %>">
			<aui:validator name="required" />
		</c:if>
	</aui:input>

	<aui:input disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "firstName") %>' name="firstName" />

	<aui:input disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "middleName") %>' name="middleName" />

	<aui:input disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "lastName") %>' name="lastName" />

	<aui:input disabled='<%= !UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "jobTitle") %>' name="jobTitle" />
</aui:fieldset>

<aui:fieldset markupview="lexicon">
	<div class="user-profile-image" id="<portlet:namespace />userProfileImage">
		<c:if test="<%= selUser != null %>">
			<c:choose>
				<c:when test='<%= UsersAdminUtil.hasUpdateFieldPermission(permissionChecker, user, selUser, "portrait") %>'>
					<liferay-ui:logo-selector
						currentLogoURL="<%= selUser.getPortraitURL(themeDisplay) %>"
						defaultLogo="<%= selUser.getPortraitId() == 0 %>"
						defaultLogoURL="<%= UserConstants.getPortraitURL(themeDisplay.getPathImage(), selUser.isMale(), 0) %>"
						logoDisplaySelector=".user-logo"
						showBackground="<%= false %>"
						tempImageFileName="<%= String.valueOf(selUser.getUserId()) %>"
					/>
				</c:when>
				<c:otherwise>
					<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="portrait" />" src="<%= selUser.getPortraitURL(themeDisplay) %>" />
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>
</aui:fieldset>

<aui:script use="aui-base">
	window['<%= PortalUtil.getPortletNamespace(PortletKeys.MY_ACCOUNT) %>changeLogo'] = function(logoURL) {
		var avatarDialog = A.one('#<portlet:namespace />userProfileImage .avatar');

		if (avatarDialog) {
			avatarDialog.attr('src', logoURL);
		}

		var avatarSidebar = A.one('#so-sidebar .profile-image img');

		if (avatarSidebar) {
			avatarSidebar.attr('src', logoURL);
		}

		var avatarDockbar = A.one('.user-fullname.user-portrait img');

		if (avatarDockbar) {
			avatarDockbar.attr('src', logoURL);
		}
	}
</aui:script>