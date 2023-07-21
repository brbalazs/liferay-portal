<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/message_boards/init.jsp" %>

<%
MBMessage message = (MBMessage)request.getAttribute(WebKeys.MESSAGE_BOARDS_MESSAGE);

long categoryId = MBUtil.getCategoryId(request, message);

int deletedAttachmentsFileEntriesCount = 0;

if (message != null) {
	deletedAttachmentsFileEntriesCount = message.getDeletedAttachmentsFileEntriesCount();
}
%>

<div class="lfr-dynamic-uploader" id="<portlet:namespace />uploaderContainer">
	<div class="lfr-upload-container" id="<portlet:namespace />fileUpload"></div>
</div>

<span id="<portlet:namespace />selectedFileNameContainer"></span>

<div class="hide" id="<portlet:namespace />metadataExplanationContainer"></div>

<div class="hide selected" id="<portlet:namespace />selectedFileNameMetadataContainer"></div>

<c:if test="<%= (deletedAttachmentsFileEntriesCount > 0) && trashHelper.isTrashEnabled(scopeGroupId) && MBMessagePermission.contains(permissionChecker, message, ActionKeys.UPDATE) %>">
	<portlet:renderURL var="viewTrashAttachmentsURL" windowState="<%= LiferayWindowState.POP_UP.toString() %>">
		<portlet:param name="mvcRenderCommandName" value="/message_boards/view_deleted_message_attachments" />
		<portlet:param name="redirect" value="<%= currentURL %>" />
		<portlet:param name="messageId" value="<%= String.valueOf(message.getMessageId()) %>" />
	</portlet:renderURL>

	<div align="right">
		<a href="javascript:;" id="view-removed-attachments-link"><liferay-ui:message arguments="<%= deletedAttachmentsFileEntriesCount %>" key='<%= (deletedAttachmentsFileEntriesCount == 1) ? "x-recently-removed-attachment" : "x-recently-removed-attachments" %>' /> &raquo;</a>
	</div>

	<aui:script use="liferay-util-window">
		var viewRemovedAttachmentsLink = A.one('#view-removed-attachments-link');

		viewRemovedAttachmentsLink.on('click', function(event) {
			Liferay.Util.openWindow({
				id: '<portlet:namespace />openRemovedPageAttachments',
				title: '<%= LanguageUtil.get(request, "removed-attachments") %>',
				uri: '<%= viewTrashAttachmentsURL %>'
			});
		});
	</aui:script>
</c:if>

<%
Date expirationDate = new Date(System.currentTimeMillis() + (GetterUtil.getInteger(PropsUtil.get(PropsKeys.SESSION_TIMEOUT)) * Time.MINUTE));

Ticket ticket = TicketLocalServiceUtil.addTicket(user.getCompanyId(), User.class.getName(), user.getUserId(), TicketConstants.TYPE_IMPERSONATE, null, expirationDate, new ServiceContext());
%>

<aui:script use="liferay-portlet-url,liferay-upload">
	var uploader = new Liferay.Upload({
		boundingBox: '#<portlet:namespace />fileUpload',

		<%
		DecimalFormatSymbols decimalFormatSymbols = DecimalFormatSymbols.getInstance(locale);
		%>

		decimalSeparator: '<%= decimalFormatSymbols.getDecimalSeparator() %>',
		deleteFile:
			'<liferay-portlet:actionURL doAsUserId="<%= user.getUserId() %>" name="/message_boards/edit_message_attachments"><portlet:param name="<%= Constants.CMD %>" value="<%= Constants.DELETE_TEMP %>" /><portlet:param name="categoryId" value="<%= String.valueOf(categoryId) %>" /></liferay-portlet:actionURL>&ticketKey=<%= ticket.getKey() %><liferay-ui:input-permissions-params modelName="<%= MBMessage.class.getName() %>" />',
		fallback: '#<portlet:namespace />fallback',

		<%
		DLConfiguration dlConfiguration = ConfigurationProviderUtil.getSystemConfiguration(DLConfiguration.class);
		%>

		fileDescription:
			'<%= StringUtil.merge(dlConfiguration.fileExtensions()) %>',
		maxFileSize: '<%= dlConfiguration.fileMaxSize() %> ',
		namespace: '<portlet:namespace />',
		rootElement: '#<portlet:namespace />uploaderContainer',
		tempFileURL: {
			method: Liferay.Service.bind('/mb.mbmessage/get-temp-attachment-names'),
			params: {
				groupId: <%= scopeGroupId %>,
				folderName: '<%= MBMessageConstants.TEMP_FOLDER_NAME %>'
			}
		},
		tempRandomSuffix: '<%= TempFileEntryUtil.TEMP_RANDOM_SUFFIX %>',
		uploadFile:
			'<liferay-portlet:actionURL doAsUserId="<%= user.getUserId() %>" name="/message_boards/edit_message_attachments"><portlet:param name="<%= Constants.CMD %>" value="<%= Constants.ADD_TEMP %>" /><portlet:param name="categoryId" value="<%= String.valueOf(categoryId) %>" /></liferay-portlet:actionURL>&ticketKey=<%= ticket.getKey() %><liferay-ui:input-permissions-params modelName="<%= MBMessage.class.getName() %>" />'
	});
</aui:script>