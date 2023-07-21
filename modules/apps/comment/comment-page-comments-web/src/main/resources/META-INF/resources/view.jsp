<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<c:if test="<%= LayoutPermissionUtil.contains(permissionChecker, layout, ActionKeys.VIEW) %>">
	<liferay-comment:discussion
		className="<%= Layout.class.getName() %>"
		classPK="<%= layout.getPlid() %>"
		formName="fm"
		redirect="<%= currentURL %>"
		userId="<%= user.getUserId() %>"
	/>
</c:if>