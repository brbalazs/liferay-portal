<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
int abstractLength = GetterUtil.getInteger(request.getAttribute(WebKeys.ASSET_ENTRY_ABSTRACT_LENGTH), AssetHelper.ASSET_ENTRY_ABSTRACT_LENGTH);
AssetRenderer<?> assetRenderer = (AssetRenderer<?>)request.getAttribute(WebKeys.ASSET_RENDERER);

FileEntry fileEntry = (FileEntry)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_ENTRY);

FileVersion fileVersion = (FileVersion)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_VERSION);

boolean showThumbnail = false;

if (Objects.equals(fileEntry.getVersion(), fileVersion.getVersion())) {
	showThumbnail = true;
}
%>

<c:if test="<%= fileVersion.isApproved() %>">
	<div class="asset-resource-info">
		<c:choose>
			<c:when test="<%= showThumbnail && ImageProcessorUtil.hasImages(fileVersion) %>">
				<div class="asset-thumbnail">
					<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="image" />" class="img-thumbnail" src="<%= DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, "&imageThumbnail=1") %>" style="<%= DLUtil.getThumbnailStyle(true, 0, 128, 128) %>" />
				</div>

				<p class="asset-description">
					<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(StringUtil.shorten(fileEntry.getDescription(), abstractLength))) %>
				</p>
			</c:when>
			<c:when test="<%= showThumbnail && PDFProcessorUtil.hasImages(fileVersion) %>">
				<div class="asset-thumbnail">
					<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="document" />" class="img-thumbnail" src="<%= DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, "&documentThumbnail=1") %>" style="<%= DLUtil.getThumbnailStyle(true, 0, 128, 128) %>" />
				</div>

				<p class="asset-description">
					<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(StringUtil.shorten(fileEntry.getDescription(), abstractLength))) %>
				</p>
			</c:when>
			<c:when test="<%= showThumbnail && VideoProcessorUtil.hasVideo(fileVersion) %>">
				<div class="asset-thumbnail">
					<img alt="<liferay-ui:message escapeAttribute="<%= true %>" key="video" />" class="img-thumbnail" src="<%= DLUtil.getPreviewURL(fileEntry, fileVersion, themeDisplay, "&videoThumbnail=1") %>" style="<%= DLUtil.getThumbnailStyle(true, 0, 128, 128) %>" />
				</div>

				<p class="asset-description">
					<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(StringUtil.shorten(fileEntry.getDescription(), abstractLength))) %>
				</p>
			</c:when>
			<c:otherwise>
				<p class="asset-description">
					<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(StringUtil.shorten(fileEntry.getDescription(), abstractLength))) %>
				</p>

				<%
				String taglibFileEntryTitle = "<span class='hide-accessible'>" + fileEntry.getTitle() + "</span>";
				%>

				<liferay-ui:icon
					iconCssClass="icon-download"
					label="<%= true %>"
					message='<%= LanguageUtil.format(request, "download-x", taglibFileEntryTitle, false) + " (" + LanguageUtil.formatStorageSize(fileVersion.getSize(), locale) + ")" %>'
					method="get"
					url="<%= assetRenderer.getURLDownload(themeDisplay) %>"
				/>
			</c:otherwise>
		</c:choose>
	</div>
</c:if>