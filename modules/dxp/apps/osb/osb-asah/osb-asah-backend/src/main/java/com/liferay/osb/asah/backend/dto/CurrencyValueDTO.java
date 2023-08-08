/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.liferay.osb.asah.backend.model.Trend;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.CurrencyValue;
import com.liferay.osb.asah.common.model.TrendClassification;

import java.math.BigDecimal;

/**
 * @author Riccardo Ferrari
 */
@GraphQLType("CurrencyValue")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyValueDTO {

	public CurrencyValueDTO(CurrencyValue currencyValue) {
		_currencyCode = currencyValue.getCurrencyCode();

		if (currencyValue.getPercentageVariation() != null) {
			_trend = new Trend();

			_trend.setPercentage(
				BigDecimal.valueOf(currencyValue.getPercentageVariation()));

			if (currencyValue.getPercentageVariation() > 0.0) {
				_trend.setTrendClassification(TrendClassification.POSITIVE);
			}
			else if (currencyValue.getPercentageVariation() < 0.0) {
				_trend.setTrendClassification(TrendClassification.NEGATIVE);
			}
			else {
				_trend.setTrendClassification(TrendClassification.NEUTRAL);
			}
		}
		else {
			_trend = null;
		}

		_value = currencyValue.getValue();
	}

	public String getCurrencyCode() {
		return _currencyCode;
	}

	public Trend getTrend() {
		return _trend;
	}

	public String getValue() {
		return _value.toPlainString();
	}

	private final String _currencyCode;
	private final Trend _trend;
	private final BigDecimal _value;

}