/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.exportimport.internal.backgroundtask;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author     Michael C. Han
 * @deprecated As of Judson (7.1.x), replaced by {@link
 *             com.liferay.exportimport.kernel.configuration.ExportImportConfigurationSettingsMapFactory}
 */
@Deprecated
public class BackgroundTaskContextMapFactory {

	public static Map<String, Serializable> buildTaskContextMap(
		long userId, long groupId, boolean privateLayout, long[] layoutIds,
		Map<String, String[]> parameterMap, String cmd, Date startDate,
		Date endDate, String fileName) {

		Map<String, Serializable> taskContextMap = new HashMap<>();

		if (cmd != null) {
			taskContextMap.put(Constants.CMD, cmd);
		}

		if (endDate != null) {
			taskContextMap.put("endDate", endDate);
		}

		taskContextMap.put("fileName", fileName);
		taskContextMap.put("groupId", groupId);

		if (ArrayUtil.isNotEmpty(layoutIds)) {
			taskContextMap.put("layoutIds", layoutIds);
		}

		if (parameterMap != null) {
			HashMap<String, String[]> serializableParameterMap = new HashMap<>(
				parameterMap);

			taskContextMap.put("parameterMap", serializableParameterMap);
		}

		taskContextMap.put("privateLayout", privateLayout);

		if (startDate != null) {
			taskContextMap.put("startDate", startDate);
		}

		taskContextMap.put("userId", userId);

		return taskContextMap;
	}

	public static Map<String, Serializable> buildTaskContextMap(
		long userId, long plid, long groupId, String portletId,
		Map<String, String[]> parameterMap, String cmd, Date startDate,
		Date endDate, String fileName) {

		Map<String, Serializable> taskContextMap = new HashMap<>();

		if (cmd != null) {
			taskContextMap.put(Constants.CMD, cmd);
		}

		if (endDate != null) {
			taskContextMap.put("endDate", endDate);
		}

		taskContextMap.put("fileName", fileName);
		taskContextMap.put("groupId", groupId);

		if (parameterMap != null) {
			HashMap<String, String[]> serializableParameterMap = new HashMap<>(
				parameterMap);

			taskContextMap.put("parameterMap", serializableParameterMap);
		}

		taskContextMap.put("plid", plid);

		if (Validator.isNotNull(portletId)) {
			taskContextMap.put("portletId", portletId);
		}

		if (startDate != null) {
			taskContextMap.put("startDate", startDate);
		}

		taskContextMap.put("userId", userId);

		return taskContextMap;
	}

}