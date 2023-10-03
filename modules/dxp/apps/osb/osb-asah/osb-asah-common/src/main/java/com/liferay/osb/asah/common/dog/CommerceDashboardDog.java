/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.CurrencyValue;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQOrderRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Riccardo Ferrari
 */
@Component
public class CommerceDashboardDog {

	public Map<String, CurrencyValue> getOrderAccountAverageCurrencyValues(
		Long channelId, boolean compareToPrevious, TimeRange timeRange) {

		Map<String, BigDecimal> currentOrderAccountAverageCurrencyValues =
			_bqOrderRepository.getOrderAccountAverageCurrencyValues(
				channelId, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Map<String, BigDecimal> previousOrderAccountAverageCurrencyValues =
			null;

		if (compareToPrevious) {
			TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

			previousOrderAccountAverageCurrencyValues =
				_bqOrderRepository.getOrderAccountAverageCurrencyValues(
					channelId, previousTimeRange.getEndLocalDateTime(),
					previousTimeRange.getStartLocalDateTime(),
					_timeZoneDog.getTimeZoneId());
		}

		Map<String, CurrencyValue> orderAccountAverageCurrencyValues =
			new HashMap<>();

		for (Map.Entry<String, BigDecimal>
				currentOrderAccountAverageCurrencyValue :
					currentOrderAccountAverageCurrencyValues.entrySet()) {

			String currencyCode =
				currentOrderAccountAverageCurrencyValue.getKey();

			CurrencyValue currencyValue = new CurrencyValue(
				currencyCode, null,
				currentOrderAccountAverageCurrencyValue.getValue());

			if (compareToPrevious) {
				BigDecimal previousOrderAccountAverageCurrencyValue =
					previousOrderAccountAverageCurrencyValues.getOrDefault(
						currencyCode, BigDecimal.ZERO);

				currencyValue.setPercentageVariation(
					_getPercentageVariation(
						currentOrderAccountAverageCurrencyValue.getValue(),
						previousOrderAccountAverageCurrencyValue));
			}

			orderAccountAverageCurrencyValues.put(currencyCode, currencyValue);
		}

		return orderAccountAverageCurrencyValues;
	}

	public Map<String, CurrencyValue> getOrderAverageCurrencyValues(
		Long channelId, boolean compareToPrevious, TimeRange timeRange) {

		Map<String, BigDecimal> currentOrderAverageCurrencyValues =
			_bqOrderRepository.getOrderAverageCurrencyValues(
				channelId, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Map<String, BigDecimal> previousOrderAverageCurrencyValues = null;

		if (compareToPrevious) {
			TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

			previousOrderAverageCurrencyValues =
				_bqOrderRepository.getOrderAverageCurrencyValues(
					channelId, previousTimeRange.getEndLocalDateTime(),
					previousTimeRange.getStartLocalDateTime(),
					_timeZoneDog.getTimeZoneId());
		}

		Map<String, CurrencyValue> orderAverageCurrencyValues = new HashMap<>();

		for (Map.Entry<String, BigDecimal> currentOrderAverageCurrencyValue :
				currentOrderAverageCurrencyValues.entrySet()) {

			String currencyCode = currentOrderAverageCurrencyValue.getKey();

			CurrencyValue currencyValue = new CurrencyValue(
				currencyCode, null,
				currentOrderAverageCurrencyValue.getValue());

			if (compareToPrevious) {
				BigDecimal previousOrderAverageCurrencyValue =
					previousOrderAverageCurrencyValues.getOrDefault(
						currencyCode, BigDecimal.ZERO);

				currencyValue.setPercentageVariation(
					_getPercentageVariation(
						currentOrderAverageCurrencyValue.getValue(),
						previousOrderAverageCurrencyValue));
			}

			orderAverageCurrencyValues.put(currencyCode, currencyValue);
		}

		return orderAverageCurrencyValues;
	}

	public Map<String, CurrencyValue> getOrderIncompleteCurrencyValues(
		Long channelId, boolean compareToPrevious, TimeRange timeRange) {

		Map<String, BigDecimal> currentOrderIncompleteCurrencyValues =
			_bqOrderRepository.getOrderIncompleteCurrencyValues(
				channelId, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Map<String, BigDecimal> previousOrderIncompleteCurrencyValues = null;

		if (compareToPrevious) {
			TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

			previousOrderIncompleteCurrencyValues =
				_bqOrderRepository.getOrderIncompleteCurrencyValues(
					channelId, previousTimeRange.getEndLocalDateTime(),
					previousTimeRange.getStartLocalDateTime(),
					_timeZoneDog.getTimeZoneId());
		}

		Map<String, CurrencyValue> orderIncompleteCurrencyValues =
			new HashMap<>();

		for (Map.Entry<String, BigDecimal> currentOrderIncompleteCurrencyValue :
				currentOrderIncompleteCurrencyValues.entrySet()) {

			String currencyCode = currentOrderIncompleteCurrencyValue.getKey();

			CurrencyValue currencyValue = new CurrencyValue(
				currencyCode, null,
				currentOrderIncompleteCurrencyValue.getValue());

			if (compareToPrevious) {
				BigDecimal previousOrderIncompleteCurrencyValue =
					previousOrderIncompleteCurrencyValues.getOrDefault(
						currencyCode, BigDecimal.ZERO);

				currencyValue.setPercentageVariation(
					_getPercentageVariation(
						currentOrderIncompleteCurrencyValue.getValue(),
						previousOrderIncompleteCurrencyValue));
			}

			orderIncompleteCurrencyValues.put(currencyCode, currencyValue);
		}

		return orderIncompleteCurrencyValues;
	}

	public Map<String, CurrencyValue> getOrderTotalCurrencyValues(
		Long channelId, boolean compareToPrevious, TimeRange timeRange) {

		Map<String, BigDecimal> currentOrderTotalCurrencyValues =
			_bqOrderRepository.getOrderTotalCurrencyValues(
				channelId, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(),
				_timeZoneDog.getTimeZoneId());

		Map<String, BigDecimal> previousOrderTotalCurrencyValues = null;

		if (compareToPrevious) {
			TimeRange previousTimeRange = timeRange.getPreviousTimeRange();

			previousOrderTotalCurrencyValues =
				_bqOrderRepository.getOrderTotalCurrencyValues(
					channelId, previousTimeRange.getEndLocalDateTime(),
					previousTimeRange.getStartLocalDateTime(),
					_timeZoneDog.getTimeZoneId());
		}

		Map<String, CurrencyValue> orderTotalCurrencyValues = new HashMap<>();

		for (Map.Entry<String, BigDecimal> currentOrderTotalCurrencyValue :
				currentOrderTotalCurrencyValues.entrySet()) {

			String currencyCode = currentOrderTotalCurrencyValue.getKey();

			CurrencyValue currencyValue = new CurrencyValue(
				currencyCode, null, currentOrderTotalCurrencyValue.getValue());

			if (compareToPrevious) {
				BigDecimal previousOrderTotalCurrencyValue =
					previousOrderTotalCurrencyValues.getOrDefault(
						currencyCode, BigDecimal.ZERO);

				currencyValue.setPercentageVariation(
					_getPercentageVariation(
						currentOrderTotalCurrencyValue.getValue(),
						previousOrderTotalCurrencyValue));
			}

			orderTotalCurrencyValues.put(currencyCode, currencyValue);
		}

		return orderTotalCurrencyValues;
	}

	private double _getPercentageVariation(
		BigDecimal currentValue, BigDecimal previousValue) {

		if (previousValue.equals(BigDecimal.ZERO) ||
			previousValue.equals(BigDecimal.valueOf(0.0))) {

			return 0.0;
		}

		currentValue = currentValue.setScale(3, RoundingMode.HALF_UP);

		previousValue = previousValue.setScale(3, RoundingMode.HALF_UP);

		BigDecimal delta = currentValue.subtract(previousValue);

		delta = delta.divide(previousValue, RoundingMode.HALF_UP);

		delta = delta.setScale(3, RoundingMode.HALF_UP);

		return delta.doubleValue() * 100;
	}

	@Autowired
	private BQOrderRepository _bqOrderRepository;

	@Autowired
	private ChannelDog _channelDog;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}