/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.comment.upgrade;

import com.liferay.message.boards.model.MBDiscussion;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.subscription.model.Subscription;
import com.liferay.subscription.service.SubscriptionLocalService;

import java.util.List;

/**
 * @author Roberto Díaz
 */
public class UpgradeDiscussionSubscriptionClassName extends UpgradeProcess {

	public UpgradeDiscussionSubscriptionClassName(
		SubscriptionLocalService subscriptionLocalService, String className,
		DeletionMode deletionMode) {

		_subscriptionLocalService = subscriptionLocalService;
		_className = className;
		_deletionMode = deletionMode;
	}

	public enum DeletionMode {

		ADD_NEW, DELETE_OLD

	}

	@Override
	protected void doUpgrade() throws Exception {
		_addSubscriptions();

		if (_deletionMode == DeletionMode.DELETE_OLD) {
			_deleteSubscriptions();
		}
	}

	private void _addSubscriptions() throws PortalException {
		String newSubscriptionClassName =
			MBDiscussion.class.getName() + StringPool.UNDERLINE + _className;

		if (_subscriptionLocalService.getSubscriptionsCount(
				newSubscriptionClassName) > 0) {

			return;
		}

		List<Subscription> subscriptions =
			_subscriptionLocalService.getSubscriptions(_className);

		for (Subscription subscription : subscriptions) {
			_subscriptionLocalService.addSubscription(
				subscription.getUserId(), subscription.getGroupId(),
				newSubscriptionClassName, subscription.getClassPK());
		}
	}

	private void _deleteSubscriptions() throws PortalException {
		List<Subscription> subscriptions =
			_subscriptionLocalService.getSubscriptions(_className);

		for (Subscription subscription : subscriptions) {
			_subscriptionLocalService.deleteSubscription(
				subscription.getSubscriptionId());
		}
	}

	private final String _className;
	private final DeletionMode _deletionMode;
	private final SubscriptionLocalService _subscriptionLocalService;

}