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

package com.liferay.commerce.subscription.web.internal.frontend;

import com.liferay.commerce.frontend.DefaultFilterImpl;

/**
 * @author Luca Pellizzon
 */
public class SubscriptionEntryFilterImpl extends DefaultFilterImpl {

	public long getPaymentSubscriptionRemainingCycles() {
		return _paymentSubscriptionRemainingCycles;
	}

	public int getPaymentSubscriptionStatus() {
		return _paymentSubscriptionStatus;
	}

	public boolean isAdvancedSearch() {
		return _advancedSearch;
	}

	public void setAdvancedSearch(boolean advancedSearch) {
		_advancedSearch = advancedSearch;
	}

	public void setPaymentSubscriptionRemainingCycles(
		long maxSubscriptionCycles) {

		_paymentSubscriptionRemainingCycles = maxSubscriptionCycles;
	}

	public void setPaymentSubscriptionStatus(int status) {
		_paymentSubscriptionStatus = status;
	}

	private boolean _advancedSearch;
	private long _paymentSubscriptionRemainingCycles;
	private int _paymentSubscriptionStatus;

}