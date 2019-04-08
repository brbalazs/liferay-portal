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
String recommendServiceEndpoint = (String)request.getAttribute("recommendServiceEndpoint");

String esNodes = (String)request.getAttribute("esNodes");

String sparkMaster = (String)request.getAttribute("sparkMaster");

String customOptions = (String)request.getAttribute("customOptions");
%>

<aui:input name="recommendServiceEndpoint" required="<%= true %>" value="<%= recommendServiceEndpoint %>">
	<aui:validator name="url" />
</aui:input>

<aui:input name="esNodes" required="<%= true %>" value="<%= esNodes %>" />

<aui:input name="sparkMaster" required="<%= true %>" value="<%= sparkMaster %>" />

<aui:input name="customOptions" type="textarea" value="<%= customOptions %>" />