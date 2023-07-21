/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.background.task.service.impl;

import com.liferay.portal.background.task.service.base.BackgroundTaskServiceBaseImpl;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskServiceImpl extends BackgroundTaskServiceBaseImpl {

	@Override
	public int getBackgroundTasksCount(
		long groupId, String taskExecutorClassName, String completed) {

		return backgroundTaskLocalService.getBackgroundTasksCount(
			groupId, taskExecutorClassName, completed);
	}

	@Override
	public String getBackgroundTaskStatusJSON(long backgroundTaskId) {
		return backgroundTaskLocalService.getBackgroundTaskStatusJSON(
			backgroundTaskId);
	}

}