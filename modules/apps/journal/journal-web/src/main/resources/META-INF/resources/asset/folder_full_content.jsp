<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/init.jsp" %>

<%
JournalFolder folder = journalDisplayContext.getFolder();
%>

<c:if test="<%= folder != null %>">

	<%
	int entriesCount = JournalArticleServiceUtil.getArticlesCount(scopeGroupId, folder.getFolderId(), journalDisplayContext.getStatus());
	int foldersCount = JournalFolderServiceUtil.getFoldersCount(scopeGroupId, folder.getFolderId(), journalDisplayContext.getStatus());
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

				<div class="lfr-asset-icon">
					<%= foldersCount %> <liferay-ui:message key='<%= (foldersCount == 1) ? "subfolder" : "subfolders" %>' />
				</div>

				<div class="last lfr-asset-icon">
					<%= entriesCount %> <liferay-ui:message key='<%= (entriesCount == 1) ? "article" : "articles" %>' />
				</div>
			</div>

			<liferay-expando:custom-attributes-available
				className="<%= JournalFolder.class.getName() %>"
			>
				<liferay-expando:custom-attribute-list
					className="<%= JournalFolder.class.getName() %>"
					classPK="<%= folder.getFolderId() %>"
					editable="<%= false %>"
					label="<%= true %>"
				/>
			</liferay-expando:custom-attributes-available>
		</aui:col>
	</aui:row>
</c:if>