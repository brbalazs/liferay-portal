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

package com.liferay.commerce.subscription.web.internal.model;

import java.math.BigDecimal;

import java.util.Date;

/**
 * @author Luca Pellizzon
 */
public class Payment {

	public Payment(Date date, long transactionId, String amount) {
		_date = date;
		_transactionId = transactionId;
		_amount = amount;
	}

	public String getAmount() {
		return _amount;
	}

	public Date getDate() {
		return _date;
	}

	public long getTransactionId() {
		return _transactionId;
	}

	private final String _amount;
	private final Date _date;
	private final long _transactionId;

}