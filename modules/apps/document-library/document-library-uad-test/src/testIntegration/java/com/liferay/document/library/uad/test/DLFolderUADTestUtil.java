/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.document.library.uad.test;

import com.liferay.document.library.kernel.model.DLFolder;
import com.liferay.document.library.kernel.service.DLFolderLocalService;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.util.List;

/**
 * @author William Newbury
 */
public class DLFolderUADTestUtil {

	public static DLFolder addDLFolder(
			DLFolderLocalService dlFolderLocalService, long userId)
		throws Exception {

		return addDLFolder(dlFolderLocalService, userId, 0L);
	}

	public static DLFolder addDLFolder(
			DLFolderLocalService dlFolderLocalService, long userId,
			long parentFolderId)
		throws Exception {

		return dlFolderLocalService.addFolder(
			userId, TestPropsValues.getGroupId(), TestPropsValues.getGroupId(),
			false, parentFolderId, RandomTestUtil.randomString(),
			RandomTestUtil.randomString(), false,
			ServiceContextTestUtil.getServiceContext());
	}

	public static DLFolder addDLFolderWithStatusByUserId(
			DLFolderLocalService dlFolderLocalService, long userId,
			long statusByUserId)
		throws Exception {

		DLFolder dlFolder = addDLFolder(dlFolderLocalService, userId);

		return dlFolderLocalService.updateStatus(
			statusByUserId, dlFolder.getFolderId(),
			WorkflowConstants.STATUS_DRAFT, null,
			ServiceContextTestUtil.getServiceContext());
	}

	public void cleanUpDependencies(List<DLFolder> dlFolders) throws Exception {
	}

}