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

<%@ include file="/forecast/init.jsp" %>

<%
String forecastServiceEndpoint = (String)request.getAttribute("forecastServiceEndpoint");

String customOptions = (String)request.getAttribute("customOptions");

String level = (String)request.getAttribute("level");

String period = (String)request.getAttribute("period");

String target = (String)request.getAttribute("target");
%>

<aui:select name="level">
	<%for (ForecastLevel forecastLevel : ForecastLevel.values()) {
		boolean selected = false;

		if (forecastLevel.getLabel().equals(level)) {
			selected = true;
		}
	%>

	<aui:option selected="<%= selected %>" value="<%= forecastLevel.getLabel() %>"> <%= LanguageUtil.get(request, forecastLevel.getLabel()) %> </aui:option>
	<%} %>
</aui:select>

<aui:select name="period">
	<%for (ForecastPeriod forecastPeriod : ForecastPeriod.values()) {
		boolean selected = false;

		if (forecastPeriod.getLabel().equals(period)) {
			selected = true;
		}
	%>

	<aui:option selected="<%= selected %>" value="<%= forecastPeriod.getLabel() %>"> <%= LanguageUtil.get(request, forecastPeriod.getLabel()) %> </aui:option>
	<%} %>
</aui:select>

<aui:select name="target">
	<%for (ForecastTarget forecastTarget : ForecastTarget.values()) {
		boolean selected = false;

		if (forecastTarget.getLabel().equals(target)) {
			selected = true;
		}
	%>

	<aui:option selected="<%= selected %>" value="<%= forecastTarget.getLabel() %>"> <%= LanguageUtil.get(request, forecastTarget.getLabel()) %> </aui:option>
	<%} %>
</aui:select>

<aui:input name="forecastServiceEndpoint" required="<%= true %>" value="<%= forecastServiceEndpoint %>">
	<aui:validator name="url" />
</aui:input>

<aui:input name="customOptions" type="textarea" value="<%= customOptions %>" />