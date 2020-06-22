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
	String redirectUrl = (String)request.getAttribute(PunchoutConstants.PUNCHOUT_RETURN_URL_ATTRIBUTE_NAME);
%>

<div>
	<c:set var="redirectLink">
		<a href="<%= redirectUrl %>"><%= redirectUrl %></a>
	</c:set>

	<liferay-ui:message arguments="${redirectLink}" key="the-punchout-cart-transfer-process-has-been-initiated.-you-should-be-redirected-automatically.-if-the-page-does-not-reload-within-a-few-seconds-please-click-this-link-x" />
</div>

<script>
	window.location.href = '<%= redirectUrl %>';
</script>