<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
UIItemsBuilder uiItemsBuilder = new UIItemsBuilder(request, ActionUtil.getFileVersion(liferayPortletRequest, ActionUtil.getFileEntry(liferayPortletRequest)), resourceBundle, dlTrashUtil);
%>

<liferay-ui:menu-item
	menuItem="<%= uiItemsBuilder.getJavacriptCheckinMenuItem() %>"
/>