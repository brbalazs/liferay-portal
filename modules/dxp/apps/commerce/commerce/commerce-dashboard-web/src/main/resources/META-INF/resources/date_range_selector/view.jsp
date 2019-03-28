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
CommerceDashboardDisplayContext commerceDashboardDisplayContext = (CommerceDashboardDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);
%>

<portlet:actionURL name="editCommerceDashboardDateRange" var="editCommerceDashboardDateRangeURL" />

<aui:form action="<%= editCommerceDashboardDateRangeURL %>" cssClass="form-group-autofit" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<liferay-ui:input-date
		cssClass="form-group-item"
		dayParam="startDateDay"
		dayValue="<%= commerceDashboardDisplayContext.getStartDateDay() %>"
		firstDayOfWeek="<%= commerceDashboardDisplayContext.getFirstDayOfWeek() - 1 %>"
		monthParam="startDateMonth"
		monthValue="<%= commerceDashboardDisplayContext.getStartDateMonth() %>"
		name="startDate"
		yearParam="startDateYear"
		yearValue="<%= commerceDashboardDisplayContext.getStartDateYear() %>"
	/>

	<liferay-ui:input-date
		cssClass="form-group-item"
		dayParam="endDateDay"
		dayValue="<%= commerceDashboardDisplayContext.getEndDateDay() %>"
		firstDayOfWeek="<%= commerceDashboardDisplayContext.getFirstDayOfWeek() - 1 %>"
		monthParam="endDateMonth"
		monthValue="<%= commerceDashboardDisplayContext.getEndDateMonth() %>"
		name="endDate"
		yearParam="endDateYear"
		yearValue="<%= commerceDashboardDisplayContext.getEndDateYear() %>"
	/>

	<aui:button type="submit" value="refresh" />
</aui:form>