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

package com.liferay.commerce.internal.price;

import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.price.CommerceOrderItemPrice;

import java.math.BigDecimal;

/**
 * @author Riccardo Alberti
 */
public class CommerceOrderItemPriceImpl implements CommerceOrderItemPrice {

	public CommerceOrderItemPriceImpl(CommerceMoney unitPrice) {
		_unitPrice = unitPrice;
	}

	@Override
	public CommerceMoney getDiscountAmount() {
		return _discountAmount;
	}

	@Override
	public BigDecimal getDiscountPercentage() {
		return _discountPercentage;
	}

	@Override
	public BigDecimal getDiscountPercentageLevel1() {
		return _discountPercentageLevel1;
	}

	@Override
	public BigDecimal getDiscountPercentageLevel2() {
		return _discountPercentageLevel2;
	}

	@Override
	public BigDecimal getDiscountPercentageLevel3() {
		return _discountPercentageLevel3;
	}

	@Override
	public BigDecimal getDiscountPercentageLevel4() {
		return _discountPercentageLevel4;
	}

	@Override
	public CommerceMoney getFinalPrice() {
		return _finalPrice;
	}

	@Override
	public CommerceMoney getPromoPrice() {
		return _promoPrice;
	}

	@Override
	public CommerceMoney getUnitPrice() {
		return _unitPrice;
	}

	public void setDiscountAmount(CommerceMoney discountAmount) {
		_discountAmount = discountAmount;
	}

	public void setDiscountPercentage(BigDecimal discountPercentage) {
		_discountPercentage = discountPercentage;
	}

	public void setDiscountPercentageLevel1(
		BigDecimal discountPercentageLevel1) {

		_discountPercentageLevel1 = discountPercentageLevel1;
	}

	public void setDiscountPercentageLevel2(
		BigDecimal discountPercentageLevel2) {

		_discountPercentageLevel2 = discountPercentageLevel2;
	}

	public void setDiscountPercentageLevel3(
		BigDecimal discountPercentageLevel3) {

		_discountPercentageLevel3 = discountPercentageLevel3;
	}

	public void setDiscountPercentageLevel4(
		BigDecimal discountPercentageLevel4) {

		_discountPercentageLevel4 = discountPercentageLevel4;
	}

	public void setFinalPrice(CommerceMoney finalPrice) {
		_finalPrice = finalPrice;
	}

	public void setPromoPrice(CommerceMoney promoPrice) {
		_promoPrice = promoPrice;
	}

	public void setUnitPrice(CommerceMoney unitPrice) {
		_unitPrice = unitPrice;
	}

	private CommerceMoney _discountAmount;
	private BigDecimal _discountPercentage;
	private BigDecimal _discountPercentageLevel1;
	private BigDecimal _discountPercentageLevel2;
	private BigDecimal _discountPercentageLevel3;
	private BigDecimal _discountPercentageLevel4;
	private CommerceMoney _finalPrice;
	private CommerceMoney _promoPrice;
	private CommerceMoney _unitPrice;

}