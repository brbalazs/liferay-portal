<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
ResultRow row = (ResultRow)request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);

Object result = row.getObject();

FileEntry fileEntry = null;
FileShortcut fileShortcut = null;

if (result instanceof AssetEntry) {
	AssetEntry assetEntry = (AssetEntry)result;

	if (Objects.equals(assetEntry.getClassName(), DLFileEntryConstants.getClassName())) {
		fileEntry = DLAppLocalServiceUtil.getFileEntry(assetEntry.getClassPK());

		fileEntry = fileEntry.toEscapedModel();
	}
	else {
		fileShortcut = DLAppLocalServiceUtil.getFileShortcut(assetEntry.getClassPK());

		fileShortcut = fileShortcut.toEscapedModel();
	}
}
else if (result instanceof FileEntry) {
	fileEntry = (FileEntry)result;
}
else if (result instanceof FileShortcut) {
	fileShortcut = (FileShortcut)result;

	fileShortcut = fileShortcut.toEscapedModel();

	fileEntry = DLAppLocalServiceUtil.getFileEntry(fileShortcut.getToFileEntryId());
}

fileEntry = fileEntry.toEscapedModel();

FileVersion latestFileVersion = fileEntry.getFileVersion();

if ((user.getUserId() == fileEntry.getUserId()) || permissionChecker.isContentReviewer(user.getCompanyId(), scopeGroupId) || DLFileEntryPermission.contains(permissionChecker, fileEntry, ActionKeys.UPDATE)) {
	latestFileVersion = fileEntry.getLatestFileVersion();
}

Date modifiedDate = latestFileVersion.getModifiedDate();

String modifiedDateDescription = LanguageUtil.getTimeDescription(request, System.currentTimeMillis() - modifiedDate.getTime(), true);

PortletURL rowURL = liferayPortletResponse.createRenderURL();

rowURL.setParameter("mvcRenderCommandName", "/document_library/view_file_entry");
rowURL.setParameter("redirect", HttpUtil.removeParameter(currentURL, liferayPortletResponse.getNamespace() + "ajax"));
rowURL.setParameter("fileEntryId", String.valueOf(fileEntry.getFileEntryId()));
%>

<span class="text-default">
	<liferay-ui:message arguments="<%= new String[] {HtmlUtil.escape(latestFileVersion.getUserName()), modifiedDateDescription} %>" key="x-modified-x-ago" />
</span>

<h2 class="h5">
	<aui:a href="<%= rowURL.toString() %>">
		<%= latestFileVersion.getTitle() %>
	</aui:a>

	<c:if test="<%= fileEntry.hasLock() || fileEntry.isCheckedOut() %>">
		<span>
			<aui:icon cssClass="icon-monospaced" image="lock" markupView="lexicon" message="locked" />
		</span>
	</c:if>
</h2>

<span class="h5 text-default">
	<aui:workflow-status markupView="lexicon" showIcon="<%= false %>" showLabel="<%= false %>" status="<%= latestFileVersion.getStatus() %>" />
</span>
<span>
	<%= DLUtil.getAbsolutePath(liferayPortletRequest, fileEntry.getFolderId()).replace(StringPool.RAQUO_CHAR, StringPool.GREATER_THAN) %>
</span>

<c:if test="<%= latestFileVersion.getModel() instanceof DLFileVersion %>">

	<%
	DLFileVersion latestDLFileVersion = (DLFileVersion)latestFileVersion.getModel();

	DLFileEntryType dlFileEntryType = latestDLFileVersion.getDLFileEntryType();
	%>

	<span class="h5 text-default">
		<%= HtmlUtil.escape(dlFileEntryType.getName(locale)) %>
	</span>
</c:if>