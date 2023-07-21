<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/blogs_admin/init.jsp" %>

<%
PortletURL portletURL = (PortletURL)request.getAttribute("view_images.jsp-portletURL");

ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

FileEntry fileEntry = (FileEntry)row.getObject();
%>

<c:if test="<%= (fileEntry.getUserId() == themeDisplay.getUserId()) || BlogsPermission.contains(permissionChecker, themeDisplay.getScopeGroupId(), ActionKeys.UPDATE) %>">
	<portlet:actionURL name="/blogs/edit_image" var="deleteImageURL">
		<portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE %>" />
		<portlet:param name="redirect" value="<%= portletURL.toString() %>" />
		<portlet:param name="fileEntryId" value="<%= String.valueOf(fileEntry.getFileEntryId()) %>" />
	</portlet:actionURL>

	<liferay-ui:icon-delete
		trash="<%= false %>"
		url="<%= deleteImageURL %>"
	/>
</c:if>