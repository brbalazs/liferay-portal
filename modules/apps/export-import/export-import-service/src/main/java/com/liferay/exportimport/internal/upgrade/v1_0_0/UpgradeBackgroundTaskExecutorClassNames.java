/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.upgrade.v1_0_0;

import com.liferay.exportimport.kernel.background.task.BackgroundTaskExecutorNames;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.LoggingTimer;

/**
 * @author Daniel Kocsis
 */
public class UpgradeBackgroundTaskExecutorClassNames extends UpgradeProcess {

	protected void deleteBackgroundTasks() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			runSQL(
				"delete from BackgroundTask where taskExecutorClassName = '" +
					"com.liferay.portal.lar.backgroundtask." +
						"StagingIndexingBackgroundTaskExecutor'");
		}
	}

	@Override
	protected void doUpgrade() throws Exception {
		String[][] renameTaskExecutorClassNamesArray =
			getRenameTaskExecutorClassNames();

		for (String[] renameTaskExecutorClassName :
				renameTaskExecutorClassNamesArray) {

			StringBundler sb = new StringBundler(5);

			sb.append("update BackgroundTask set taskExecutorClassName = '");
			sb.append(renameTaskExecutorClassName[1]);
			sb.append("' where taskExecutorClassName = '");
			sb.append(renameTaskExecutorClassName[0]);
			sb.append("'");

			runSQL(sb.toString());
		}

		deleteBackgroundTasks();
	}

	protected String[][] getRenameTaskExecutorClassNames() {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			return new String[][] {
				{
					"com.liferay.portal.lar.backgroundtask." +
						"LayoutExportBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						LAYOUT_EXPORT_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"LayoutImportBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						LAYOUT_IMPORT_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"LayoutRemoteStagingBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						LAYOUT_REMOTE_STAGING_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"LayoutStagingBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						LAYOUT_STAGING_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"PortletExportBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						PORTLET_EXPORT_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"PortletImportBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						PORTLET_IMPORT_BACKGROUND_TASK_EXECUTOR
				},
				{
					"com.liferay.portal.lar.backgroundtask." +
						"PortletStagingBackgroundTaskExecutor",
					BackgroundTaskExecutorNames.
						PORTLET_STAGING_BACKGROUND_TASK_EXECUTOR
				}
			};
		}
	}

}