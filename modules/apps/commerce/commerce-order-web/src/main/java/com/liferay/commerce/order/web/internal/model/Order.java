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

import com.liferay.commerce.frontend.model.LabelField;

/**
 * @author Alessio Antonio Rendina
 */
public class Order {

	public Order(
		long orderId, String createDate, String orderStatus,
		String paymentStatus, LabelField fulfillmentWorkflow, String account,
		String accountCode, String amount) {

		_orderId = orderId;
		_createDate = createDate;
		_orderStatus = orderStatus;
		_paymentStatus = paymentStatus;
		_fulfillmentWorkflow = fulfillmentWorkflow;
		_account = account;
		_accountCode = accountCode;
		_amount = amount;
	}

	public String getAccount() {
		return _account;
	}

	public String getAccountCode() {
		return _accountCode;
	}

	public String getAmount() {
		return _amount;
	}

	public String getCreateDate() {
		return _createDate;
	}

	public LabelField getFulfillmentWorkflow() {
		return _fulfillmentWorkflow;
	}

	public long getOrderId() {
		return _orderId;
	}

	public String getOrderStatus() {
		return _orderStatus;
	}

	public String getPaymentStatus() {
		return _paymentStatus;
	}

	private final String _account;
	private final String _accountCode;
	private final String _amount;
	private final String _createDate;
	private final LabelField _fulfillmentWorkflow;
	private final long _orderId;
	private final String _orderStatus;
	private final String _paymentStatus;

}