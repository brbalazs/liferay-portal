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
CommerceDashboardForecastsChartPortletInstanceConfiguration commerceDashboardForecastsChartPortletInstanceConfiguration = portletDisplay.getPortletInstanceConfiguration(CommerceDashboardForecastsChartPortletInstanceConfiguration.class);

commerceDashboardForecastsChartPortletInstanceConfiguration = ParameterMapUtil.setParameterMap(CommerceDashboardForecastsChartPortletInstanceConfiguration.class, commerceDashboardForecastsChartPortletInstanceConfiguration, request.getParameterMap(), "preferences--", "--");

String redirect = ParamUtil.getString(request, "redirect");
%>

<liferay-portlet:actionURL portletConfiguration="<%= true %>" var="configurationActionURL" />

<liferay-portlet:renderURL portletConfiguration="<%= true %>" varImpl="configurationRenderURL" />

<liferay-frontend:edit-form
	action="<%= configurationActionURL %>"
	method="post"
	name="fm"
>
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="redirect" type="hidden" value="<%= configurationRenderURL %>" />

	<liferay-frontend:edit-form-body>
		<liferay-frontend:fieldset-group>
			<liferay-frontend:fieldset>
				<aui:input checked="<%= commerceDashboardForecastsChartPortletInstanceConfiguration.filterBySKU() %>" label="filter-by-sku" name="preferences--filterBySKU--" type="checkbox" />

				<aui:select name="preferences--period--" showEmptyOption="<%= true %>">

					<%
					for (int curPeriod : CommerceForecastEntryConstants.PERIODS) {
					%>

						<aui:option label="<%= CommerceForecastEntryConstants.getPeriodLabel(curPeriod) %>" selected="<%= curPeriod == commerceDashboardForecastsChartPortletInstanceConfiguration.period() %>" value="<%= curPeriod %>" />

					<%
					}
					%>

				</aui:select>

				<aui:select name="preferences--target--">

					<%
					for (int curTarget : CommerceForecastEntryConstants.TARGETS) {
					%>

						<aui:option label="<%= CommerceForecastEntryConstants.getTargetLabel(curTarget) %>" selected="<%= curTarget == commerceDashboardForecastsChartPortletInstanceConfiguration.target() %>" value="<%= curTarget %>" />

					<%
					}
					%>

				</aui:select>
			</liferay-frontend:fieldset>
		</liferay-frontend:fieldset-group>
	</liferay-frontend:edit-form-body>

	<liferay-frontend:edit-form-footer>
		<aui:button type="submit" />

		<aui:button href="<%= redirect %>" type="cancel" />
	</liferay-frontend:edit-form-footer>
</liferay-frontend:edit-form>