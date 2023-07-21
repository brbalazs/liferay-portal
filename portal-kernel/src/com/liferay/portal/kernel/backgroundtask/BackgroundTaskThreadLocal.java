/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.backgroundtask;

import com.liferay.petra.lang.CentralizedThreadLocal;

/**
 * @author Michael C. Han
 */
public class BackgroundTaskThreadLocal {

	public static long getBackgroundTaskId() {
		return _backgroundTaskId.get();
	}

	public static boolean hasBackgroundTask() {
		long backgroundTaskId = getBackgroundTaskId();

		if (backgroundTaskId > 0) {
			return true;
		}

		return false;
	}

	public static void setBackgroundTaskId(long backgroundTaskId) {
		if (backgroundTaskId > 0) {
			_backgroundTaskId.set(backgroundTaskId);
		}
	}

	private static final ThreadLocal<Long> _backgroundTaskId =
		new CentralizedThreadLocal<>(
			BackgroundTaskThreadLocal.class + "._backgroundTaskId", () -> 0L);

}