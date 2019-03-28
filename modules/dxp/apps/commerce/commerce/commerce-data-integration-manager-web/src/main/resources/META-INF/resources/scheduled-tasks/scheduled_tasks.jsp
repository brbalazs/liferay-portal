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
ScheduledTasksDataIntegrationDisplayContext scheduledTasksDataIntegrationDisplayContext = (ScheduledTasksDataIntegrationDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

PortletURL portletURL = scheduledTasksDataIntegrationDisplayContext.getPortletURL();

portletURL.setParameter("searchContainerId", "schedledTasks");

request.setAttribute("view.jsp-portletURL", portletURL);
%>

<c:if test="<%= scheduledTasksDataIntegrationDisplayContext.hasAdminPermission() %>">
	<liferay-util:include page="/scheduled_tasks_toolbar.jsp" servletContext="<%= application %>">
		<liferay-util:param name="searchContainerId" value="scheduledTasks" />
	</liferay-util:include>

	<div id="<portlet:namespace />scheduledTasksContainer">
		<div class="closed container-fluid-1280" id="<portlet:namespace />infoPanelId">
			<div class="container">
				<aui:form action="<%= portletURL.toString() %>" method="post" name="fm">
					<aui:input name="<%= Constants.CMD %>" type="hidden" />
					<aui:input name="redirect" type="hidden" value="<%= portletURL.toString() %>" />
					<aui:input name="deleteScheduledTaskIds" type="hidden" />

					<div class="scheduled-task-lists-container" id="<portlet:namespace />entriesContainer">
						<liferay-ui:search-container
							id="scheduledTasks"
							searchContainer="<%= scheduledTasksDataIntegrationDisplayContext.getSearchContainer() %>"
						>
							<liferay-ui:search-container-row
								className="com.liferay.commerce.data.integration.manager.model.ScheduledTask"
								cssClass="entry-display-style"
								keyProperty="scheduledTaskId"
								modelVar="scheduledTask"
							>

								<%
								PortletURL rowURL = renderResponse.createRenderURL();

								rowURL.setParameter("mvcRenderCommandName", "editScheduledTask");
								rowURL.setParameter("redirect", currentURL);
								rowURL.setParameter("scheduledTaskId", String.valueOf(scheduledTask.getScheduledTaskId()));
								rowURL.setParameter("dataIntegrationAdminModuleKey", ScheduledTasksAdminModule.KEY);
								%>

								<liferay-ui:search-container-column-text
									cssClass="important table-cell-content"
									href="<%= rowURL %>"
									property="name"
								/>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="frequency"
								>
									<%= LanguageUtil.get(request, scheduledTask.getFrequency()) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="last-fire-time"
								>
									<%= scheduledTasksDataIntegrationDisplayContext.getLastExecutionDate(scheduledTask) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-text
									cssClass="table-cell-content"
									name="next-fire-time"
								>
									<%= scheduledTasksDataIntegrationDisplayContext.getNextExecutionDate(scheduledTask) %>
								</liferay-ui:search-container-column-text>

								<liferay-ui:search-container-column-jsp
									cssClass="table-cell-content"
									path="/scheduled-tasks/buttons.jsp"
								/>

								<liferay-ui:search-container-column-jsp
									cssClass="entry-action-column"
									path="/scheduled-tasks/scheduled_task_action.jsp"
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