<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/document_library/init.jsp" %>

<%
Folder folder = (Folder)request.getAttribute(WebKeys.DOCUMENT_LIBRARY_FOLDER);
%>

<c:if test="<%= folder != null %>">

	<%
	int status = WorkflowConstants.STATUS_APPROVED;

	if (permissionChecker.isContentReviewer(user.getCompanyId(), scopeGroupId)) {
		status = WorkflowConstants.STATUS_ANY;
	}

	int foldersCount = DLAppServiceUtil.getFoldersCount(folder.getRepositoryId(), folder.getFolderId());
	int fileEntriesCount = DLAppServiceUtil.getFileEntriesAndFileShortcutsCount(folder.getRepositoryId(), folder.getFolderId(), status);
	%>

	<aui:row>
		<aui:col cssClass="lfr-asset-column lfr-asset-column-details" width="<%= 100 %>">
			<c:if test="<%= Validator.isNotNull(folder.getDescription()) %>">
				<div class="lfr-asset-description">
					<%= HtmlUtil.replaceNewLine(HtmlUtil.escape(folder.getDescription())) %>
				</div>
			</c:if>

			<div class="lfr-asset-metadata">
				<div class="icon-calendar lfr-asset-icon">
					<liferay-ui:message arguments="<%= dateFormatDateTime.format(folder.getModifiedDate()) %>" key="last-updated-x" translateArguments="<%= false %>" />
				</div>

				<%
				AssetRendererFactory<?> dlFolderAssetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(DLFolder.class.getName());
				%>

				<div class="lfr-asset-icon">
					<liferay-ui:icon
						icon="<%= dlFolderAssetRendererFactory.getIconCssClass() %>"
						markupView="lexicon"
					/>

					<%= foldersCount %> <liferay-ui:message key='<%= (foldersCount == 1) ? "subfolder" : "subfolders" %>' />
				</div>

				<%
				AssetRendererFactory<?> dlFileEntryAssetRendererFactory = AssetRendererFactoryRegistryUtil.getAssetRendererFactoryByClassName(DLFileEntry.class.getName());
				%>

				<div class="last lfr-asset-icon">
					<liferay-ui:icon
						icon="<%= dlFileEntryAssetRendererFactory.getIconCssClass() %>"
						markupView="lexicon"
					/>

					<%= fileEntriesCount %> <liferay-ui:message key='<%= (fileEntriesCount == 1) ? "document" : "documents" %>' />
				</div>
			</div>

			<liferay-expando:custom-attributes-available
				className="<%= DLFolderConstants.getClassName() %>"
			>
				<liferay-expando:custom-attribute-list
					className="<%= DLFolderConstants.getClassName() %>"
					classPK="<%= (folder != null) ? folder.getFolderId() : 0 %>"
					editable="<%= false %>"
					label="<%= true %>"
				/>
			</liferay-expando:custom-attributes-available>
		</aui:col>
	</aui:row>
</c:if>