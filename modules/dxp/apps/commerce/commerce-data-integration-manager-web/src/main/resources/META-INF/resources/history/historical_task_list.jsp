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

PortletURL portletURL = historyDataIntegrationDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "histories");

request.setAttribute("view.jsp-portletURL", portletURL);
%>

<c:if test="<%= historyDataIntegrationDisplayContext.hasAdminPermission() %>">
	<liferay-util:include page="/history_toolbar.jsp" servletContext="<%= application %>">
		<liferay-util:param name="searchContainerId" value="histories" />
	</liferay-util:include>

	<div id="<portlet:namespace />processesContainer">
		<div class="closed container-fluid-1280" id="<portlet:namespace />infoPanelId">
			<div class="container">
				<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
					<aui:input name="<%= Constants.CMD %>" type="hidden" />
					<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
					<aui:input name="deleteHistoryIds" type="hidden" />

					<div class="process-lists-container" id="<portlet:namespace />entriesContainer">
						<liferay-ui:search-container
							id="histories"
							searchContainer="<%= historyDataIntegrationDisplayContext.getSearchContainer() %>"
						>
							<liferay-ui:search-container-row
								className="com.liferay.commerce.data.integration.manager.model.History"
								cssClass="entry-display-style"
								keyProperty="historyId"
								modelVar="history"
							>

								<%
								PortletURL rowURL = renderResponse.createRenderURL();

								rowURL.setParameter("mvcRenderCommandName", "viewHistoryDetails");
								rowURL.setParameter("redirect", currentURL);
								rowURL.setParameter("historyId", String.valueOf(history.getHistoryId()));
								%>

								<liferay-ui:search-container-column-text
									cssClass="important table-cell-content"
									href="<%= rowURL %>"
									name="start-date"
								>
									<%= historyDataIntegrationDisplayContext.getFormattedDate(history.getStartDate()) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="runtime"
								>
									<%= (history.getEndDate() == null) ? StringPool.DASH : String.valueOf(history.getEndDate().getTime() - history.getStartDate().getTime()) + " ms" %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="process"
									property="scheduledTaskName"
								/>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="launch-type"
									property="launchType"
									translate="true"
								/>

								<liferay-ui:search-container-column-text
									name="status"
								>
									<h6 class="background-task-status-row background-task-status-<%= BackgroundTaskConstants.getStatusLabel(history.getStatus()) %> <%= BackgroundTaskConstants.getStatusCssClass(history.getStatus()) %>">
										<liferay-ui:message key="<%= BackgroundTaskConstants.getStatusLabel(history.getStatus()) %>" />
									</h6>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-jsp
									cssClass="entry-action-column"
									path="/history/historical_task_action.jsp"
								/>
							</liferay-ui:search-container-row>

							<liferay-ui:search-iterator
								displayStyle="list"
								markupView="lexicon"
							/>
						</liferay-ui:search-container>
					</div>
				</aui:form>
			</div>
		</div>
	</div>
</c:if>