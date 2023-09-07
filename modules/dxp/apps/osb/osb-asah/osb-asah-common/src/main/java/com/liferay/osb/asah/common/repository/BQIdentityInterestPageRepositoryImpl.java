/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQIdentityInterestPage;
import com.liferay.osb.asah.common.filter.expression.FilterExpression;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.InsertValuesStep6;
import org.jooq.Record;
import org.jooq.Record3;
import org.jooq.SelectFinalStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.SortField;
import org.jooq.impl.DSL;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Leslie Wong
 */
public class BQIdentityInterestPageRepositoryImpl
	implements CustomBQIdentityInterestPageRepository {

	public BQIdentityInterestPageRepositoryImpl(
		DSLContext dslContext, QueryExecutor queryExecutor) {

		_dslContext = dslContext;
		_queryExecutor = queryExecutor;
	}

	@Override
	public long countActivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType) {

		return _queryExecutor.queryForLong(
			_dslContext.with(
				"ActivePage"
			).as(
				_getActivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).selectCount(
			).from(
				"ActivePage"
			));
	}

	@Override
	public long countInactivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType) {

		return _queryExecutor.queryForLong(
			_dslContext.with(
				"ActivePage"
			).as(
				_getActivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).with(
				"InactivePage"
			).as(
				_getInactivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).selectCount(
			).from(
				"InactivePage"
			).where(
				DSL.field(
					"STRUCT(canonicalUrl, title)"
				).notIn(
					_dslContext.select(
						DSL.field("STRUCT(canonicalUrl, title)")
					).from(
						"ActivePage"
					)
				)
			));
	}

	@Override
	public void deleteAll() {
		_queryExecutor.queryExecute(
			_dslContext.delete(
				DSL.table("BQIdentityInterestPage")
			).where(
				DSL.trueCondition()
			));
	}

	@Override
	public List<Map<String, Object>> getActivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType, Pageable pageable) {

		return _queryExecutor.queryForList(
			Function.identity(),
			_dslContext.with(
				"ActivePage"
			).as(
				_getActivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).select(
				DSL.field(
					"canonicalUrl"
				).as(
					"url"
				),
				DSL.field("title"),
				DSL.field(
					"views", BigDecimal.class
				).as(
					"uniqueVisitsCount"
				)
			).from(
				"ActivePage"
			).orderBy(
				_getSortFields(pageable.getSort())
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	public List<BQIdentityInterestPage> getBQIdentityInterestPages(
		String keyword) {

		return _queryExecutor.queryForList(
			recordMap -> {
				BigDecimal channelIdBigDecimal = (BigDecimal)recordMap.get(
					"channelId");

				recordMap.put("channelId", channelIdBigDecimal.longValue());

				BigDecimal viewsBigDecimal = (BigDecimal)recordMap.get("views");

				recordMap.put("views", viewsBigDecimal.longValue());

				return new BQIdentityInterestPage(recordMap);
			},
			_dslContext.select(
				DSL.field("canonicalUrl"), DSL.field("channelId"),
				DSL.field(
					"userId"
				).as(
					"identityId"
				),
				DSL.val(
					keyword
				).as(
					"keyword"
				),
				DSL.max(
					DSL.field("title")
				).as(
					"title"
				),
				DSL.count(
					DSL.asterisk()
				).as(
					"views"
				)
			).from(
				DSL.table(
					"BQEvent"
				).as(
					"Event"
				)
			).where(
				DSL.and(
					DSL.field(
						"Event.applicationId"
					).eq(
						"Page"
					),
					DSL.field(
						"Event.eventId"
					).eq(
						"pageViewed"
					),
					DSL.exists(
						DSL.selectFrom(
							"UNNEST(ARRAY_CONCAT(SPLIT(LOWER(Event." +
								"assetTitle), ' '), SPLIT(LOWER(Event." +
									"description), ' '), SPLIT(LOWER(Event." +
										"keywords), ' '))) AS keyword"
						).where(
							DSL.field(
								"keyword"
							).eq(
								StringUtils.lowerCase(keyword)
							)
						)))
			).groupBy(
				DSL.field("canonicalUrl"), DSL.field("channelId"),
				DSL.field("userId")
			));
	}

	@Override
	public List<Map<String, Object>> getInactivePagesTransformations(
		@Nullable Long channelId, @Nullable String filterString, String ownerId,
		String ownerType, Pageable pageable) {

		return _queryExecutor.queryForList(
			Function.identity(),
			_dslContext.with(
				"ActivePage"
			).as(
				_getActivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).with(
				"InactivePage"
			).as(
				_getInactivePageSelectFinalStep(
					channelId, filterString, ownerId, ownerType)
			).select(
				DSL.field(
					"InactivePage.canonicalUrl"
				).as(
					"url"
				),
				DSL.field("InactivePage.title"),
				DSL.val(
					0, BigDecimal.class
				).as(
					"uniqueVisitsCount"
				)
			).from(
				"InactivePage"
			).where(
				DSL.field(
					"STRUCT(canonicalUrl, title)"
				).notIn(
					_dslContext.select(
						DSL.field("STRUCT(canonicalUrl, title)")
					).from(
						"ActivePage"
					)
				)
			).orderBy(
				_getSortFields(pageable.getSort())
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public void insertAll(
		List<BQIdentityInterestPage> bqIdentityInterestPages) {

		InsertValuesStep6<Record, String, Long, Object, Object, Object, Long>
			insertValuesStep6 = _dslContext.insertInto(
				DSL.table("BQIdentityInterestPage")
			).columns(
				DSL.field("canonicalUrl", String.class),
				DSL.field("channelId", Long.class), DSL.field("identityId"),
				DSL.field("keyword"), DSL.field("title"),
				DSL.field("views", Long.class)
			);

		for (BQIdentityInterestPage bqIdentityInterestPage :
				bqIdentityInterestPages) {

			insertValuesStep6 = insertValuesStep6.values(
				bqIdentityInterestPage.getCanonicalUrl(),
				bqIdentityInterestPage.getChannelId(),
				bqIdentityInterestPage.getIdentityId(),
				bqIdentityInterestPage.getKeyword(),
				bqIdentityInterestPage.getTitle(),
				bqIdentityInterestPage.getViews());
		}

		_queryExecutor.queryExecute(insertValuesStep6);
	}

	private SelectFinalStep<Record3<String, String, BigDecimal>>
		_getActivePageSelectFinalStep(
			Long channelId, String filterString, String ownerId,
			String ownerType) {

		SelectSelectStep<Record3<String, String, BigDecimal>> selectSelectStep =
			_dslContext.select(
				DSL.field("canonicalUrl", String.class),
				DSL.field("title", String.class),
				DSL.sum(
					DSL.field("views", Long.class)
				).as(
					"views"
				));

		SelectJoinStep<Record3<String, String, BigDecimal>> selectJoinStep =
			_getSelectJoinStep(ownerType, selectSelectStep);

		return selectJoinStep.where(
			_getConditions(channelId, filterString, true, ownerId, ownerType)
		).groupBy(
			DSL.field("canonicalUrl"), DSL.field("title")
		);
	}

	private List<Condition> _getConditions(
		Long channelId, String filterString, boolean includeOwner,
		String ownerId, String ownerType) {

		List<Condition> conditions = new ArrayList<>();

		if (ownerType.equals("individual")) {
			if (includeOwner) {
				conditions.add(
					DSL.field(
						"Identity.individualId"
					).eq(
						ownerId
					));
			}
			else {
				conditions.add(
					DSL.field(
						"Identity.individualId"
					).ne(
						ownerId
					));
			}
		}
		else if (ownerType.equals("individual-segment")) {
			if (includeOwner) {
				conditions.add(
					DSL.field(
						"Membership.segmentId"
					).eq(
						Long.parseLong(ownerId)
					));
			}
			else {
				conditions.add(
					DSL.or(
						DSL.field(
							"Membership.segmentId"
						).isNull(),
						DSL.field(
							"Membership.segmentId"
						).ne(
							Long.parseLong(ownerId)
						)));
			}
		}

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"IdentityInterestPage.channelId"
				).eq(
					channelId
				));
		}

		if (StringUtils.isNotBlank(filterString)) {
			FilterExpression filterExpression = new FilterExpression(
				channelId, filterString);

			conditions.add(filterExpression.getCondition());
		}

		return conditions;
	}

	private SelectFinalStep<Record3<String, String, BigDecimal>>
		_getInactivePageSelectFinalStep(
			Long channelId, String filterString, String ownerId,
			String ownerType) {

		SelectSelectStep<Record3<String, String, BigDecimal>> selectSelectStep =
			_dslContext.select(
				DSL.field("canonicalUrl", String.class),
				DSL.field("title", String.class),
				DSL.val(
					0, BigDecimal.class
				).as(
					"views"
				));

		SelectJoinStep<Record3<String, String, BigDecimal>> selectJoinStep =
			_getSelectJoinStep(ownerType, selectSelectStep);

		return selectJoinStep.where(
			_getConditions(channelId, filterString, false, ownerId, ownerType)
		).groupBy(
			DSL.field("canonicalUrl"), DSL.field("title")
		);
	}

	private SelectJoinStep<Record3<String, String, BigDecimal>>
		_getSelectJoinStep(
			String ownerType,
			SelectSelectStep<Record3<String, String, BigDecimal>>
				selectSelectStep) {

		SelectJoinStep<Record3<String, String, BigDecimal>> selectJoinStep =
			selectSelectStep.from(
				DSL.table(
					"BQIdentityInterestPage"
				).as(
					"IdentityInterestPage"
				));

		if (ownerType.equals("individual")) {
			return selectJoinStep.join(
				DSL.table(
					"BQIdentity"
				).as(
					"Identity"
				)
			).on(
				DSL.field(
					"IdentityInterestPage.identityId"
				).eq(
					DSL.field("Identity.id")
				)
			);
		}
		else if (ownerType.equals("individual-segment")) {
			return selectJoinStep.leftOuterJoin(
				DSL.table(
					"BQMembership"
				).as(
					"Membership"
				)
			).on(
				DSL.field(
					"IdentityInterestPage.identityId"
				).eq(
					DSL.field("Membership.identityId")
				)
			);
		}

		throw new IllegalArgumentException("Invalid ownerType " + ownerType);
	}

	private Collection<SortField<?>> _getSortFields(Sort sort) {
		Collection<SortField<?>> sortFields = new ArrayList<>();

		List<Sort.Order> sortOrders = new ArrayList<>();

		if (sort != null) {
			sortOrders = sort.toList();
		}

		if (sortOrders.isEmpty()) {
			sortFields.add(
				DSL.field(
					"uniqueVisitsCount"
				).desc());
			sortFields.add(
				DSL.field(
					"title"
				).asc());

			return sortFields;
		}

		for (Sort.Order sortOrder : sortOrders) {
			String fieldName = sortOrder.getProperty();

			Field<?> field = DSL.field(fieldName);

			if (sortOrder.getDirection() == Sort.Direction.ASC) {
				sortFields.add(field.asc());
			}
			else {
				sortFields.add(field.desc());
			}
		}

		return sortFields;
	}

	private final DSLContext _dslContext;
	private final QueryExecutor _queryExecutor;

}