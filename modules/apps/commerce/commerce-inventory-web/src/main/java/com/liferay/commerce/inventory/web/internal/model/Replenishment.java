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

import java.util.Date;

/**
 * @author Luca Pellizzon
 */
public class Replenishment {

	public Replenishment(String warehouse, Date date, int quantity) {
		_warehouse = warehouse;
		_date = date;
		_quantity = quantity;
	}

	public Date getDate() {
		return _date;
	}

	public int getQuantity() {
		return _quantity;
	}

	public String getWarehouse() {
		return _warehouse;
	}

	private final Date _date;
	private final int _quantity;
	private final String _warehouse;

}