<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/card/vertical_card/init.jsp" %>

<%@ include file="/card/vertical_card/start.jspf" %>

<div class="aspect-ratio <%= imageCSSClass %> <%= backgroundImage ? "aspect-ratio-bg-center aspect-ratio-bg-cover" : StringPool.BLANK %>" style="<%= backgroundImage ? "background-image: url(" + imageUrl + ")" : StringPool.BLANK %>">
	<aui:a href="<%= url %>">
		<img alt="" class="<%= backgroundImage ? " sr-only" : StringPool.BLANK %>" src="<%= imageUrl %>" />
	</aui:a>

	<c:if test="<%= Validator.isNotNull(stickerBottom) %>">
		<%= stickerBottom %>
	</c:if>
</div>

<%@ include file="/card/vertical_card/end.jspf" %>