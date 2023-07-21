/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.html.preview.model.impl;

import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;

/**
 * @author Pavel Savinov
 */
public class HtmlPreviewEntryImpl extends HtmlPreviewEntryBaseImpl {

	@Override
	public String getImagePreviewURL(ThemeDisplay themeDisplay) {
		long fileEntryId = getFileEntryId();

		if (fileEntryId <= 0) {
			return StringPool.BLANK;
		}

		try {
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
				fileEntryId);

			return DLUtil.getImagePreviewURL(fileEntry, themeDisplay);
		}
		catch (Exception e) {
			_log.error("Unable to get HTML preview entry image URL", e);
		}

		return StringPool.BLANK;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		HtmlPreviewEntryImpl.class);

}