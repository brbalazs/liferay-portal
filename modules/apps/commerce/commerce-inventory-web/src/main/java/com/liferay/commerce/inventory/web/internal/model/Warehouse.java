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

package com.liferay.commerce.inventory.web.internal.model;

/**
 * @author Luca Pellizzon
 */
public class Warehouse {

	public Warehouse(
		String warehouse, int quantity, int reserved, int incoming) {

		_warehouse = warehouse;
		_quantity = quantity;
		_reserved = reserved;

		if ((quantity > 0) && (reserved >= 0)) {
			_available = quantity - reserved;
		}
		else {
			_available = 0;
		}

		_incoming = incoming;
	}

	public int getAvailable() {
		return _available;
	}

	public int getIncoming() {
		return _incoming;
	}

	public int getQuantity() {
		return _quantity;
	}

	public int getReserved() {
		return _reserved;
	}

	public String getWarehouse() {
		return _warehouse;
	}

	private final int _available;
	private final int _incoming;
	private final int _quantity;
	private final int _reserved;
	private final String _warehouse;

}