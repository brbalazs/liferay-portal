/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.notification.bundle.usernotificationmanagerutil;

import com.liferay.portal.kernel.model.UserNotificationEvent;
import com.liferay.portal.kernel.notifications.UserNotificationFeedEntry;
import com.liferay.portal.kernel.notifications.UserNotificationHandler;
import com.liferay.portal.kernel.service.ServiceContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Peter Fellwock
 */
@Component(
	immediate = true, property = "service.ranking:Integer=" + Integer.MAX_VALUE,
	service = UserNotificationHandler.class
)
public class TestUserNotificationHandler implements UserNotificationHandler {

	public static final String LINK = "http://www.liferay.com";

	public static final String PORTLET_ID = "PORTLET_ID";

	public static final String SELECTOR = "SELECTOR";

	@Override
	public String getPortletId() {
		return PORTLET_ID;
	}

	@Override
	public String getSelector() {
		return SELECTOR;
	}

	@Override
	public UserNotificationFeedEntry interpret(
		UserNotificationEvent userNotificationEvent,
		ServiceContext serviceContext) {

		boolean applicable = isApplicable(
			userNotificationEvent, serviceContext);

		return new UserNotificationFeedEntry(false, "body", LINK, applicable);
	}

	@Override
	public boolean isDeliver(
		long userId, long classNameId, int notificationType, int deliveryType,
		ServiceContext serviceContext) {

		if (userId == 1) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isOpenDialog() {
		return false;
	}

}