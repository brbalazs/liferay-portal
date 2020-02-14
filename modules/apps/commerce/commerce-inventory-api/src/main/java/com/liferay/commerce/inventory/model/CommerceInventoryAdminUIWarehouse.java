/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.inventory.model;

/**
 * @author Luca Pellizzon
 */
public class CommerceInventoryAdminUIWarehouse {

	public CommerceInventoryAdminUIWarehouse(
		String name, int stockQuantity, int reservedQuantity,
		int replenishmentQuantity) {

		_name = name;
		_stockQuantity = stockQuantity;
		_reservedQuantity = reservedQuantity;
		_replenishmentQuantity = replenishmentQuantity;
	}

	public String getName() {
		return _name;
	}

	public int getReplenishmentQuantity() {
		return _replenishmentQuantity;
	}

	public int getReservedQuantity() {
		return _reservedQuantity;
	}

	public int getStockQuantity() {
		return _stockQuantity;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setReplenishmentQuantity(int replenishmentQuantity) {
		_replenishmentQuantity = replenishmentQuantity;
	}

	public void setReservedQuantity(int reservedQuantity) {
		_reservedQuantity = reservedQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		_stockQuantity = stockQuantity;
	}

	private String _name;
	private int _replenishmentQuantity;
	private int _reservedQuantity;
	private int _stockQuantity;

}