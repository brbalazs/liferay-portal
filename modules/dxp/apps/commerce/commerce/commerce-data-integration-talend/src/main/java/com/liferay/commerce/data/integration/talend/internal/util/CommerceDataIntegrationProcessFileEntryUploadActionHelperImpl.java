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

package com.liferay.commerce.data.integration.talend.internal.util;

import com.liferay.commerce.data.integration.exception.FileEntryValidationException;
import com.liferay.commerce.data.integration.talend.internal.configuration.CommerceDataIntegrationProcessConfiguration;
import com.liferay.commerce.data.integration.trigger.CommerceDataIntegrationProcessFileEntryUploadActionHelper;
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
 * @author Alessio Antonio Rendina
 */
@Component(
	configurationPid = "com.liferay.commerce.data.integration.talend.internal.configuration.CommerceDataIntegrationProcessConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL,
	service = CommerceDataIntegrationProcessFileEntryUploadActionHelper.class
)
public class CommerceDataIntegrationProcessFileEntryUploadActionHelperImpl
	implements CommerceDataIntegrationProcessFileEntryUploadActionHelper {

	public DLFileEntry upload(
			ActionRequest actionRequest, String fileNameParameter)
		throws IOException, PortalException {

		DLFileEntry dlFileEntry = null;

		UploadPortletRequest uploadPortletRequest =
			_portal.getUploadPortletRequest(actionRequest);

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

			try (InputStream inputStream = uploadPortletRequest.getFileAsStream(
					fileNameParameter)) {

				ThemeDisplay themeDisplay =
					(ThemeDisplay)uploadPortletRequest.getAttribute(
						WebKeys.THEME_DISPLAY);

				String contentType = uploadPortletRequest.getContentType(
					fileNameParameter);

				ServiceContext serviceContext =
					ServiceContextFactory.getInstance(uploadPortletRequest);

				dlFileEntry = addFileEntry(
					fileName, contentType, inputStream, themeDisplay,
					serviceContext);
			}
		}

		return dlFileEntry;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_commerceDataIntegrationProcessConfiguration =
			ConfigurableUtil.createConfigurable(
				CommerceDataIntegrationProcessConfiguration.class, properties);
	}

	protected DLFileEntry addFileEntry(
			String fileName, String contentType, InputStream inputStream,
			ThemeDisplay themeDisplay, ServiceContext serviceContext)
		throws PortalException {

		Folder folder = _getOrCreateFolder(
			themeDisplay.getScopeGroupId(), 0L,
			_PREFIX_FOLDER_NAME_ + themeDisplay.getScopeGroupId(),
			serviceContext);

		return _addOrUpdateFile(
			folder.getFolderId(), fileName, inputStream, contentType,
			serviceContext);
	}

	@Reference
	protected DLAppLocalService dlAppLocalService;

	@Reference
	protected DLFileEntryLocalService dlFileEntryLocalService;

	private DLFileEntry _addOrUpdateFile(
			long folderId, String fileName, InputStream inputStream,
			String mimeType, ServiceContext serviceContext)
		throws PortalException {

		DLFileEntry fileEntry = null;

		long groupId = serviceContext.getScopeGroupId();

		DLFileEntry dlFileEntry = dlFileEntryLocalService.fetchFileEntry(
			groupId, folderId, fileName);

		if (dlFileEntry == null) {
			fileEntry = dlFileEntryLocalService.addFileEntry(
				serviceContext.getUserId(), groupId, groupId, folderId,
				fileName, mimeType, fileName, fileName, null, 0, null, null,
				inputStream, 0, serviceContext);
		}
		else {
			fileEntry = dlFileEntryLocalService.updateFileEntry(
				serviceContext.getUserId(), dlFileEntry.getFileEntryId(),
				fileName, mimeType, fileName, fileName, StringPool.BLANK, true,
				0L, null, null, inputStream, 0L, serviceContext);
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
		}

		if (folder == null) {
			folder = dlAppLocalService.addFolder(
				serviceContext.getUserId(), repositoryId, parentFolderId,
				folderName, StringPool.BLANK, serviceContext);
		}

		return folder;
	}

	private void _validateFile(String fileName, long size) throws Exception {
		if ((_commerceDataIntegrationProcessConfiguration.imageMaxSize() > 0) &&
			(size >
				_commerceDataIntegrationProcessConfiguration.imageMaxSize())) {

			throw new FileEntryValidationException(
				"File size exceeds configured limit");
		}

		String extension = FileUtil.getExtension(fileName);

		String[] imageExtensions =
			_commerceDataIntegrationProcessConfiguration.imageExtensions();

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
		CommerceDataIntegrationProcessFileEntryUploadActionHelperImpl.class);

	private volatile CommerceDataIntegrationProcessConfiguration
		_commerceDataIntegrationProcessConfiguration;

	@Reference
	private Portal _portal;

}