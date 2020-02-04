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

package com.liferay.commerce.order.web.internal.frontend;

import com.liferay.commerce.frontend.DefaultFilterImpl;

/**
 * @author Alessio Antonio Rendina
 */
public class OrderFilterImpl extends DefaultFilterImpl {

	public String getAdvanceStatus() {
		return _advanceStatus;
	}

	public int getOrderStatus() {
		return _orderStatus;
	}

	public void setAdvanceStatus(String advanceStatus) {
		_advanceStatus = advanceStatus;
	}

	public void setOrderStatus(int orderStatus) {
		_orderStatus = orderStatus;
	}

	private String _advanceStatus;
	private int _orderStatus;

}