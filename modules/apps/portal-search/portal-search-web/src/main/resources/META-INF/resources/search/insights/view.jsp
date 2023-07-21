<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %><%@
page import="com.liferay.portal.search.web.internal.search.insights.display.context.SearchInsightsDisplayContext" %>

<%
SearchInsightsDisplayContext searchInsightsDisplayContext = (SearchInsightsDisplayContext)java.util.Objects.requireNonNull(request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT));
%>

<style>
	<!--
	.full-query {
		font-size: x-small;
	}
	-->
</style>

<div class="full-query">
	<code>
		<%= HtmlUtil.escape(searchInsightsDisplayContext.getQueryString()) %>
	</code>
</div>