/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.Map;

/**
 * @author Riccardo Ferrari
 */
public interface CustomBQOrderRepository {

	public Map<String, BigDecimal> getOrderAccountAverageCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public Map<String, BigDecimal> getOrderAverageCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public Map<String, BigDecimal> getOrderIncompleteCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public Map<String, BigDecimal> getOrderTotalCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

}