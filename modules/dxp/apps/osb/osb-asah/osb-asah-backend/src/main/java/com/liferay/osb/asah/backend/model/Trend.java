/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.model.TrendClassification;

import java.math.BigDecimal;

import java.util.Objects;

/**
 * @author Inácio Nery
 */
public class Trend {

	public Trend() {
	}

	public Trend(
		TrendClassification trendClassification, BigDecimal percentage) {

		_trendClassification = trendClassification;
		_percentage = percentage;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Trend)) {
			return false;
		}

		Trend trend = (Trend)obj;

		if (Objects.equals(_percentage, trend._percentage) &&
			Objects.equals(_trendClassification, trend._trendClassification)) {

			return true;
		}

		return false;
	}

	public BigDecimal getPercentage() {
		return _percentage;
	}

	public TrendClassification getTrendClassification() {
		return _trendClassification;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_trendClassification, _percentage);
	}

	public void setPercentage(BigDecimal percentage) {
		_percentage = percentage;
	}

	public void setTrendClassification(
		TrendClassification trendClassification) {

		_trendClassification = trendClassification;
	}

	private BigDecimal _percentage;
	private TrendClassification _trendClassification;

}