/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQMembership;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.model.MembershipCountSnapshot;
import com.liferay.osb.asah.common.repository.CustomBQMembershipRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import org.jooq.AggregateFunction;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DeleteUsingStep;
import org.jooq.Field;
import org.jooq.InsertValuesStep7;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record7;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Inácio Nery
 */
public class BQMembershipRepositoryImpl
	implements CustomBQMembershipRepository {

	public BQMembershipRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long countActiveMembersBySegmentId(
		@Nullable Boolean includeAnonymousUsers, Long segmentId,
		ZoneId zoneId) {

		LocalDateTime localDateTime = LocalDateTime.now(zoneId);

		localDateTime = localDateTime.minusDays(30);

		List<Condition> conditions = new ArrayList<>();

		if (BooleanUtils.isFalse(includeAnonymousUsers)) {
			conditions.add(
				DSL.field(
					"Membership.individualId"
				).isNotNull());
		}

		conditions.add(
			DSL.field(
				"Membership.segmentId"
			).eq(
				segmentId
			));

		return _queryExecutor.queryForLong(
			_dslContext.with(
				"SuppressedIdentityActivity"
			).as(
				_dslContext.select(
					DSL.field("IdentityActivity.identityId"),
					DSL.when(
						DSL.not(
							DSL.coalesce(
								DSL.field(
									"Individual.suppressed", Boolean.class),
								false)),
						DSL.field("IdentityActivity.individualId")
					).as(
						"individualId"
					),
					DSL.field("IdentityActivity.lastActivityDate")
				).from(
					DSL.table(
						"BQIdentityActivity"
					).as(
						"IdentityActivity"
					)
				).leftJoin(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).on(
					DSL.field(
						"IdentityActivity.individualId"
					).eq(
						DSL.field("Individual.id")
					)
				)
			).with(
				"ActiveMembers"
			).as(
				_dslContext.select(
					DSL.field("SuppressedIdentityActivity.individualId"),
					DSL.field("SuppressedIdentityActivity.identityId")
				).from(
					DSL.table(
						"BQMembership"
					).as(
						"Membership"
					)
				).leftJoin(
					"SuppressedIdentityActivity"
				).on(
					DSL.and(
						DSL.field(
							"SuppressedIdentityActivity.lastActivityDate"
						).greaterOrEqual(
							DSL.field(
								"TIMESTAMP '" +
									DateUtil.toUTCString(localDateTime) + "'")
						),
						DSL.field(
							"Membership.identityId"
						).eq(
							DSL.field("SuppressedIdentityActivity.identityId")
						),
						DSL.coalesce(
							DSL.field("Membership.individualId"), ""
						).eq(
							DSL.coalesce(
								DSL.field(
									"SuppressedIdentityActivity.individualId"),
								"")
						))
				).where(
					conditions
				).groupBy(
					DSL.field("SuppressedIdentityActivity.individualId"),
					DSL.field("SuppressedIdentityActivity.identityId")
				)
			).select(
				DSL.countDistinct(
					DSL.coalesce(
						DSL.field("individualId"), DSL.field("identityId")))
			).from(
				"ActiveMembers"
			));
	}

	@Override
	public long countByIdentityIdAndSegmentId(
		String identityId, Long segmentId) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQMembership"
			).where(
				_getConditions(
					Collections.singletonList(identityId),
					Collections.singletonList(segmentId), null)
			));
	}

	@Override
	public long countByIdentityIdInAndSegmentIdAndStatus(
		List<String> identityIds, Long segmentId, String status) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQMembership"
			).where(
				_getConditions(
					identityIds, Collections.singletonList(segmentId), status)
			));
	}

	@Override
	public long countBySegmentId(Long segmentId) {
		return _queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				DSL.table("BQMembership")
			).where(
				DSL.field(
					"segmentId"
				).eq(
					segmentId
				)
			));
	}

	@Override
	public long countBySegmentIdAndStatus(Long segmentId, String status) {
		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQMembership"
			).where(
				_getConditions(
					null, Collections.singletonList(segmentId), status)
			));
	}

	@Override
	public void deleteByIndividualIdAndSegmentId(
		String individualId, Long segmentId) {

		Field<Object> individualIdField = DSL.field("individualId");
		Field<Object> segmentIdField = DSL.field("segmentId");

		DeleteUsingStep<Record> deleteUsingStep = _dslContext.deleteFrom(
			DSL.table("BQMembership"));

		_queryExecutor.queryExecute(
			deleteUsingStep.where(
				DSL.and(
					individualIdField.eq(individualId),
					segmentIdField.eq(segmentId))));
	}

	@Override
	public void deleteBySegmentIdIn(List<Long> segmentIds) {
		Field<Object> segmentIdField = DSL.field("segmentId");

		DeleteUsingStep<Record> deleteUsingStep = _dslContext.deleteFrom(
			DSL.table("BQMembership"));

		_queryExecutor.queryExecute(
			deleteUsingStep.where(segmentIdField.in(segmentIds)));
	}

	@Override
	public boolean existsByIdentityIdAndSegmentIdAndStatus(
		String identityId, Long segmentId, String status) {

		return _queryExecutor.queryExists(
			_dslContext.select(
				DSL.field("identityId")
			).from(
				DSL.table("BQMembership")
			).where(
				DSL.and(
					DSL.field(
						"identityId"
					).eq(
						identityId
					),
					DSL.field(
						"segmentId"
					).eq(
						segmentId
					))
			));
	}

	@Override
	public List<BQMembership> findAll() {
		return _queryExecutor.queryForList(
			BQMembership::new,
			_dslContext.selectFrom(DSL.table("BQMembership")));
	}

	@Override
	public List<BQMembership> findByIdentityIdAndStatus(
		String identityId, String status) {

		return _queryExecutor.queryForList(
			BQMembership::new,
			_dslContext.select(
			).from(
				"BQMembership"
			).where(
				_getConditions(
					Collections.singletonList(identityId), null, status)
			));
	}

	@Override
	public List<BQMembership> findByIdentityIdInAndSegmentIdAndStatus(
		List<String> identityIds, Long segmentId, String status,
		Pageable pageable) {

		return _queryExecutor.queryForList(
			BQMembership::new,
			_dslContext.select(
			).from(
				"BQMembership"
			).where(
				_getConditions(
					identityIds, Collections.singletonList(segmentId), status)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<BQMembership> findByIndividualIdAndSegmentIdInAndStatus(
		String individualId, List<Long> segmentIds, String status) {

		return _queryExecutor.queryForList(
			BQMembership::new,
			_dslContext.selectFrom(
				"BQMembership"
			).where(
				DSL.and(
					DSL.field(
						"individualId"
					).eq(
						individualId
					),
					DSL.field(
						"segmentId", Long.class
					).in(
						segmentIds
					),
					DSL.field(
						"status"
					).in(
						status
					))
			));
	}

	@Override
	public List<BQMembership> findBySegmentIdAndStatus(
		Long segmentId, String status, Pageable pageable) {

		return _queryExecutor.queryForList(
			BQMembership::new,
			_dslContext.select(
			).from(
				"BQMembership"
			).where(
				_getConditions(
					null, Collections.singletonList(segmentId), status)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<String> findIdentityIdBySegmentIdAndStatus(
		Long segmentId, String status) {

		Field<Object> identityIdField = DSL.field("identityId");

		SelectSelectStep<Record1<Object>> selectSelectStep = _dslContext.select(
			identityIdField);

		Field<Object> segmentIdField = DSL.field("segmentId");
		Field<Object> statusField = DSL.field("status");

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get("identityId")),
			selectSelectStep.from(
				"BQMembership"
			).where(
				segmentIdField.eq(segmentId)
			).and(
				statusField.eq(status)
			));
	}

	@Override
	public List<String> findIdentityIdBySegmentIdIn(
		List<Long> segmentIds, int max, int min, boolean ascending) {

		Field<Object> identityIdField = DSL.field("identityId");

		SelectSelectStep<Record1<Object>> selectSelectStep = _dslContext.select(
			identityIdField);

		Field<Object> segmentIdField = DSL.field("segmentId");

		AggregateFunction<Integer> aggregateFunction = DSL.count(
			segmentIdField);

		return _queryExecutor.queryForList(
			recordMap -> String.valueOf(recordMap.get("identityId")),
			selectSelectStep.from(
				"BQMembership"
			).where(
				segmentIdField.in(segmentIds)
			).groupBy(
				identityIdField
			).having(
				_getConditions(max, min, ascending)
			).orderBy(
				ascending ? aggregateFunction.asc() : aggregateFunction.desc()
			));
	}

	@Override
	public List<Long> findSegmentIdByIdentityIdAndStatus(
		String identityId, String status) {

		Field<Object> segmentIdField = DSL.field("segmentId");

		SelectSelectStep<Record1<Object>> selectSelectStep = _dslContext.select(
			segmentIdField);

		Field<Object> statusField = DSL.field("status");
		Field<Object> identityIdField = DSL.field("identityId");

		return _queryExecutor.queryForList(
			recordMap -> {
				BigDecimal bigDecimal = (BigDecimal)recordMap.get("segmentId");

				return bigDecimal.longValue();
			},
			selectSelectStep.from(
				"BQMembership"
			).where(
				statusField.eq(status)
			).and(
				identityIdField.eq(identityId)
			));
	}

	@Override
	public List<Long> findSegmentIdByIdentityIdInAndStatus(
		List<String> identityIds, String status) {

		Field<Object> segmentIdField = DSL.field("segmentId");

		SelectSelectStep<Record1<Object>> selectSelectStep =
			_dslContext.selectDistinct(segmentIdField);

		Field<Object> statusField = DSL.field("status");
		Field<Object> identityIdField = DSL.field("identityId");

		return _queryExecutor.queryForList(
			recordMap -> {
				BigDecimal bigDecimal = (BigDecimal)recordMap.get("segmentId");

				return bigDecimal.longValue();
			},
			selectSelectStep.from(
				"BQMembership"
			).where(
				statusField.eq(status)
			).and(
				identityIdField.in(identityIds)
			));
	}

	@Override
	public List<Long> findSegmentIdByIndividualId(String individualId) {
		Field<Long> segmentIdField = DSL.field(
			"BQMembership.segmentId", Long.class);

		SelectSelectStep<Record1<Long>> selectSelectStep = _dslContext.select(
			segmentIdField);

		Field<String> individualIdField = DSL.field(
			"individualId", String.class);

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal segmentId = (BigDecimal)record.get("segmentId");

				return segmentId.longValue();
			},
			selectSelectStep.from(
				"BQMembership"
			).where(
				DSL.and(
					individualIdField.eq(individualId),
					individualIdField.notIn(
						_dslContext.select(
							DSL.field(
								"TO_HEX(SHA256(emailAddress))", String.class)
						).from(
							"Suppression"
						)))
			));
	}

	@Override
	public List<Map<String, Long>>
		findSegmentIdIdentitiesCountByIndividualIdAndStatus(
			String individualId, String status) {

		Field<Long> segmentIdField = DSL.field(
			"BQMembership.segmentId", Long.class);
		Field<BigDecimal> identitiesCountField = DSL.field(
			"identitiesCount", BigDecimal.class);

		Field<String> statusField = DSL.field("status", String.class);
		Field<String> individualIdField = DSL.field(
			"individualId", String.class);

		return _queryExecutor.queryForList(
			record -> {
				BigDecimal identitiesCount = (BigDecimal)record.get(
					"identitiesCount");
				BigDecimal segmentId = (BigDecimal)record.get("segmentId");

				Map<String, Long> map = new HashMap<>();

				map.put("identitiesCount", identitiesCount.longValue());
				map.put("segmentId", segmentId.longValue());

				return map;
			},
			_dslContext.with(
				"SegmentsWithIndividual"
			).as(
				_dslContext.select(
					segmentIdField,
					DSL.coalesce(
						identitiesCountField, BigDecimal.ZERO
					).as(
						"identitiesCount"
					),
					DSL.rowNumber(
					).over(
						DSL.partitionBy(
							DSL.field("BQMembershipChange.segmentId")
						).orderBy(
							DSL.field(
								"BQMembershipChange.createDate"
							).desc()
						)
					).as(
						"rowNumber"
					)
				).from(
					"BQMembership"
				).join(
					"BQMembershipChange"
				).on(
					DSL.field(
						"BQMembership.segmentId"
					).eq(
						DSL.field("BQMembershipChange.segmentId")
					)
				).where(
					statusField.eq(status)
				).and(
					individualIdField.eq(individualId)
				)
			).select(
				DSL.field("segmentId"), DSL.field("identitiesCount")
			).from(
				"SegmentsWithIndividual"
			).where(
				DSL.field(
					"rowNumber"
				).eq(
					1
				)
			));
	}

	@Override
	public List<Long> findTop20SegmentIdByIndividualId(String individualId) {
		Field<Object> segmentIdField = DSL.field("segmentId");

		SelectSelectStep<Record1<Object>> selectSelectStep = _dslContext.select(
			segmentIdField);

		Field<Object> individualIdField = DSL.field("individualId");

		return _queryExecutor.queryForList(
			recordMap -> {
				BigDecimal segmentIdBigDecimal = (BigDecimal)recordMap.get(
					"segmentId");

				return segmentIdBigDecimal.longValue();
			},
			selectSelectStep.from(
				"BQMembership"
			).where(
				individualIdField.eq(individualId)
			).groupBy(
				segmentIdField
			).orderBy(
				DSL.count(
					individualIdField
				).desc()
			).limit(
				20
			));
	}

	@Override
	public MembershipCountSnapshot getMembershipCountSnapshot(
		Long channelId, Long segmentId) {

		Map<String, Object> membershipSnapshot = _queryExecutor.queryForMap(
			_dslContext.select(
				DSL.countDistinct(
					DSL.coalesce(
						DSL.field("Individual.id"), DSL.field("identityId"))
				).as(
					"identitiesCount"
				),
				DSL.countDistinct(
					DSL.field("Individual.id")
				).as(
					"individualsCount"
				)
			).from(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).leftJoin(
				DSL.table(
					"BQIndividual"
				).as(
					"Individual"
				)
			).on(
				DSL.and(
					DSL.field(
						"Membership.individualId"
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
			).where(
				DSL.field(
					"channelId", Long.class
				).eq(
					channelId
				),
				DSL.field(
					"segmentId", Long.class
				).eq(
					segmentId
				)
			));

		if (membershipSnapshot == null) {
			return new MembershipCountSnapshot(channelId, 0L, 0L, segmentId);
		}

		BigDecimal identitiesCountBigDecimal =
			(BigDecimal)membershipSnapshot.getOrDefault(
				"identitiesCount", BigDecimal.ZERO);
		BigDecimal individualsCountBigDecimal =
			(BigDecimal)membershipSnapshot.getOrDefault(
				"individualsCount", BigDecimal.ZERO);

		return new MembershipCountSnapshot(
			channelId, identitiesCountBigDecimal.longValue(),
			individualsCountBigDecimal.longValue(), segmentId);
	}

	@Override
	public BQMembership insert(BQMembership bqMembership) {
		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQMembership")
			).columns(
				DSL.field("channelId", Long.class),
				DSL.field("createDate", Date.class),
				DSL.field("individualId", String.class),
				DSL.field("identityId", String.class),
				DSL.field("modifiedDate", Date.class),
				DSL.field("segmentId", Long.class),
				DSL.field("status", String.class)
			).values(
				bqMembership.getChannelId(), bqMembership.getCreateDate(),
				bqMembership.getIndividualId(), bqMembership.getIdentityId(),
				bqMembership.getModifiedDate(), bqMembership.getSegmentId(),
				bqMembership.getStatus()
			));

		return bqMembership;
	}

	@Override
	public void insertAll(List<BQMembership> bqMemberships) {
		InsertValuesStep7
			<Record, Long, Date, String, String, Date, Long, String>
				insertValuesStep7 = _dslContext.insertInto(
					DSL.table("BQMembership")
				).columns(
					DSL.field("channelId", Long.class),
					DSL.field("createDate", Date.class),
					DSL.field("individualId", String.class),
					DSL.field("identityId", String.class),
					DSL.field("modifiedDate", Date.class),
					DSL.field("segmentId", Long.class),
					DSL.field("status", String.class)
				);

		for (BQMembership bqMembership : bqMemberships) {
			insertValuesStep7 = insertValuesStep7.values(
				bqMembership.getChannelId(), bqMembership.getCreateDate(),
				bqMembership.getIndividualId(), bqMembership.getIdentityId(),
				bqMembership.getModifiedDate(), bqMembership.getSegmentId(),
				bqMembership.getStatus());
		}

		_queryExecutor.queryExecute(insertValuesStep7);
	}

	@Override
	public void updateBQMemberships(
		Long channelId, String filterString, Boolean includeAnonymousUsers,
		Long segmentId) {

		Date date = new Date();

		SelectSelectStep
			<Record7<Long, Date, String, String, Date, Long, String>>
				selectSelectStep = _dslContext.select(
					DSL.val(
						channelId, Long.class
					).as(
						"channelId"
					),
					DSL.val(
						date, Date.class
					).as(
						"createDate"
					),
					DSL.field(
						"Identity.id", String.class
					).as(
						"identityId"
					),
					DSL.when(
						DSL.or(
							DSL.field(
								"Individual.suppressed", Boolean.class
							).isNull(),
							DSL.field(
								"Individual.suppressed", Boolean.class
							).ne(
								Boolean.TRUE
							)),
						DSL.field("Individual.id", String.class)
					).as(
						"individualId"
					),
					DSL.val(
						date, Date.class
					).as(
						"modifiedDate"
					),
					DSL.val(
						segmentId
					).as(
						"segmentId"
					),
					DSL.val(
						"ACTIVE"
					).as(
						"status"
					));

		SelectJoinStep<Record7<Long, Date, String, String, Date, Long, String>>
			selectJoinStep = selectSelectStep.from(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				).leftJoin(
					DSL.table(
						"BQIdentityActivity"
					).as(
						"IdentityActivity"
					)
				).on(
					DSL.field(
						"Identity.id"
					).eq(
						DSL.field("IdentityActivity.identityId")
					)
				));

		FilterExpression filterExpression = new FilterExpression(
			channelId, filterString, true);

		selectJoinStep = _getSelectJoinStep(
			includeAnonymousUsers, filterExpression.getReferencedTableNames(),
			selectJoinStep);

		List<Condition> conditions = new ArrayList<>();

		conditions.add(filterExpression.getCondition());

		conditions.add(
			DSL.field(
				"IdentityActivity.channelId"
			).eq(
				channelId
			));

		if (BooleanUtils.isFalse(includeAnonymousUsers)) {
			conditions.add(
				DSL.field(
					"Individual.id"
				).isNotNull());
		}

		// TODO Replace DELETE/INSERT by BigQuery MERGE Statement

		_queryExecutor.queryExecute(
			_dslContext.deleteFrom(
				DSL.table("BQMembership")
			).where(
				DSL.field(
					"segmentId", Long.class
				).eq(
					segmentId
				)
			));

		_queryExecutor.queryExecute(
			_dslContext.insertInto(
				DSL.table("BQMembership")
			).columns(
				DSL.field("channelId", Long.class),
				DSL.field("createDate", Date.class),
				DSL.field("identityId", String.class),
				DSL.field("individualId", String.class),
				DSL.field("modifiedDate", Date.class),
				DSL.field("segmentId", Long.class),
				DSL.field("status", String.class)
			).select(
				selectJoinStep.where(
					conditions
				).groupBy(
					DSL.field("Identity.id"), DSL.field("individualId"),
					DSL.field("segmentId")
				)
			));
	}

	private List<Condition> _getConditions(
		int max, int min, boolean ascending) {

		List<Condition> conditions = new ArrayList<>();

		AggregateFunction<Integer> aggregateFunction = DSL.count(
			DSL.field("segmentId"));

		conditions.add(aggregateFunction.ge(min));

		if (ascending) {
			conditions.add(aggregateFunction.le(max));
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		List<String> identityIds, List<Long> segmentIds, String status) {

		List<Condition> conditions = new ArrayList<>();

		if ((identityIds != null) && !identityIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"identityId"
				).in(
					identityIds
				));
		}

		if ((segmentIds != null) && !segmentIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"segmentId"
				).in(
					segmentIds
				));
		}

		if (StringUtils.isNotBlank(status)) {
			conditions.add(
				DSL.field(
					"status"
				).eq(
					status
				));
		}

		return conditions;
	}

	private <R extends Record> SelectJoinStep<R> _getSelectJoinStep(
		Boolean includeAnonymousUsers, Set<String> referencedTableNames,
		SelectJoinStep<R> selectJoinStep) {

		if (BooleanUtils.isFalse(includeAnonymousUsers)) {
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

		if (referencedTableNames.contains("Event")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQEvent"
				).as(
					"Event"
				)
			).on(
				DSL.field(
					"Event.userId"
				).eq(
					DSL.field("Identity.id")
				)
			);
		}

		if (referencedTableNames.contains("Individual")) {
			if (BooleanUtils.isTrue(includeAnonymousUsers)) {
				selectJoinStep = selectJoinStep.leftJoin(
					DSL.table(
						"BQIndividual"
					).as(
						"Individual"
					)
				).on(
					DSL.field(
						"Identity.individualId"
					).eq(
						DSL.field("Individual.id")
					)
				);
			}

			if (referencedTableNames.contains("ExpandoValue")) {
				Stream<String> stream = referencedTableNames.stream();

				Set<String> fields = stream.filter(
					s -> s.startsWith("IndividualFields_")
				).collect(
					Collectors.toSet()
				);

				for (String field : fields) {
					selectJoinStep = selectJoinStep.crossJoin(
						"UNNEST(Individual.fields) AS " + field);
				}
			}
		}

		if (referencedTableNames.contains("Session")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQSession"
				).as(
					"Session"
				)
			).on(
				DSL.field(
					"Identity.id"
				).eq(
					DSL.field("Session.userId")
				)
			);

			if (referencedTableNames.contains("SessionReferrers")) {
				selectJoinStep = selectJoinStep.crossJoin(
					DSL.table("UNNEST(Session.referrers) AS SessionReferrer"));
			}

			if (referencedTableNames.contains("SessionUrls")) {
				selectJoinStep = selectJoinStep.crossJoin(
					DSL.table("UNNEST(Session.urls) AS SessionUrl"));
			}
		}

		if (referencedTableNames.contains("User")) {
			selectJoinStep = selectJoinStep.join(
				DSL.table(
					"BQUser"
				).as(
					"User"
				)
			).on(
				DSL.field(
					"Individual.emailAddress"
				).eq(
					DSL.field("User.emailAddress")
				)
			);
		}

		return selectJoinStep;
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}