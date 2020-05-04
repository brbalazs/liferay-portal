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

import java.util.List;
import java.util.Objects;

/**
 * @author Riccardo Alberti
 * @author Igor Beslic
 */
public class TestCommerceOptionValue implements CommerceOptionValue {

	public TestCommerceOptionValue(
		long cpInstanceId, String optionKey, String optionValueKey,
		BigDecimal price, String priceType, int quantity) {

		_cpInstanceId = cpInstanceId;
		_optionKey = optionKey;
		_optionValueKey = optionValueKey;
		_price = price;
		_priceType = priceType;
		_quantity = quantity;
	}

	@Override
	public long getCPInstanceId() {
		return _cpInstanceId;
	}

	@Override
	public CommerceOptionValue getFirstMatch(
		List<CommerceOptionValue> commerceOptionValues) {

		for (CommerceOptionValue commerceOptionValue : commerceOptionValues) {
			if (matches(commerceOptionValue)) {
				return commerceOptionValue;
			}
		}

		return null;
	}

	@Override
	public String getOptionKey() {
		return _optionKey;
	}

	@Override
	public String getOptionValueKey() {
		return _optionValueKey;
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

	@Override
	public boolean matches(CommerceOptionValue commerceOptionValue) {
		if (commerceOptionValue == null) {
			return false;
		}

		if (Objects.equals(_optionKey, commerceOptionValue.getOptionKey()) &&
			Objects.equals(
				_optionValueKey, commerceOptionValue.getOptionValueKey())) {

			return true;
		}

		return false;
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

	@Override
	public String toJSON() {
		return String.format(
			_JSON_SERIALIZED_PATTERN, _cpInstanceId, _optionKey, _price,
			_priceType, _quantity, _optionValueKey);
	}

	private static final String _JSON_SERIALIZED_PATTERN =
		"{\"cpInstanceId\":%d, \"key\":\"%s\", \"price\":\"%s\", " +
			"\"priceType\":\"%s\", \"quantity\":%d, \"value\":\"%s\"}";

	private long _cpInstanceId;
	private String _optionKey;
	private final String _optionValueKey;
	private BigDecimal _price;
	private String _priceType;
	private int _quantity;

}