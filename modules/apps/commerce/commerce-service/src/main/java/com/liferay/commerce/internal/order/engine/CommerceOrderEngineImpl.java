/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.internal.order.engine;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.exception.CommerceOrderStatusException;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.notification.util.CommerceNotificationHelper;
import com.liferay.commerce.order.engine.CommerceOrderEngine;
import com.liferay.commerce.order.status.CommerceOrderStatus;
import com.liferay.commerce.order.status.CommerceOrderStatusRegistry;
import com.liferay.commerce.subscription.CommerceSubscriptionEntryHelperUtil;
import com.liferay.portal.kernel.exception.PortalException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 */
@Component(immediate = true, service = CommerceOrderEngine.class)
public class CommerceOrderEngineImpl implements CommerceOrderEngine {

	@Override
	public CommerceOrderStatus getCurrentCommerceOrderStatus(
		CommerceOrder commerceOrder) {

		return _commerceOrderStatusRegistry.getCommerceOrderStatus(
			commerceOrder.getOrderStatus());
	}

	@Override
	public List<CommerceOrderStatus> getNextCommerceOrderStatuses(
			CommerceOrder commerceOrder)
		throws PortalException {

		CommerceOrderStatus currentCommerceOrderStatus =
			_commerceOrderStatusRegistry.getCommerceOrderStatus(
				commerceOrder.getOrderStatus());

		if (currentCommerceOrderStatus == null) {
			return Collections.emptyList();
		}

		List<CommerceOrderStatus> commerceOrderStatuses =
			_commerceOrderStatusRegistry.getCommerceOrderStatuses();

		int currentOrderStatusIndex = commerceOrderStatuses.indexOf(
			currentCommerceOrderStatus);

		if (currentOrderStatusIndex != (commerceOrderStatuses.size() - 1)) {
			CommerceOrderStatus nextCommerceOrderStatus =
				commerceOrderStatuses.get(currentOrderStatusIndex + 1);

			List<CommerceOrderStatus> nextCommerceOrderStatuses =
				new ArrayList<>();

			for (CommerceOrderStatus commerceOrderStatus :
					commerceOrderStatuses) {

				if ((commerceOrderStatus.getPriority() ==
						nextCommerceOrderStatus.getPriority()) &&
					commerceOrderStatus.isTransitionCriteriaMet(
						commerceOrder)) {

					nextCommerceOrderStatuses.add(commerceOrderStatus);
				}
			}

			return nextCommerceOrderStatuses;
		}

		return Collections.emptyList();
	}

	@Override
	public CommerceOrder transitionCommerceOrder(
			CommerceOrder commerceOrder, int orderStatus, long userId)
		throws PortalException {

		CommerceOrderStatus commerceOrderStatus =
			_commerceOrderStatusRegistry.getCommerceOrderStatus(orderStatus);

		if (commerceOrderStatus == null) {
			throw new CommerceOrderStatusException();
		}

		CommerceOrderStatus currentCommerceOrderStatus =
			_commerceOrderStatusRegistry.getCommerceOrderStatus(
				commerceOrder.getOrderStatus());

		if (!currentCommerceOrderStatus.isComplete(commerceOrder) ||
			!commerceOrderStatus.isTransitionCriteriaMet(commerceOrder)) {

			throw new CommerceOrderStatusException();
		}

		_sendOrderStatusMessage(commerceOrder, commerceOrderStatus.getKey());

		return commerceOrderStatus.doTransition(commerceOrder, userId);
	}

	private void _sendOrderStatusMessage(
			CommerceOrder commerceOrder, int orderStatus)
		throws PortalException {

		if (orderStatus == CommerceOrderConstants.ORDER_STATUS_TO_FULFILL) {
			_commerceNotificationHelper.sendNotifications(
				commerceOrder.getScopeGroupId(), commerceOrder.getUserId(),
				CommerceOrderConstants.ORDER_NOTIFICATION_PLACED,
				commerceOrder);

			CommerceSubscriptionEntryHelperUtil.checkCommerceSubscriptions(
				commerceOrder);
		}
		else {
			_commerceNotificationHelper.sendNotifications(
				commerceOrder.getScopeGroupId(), commerceOrder.getUserId(),
				CommerceOrderConstants.getNotificationKey(orderStatus),
				commerceOrder);
		}
	}

	@Reference
	private CommerceNotificationHelper _commerceNotificationHelper;

	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;

}