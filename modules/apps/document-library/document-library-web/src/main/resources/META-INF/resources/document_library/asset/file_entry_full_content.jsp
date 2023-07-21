<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
FileEntry fileEntry = (FileEntry)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_ENTRY);

boolean showExtraInfo = ParamUtil.getBoolean(request, "showExtraInfo");

DLViewFileVersionDisplayContext dlViewFileVersionDisplayContext = dlDisplayContextProvider.getDLViewFileVersionDisplayContext(request, response, fileEntry.getFileVersion());
%>

<c:choose>
	<c:when test="<%= PropsValues.DL_FILE_ENTRY_PREVIEW_ENABLED && !showExtraInfo && (fileEntry.getSize() > 0) && dlViewFileVersionDisplayContext.hasPreview() %>">
		<liferay-util:include page="/document_library/view_file_entry_simple_view.jsp" servletContext="<%= application %>" />
	</c:when>
	<c:otherwise>
		<liferay-util:include page="/document_library/view_file_entry.jsp" servletContext="<%= application %>">
			<liferay-util:param name="addPortletBreadcrumbEntries" value="<%= Boolean.FALSE.toString() %>" />
		</liferay-util:include>
	</c:otherwise>
</c:choose>