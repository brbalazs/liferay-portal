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
DataIntegrationProcessActionHelper dataIntegrationProcessActionHelper = (DataIntegrationProcessActionHelper)request.getAttribute(DataIntegrationProcessWebKeys.DI_PROCESS_ACTION_HELPER);

Process process = dataIntegrationProcessActionHelper.getProcess(request);
%>

<c:if test="<%= process != null %>">
	<aui:model-context bean="<%= process %>" model="<%= Process.class %>" />
</c:if>

<aui:input name="className" />
<aui:input name="contextProperties" type="file" />
<aui:input name="srcArchive" required="<%= true %>" type="file" />