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

package com.liferay.commerce.order.web.internal.model;

/**
 * @author Alessio Antonio Rendina
 */
public class ShipmentItem {

	public ShipmentItem(
		long shipmentItemId, long orderId, String sku, int orderedCount,
		int shippedQuantity, int shippableQuantity) {

		_shipmentItemId = shipmentItemId;
		_orderId = orderId;
		_sku = sku;
		_orderedCount = orderedCount;
		_shippedQuantity = shippedQuantity;
		_shippableQuantity = shippableQuantity;
	}

	public int getOrderedCount() {
		return _orderedCount;
	}

	public long getOrderId() {
		return _orderId;
	}

	public long getShipmentItemId() {
		return _shipmentItemId;
	}

	public int getShippableQuantity() {
		return _shippableQuantity;
	}

	public int getShippedQuantity() {
		return _shippedQuantity;
	}

	public String getSku() {
		return _sku;
	}

	private final int _orderedCount;
	private final long _orderId;
	private final long _shipmentItemId;
	private final int _shippableQuantity;
	private final int _shippedQuantity;
	private final String _sku;

}