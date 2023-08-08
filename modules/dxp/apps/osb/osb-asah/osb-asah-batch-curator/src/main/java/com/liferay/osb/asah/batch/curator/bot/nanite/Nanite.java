/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.batch.curator.bot.nanite;

import org.json.JSONObject;

/**
 * @author Brian Wing Shun Chan
 */
public interface Nanite {

	public boolean isLogRunEnabled();

	public void logCompleted(
		String asahTaskId, JSONObject contextJSONObject, long duration);

	public void logFailed(
		String asahTaskId, JSONObject contextJSONObject, long duration,
		Throwable throwable);

	public void logStart(JSONObject contextJSONObject);

	public void run(JSONObject contextJSONObject) throws Exception;

}