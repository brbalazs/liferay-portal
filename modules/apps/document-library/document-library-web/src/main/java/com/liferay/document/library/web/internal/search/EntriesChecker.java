/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.web.internal.search;

import com.liferay.document.library.kernel.exception.NoSuchFileEntryException;
import com.liferay.document.library.kernel.exception.NoSuchFileShortcutException;
import com.liferay.document.library.kernel.model.DLFileShortcut;
import com.liferay.document.library.kernel.service.DLAppServiceUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.kernel.exception.NoSuchRepositoryEntryException;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileShortcut;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergio González
 */
public class EntriesChecker extends EmptyOnClickRowChecker {

	public EntriesChecker(
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse) {

		super(liferayPortletResponse);

		_liferayPortletResponse = liferayPortletResponse;

		ThemeDisplay themeDisplay =
			(ThemeDisplay)liferayPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		_permissionChecker = themeDisplay.getPermissionChecker();
	}

	@Override
	public String getAllRowsCheckBox() {
		return null;
	}

	@Override
	public String getAllRowsCheckBox(HttpServletRequest request) {
		return null;
	}

	@Override
	public String getRowCheckBox(
		HttpServletRequest request, boolean checked, boolean disabled,
		String primaryKey) {

		FileEntry fileEntry = null;
		FileShortcut fileShortcut = null;
		Folder folder = null;

		long entryId = GetterUtil.getLong(primaryKey);

		try {
			fileEntry = DLAppServiceUtil.getFileEntry(entryId);
		}
		catch (Exception e1) {
			if (e1 instanceof NoSuchFileEntryException ||
				e1 instanceof NoSuchRepositoryEntryException) {

				try {
					fileShortcut = DLAppServiceUtil.getFileShortcut(entryId);
				}
				catch (Exception e2) {
					if (e2 instanceof NoSuchFileShortcutException) {
						try {
							folder = DLAppServiceUtil.getFolder(entryId);
						}
						catch (Exception e3) {
							return StringPool.BLANK;
						}
					}
					else {
						return StringPool.BLANK;
					}
				}
			}
			else {
				return StringPool.BLANK;
			}
		}

		String name = null;

		if (fileEntry != null) {
			name = FileEntry.class.getSimpleName();
		}
		else if (fileShortcut != null) {
			name = DLFileShortcut.class.getSimpleName();
		}
		else if (folder != null) {
			name = Folder.class.getSimpleName();
		}

		String checkBoxRowIds = getEntryRowIds();

		String checkBoxAllRowIds = "'#" + getAllRowIds() + "'";
		String checkBoxPostOnClick =
			_liferayPortletResponse.getNamespace() + "toggleActionsButton();";

		return getRowCheckBox(
			request, checked, disabled,
			_liferayPortletResponse.getNamespace() + RowChecker.ROW_IDS + name,
			primaryKey, checkBoxRowIds, checkBoxAllRowIds, checkBoxPostOnClick);
	}

	protected String getEntryRowIds() {
		StringBundler sb = new StringBundler(13);

		sb.append("['");
		sb.append(_liferayPortletResponse.getNamespace());
		sb.append(RowChecker.ROW_IDS);
		sb.append(Folder.class.getSimpleName());
		sb.append("', '");
		sb.append(_liferayPortletResponse.getNamespace());
		sb.append(RowChecker.ROW_IDS);
		sb.append(DLFileShortcut.class.getSimpleName());
		sb.append("', '");
		sb.append(_liferayPortletResponse.getNamespace());
		sb.append(RowChecker.ROW_IDS);
		sb.append(FileEntry.class.getSimpleName());
		sb.append("']");

		return sb.toString();
	}

	private final LiferayPortletResponse _liferayPortletResponse;
	private final PermissionChecker _permissionChecker;

}