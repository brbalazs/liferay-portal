/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.uad.test;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestDataConstants;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.List;

/**
 * @author William Newbury
 */
public class DLFileEntryUADTestUtil {

	public static DLFileEntry addDLFileEntry(
			DLAppLocalService dlAppLocalService,
			DLFileEntryLocalService dlFileEntryLocalService,
			DLFolderLocalService dlFolderLocalService, long userId)
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		DLFolder dlFolder = dlFolderLocalService.addFolder(
			userId, TestPropsValues.getGroupId(), TestPropsValues.getGroupId(),
			false, 0L, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), false, serviceContext);

		byte[] bytes = TestDataConstants.TEST_BYTE_ARRAY;

		InputStream is = new ByteArrayInputStream(bytes);

		FileEntry fileEntry = dlAppLocalService.addFileEntry(
			userId, dlFolder.getRepositoryId(), dlFolder.getFolderId(),
			RandomTestUtil.randomString(), ContentTypes.TEXT_PLAIN,
			RandomTestUtil.randomString(), StringPool.BLANK, StringPool.BLANK,
			is, bytes.length, serviceContext);

		return dlFileEntryLocalService.getFileEntry(fileEntry.getFileEntryId());
	}

	public static void cleanUpDependencies(
			DLAppLocalService dlAppLocalService,
			DLFileEntryLocalService dlFileEntryLocalService,
			DLFolderLocalService dlFolderLocalService,
			List<DLFileEntry> dlFileEntries)
		throws Exception {

		for (DLFileEntry dlFileEntry : dlFileEntries) {
			if (dlFileEntryLocalService.fetchDLFileEntry(
					dlFileEntry.getFileEntryId()) != null) {

				dlAppLocalService.deleteFileEntry(dlFileEntry.getFileEntryId());
			}

			dlFolderLocalService.deleteFolder(dlFileEntry.getFolderId());
		}
	}

}