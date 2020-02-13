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

<%@ include file="/side_panel_content/init.jsp" %>

<aui:script require="commerce-frontend-js/utilities/iframes.es as iframesUtils">
	iframesUtils.initializeIframeListeners();
</aui:script>

<div class="side-panel-iframe">

	<c:if test="<%= Validator.isNotNull(title) %>">
		<div class="side-panel-iframe-header">
			<div class="side-panel-iframe-title">
				<%= title %>
			</div>
		</div>
	</c:if>

	<button class="side-panel-iframe-close btn btn-unstyled">
		<clay:icon symbol="times" />
	</button>

	<c:if test="<%= !Validator.isNotNull(screenNavigatorKey) %>">
		<div class="side-panel-iframe-content">
	</c:if>


