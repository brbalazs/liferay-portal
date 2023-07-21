/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.fragment.model.impl;

import com.liferay.fragment.constants.FragmentExportImportConstants;
import com.liferay.fragment.model.FragmentEntry;
import com.liferay.fragment.service.FragmentEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.zip.ZipWriter;

import java.util.List;

/**
 * @author Eudaldo Alonso
 */
public class FragmentCollectionImpl extends FragmentCollectionBaseImpl {

	@Override
	public void populateZipWriter(ZipWriter zipWriter) throws Exception {
		String path = StringPool.SLASH + getFragmentCollectionKey();

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		jsonObject.put("description", getDescription());
		jsonObject.put("name", getName());

		zipWriter.addEntry(
			path + StringPool.SLASH +
				FragmentExportImportConstants.FILE_NAME_COLLECTION_CONFIG,
			jsonObject.toString());

		List<FragmentEntry> fragmentEntries =
			FragmentEntryLocalServiceUtil.getFragmentEntries(
				getFragmentCollectionId(), QueryUtil.ALL_POS,
				QueryUtil.ALL_POS);

		for (FragmentEntry fragmentEntry : fragmentEntries) {
			fragmentEntry.populateZipWriter(zipWriter, path);
		}
	}

}