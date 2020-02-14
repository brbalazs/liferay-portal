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

import java.util.Date;

/**
 * @author Luca Pellizzon
 */
public class CommerceInventoryAdminUIReplenishment {

	public CommerceInventoryAdminUIReplenishment(
		String name, Date date, int quantity) {

		_name = name;
		_date = date;
		_quantity = quantity;
	}

	public Date getDate() {
		return _date;
	}

	public String getName() {
		return _name;
	}

	public int getQuantity() {
		return _quantity;
	}

	public void setDate(Date date) {
		_date = date;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setQuantity(int quantity) {
		_quantity = quantity;
	}

	private Date _date;
	private String _name;
	private int _quantity;

}