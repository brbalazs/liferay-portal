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

package com.liferay.commerce.product.option.test;

import com.liferay.commerce.product.option.CommerceOptionValue;

import java.math.BigDecimal;

/**
 * @author Riccardo Alberti
 */
public class TestCommerceOptionValue implements CommerceOptionValue {

	public TestCommerceOptionValue(
		long cpInstanceId, String optionKey, BigDecimal price, String priceType,
		int quantity) {

		_cpInstanceId = cpInstanceId;
		_optionKey = optionKey;
		_price = price;
		_priceType = priceType;
		_quantity = quantity;
	}

	@Override
	public long getCPInstanceId() {
		return _cpInstanceId;
	}

	@Override
	public String getOptionKey() {
		return _optionKey;
	}

	@Override
	public BigDecimal getPrice() {
		return _price;
	}

	@Override
	public String getPriceType() {
		return _priceType;
	}

	@Override
	public int getQuantity() {
		return _quantity;
	}

	public void setCpInstanceId(long cpInstanceId) {
		_cpInstanceId = cpInstanceId;
	}

	public void setOptionKey(String optionKey) {
		_optionKey = optionKey;
	}

	public void setPrice(BigDecimal price) {
		_price = price;
	}

	public void setPriceType(String priceType) {
		_priceType = priceType;
	}

	public void setQuantity(int quantity) {
		_quantity = quantity;
	}

	private long _cpInstanceId;
	private String _optionKey;
	private BigDecimal _price;
	private String _priceType;
	private int _quantity;

}