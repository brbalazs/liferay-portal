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
CommercePunchoutDisplayContext commercePunchoutDisplayContext = (CommercePunchoutDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

long commerceChannelId = commercePunchoutDisplayContext.getCommerceChannelId();
%>

<portlet:actionURL name="editCommercePunchoutConfiguration" var="editCommercePunchoutConfigurationActionURL" />

<aui:form action="<%= editCommercePunchoutConfigurationActionURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="commerceChannelId" type="hidden" value="<%= commerceChannelId %>" />

	<commerce-ui:info-box
		title='<%= LanguageUtil.get(request, "configuration") %>'
	>
		<aui:input checked="<%= commercePunchoutDisplayContext.enabled() %>" label="enabled" name="settings--enabled--" type="checkbox" />

		<aui:input label="punchout-start-url" name="settings--punchoutStartURL--" value="<%= commercePunchoutDisplayContext.getPunchoutStartURL() %>" />
	</commerce-ui:info-box>
</aui:form>