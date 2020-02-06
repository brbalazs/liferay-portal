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
import com.liferay.portal.kernel.transaction.Propagation;
import com.liferay.portal.kernel.transaction.TransactionCommitCallbackUtil;
import com.liferay.portal.kernel.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alec Sloan
 */
@Component(immediate = true, service = CommerceOrderEngine.class)
public class CommerceOrderEngineImpl implements CommerceOrderEngine {

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, rollbackFor = Exception.class
	)
	public CommerceOrderStatus getCurrentCommerceOrderStatus(
		CommerceOrder commerceOrder) {

		return _commerceOrderStatusRegistry.getCommerceOrderStatus(
			commerceOrder.getOrderStatus());
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, rollbackFor = Exception.class
	)
	public List<CommerceOrderStatus> getNextCommerceOrderStatuses(
			CommerceOrder commerceOrder)
		throws PortalException {

		CommerceOrderStatus currentCommerceOrderStatus =
			_commerceOrderStatusRegistry.getCommerceOrderStatus(
				commerceOrder.getOrderStatus());

		List<CommerceOrderStatus> nextCommerceOrderStatuses = new ArrayList<>();

		if (currentCommerceOrderStatus == null) {
			return nextCommerceOrderStatuses;
		}
		else if (currentCommerceOrderStatus.getKey() ==
					CommerceOrderConstants.ORDER_STATUS_BLOCKED) {

			nextCommerceOrderStatuses.add(
				_commerceOrderStatusRegistry.getCommerceOrderStatus(
					CommerceOrderConstants.ORDER_STATUS_BLOCKED));

			return nextCommerceOrderStatuses;
		}

		List<CommerceOrderStatus> commerceOrderStatuses =
			_commerceOrderStatusRegistry.getCommerceOrderStatuses();

		int currentOrderStatusIndex = commerceOrderStatuses.indexOf(
			currentCommerceOrderStatus);

		if (currentOrderStatusIndex != (commerceOrderStatuses.size() - 1)) {
			CommerceOrderStatus nextCommerceOrderStatus =
				commerceOrderStatuses.get(currentOrderStatusIndex + 1);

			for (CommerceOrderStatus commerceOrderStatus :
					commerceOrderStatuses) {

				if (commerceOrderStatus.isTransitionCriteriaMet(
						commerceOrder) &&
					(((commerceOrderStatus.getPriority() ==
						CommerceOrderConstants.ORDER_STATUS_ANY) &&
					  (currentCommerceOrderStatus.getKey() !=
						  CommerceOrderConstants.ORDER_STATUS_OPEN)) ||
					 (commerceOrderStatus.getPriority() ==
						 nextCommerceOrderStatus.getPriority()))) {

					nextCommerceOrderStatuses.add(commerceOrderStatus);
				}
			}
		}

		return nextCommerceOrderStatuses;
	}

	@Override
	@Transactional(
		propagation = Propagation.REQUIRED, rollbackFor = Exception.class
	)
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
			!commerceOrderStatus.isTransitionCriteriaMet(commerceOrder) ||
			((currentCommerceOrderStatus.getKey() ==
				CommerceOrderConstants.ORDER_STATUS_BLOCKED) &&
			 (commerceOrderStatus.getKey() !=
				 CommerceOrderConstants.ORDER_STATUS_BLOCKED) &&
			 (commerceOrderStatus.getKey() !=
				 CommerceOrderConstants.ORDER_STATUS_FULFILLED))) {

			throw new CommerceOrderStatusException();
		}

		_sendOrderStatusMessage(commerceOrder, commerceOrderStatus.getKey());

		return commerceOrderStatus.doTransition(commerceOrder, userId);
	}

	private void _sendOrderStatusMessage(
		CommerceOrder commerceOrder, int orderStatus) {

		TransactionCommitCallbackUtil.registerCallback(
			new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					if (orderStatus ==
							CommerceOrderConstants.ORDER_STATUS_TO_FULFILL) {

						CommerceSubscriptionEntryHelperUtil.
							checkCommerceSubscriptions(commerceOrder);
					}

					_commerceNotificationHelper.sendNotifications(
						commerceOrder.getScopeGroupId(),
						commerceOrder.getUserId(),
						CommerceOrderConstants.getNotificationKey(orderStatus),
						commerceOrder);

					return null;
				}

			});
	}

	@Reference
	private CommerceNotificationHelper _commerceNotificationHelper;

	@Reference
	private CommerceOrderStatusRegistry _commerceOrderStatusRegistry;

}