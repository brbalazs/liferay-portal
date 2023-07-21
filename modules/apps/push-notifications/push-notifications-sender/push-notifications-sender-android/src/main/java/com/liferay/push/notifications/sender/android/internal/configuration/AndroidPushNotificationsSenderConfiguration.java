/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.push.notifications.sender.android.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Andrea Di Giorgi
 */
@ExtendedObjectClassDefinition(category = "notifications")
@Meta.OCD(
	id = "com.liferay.push.notifications.sender.android.internal.configuration.AndroidPushNotificationsSenderConfiguration",
	localization = "content/Language",
	name = "android-push-notifications-sender-configuration-name"
)
public interface AndroidPushNotificationsSenderConfiguration {

	@Meta.AD(
		description = "android-api-key-description",
		name = "android-api-key-name", required = false
	)
	public String apiKey();

	@Meta.AD(
		description = "retries-description", name = "retries-name",
		required = false
	)
	public int retries();

}