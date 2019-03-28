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

package com.liferay.commerce.data.integration.manager.internal.helper;

import com.liferay.commerce.data.integration.manager.configuration.DataIntegrationProcessConfiguration;
import com.liferay.commerce.data.integration.manager.exception.FileEntryValidationException;
import com.liferay.commerce.data.integration.manager.helper.DataIntegrationProcessFileEntryUploadActionHelper;
import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFileVersion;
import com.liferay.document.library.kernel.service.DLAppLocalService;
import com.liferay.document.library.kernel.service.DLFileEntryLocalService;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.upload.UniqueFileNameProvider;

import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.Map;

import javax.portlet.ActionRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;

/**
 * @author guywandji
 */
@Component(
	configurationPid = "com.liferay.commerce.data.integration.manager.configuration.DataIntegrationProcessConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	service = DataIntegrationProcessFileEntryUploadActionHelper.class
)
public class DataIntegrationProcessFileEntryUploadActionHelperImpl
	implements DataIntegrationProcessFileEntryUploadActionHelper {

	public DLFileEntry upload(
			ActionRequest actionRequest, String fileNameParameter)
		throws IOException, PortalException {

		DLFileEntry dlFileEntry = null;

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

		ThemeDisplay themeDisplay =
			(ThemeDisplay)uploadPortletRequest.getAttribute(
				WebKeys.THEME_DISPLAY);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			uploadPortletRequest);

		String fileName = uploadPortletRequest.getFileName(fileNameParameter);

		if (Validator.isFileName(fileName)) {
			long size = uploadPortletRequest.getSize(fileNameParameter);

			try {
				_validateFile(fileName, size);
			}
			catch (Exception e) {
				_log.error(e, e);

				throw new PortalException(e.getMessage());
			}

			String contentType = uploadPortletRequest.getContentType(
				fileNameParameter);

			try (InputStream inputStream = uploadPortletRequest.getFileAsStream(
					fileNameParameter)) {

				dlFileEntry = addFileEntry(
					fileName, contentType, inputStream, themeDisplay,
					serviceContext);
			}
		}

		return dlFileEntry;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_dataIntegrationProcessConfiguration =
			ConfigurableUtil.createConfigurable(
				DataIntegrationProcessConfiguration.class, properties);
	}

	protected DLFileEntry addFileEntry(
			String fileName, String contentType, InputStream inputStream,
			ThemeDisplay themeDisplay, ServiceContext serviceContext)
		throws PortalException {

		Folder folder = _getOrCreateFolder(
			themeDisplay.getScopeGroupId(), 0L,
			_PREFIX_FOLDER_NAME_ + themeDisplay.getScopeGroupId(),
			serviceContext);

		long folderId = folder.getFolderId();

		return _addOrUpdateFile(
			folderId, fileName, inputStream, contentType, serviceContext);
	}

	@Reference
	protected DLAppLocalService dlAppLocalService;

	@Reference
	protected DLFileEntryLocalService dlFileEntryLocalService;

	private DLFileEntry _addOrUpdateFile(
			long folderId, String fileName, InputStream inStream,
			String mimeType, ServiceContext serviceContext)
		throws PortalException {

		long groupId = serviceContext.getScopeGroupId();
		DLFileEntry fileEntry = null;

		DLFileEntry dlFileEntry = dlFileEntryLocalService.fetchFileEntry(
			groupId, folderId, fileName);

		if (dlFileEntry == null) {
			fileEntry = dlFileEntryLocalService.addFileEntry(
				serviceContext.getUserId(), groupId, groupId, folderId,
				fileName, mimeType, fileName, fileName, null, 0, null, null,
				inStream, 0, serviceContext);
		}
		else {
			fileEntry = dlFileEntryLocalService.updateFileEntry(
				serviceContext.getUserId(), dlFileEntry.getFileEntryId(),
				fileName, mimeType, fileName, fileName, "", true, 0L, null,
				null, inStream, 0L, serviceContext);
		}

		DLFileVersion dlFileVersion = fileEntry.getLatestFileVersion(false);

		dlFileEntryLocalService.updateStatus(
			serviceContext.getUserId(), dlFileVersion.getFileVersionId(),
			WorkflowConstants.STATUS_APPROVED, serviceContext,
			Collections.emptyMap());

		return fileEntry;
	}

	private Folder _getOrCreateFolder(
			long repositoryId, long parentFolderId, String folderName,
			ServiceContext serviceContext)
		throws PortalException {

		Folder folder = null;

		try {
			folder = dlAppLocalService.getFolder(
				repositoryId, parentFolderId, folderName);
		}
		catch (PortalException pe) {
			if (_log.isDebugEnabled()) {
				_log.debug(pe, pe);
			}

			folder = null;
		}

		if (folder == null) {
			folder = dlAppLocalService.addFolder(
				serviceContext.getUserId(), repositoryId, parentFolderId,
				folderName, "", serviceContext);
		}

		return folder;
	}

	private void _validateFile(String fileName, long size) throws Exception {
		if ((_dataIntegrationProcessConfiguration.imageMaxSize() > 0) &&
			(size > _dataIntegrationProcessConfiguration.imageMaxSize())) {

			throw new FileEntryValidationException(
				"File size exceed configured limit");
		}

		String extension = FileUtil.getExtension(fileName);

		String[] imageExtensions =
			_dataIntegrationProcessConfiguration.imageExtensions();

		for (String imageExtension : imageExtensions) {
			if (StringPool.STAR.equals(imageExtension) ||
				imageExtension.equals(StringPool.PERIOD + extension)) {

				return;
			}
		}

		throw new FileEntryValidationException(
			"Invalid image for file name " + fileName);
	}

	private static final String _PREFIX_FOLDER_NAME_ = "PROCESSES_";

	private static final Log _log = LogFactoryUtil.getLog(
		DataIntegrationProcessFileEntryUploadActionHelperImpl.class);

	private volatile DataIntegrationProcessConfiguration
		_dataIntegrationProcessConfiguration;

	@Reference
	private Portal _portal;

	@Reference
	private UniqueFileNameProvider _uniqueFileNameProvider;

}