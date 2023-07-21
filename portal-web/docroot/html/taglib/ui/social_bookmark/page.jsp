<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/html/taglib/ui/social_bookmark/init.jsp" %>

<c:choose>
	<c:when test='<%= displayStyle.equals("menu") %>'>
		<c:if test="<%= Validator.isNotNull(postUrl) %>">
			<liferay-ui:icon
				image="<%= icon %>"
				label="<%= true %>"
				linkCssClass="social-bookmark"
				message="<%= type %>"
				method="get"
				src="<%= icon %>"
				url="<%= postUrl %>"
			/>
		</c:if>
	</c:when>
	<c:otherwise>
		<liferay-util:html-bottom
			outputKey='<%= "taglib_ui_social_bookmark_link_" + type %>'
		>
			<style type="text/css">
				.taglib-social-bookmarks .taglib-social-bookmark-<%= type %> a.social-bookmark-link {
					background-image: url(/html/taglib/ui/social_bookmark/icons/<%= type %>.png);
				}
			</style>
		</liferay-util:html-bottom>

		<aui:a cssClass="social-bookmark-link" href="<%= postUrl %>" target="<%= target %>"><liferay-ui:message key="<%= messageKey %>" /></aui:a>
	</c:otherwise>
</c:choose>