/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.repository.model.FileEntry;

import java.io.File;
import java.io.InputStream;

/**
 * @author     Sergio González
 * @author     Matthew Kong
 * @author     Alexander Chow
 * @author     Iván Zaera
 * @deprecated As of Wilberforce (7.0.x), replaced by {@link TempFileEntryUtil}
 */
@Deprecated
public class TempFileUtil {

	public static FileEntry addTempFileEntry(
			long groupId, long userId, String fileName, String tempFolderName,
			File file, String mimeType)
		throws PortalException {

		return TempFileEntryUtil.addTempFileEntry(
			groupId, userId, fileName, tempFolderName, file, mimeType);
	}

	public static FileEntry addTempFileEntry(
			long groupId, long userId, String fileName, String tempFolderName,
			InputStream inputStream, String mimeType)
		throws PortalException {

		return TempFileEntryUtil.addTempFileEntry(
			groupId, userId, fileName, tempFolderName, inputStream, mimeType);
	}

	public static void deleteTempFileEntry(long fileEntryId)
		throws PortalException {

		TempFileEntryUtil.deleteTempFileEntry(fileEntryId);
	}

	public static void deleteTempFileEntry(
			long groupId, long userId, String folderName, String fileName)
		throws PortalException {

		TempFileEntryUtil.deleteTempFileEntry(
			groupId, userId, folderName, fileName);
	}

	public static FileEntry getTempFileEntry(
			long groupId, long userId, String folderName, String fileName)
		throws PortalException {

		return TempFileEntryUtil.getTempFileEntry(
			groupId, userId, folderName, fileName);
	}

	public static String[] getTempFileNames(
			long groupId, long userId, String folderName)
		throws PortalException {

		return TempFileEntryUtil.getTempFileNames(groupId, userId, folderName);
	}

}