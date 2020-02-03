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

package com.liferay.commerce.discount;

import java.math.BigDecimal;

/**
 * @author Riccardo Alberti
 */
public class CommerceDiscountLevel {

	public CommerceDiscountLevel(BigDecimal discountValue) {
		_id = 0;
		_usePercentage = true;
		_discountValue = discountValue;
		_discountAmount = BigDecimal.ZERO;
	}

	public CommerceDiscountLevel(
		long id, boolean usePercentage, BigDecimal discountValue,
		BigDecimal discountAmount) {

		_id = id;
		_usePercentage = usePercentage;
		_discountValue = discountValue;
		_discountAmount = discountAmount;
	}

	public BigDecimal getDiscountAmount() {
		return _discountAmount;
	}

	public BigDecimal getDiscountValue() {
		return _discountValue;
	}

	public long getId() {
		return _id;
	}

	public boolean isUsePercentage() {
		return _usePercentage;
	}

	private final BigDecimal _discountAmount;
	private final BigDecimal _discountValue;
	private final long _id;
	private final boolean _usePercentage;

}