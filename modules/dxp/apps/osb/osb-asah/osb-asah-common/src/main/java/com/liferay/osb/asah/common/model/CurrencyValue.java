/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.math.BigDecimal;

import java.util.Objects;

/**
 * @author Riccardo Ferrari
 */
public class CurrencyValue {

	public CurrencyValue() {
	}

	public CurrencyValue(
		String currencyCode, Double percentageVariation, BigDecimal value) {

		_currencyCode = currencyCode;
		_percentageVariation = percentageVariation;
		_value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}

		CurrencyValue currencyValue = (CurrencyValue)obj;

		if (Objects.equals(_currencyCode, currencyValue._currencyCode) &&
			Objects.equals(
				_percentageVariation, currencyValue._percentageVariation) &&
			Objects.equals(_value, currencyValue._value)) {

			return true;
		}

		return false;
	}

	public String getCurrencyCode() {
		return _currencyCode;
	}

	public Double getPercentageVariation() {
		return _percentageVariation;
	}

	public BigDecimal getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_currencyCode, _percentageVariation, _value);
	}

	public void setCurrencyCode(String currencyCode) {
		_currencyCode = currencyCode;
	}

	public void setPercentageVariation(Double percentageVariation) {
		_percentageVariation = percentageVariation;
	}

	public void setValue(BigDecimal value) {
		_value = value;
	}

	private String _currencyCode;
	private Double _percentageVariation;
	private BigDecimal _value;

}