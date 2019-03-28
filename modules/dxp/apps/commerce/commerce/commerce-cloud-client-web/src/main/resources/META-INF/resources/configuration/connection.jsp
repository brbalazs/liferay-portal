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
CommerceCloudClientConfigurationDisplayContext commerceCloudClientConfigurationDisplayContext = (CommerceCloudClientConfigurationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceCloudClientConfiguration commerceCloudClientConfiguration = commerceCloudClientConfigurationDisplayContext.getCommerceCloudClientConfiguration();
String redirect = portletDisplay.getURLBack();
%>

<aui:input name="<%= Constants.CMD %>" type="hidden" value="connection" />

<aui:fieldset-group markupView="lexicon">
	<aui:fieldset>
		<aui:input name="serverHost" value="<%= commerceCloudClientConfiguration.serverHost() %>" />

		<aui:input name="projectId" value="<%= commerceCloudClientConfiguration.projectId() %>" />

		<aui:button-row>
			<aui:button type="submit" />

			<aui:button href="<%= redirect %>" type="cancel" />
		</aui:button-row>
	</aui:fieldset>
</aui:fieldset-group>