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

package com.liferay.commerce.product.internal.option;

import com.liferay.commerce.product.option.CommerceOptionValue;

import java.math.BigDecimal;

/**
 * @author Igor Beslic
 */
public class CommerceOptionValueImpl implements CommerceOptionValue {

	@Override
	public long getCPInstanceId() {
		return 0;
	}

	@Override
	public String getOptionKey() {
		return null;
	}

	@Override
	public BigDecimal getPrice() {
		return null;
	}

	@Override
	public String getPriceType() {
		return null;
	}

	@Override
	public int getQuantity() {
		return 0;
	}

	public static class Builder {

		public CommerceOptionValue build() {
			CommerceOptionValueImpl commerceOptionValue =
				new CommerceOptionValueImpl();

			commerceOptionValue._cpInstanceId = _cpInstanceId;
			commerceOptionValue._optionKey = _optionKey;
			commerceOptionValue._price = _price;
			commerceOptionValue._priceType = _priceType;
			commerceOptionValue._quantity = _quantity;

			return commerceOptionValue;
		}

		public Builder cpInstanceId(long cpInstanceId) {
			_cpInstanceId = cpInstanceId;

			return this;
		}

		public Builder optionKey(String optionKey) {
			_optionKey = optionKey;

			return this;
		}

		public Builder price(BigDecimal price) {
			_price = price;

			return this;
		}

		public Builder priceType(String priceType) {
			_priceType = priceType;

			return this;
		}

		public Builder quantity(int quantity) {
			_quantity = quantity;

			return this;
		}

		private long _cpInstanceId;
		private String _optionKey;
		private BigDecimal _price;
		private String _priceType;
		private int _quantity;

	}

	private CommerceOptionValueImpl() {
	}

	private long _cpInstanceId;
	private String _optionKey;
	private BigDecimal _price;
	private String _priceType;
	private int _quantity;

}