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

String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);

Process process = dataIntegrationProcessListDisplayContext.getProcess();

long processId = dataIntegrationProcessListDisplayContext.getProcessId();

String processType = ParamUtil.getString(request, "processType");

String title = LanguageUtil.get(request, "add-process");

if (process != null) {
	processType = process.getProcessType();

	title = process.getName();
}

Map<String, String> processTypes = (HashMap)request.getAttribute("processTypes");

List<String> processTypeKeys = new ArrayList(processTypes.keySet());

if ((processTypeKeys.size() > 0) && (processType == "")) {
	processType = ParamUtil.getString(request, "processType", processTypeKeys.get(0));
}
%>

<liferay-util:include page="/navbar.jsp" servletContext="<%= application %>" />

<%@ include file="/breadcrumb.jspf" %>

<portlet:actionURL name="editProcess" var="editProcessActionURL" />

<div class="closed container-fluid-1280" id="<portlet:namespace />editProcessId">
	<div class="container main-content-body sheet">
		<aui:form action="<%= editProcessActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
			<aui:input name="processId" type="hidden" value="<%= String.valueOf(processId) %>" />

			<div class="lfr-form-content">
				<c:if test="<%= process != null %>">
					<aui:model-context bean="<%= process %>" model="<%= Process.class %>" />
				</c:if>

				<aui:fieldset>
					<aui:input name="name" required="<%= true %>" />
					<aui:input name="version" />

					<c:if test="<%= processTypes != null %>">
						<aui:select label="process-type" name="processType" onChange='<%= renderResponse.getNamespace() + "selectProcessType();" %>'>

							<%
							for (String processTypeKey : processTypes.keySet()) {
							%>

								<aui:option label="<%= processTypeKey %>" selected="<%= (process != null) && (process.getProcessType() == processTypeKey) %>" value="<%= processTypeKey %>" />

							<%
							}
							%>

						</aui:select>

						<%
						ProcessTypeJSPContributor processTypeJSPContributor = dataIntegrationProcessListDisplayContext.getProcessTypeJSPContributor(processType);
						%>

						<c:if test="<%= processTypeJSPContributor != null %>">

							<%
							processTypeJSPContributor.render(request, response);
							%>

						</c:if>

					</c:if>
				</aui:fieldset>

				<aui:button-row>
					<aui:button cssClass="btn-lg" type="submit" value="save" />

					<aui:button cssClass="btn-lg" href="<%= backURL %>" type="cancel" />
				</aui:button-row>
			</div>
		</aui:form>
	</div>
</div>

<aui:script>
	Liferay.provide(
		window,
		'<portlet:namespace />selectProcessType',
		function() {
			var A = AUI();

			var processType = A.one('#<portlet:namespace />processType').val();

			var portletURL = new Liferay.PortletURL.createURL('<%= currentURLObj %>');

			portletURL.setParameter('processType', processType);

			window.location.replace(portletURL.toString());
		},
		['liferay-portlet-url']
	);
</aui:script>