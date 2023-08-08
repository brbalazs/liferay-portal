/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.repository.BQOrderRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.util.GetterUtil;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.AggregateFunction;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SelectHavingStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author Riccardo Ferrari
 */
@Repository
public class BQOrderRepositoryImpl implements BQOrderRepository {

	public BQOrderRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public Map<String, BigDecimal> getOrderAccountAverageCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		if (channelId == null) {
			return Collections.emptyMap();
		}

		AggregateFunction<Integer> accountIdCountDistinctAggregateFunction =
			DSL.countDistinct(DSL.field("accountId"));

		AggregateFunction<BigDecimal> totalSumAggregateFunction = DSL.sum(
			DSL.field(
				"total"
			).cast(
				BigDecimal.class
			));

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_getSelectHavingStep(
				Arrays.asList(
					DSL.field("currencyCode"),
					totalSumAggregateFunction.div(
						accountIdCountDistinctAggregateFunction
					).as(
						"orderAccountAverageCurrencyValue"
					)),
				_getConditions(
					channelId, Arrays.asList(0L, 1L, 10L, 14L, 15L, 20L),
					rangeEndLocalDateTime, rangeStartLocalDateTime,
					timeZoneId)),
			GetterUtil::getBigDecimal);
	}

	@Override
	public Map<String, BigDecimal> getOrderAverageCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		if (channelId == null) {
			return Collections.emptyMap();
		}

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_getSelectHavingStep(
				Arrays.asList(
					DSL.field("currencyCode"),
					DSL.avg(
						DSL.field(
							"total"
						).cast(
							BigDecimal.class
						)
					).as(
						"orderAverageCurrencyValue"
					)),
				_getConditions(
					channelId, Arrays.asList(0L, 1L, 10L, 14L, 15L, 20L),
					rangeEndLocalDateTime, rangeStartLocalDateTime,
					timeZoneId)),
			GetterUtil::getBigDecimal);
	}

	@Override
	public Map<String, BigDecimal> getOrderIncompleteCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		if (channelId == null) {
			return Collections.emptyMap();
		}

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_getSelectHavingStep(
				Arrays.asList(
					DSL.field("currencyCode"),
					DSL.sum(
						DSL.field(
							"total"
						).cast(
							BigDecimal.class
						)
					).as(
						"orderIncompleteCurrencyValue"
					)),
				_getConditions(
					channelId, Arrays.asList(2L, 6L), rangeEndLocalDateTime,
					rangeStartLocalDateTime, timeZoneId)),
			GetterUtil::getBigDecimal);
	}

	@Override
	public Map<String, BigDecimal> getOrderTotalCurrencyValues(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		if (channelId == null) {
			return Collections.emptyMap();
		}

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_getSelectHavingStep(
				Arrays.asList(
					DSL.field("currencyCode"),
					DSL.sum(
						DSL.field(
							"total"
						).cast(
							BigDecimal.class
						)
					).as(
						"orderTotalCurrencyValue"
					)),
				_getConditions(
					channelId, Arrays.asList(0L, 1L, 10L, 14L, 15L, 20L),
					rangeEndLocalDateTime, rangeStartLocalDateTime,
					timeZoneId)),
			GetterUtil::getBigDecimal);
	}

	private List<Condition> _getConditions(
		Long channelId, List<Long> orderStatuses,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"channelId", Long.class
			).eq(
				channelId
			));
		conditions.add(
			DSL.field(
				"orderDate"
			).between(
				_dslHelper.getDateParam(rangeStartLocalDateTime, timeZoneId),
				_dslHelper.getDateParam(rangeEndLocalDateTime, timeZoneId)
			));
		conditions.add(
			DSL.field(
				"orderStatus"
			).in(
				orderStatuses
			));

		return conditions;
	}

	private SelectHavingStep<Record> _getSelectHavingStep(
		List<Field> fields, List<Condition> conditions) {

		return _dslContext.select(
			fields
		).from(
			DSL.table("BQOrder")
		).where(
			conditions
		).groupBy(
			DSL.field("currencyCode")
		);
	}

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private QueryExecutor _queryExecutor;

}