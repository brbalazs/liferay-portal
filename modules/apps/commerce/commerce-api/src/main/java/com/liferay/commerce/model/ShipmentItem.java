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

package com.liferay.commerce.model;

/**
 * @author Alessio Antonio Rendina
 * @author Alec Sloan
 */
public class ShipmentItem {

	public ShipmentItem(
		long orderId, int orderedQuantity, long shipmentItemId,
		int shippedQuantity, Sku sku,
		int toSendQuantity, String warehouse) {

		_shipmentItemId = shipmentItemId;
		_orderId = orderId;
		_sku = sku;
		_orderedQuantity = orderedQuantity;
		_shippedQuantity = shippedQuantity;
		_toSendQuantity = toSendQuantity;
		_warehouse = warehouse;
	}

	public int getOrderedQuantity() {
		return _orderedQuantity;
	}

	public long getOrderId() {
		return _orderId;
	}

	public long getShipmentItemId() {
		return _shipmentItemId;
	}

	public int getShippedQuantity() {
		return _shippedQuantity;
	}

	public Sku getSku() {
		return _sku;
	}

	public int getToSendQuantity() {
		return _toSendQuantity;
	}

	public String getWarehouse() {
		return _warehouse;
	}

	private final int _orderedQuantity;
	private final long _orderId;
	private final long _shipmentItemId;
	private final int _shippedQuantity;
	private final Sku _sku;
	private final int _toSendQuantity;
	private final String _warehouse;

}