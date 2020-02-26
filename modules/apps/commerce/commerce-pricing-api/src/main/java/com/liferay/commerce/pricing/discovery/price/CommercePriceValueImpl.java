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

package com.liferay.commerce.pricing.discovery.price;

import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.discount.CommerceDiscountLevel;
import com.liferay.commerce.discount.CommerceDiscountValue;
import com.liferay.commerce.price.CommercePriceValue;

import java.math.BigDecimal;

/**
 * @author Riccardo Alberti
 */
public class CommercePriceValueImpl implements CommercePriceValue {

	public CommercePriceValueImpl(
		CommerceMoney commerceMoney, int minQuantity,
		CommerceDiscountValue commerceDiscountValue) {

		_commerceMoney = commerceMoney;
		_minQuantity = minQuantity;
		_commerceDiscountValue = commerceDiscountValue;
	}

	public CommerceMoney getCommerceMoney() {
		return _commerceMoney;
	}

	public CommerceDiscountValue getCommerceDiscountValue() {
		return _commerceDiscountValue;
	}

	public int getMinQuantity() {
		return _minQuantity;
	}

	private final CommerceMoney _commerceMoney;
	private final CommerceDiscountValue _commerceDiscountValue;
	private final int _minQuantity;

}