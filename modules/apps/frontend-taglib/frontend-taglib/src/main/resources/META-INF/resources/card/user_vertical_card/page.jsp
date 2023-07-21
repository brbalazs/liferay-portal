<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/card/user_vertical_card/init.jsp" %>

<%@ include file="/card/vertical_card/start.jspf" %>

<c:choose>
	<c:when test="<%= Validator.isNotNull(portraitURL) %>">
		<aui:a href="<%= url %>">
			<div class="aspect-ratio aspect-ratio-bg-center aspect-ratio-bg-cover" style="background-image: url(<%= portraitURL %>);">
				<img alt="" class="sr-only" src="<%= portraitURL %>" />
			</div>
		</aui:a>
	</c:when>
	<c:otherwise>
		<div class="aspect-ratio aspect-ratio-bg-center aspect-ratio-bg-cover <%= colorCssClass %>">
			<span class="h1 user-vertical-card-initials"><%= userInitials %></span>
		</div>
	</c:otherwise>
</c:choose>

<%@ include file="/card/vertical_card/end.jspf" %>