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

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ taglib uri="http://liferay.com/tld/aui" prefix="aui" %><%@
taglib uri="http://liferay.com/tld/chart" prefix="chart" %><%@
taglib uri="http://liferay.com/tld/clay" prefix="clay" %><%@
taglib uri="http://liferay.com/tld/frontend" prefix="liferay-frontend" %><%@
taglib uri="http://liferay.com/tld/portlet" prefix="liferay-portlet" %><%@
taglib uri="http://liferay.com/tld/theme" prefix="liferay-theme" %><%@
taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui" %>

<%@ page import="com.liferay.commerce.currency.model.CommerceMoney" %><%@
page import="com.liferay.commerce.dashboard.web.internal.configuration.CommerceDashboardForecastsChartPortletInstanceConfiguration" %><%@
page import="com.liferay.commerce.dashboard.web.internal.display.context.CommerceDashboardDisplayContext" %><%@
page import="com.liferay.commerce.dashboard.web.internal.display.context.CommerceDashboardForecastsChartDisplayContext" %><%@
page import="com.liferay.commerce.dashboard.web.internal.display.context.CommerceDashboardInstanceSelectorDisplayContext" %><%@
page import="com.liferay.commerce.dashboard.web.internal.display.context.CommerceDashboardKPIDisplayContext" %><%@
page import="com.liferay.commerce.forecast.model.CommerceForecastEntryConstants" %><%@
page import="com.liferay.commerce.product.model.CPDefinition" %><%@
page import="com.liferay.commerce.product.model.CPInstance" %><%@
page import="com.liferay.frontend.taglib.chart.model.predictive.PredictiveChartConfig" %><%@
page import="com.liferay.petra.string.StringPool" %><%@
page import="com.liferay.portal.configuration.metatype.util.ParameterMapUtil" %><%@
page import="com.liferay.portal.kernel.language.LanguageUtil" %><%@
page import="com.liferay.portal.kernel.util.Constants" %><%@
page import="com.liferay.portal.kernel.util.HtmlUtil" %><%@
page import="com.liferay.portal.kernel.util.ParamUtil" %><%@
page import="com.liferay.portal.kernel.util.StringUtil" %><%@
page import="com.liferay.portal.kernel.util.WebKeys" %>

<%@ page import="java.util.List" %><%@
page import="java.util.Map" %>

<liferay-frontend:defineObjects />

<liferay-theme:defineObjects />

<portlet:defineObjects />