/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.entity.BQIdentity;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.CustomBQIdentityRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.BooleanUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.SelectConditionStep;
import org.jooq.SelectFinalStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public class BQIdentityRepositoryImpl
	extends BaseRepository implements CustomBQIdentityRepository {

	public BQIdentityRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public long countBQIndividuals(boolean includeAnonymousUsers) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		SelectJoinStep<Record1<Integer>> selectJoinStep = selectSelectStep.from(
			DSL.table(
				"BQIdentity"
			).as(
				"Identity"
			));

		if (!includeAnonymousUsers) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).on(
				DSL.and(
					DSL.field(
						"Identity.individualId"
					).eq(
						DSL.field("Individual.id")
					),
					DSL.or(
						DSL.field(
							"Individual.suppressed"
						).isNull(),
						DSL.field(
							"Individual.suppressed"
						).notEqual(
							DSL.val(Boolean.TRUE)
						)))
			);
		}

		return _queryExecutor.queryForLong(selectJoinStep);
	}

	@Override
	public List<BQIdentity> findAll() {
		return _queryExecutor.queryForList(
			BQIdentity::new,
			_dslContext.select(
				DSL.asterisk()
			).from(
				"BQIdentity"
			));
	}

	@Override
	public List<BQIdentity> findByIdIn(Collection<String> ids) {
		return _queryExecutor.queryForList(
			BQIdentity::new,
			_dslContext.selectFrom(
				"BQIdentity"
			).where(
				DSL.field(
					"id"
				).in(
					ids
				)
			));
	}

	@Override
	public List<String> getBQIdentityIds(String bqIndividualId) {
		return _queryExecutor.queryForList(
			record -> (String)record.get("id"),
			_dslContext.select(
				DSL.field("id", String.class)
			).from(
				"BQIdentity"
			).where(
				DSL.and(
					DSL.field(
						"individualId", String.class
					).eq(
						bqIndividualId
					),
					DSL.field(
						"individualId"
					).notIn(
						_dslContext.select(
							DSL.field("TO_HEX(SHA256(emailAddress))")
						).from(
							"Suppression"
						)
					))
			));
	}

	@Override
	public String getBQIndividualId(String id) {
		Optional<String> bqIndividualIdOptional = _queryExecutor.queryForObject(
			recordMap -> (String)recordMap.get("individualId"),
			_dslContext.select(
				DSL.field("individualId")
			).from(
				"BQIdentity"
			).where(
				DSL.and(
					DSL.field(
						"id"
					).eq(
						id
					),
					DSL.field(
						"individualId"
					).notIn(
						_dslContext.select(
							DSL.field("TO_HEX(SHA256(emailAddress))")
						).from(
							"Suppression"
						)
					))
			));

		return bqIndividualIdOptional.orElse(null);
	}

	@Override
	public long getBQIndividualsCount(
		@Nullable Boolean active, @Nullable Long channelId, LocalDate localDate,
		MetricType metricType, ZoneId zoneId) {

		Optional<BigDecimal> optional = _queryExecutor.queryForObject(
			record -> record.get("count"),
			(SelectFinalStep)_getSelectConditionStep(
				active, channelId, localDate, metricType, 0, zoneId));

		if (optional.isPresent()) {
			BigDecimal value = optional.get();

			return value.longValue();
		}

		return 0;
	}

	@Override
	public List<Long> getBQIndividualsCounts(
		@Nullable Boolean active, @Nullable Long channelId,
		List<LocalDate> localDates, List<MetricType> metricTypes,
		ZoneId zoneId) {

		SelectConditionStep<Record1<BigDecimal>> selectConditionStep = null;

		int unionOrder = 0;

		for (LocalDate localDate : localDates) {
			for (MetricType metricType : metricTypes) {
				SelectConditionStep<Record1<BigDecimal>>
					curSelectConditionStep = _getSelectConditionStep(
						active, channelId, localDate, metricType, unionOrder++,
						zoneId);

				if (selectConditionStep == null) {
					selectConditionStep = curSelectConditionStep;
				}
				else {
					selectConditionStep.unionAll(curSelectConditionStep);
				}
			}
		}

		if (selectConditionStep == null) {
			return Collections.emptyList();
		}

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal count = (BigDecimal)record.get("count");

				return count.longValue();
			},
			selectConditionStep.orderBy(DSL.field("unionOrder")));
	}

	@Override
	public BQIdentity insert(BQIdentity bqIdentity) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("Identity_Raw")
			).columns(
				DSL.field("createDate"), DSL.field("id"),
				DSL.field("individualId")
			).values(
				_dslHelper.getDateParam(bqIdentity.getCreateDate()),
				bqIdentity.getId(), bqIdentity.getIndividualId()
			));

		return bqIdentity;
	}

	private List<Condition> _getConditions(
		Boolean active, Long channelId, LocalDate localDate,
		MetricType metricType, ZoneId zoneId) {

		LocalDateTime localDateTime = localDate.atTime(LocalTime.MAX);

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				metricType.getFieldName()
			).lessOrEqual(
				_dslHelper.getDateParam(localDateTime, zoneId.toString())
			));

		if (BooleanUtils.isTrue(active) || (channelId != null)) {
			Condition condition = DSL.field(
				"Identity.id"
			).eq(
				DSL.field("IdentityActivity.identityId")
			);

			if (channelId != null) {
				condition = DSL.and(
					condition,
					DSL.field(
						"IdentityActivity.channelId"
					).eq(
						channelId
					));
			}

			if (BooleanUtils.isTrue(active)) {
				TimeRange timeRange = TimeRange.LAST_30_DAYS;

				condition = DSL.and(
					condition,
					DSL.field(
						"IdentityActivity.lastActivityDate"
					).greaterThan(
						_dslHelper.getDateParam(
							timeRange.getStartLocalDateTime(),
							zoneId.toString())
					));
			}

			conditions.add(
				DSL.exists(
					DSL.select(
						DSL.field("IdentityActivity.identityId")
					).from(
						DSL.table(
							"BQIdentityActivity"
						).as(
							"IdentityActivity"
						)
					).where(
						condition
					)));
		}

		if (metricType == IndividualMetricType.ANONYMOUS_INDIVIDUALS) {
			conditions.add(
				DSL.field(
					"Individual.id"
				).isNull());
		}

		if (metricType == IndividualMetricType.KNOWN_INDIVIDUALS) {
			conditions.add(
				DSL.field(
					"Individual.id"
				).isNotNull());
		}

		return conditions;
	}

	private SelectConditionStep _getSelectConditionStep(
		@Nullable Boolean active, @Nullable Long channelId, LocalDate localDate,
		MetricType metricType, int unionOrder, ZoneId zoneId) {

		SelectSelectStep<Record2<BigDecimal, Integer>> selectSelectStep =
			_dslContext.select(
				DSL.cast(
					DSL.countDistinct(
						DSL.coalesce(
							DSL.field("Individual.id"),
							DSL.field("Identity.id"))),
					BigDecimal.class
				).as(
					"count"
				),
				DSL.val(
					unionOrder, Integer.class
				).as(
					"unionOrder"
				));

		SelectJoinStep<Record2<BigDecimal, Integer>> selectJoinStep =
			selectSelectStep.from(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				));

		selectJoinStep = selectJoinStep.leftJoin(
			DSL.table(
				"BQIndividual"
			).as(
				"Individual"
			)
		).on(
			DSL.and(
				DSL.field(
					"Identity.individualId"
				).eq(
					DSL.field("Individual.id")
				),
				DSL.or(
					DSL.field(
						"Individual.suppressed"
					).isNull(),
					DSL.field(
						"Individual.suppressed"
					).notEqual(
						DSL.val(Boolean.TRUE)
					)))
		);

		return selectJoinStep.where(
			_getConditions(active, channelId, localDate, metricType, zoneId));
	}

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private QueryExecutor _queryExecutor;

}