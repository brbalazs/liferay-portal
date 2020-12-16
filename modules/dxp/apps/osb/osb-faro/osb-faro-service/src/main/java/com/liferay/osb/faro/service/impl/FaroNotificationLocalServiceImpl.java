/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.service.impl;

import com.liferay.osb.faro.model.FaroNotification;
import com.liferay.osb.faro.service.base.FaroNotificationLocalServiceBaseImpl;
import com.liferay.portal.kernel.search.Indexable;
import com.liferay.portal.kernel.search.IndexableType;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Geyson Silva
 */
public class FaroNotificationLocalServiceImpl
	extends FaroNotificationLocalServiceBaseImpl {

	@Indexable(type = IndexableType.REINDEX)
	@Override
	public FaroNotification addFaroNotification(
		long userId, long groupId, String scope, String type, String subType) {

		long faroNotificationId = counterLocalService.increment();

		FaroNotification faroNotification = faroNotificationPersistence.create(
			faroNotificationId);

		faroNotification.setGroupId(groupId);
		faroNotification.setUserId(userId);

		long now = System.currentTimeMillis();

		faroNotification.setCreateTime(now);
		faroNotification.setModifiedTime(now);

		faroNotification.setRead(false);
		faroNotification.setScope(scope);
		faroNotification.setType(type);
		faroNotification.setSubType(subType);

		return faroNotificationPersistence.update(faroNotification);
	}

	@Override
	public void clearDismissedNotifications() {
		List<FaroNotification> dismissedNotifications =
			faroNotificationFinder.findDismissedNotifications();

		Stream<FaroNotification> stream = dismissedNotifications.stream();

		stream.forEach(faroNotificationPersistence::remove);
	}

	@Override
	public List<FaroNotification> findFaroNotificationsLast30Days(
		long groupId, long userId) {

		return faroNotificationFinder.findLast30Days(groupId, userId);
	}

}