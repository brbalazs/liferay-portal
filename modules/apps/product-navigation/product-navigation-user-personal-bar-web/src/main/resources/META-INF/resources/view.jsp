<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<c:choose>
	<c:when test="<%= themeDisplay.isSignedIn() %>">
		<span class="user-avatar-link">
			<a class="text-default" data-qa-id="openUserMenu" href="javascript:;" id="<portlet:namespace />sidenavUserToggle">
				<c:if test="<%= themeDisplay.isImpersonated() %>">
					<aui:icon image="asterisk" markupView="lexicon" />
				</c:if>

				<span class="user-avatar-image">
					<liferay-ui:user-portrait
						user="<%= user %>"
					/>
				</span>
				<span class="user-full-name">
					<%= HtmlUtil.escape(user.getFullName()) %>
				</span>
			</a>

			<%
			int notificationsCount = GetterUtil.getInteger(request.getAttribute(ProductNavigationUserPersonalBarWebKeys.NOTIFICATIONS_COUNT));
			%>

			<c:if test="<%= notificationsCount > 0 %>">

				<%
				PortletURL notificationsURL = PortletProviderUtil.getPortletURL(request, UserNotificationEvent.class.getName(), PortletProvider.Action.VIEW);
				%>

				<aui:a href="<%= (notificationsURL != null) ? notificationsURL.toString() : null %>">
					<span class="panel-notifications-count sticker sticker-right sticker-rounded sticker-sm sticker-warning"><%= notificationsCount %></span>
				</aui:a>
			</c:if>
		</span>

		<aui:script sandbox="<%= true %>">
			var sidenavUserToggle = $('#<portlet:namespace />sidenavUserToggle');

			sidenavUserToggle.on(
				'click',
				function(event) {
					Liferay.fire('ProductMenu:openUserMenu');
				}
			);
		</aui:script>
	</c:when>
	<c:otherwise>

		<%
		Map<String, Object> anchorData = new HashMap<String, Object>();

		anchorData.put("redirect", String.valueOf(PortalUtil.isLoginRedirectRequired(request)));
		%>

		<span class="sign-in text-default" role="presentation">
			<aui:icon cssClass="sign-in text-default" data="<%= anchorData %>" image="user" label="sign-in" markupView="lexicon" url="<%= themeDisplay.getURLSignIn() %>" />
		</span>
	</c:otherwise>
</c:choose>