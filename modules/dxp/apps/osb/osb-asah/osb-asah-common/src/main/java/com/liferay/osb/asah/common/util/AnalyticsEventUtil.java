/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.model.AnalyticsEventsMessage;

import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author Marcos Martins
 */
public class AnalyticsEventUtil {

	public static String generateAnalyticsEventId(
		String dataSourceId, AnalyticsEventsMessage.Event event,
		String projectId, String userId) {

		Date eventDate = event.getEventDate();

		Map<String, String> eventProperties = event.getProperties();

		return DigestUtils.sha256Hex(
			String.join(
				"#", projectId, dataSourceId, userId, event.getApplicationId(),
				event.getEventId(), String.valueOf(eventProperties.hashCode()),
				String.valueOf(eventDate.getTime())));
	}

}