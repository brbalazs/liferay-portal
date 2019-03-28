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
HistoryDataIntegrationDisplayContext historyDataIntegrationDisplayContext = (HistoryDataIntegrationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

String redirect = ParamUtil.getString(request, "redirect");

String backURL = ParamUtil.getString(request, "backURL", redirect);

History history = historyDataIntegrationDisplayContext.getHistory();

long historyId = historyDataIntegrationDisplayContext.getHistoryId();

Date defaultDate = new Date();
int startDay, startMonth, startYear;

startDay = defaultDate.getDate();
startYear = defaultDate.getYear() + 1900;
startMonth = defaultDate.getMonth();
long timeMillis = 0L;

String startDate = "";

if (history != null) {
	timeMillis = history.getEndDate().getTime() - history.getStartDate().getTime();
	startDate = historyDataIntegrationDisplayContext.getFormattedDate(history.getStartDate());
}

String title = startDate;
%>

<liferay-util:include page="/navbar.jsp" servletContext="<%= application %>" />

<%@ include file="/breadcrumb.jspf" %>

<portlet:actionURL name="editHistory" var="editHistoryActionURL" />
<div class="closed container-fluid-1280" id="<portlet:namespace />editHistoryId">
	<div class="container main-content-body sheet">
		<aui:form action="<%= editHistoryActionURL %>" cssClass="container-fluid-1280" method="post" name="fm">
			<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
			<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
			<aui:input name="historyId" type="hidden" value="<%= String.valueOf(historyId) %>" />

			<div class="lfr-form-content">
				<aui:fieldset>
					<aui:input disabled="<%= true %>" label="start-date" name="startDate" value="<%= startDate %>" />

					<aui:input disabled="<%= true %>" name="status" value="<%= LanguageUtil.get(request, BackgroundTaskConstants.getStatusLabel(history.getStatus())) %>" />

					<aui:input disabled="<%= true %>" label="runtime" name="runTime" value='<%= String.valueOf(timeMillis) + " ms" %>' />
				</aui:fieldset>

				<aui:row>
					<c:if test="<%= history.getErrorLogFileEntryId() > 0 %>">
						<a href="<%= historyDataIntegrationDisplayContext.getErroLogFileEntryURL() %>" target="_BLANK"><%= LanguageUtil.get(request, "runtime-errors") %></a>
					</c:if>
				</aui:row>

				<aui:row>
					<c:if test="<%= history.getRuntimeLogFileEntryId() > 0 %>">
						<a href="<%= historyDataIntegrationDisplayContext.getRuntimeLogFileEntryURL() %>" target="_BLANK"><%= LanguageUtil.get(request, "runtime-log") %></a>
					</c:if>
				</aui:row>

				<aui:button-row>
					<aui:button cssClass="btn-lg" href="<%= backURL %>" type="cancel" />
				</aui:button-row>
			</div>
		</aui:form>
	</div>
</div>