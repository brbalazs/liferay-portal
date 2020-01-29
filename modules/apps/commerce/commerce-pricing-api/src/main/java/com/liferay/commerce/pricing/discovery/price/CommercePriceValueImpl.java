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
import com.liferay.commerce.price.CommercePriceValue;

/**
 * @author Riccardo Alberti
 */
public class CommercePriceValueImpl implements CommercePriceValue {

	public CommercePriceValueImpl(
		CommerceMoney commerceMoney, int minQuantity,
		CommerceDiscountLevel discountLevel1,
		CommerceDiscountLevel discountLevel2,
		CommerceDiscountLevel discountLevel3,
		CommerceDiscountLevel discountLevel4) {

		_commerceMoney = commerceMoney;
		_minQuantity = minQuantity;
		_discountLevels = new CommerceDiscountLevel[] {
			discountLevel1, discountLevel2, discountLevel3, discountLevel4
		};
	}

	public CommercePriceValueImpl(
		CommerceMoney commerceMoney, int minQuantity,
		CommerceDiscountLevel[] discountLevels) {

		_commerceMoney = commerceMoney;
		_minQuantity = minQuantity;
		_discountLevels = discountLevels;
	}

	public CommerceMoney getCommerceMoney() {
		return _commerceMoney;
	}

	public CommerceDiscountLevel[] getDiscountLevels() {
		return _discountLevels;
	}

	public int getMinQuantity() {
		return _minQuantity;
	}

	private final CommerceMoney _commerceMoney;
	private final CommerceDiscountLevel[] _discountLevels;
	private final int _minQuantity;

}