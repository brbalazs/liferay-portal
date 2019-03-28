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
DataIntegrationProcessListDisplayContext dataIntegrationProcessListDisplayContext = (DataIntegrationProcessListDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Process process = (Process)row.getObject();
%>

<liferay-ui:icon-menu
	direction="left-side"
	icon="<%= StringPool.BLANK %>"
	markupView="lexicon"
	message="<%= StringPool.BLANK %>"
	showWhenSingleIcon="<%= true %>"
>
	<liferay-ui:icon
		message="edit"
		url="<%= dataIntegrationProcessListDisplayContext.getEditProcessURL(process.getProcessId()).toString() %>"
	/>

	<liferay-ui:icon-delete
		url="<%= dataIntegrationProcessListDisplayContext.getDeleteProcessURL(process.getProcessId()) %>"
	/>
</liferay-ui:icon-menu>