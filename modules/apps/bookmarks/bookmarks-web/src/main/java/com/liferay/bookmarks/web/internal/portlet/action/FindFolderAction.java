/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.bookmarks.web.internal.portlet.action;

import com.liferay.bookmarks.constants.BookmarksPortletKeys;
import com.liferay.bookmarks.model.BookmarksFolder;
import com.liferay.bookmarks.service.BookmarksFolderLocalService;
import com.liferay.portal.kernel.struts.StrutsAction;
import com.liferay.portal.struts.FindStrutsAction;

import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alexander Chow
 */
@Component(
	property = "path=/bookmarks/find_folder", service = StrutsAction.class
)
public class FindFolderAction extends FindStrutsAction {

	@Override
	protected long getGroupId(long primaryKey) throws Exception {
		BookmarksFolder folder = _bookmarksFolderLocalService.getFolder(
			primaryKey);

		return folder.getGroupId();
	}

	@Override
	protected String getPrimaryKeyParameterName() {
		return "folderId";
	}

	@Override
	protected String getStrutsAction(
		HttpServletRequest request, String portletId) {

		return "/bookmarks/view_folder";
	}

	@Override
	protected String[] initPortletIds() {
		return new String[] {BookmarksPortletKeys.BOOKMARKS};
	}

	@Reference(unbind = "-")
	protected void setBookmarksFolderLocalService(
		BookmarksFolderLocalService bookmarksFolderLocalService) {

		_bookmarksFolderLocalService = bookmarksFolderLocalService;
	}

	private BookmarksFolderLocalService _bookmarksFolderLocalService;

}