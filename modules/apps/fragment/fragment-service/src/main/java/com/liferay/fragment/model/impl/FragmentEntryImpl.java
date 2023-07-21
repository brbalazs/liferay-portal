/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.model.impl;

import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.util.DLUtil;
import com.liferay.fragment.constants.FragmentExportImportConstants;
import com.liferay.fragment.service.FragmentEntryLinkLocalServiceUtil;
import com.liferay.fragment.util.FragmentEntryRenderUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.zip.ZipWriter;

/**
 * @author Eudaldo Alonso
 */
public class FragmentEntryImpl extends FragmentEntryBaseImpl {

	@Override
	public String getContent() {
		return FragmentEntryRenderUtil.renderFragmentEntry(this);
	}

	@Override
	public String getImagePreviewURL(ThemeDisplay themeDisplay) {
		if (getPreviewFileEntryId() <= 0) {
			return StringPool.BLANK;
		}

		try {
			FileEntry fileEntry = DLAppLocalServiceUtil.getFileEntry(
				getPreviewFileEntryId());

			if (fileEntry == null) {
				return StringPool.BLANK;
			}

			return DLUtil.getImagePreviewURL(fileEntry, themeDisplay);
		}
		catch (Exception e) {
			_log.error("Unable to get preview entry image URL", e);
		}

		return StringPool.BLANK;
	}

	@Override
	public int getUsageCount() {
		return FragmentEntryLinkLocalServiceUtil.getFragmentEntryLinksCount(
			getGroupId(), getFragmentEntryId());
	}

	@Override
	public void populateZipWriter(ZipWriter zipWriter, String path)
		throws Exception {

		path = path + StringPool.SLASH + getFragmentEntryKey();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("cssPath", "src/index.css");
		jsonObject.put("htmlPath", "src/index.html");
		jsonObject.put("jsPath", "src/index.js");
		jsonObject.put("name", getName());

		zipWriter.addEntry(
			path + StringPool.SLASH +
				FragmentExportImportConstants.FILE_NAME_FRAGMENT_CONFIG,
			jsonObject.toString());

		zipWriter.addEntry(path + "/src/index.css", getCss());
		zipWriter.addEntry(path + "/src/index.js", getJs());
		zipWriter.addEntry(path + "/src/index.html", getHtml());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		FragmentEntryImpl.class);

}