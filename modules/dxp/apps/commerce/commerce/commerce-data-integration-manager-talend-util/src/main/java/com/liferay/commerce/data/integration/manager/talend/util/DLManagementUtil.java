/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.manager.talend.util;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.upload.UniqueFileNameProvider;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(immediate = true, service = DLManagementUtil.class)
public class DLManagementUtil {

	public DLFileEntry addFileEntry(
			long userId, long companyId, long groupId, String taskName,
			String fileName, String contentType, File file)
		throws PortalException {

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setCompanyId(companyId);
		serviceContext.setScopeGroupId(groupId);
		serviceContext.setUserId(userId);

		Folder folder = _getOrCreateFolder(
			userId, groupId, 0L, taskName + "_" + groupId, serviceContext);

		long folderId = folder.getFolderId();

		return _addOrUpdateFile(
			userId, folderId, fileName, file, contentType, serviceContext);
	}

	@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
	public String unzipFile(File archive) throws IOException, PortalException {
		String uncompressedDirectory = "";

		FileInputStream fis = null;

		ZipInputStream zis = null;

		FileOutputStream fos = null;

		File uncompressedFolder = FileUtil.createTempFolder();

		byte[] buffer = new byte[1024];

		try {
			fis = new FileInputStream(archive);

			zis = new ZipInputStream(fis);

			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {
				String fileName = ze.getName();

				uncompressedDirectory = uncompressedFolder.getAbsolutePath();

				File newFile = new File(
					uncompressedDirectory + File.separator + fileName);

				File parentFolder = new File(newFile.getParent());

				parentFolder.mkdirs();

				if (!ze.isDirectory()) {
					fos = new FileOutputStream(newFile);

					int len;

					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
				}

				zis.closeEntry();

				ze = zis.getNextEntry();
			}
		}
		catch (IOException ioe) {
			_log.error(ioe, ioe);
		}
		finally {
			if (zis != null) {
				zis.closeEntry();
				zis.close();
			}

			if (fis != null) {
				fis.close();
			}

			if (fos != null) {
				fos.close();
			}
		}

		return uncompressedDirectory;
	}

	private DLFileEntry _addOrUpdateFile(
			long userId, long folderId, String fileName, File file,
			String mimeType, ServiceContext serviceContext)
		throws PortalException {

		long groupId = serviceContext.getScopeGroupId();

		DLFileEntry fileEntry = null;

		String uniqueFileName = _uniqueFileNameProvider.provide(
			fileName, curFileName -> _exists(groupId, curFileName, folderId));

		fileEntry = _dlFileEntryLocalService.addFileEntry(
			userId, groupId, groupId, folderId, uniqueFileName, mimeType,
			uniqueFileName, uniqueFileName, null, 0, null, file, null, 0,
			serviceContext);

		return fileEntry;
	}

	private boolean _exists(long groupId, String curFileName, long folderId) {
		try {
			if (_dlAppLocalService.getFileEntry(
					groupId, folderId, curFileName) != null) {

				return true;
			}

			return false;
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			return false;
		}
	}

	private Folder _getOrCreateFolder(
			long userId, long repositoryId, long parentFolderId,
			String folderName, ServiceContext serviceContext)
		throws PortalException {

		Folder folder = null;

		try {
			folder = _dlAppLocalService.getFolder(
				repositoryId, parentFolderId, folderName);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			folder = null;
		}

		if (folder == null) {
			folder = _dlAppLocalService.addFolder(
				userId, repositoryId, parentFolderId, folderName, "",
				serviceContext);
		}

		return folder;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		DLManagementUtil.class);

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private DLFileEntryLocalService _dlFileEntryLocalService;

	@Reference
	private UniqueFileNameProvider _uniqueFileNameProvider;

}