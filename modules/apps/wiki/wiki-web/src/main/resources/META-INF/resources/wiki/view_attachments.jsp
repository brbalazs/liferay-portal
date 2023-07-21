<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/wiki/init.jsp" %>

<%
final WikiPage wikiPage = (WikiPage)request.getAttribute(WikiWebKeys.WIKI_PAGE);
%>

<c:if test="<%= wikiPage.getAttachmentsFileEntriesCount() > 0 %>">
	<div class="page-attachments">
		<h5><liferay-ui:message key="attachments" /></h5>

		<div class="row">

			<%
			List<FileEntry> attachmentsFileEntries = wikiPage.getAttachmentsFileEntries();

			DLMimeTypeDisplayContext dlMimeTypeDisplayContext = (DLMimeTypeDisplayContext)request.getAttribute(WikiWebKeys.DL_MIME_TYPE_DISPLAY_CONTEXT);

			for (FileEntry fileEntry : attachmentsFileEntries) {
			%>

				<div class="col-md-4">
					<liferay-frontend:horizontal-card
						text="<%= fileEntry.getTitle() %>"
						url='<%= PortletFileRepositoryUtil.getDownloadPortletFileEntryURL(themeDisplay, fileEntry, "status=" + WorkflowConstants.STATUS_APPROVED) %>'
					>
						<liferay-frontend:horizontal-card-col>
							<span class="icon-monospaced sticker <%= (dlMimeTypeDisplayContext != null) ? dlMimeTypeDisplayContext.getCssClassFileMimeType(fileEntry.getMimeType()) : "file-icon-color-0" %>"><%= StringUtil.shorten(StringUtil.upperCase(fileEntry.getExtension()), 3, StringPool.BLANK) %></span>
						</liferay-frontend:horizontal-card-col>
					</liferay-frontend:horizontal-card>
				</div>

			<%
			}
			%>

		</div>
	</div>
</c:if>