<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
FileVersion fileVersion = (FileVersion)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FILE_VERSION);

int status = ParamUtil.getInteger(request, "status", WorkflowConstants.STATUS_ANY);

FileEntry fileEntry = fileVersion.getFileEntry();

boolean hasAudio = AudioProcessorUtil.hasAudio(fileVersion);
boolean hasImages = ImageProcessorUtil.hasImages(fileVersion);

boolean hasPDFImages = PDFProcessorUtil.hasImages(fileVersion);
boolean hasVideo = VideoProcessorUtil.hasVideo(fileVersion);

boolean showImageContainer = true;
%>

<%@ include file="/document_library/view_file_entry_preview.jspf" %>