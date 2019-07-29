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
CommerceDashboardForecastsChartDisplayContext commerceDashboardForecastsChartDisplayContext = (CommerceDashboardForecastsChartDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

PredictiveChartConfig predictiveChartConfig = commerceDashboardForecastsChartDisplayContext.getPredictiveChartConfig();
%>

<c:choose>
	<c:when test="<%= predictiveChartConfig != null %>">
		<chart:predictive
			config="<%= predictiveChartConfig %>"
		/>
	</c:when>
	<c:otherwise>
		<clay:alert
			message='<%= LanguageUtil.get(request, "not-enough-data-is-available-to-display-this-chart") %>'
			title="Info"
		/>
	</c:otherwise>
</c:choose>