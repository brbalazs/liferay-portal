/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.push.notifications.sender.android.internal;

import com.google.android.gcm.server.Result;

import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.push.notifications.sender.BaseResponse;

/**
 * @author Bruno Farache
 */
public class AndroidResponse extends BaseResponse {

	public AndroidResponse(
		Result result, String token, JSONObject payloadJSONObject) {

		super(AndroidPushNotificationsSender.PLATFORM);

		this.token = token;

		canonicalRegistrationId = result.getCanonicalRegistrationId();
		id = result.getMessageId();
		payload = payloadJSONObject.toString();

		status = result.getErrorCodeName();

		if (Validator.isNull(status)) {
			succeeded = true;
		}
	}

	public String getCanonicalRegistrationId() {
		return canonicalRegistrationId;
	}

	protected String canonicalRegistrationId;

}