<%--
/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */
--%>

<%@ include file="/item/selector/init.jsp" %>

<%
DLMimeTypeDisplayContext dlMimeTypeDisplayContext = (DLMimeTypeDisplayContext)request.getAttribute(WikiItemSelectorWebKeys.DL_MIME_TYPE_DISPLAY_CONTEXT);
WikiAttachmentItemSelectorViewDisplayContext wikiAttachmentItemSelectorViewDisplayContext = (WikiAttachmentItemSelectorViewDisplayContext)request.getAttribute(WikiItemSelectorWebKeys.WIKI_ATTACHMENT_ITEM_SELECTOR_VIEW_DISPLAY_CONTEXT);

int cur = ParamUtil.getInteger(request, SearchContainer.DEFAULT_CUR_PARAM, SearchContainer.DEFAULT_CUR);
int delta = ParamUtil.getInteger(request, SearchContainer.DEFAULT_DELTA_PARAM, SearchContainer.DEFAULT_DELTA);

int[] startAndEnd = SearchPaginationUtil.calculateStartAndEnd(cur, delta);

int start = startAndEnd[0];
int end = startAndEnd[1];

WikiPage wikiPage = wikiAttachmentItemSelectorViewDisplayContext.getWikiPage();

List portletFileEntries = null;
int portletFileEntriesCount = 0;

String[] mimeTypes = wikiAttachmentItemSelectorViewDisplayContext.getMimeTypes();

if (wikiPage.getAttachmentsFolderId() != DLFolderConstants.DEFAULT_PARENT_FOLDER_ID) {
	if (wikiAttachmentItemSelectorViewDisplayContext.isSearch()) {
		SearchContext searchContext = SearchContextFactory.getInstance(request);

		if (ArrayUtil.isNotEmpty(mimeTypes)) {
			searchContext.setAttribute("mimeTypes", mimeTypes);
		}

		searchContext.setEnd(end);
		searchContext.setFolderIds(new long[] {wikiPage.getAttachmentsFolderId()});
		searchContext.setStart(start);

		Folder folder = PortletFileRepositoryUtil.getPortletFolder(wikiPage.getAttachmentsFolderId());

		Hits hits = PortletFileRepositoryUtil.searchPortletFileEntries(folder.getRepositoryId(), searchContext);

		portletFileEntriesCount = hits.getLength();

		Document[] docs = hits.getDocs();

		portletFileEntries = new ArrayList(docs.length);

		for (Document doc : docs) {
			long fileEntryId = GetterUtil.getLong(doc.get(Field.ENTRY_CLASS_PK));

			FileEntry fileEntry = null;

			try {
				fileEntry = PortletFileRepositoryUtil.getPortletFileEntry(fileEntryId);
			}
			catch (Exception e) {
				if (_log.isWarnEnabled()) {
					_log.warn("Documents and Media search index is stale and contains file entry {" + fileEntryId + "}");
				}

				continue;
			}

			portletFileEntries.add(fileEntry);
		}
	}
	else {
		String orderByCol = ParamUtil.getString(request, "orderByCol", "title");
		String orderByType = ParamUtil.getString(request, "orderByType", "asc");

		OrderByComparator<FileEntry> orderByComparator = DLUtil.getRepositoryModelOrderByComparator(orderByCol, orderByType);

		if (ArrayUtil.isNotEmpty(mimeTypes)) {
			portletFileEntries = wikiPage.getAttachmentsFileEntries(mimeTypes, start, end, orderByComparator);
			portletFileEntriesCount = wikiPage.getAttachmentsFileEntriesCount(mimeTypes);
		}
		else {
			portletFileEntries = wikiPage.getAttachmentsFileEntries(start, end, orderByComparator);
			portletFileEntriesCount = wikiPage.getAttachmentsFileEntriesCount();
		}
	}
}
%>

<liferay-item-selector:repository-entry-browser
	dlMimeTypeDisplayContext="<%= dlMimeTypeDisplayContext %>"
	emptyResultsMessage='<%= LanguageUtil.get(resourceBundle, "there-are-no-wiki-attachments") %>'
	itemSelectedEventName="<%= wikiAttachmentItemSelectorViewDisplayContext.getItemSelectedEventName() %>"
	itemSelectorReturnTypeResolver="<%= wikiAttachmentItemSelectorViewDisplayContext.getItemSelectorReturnTypeResolver() %>"
	portletURL="<%= wikiAttachmentItemSelectorViewDisplayContext.getPortletURL(request, liferayPortletResponse) %>"
	repositoryEntries="<%= portletFileEntries %>"
	repositoryEntriesCount="<%= portletFileEntriesCount %>"
	showDragAndDropZone="<%= false %>"
	tabName="<%= wikiAttachmentItemSelectorViewDisplayContext.getTitle(locale) %>"
	uploadURL="<%= wikiAttachmentItemSelectorViewDisplayContext.getUploadURL(liferayPortletResponse) %>"
/>

<%!
private static Log _log = LogFactoryUtil.getLog("com_liferay_wiki_web.item.selector.wiki_page_attachments_jsp");
%>