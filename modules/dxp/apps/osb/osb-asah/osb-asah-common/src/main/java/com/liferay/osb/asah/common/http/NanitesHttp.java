/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.http;

import org.json.JSONArray;

/**
 * @author Shinn Lok
 */
public interface NanitesHttp {

	public void removeSchedule();

	public void rescheduleNanites();

	public void run(JSONArray jsonArray);

	public void scheduleAsahTask(Long asahTaskId);

	public void unscheduleAsahTask(Long asahTaskId);

}