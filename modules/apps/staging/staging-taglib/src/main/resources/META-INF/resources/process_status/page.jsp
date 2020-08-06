<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/process_status/init.jsp" %>

<%
String clayClassPostfix = "info";

if (backgroundTaskStatus == BackgroundTaskConstants.STATUS_FAILED) {
	clayClassPostfix = "danger";
}
else if (backgroundTaskStatus == BackgroundTaskConstants.STATUS_IN_PROGRESS) {
	clayClassPostfix = "warning";
}
else if (backgroundTaskStatus == BackgroundTaskConstants.STATUS_SUCCESSFUL) {
	clayClassPostfix = "success";
}
%>

<span class="label label-<%= clayClassPostfix %> process-status background-task-status-<%= backgroundTaskStatusLabel %>" data-qa-id="processResult">
	<liferay-ui:message key="<%= backgroundTaskStatusLabel %>" />
</span>