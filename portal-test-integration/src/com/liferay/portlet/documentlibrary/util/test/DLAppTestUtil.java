/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portlet.documentlibrary.util.test;

import com.liferay.document.library.kernel.model.DLFileEntryTypeConstants;
import com.liferay.document.library.kernel.model.DLSyncConstants;
import com.liferay.document.library.kernel.service.DLAppLocalServiceUtil;
import com.liferay.document.library.kernel.service.DLFileEntryLocalServiceUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.FileVersion;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.util.TestDataConstants;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.kernel.workflow.WorkflowThreadLocal;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander Chow
 */
public abstract class DLAppTestUtil {

	public static FileEntry addFileEntryWithWorkflow(
			long userId, long groupId, long folderId, String sourceFileName,
			String title, boolean approved, ServiceContext serviceContext)
		throws Exception {

		boolean workflowEnabled = WorkflowThreadLocal.isEnabled();

		try {
			WorkflowThreadLocal.setEnabled(true);

			serviceContext = (ServiceContext)serviceContext.clone();

			serviceContext.setWorkflowAction(
				WorkflowConstants.ACTION_SAVE_DRAFT);

			FileEntry fileEntry = DLAppLocalServiceUtil.addFileEntry(
				userId, groupId, folderId, sourceFileName,
				ContentTypes.TEXT_PLAIN, title, StringPool.BLANK,
				StringPool.BLANK, TestDataConstants.TEST_BYTE_ARRAY,
				serviceContext);

			if (approved) {
				return updateStatus(fileEntry, serviceContext);
			}

			return fileEntry;
		}
		finally {
			WorkflowThreadLocal.setEnabled(workflowEnabled);
		}
	}

	public static void populateNotificationsServiceContext(
			ServiceContext serviceContext, String command)
		throws Exception {

		serviceContext.setAttribute("entryURL", "http://localhost");

		if (Validator.isNotNull(command)) {
			serviceContext.setCommand(command);
		}

		serviceContext.setLayoutFullURL("http://localhost");
	}

	public static void populateServiceContext(
			ServiceContext serviceContext, long fileEntryTypeId)
		throws Exception {

		if (fileEntryTypeId !=
				DLFileEntryTypeConstants.FILE_ENTRY_TYPE_ID_ALL) {

			serviceContext.setAttribute("fileEntryTypeId", fileEntryTypeId);
		}

		serviceContext.setLayoutFullURL("http://localhost");
	}

	protected static FileEntry updateStatus(
			FileEntry fileEntry, ServiceContext serviceContext)
		throws Exception {

		FileVersion fileVersion = fileEntry.getFileVersion();

		Map<String, Serializable> workflowContext = new HashMap<>();

		workflowContext.put(WorkflowConstants.CONTEXT_URL, "http://localhost");
		workflowContext.put("event", DLSyncConstants.EVENT_ADD);

		DLFileEntryLocalServiceUtil.updateStatus(
			TestPropsValues.getUserId(), fileVersion.getFileVersionId(),
			WorkflowConstants.STATUS_APPROVED, serviceContext, workflowContext);

		return DLAppLocalServiceUtil.getFileEntry(fileEntry.getFileEntryId());
	}

}