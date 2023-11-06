/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.backend.repository.PagePathRepository;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;

import java.math.BigDecimal;

import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Marcellus Tavares
 */
@Repository
public class PagePathRepositoryImpl implements PagePathRepository {

	@Override
	public List<AdjacentPageViewsMetric> getAdjacentPagesViewsMetric(
		String canonicalUrl, @Nullable Long channelId, @Nullable Long segmentId,
		TimeRange timeRange, @Nullable String title, ZoneId zoneId) {

		SelectJoinStep<Record> select = _dslContext.with(
			"PagePath"
		).as(
			_dslContext.select(
				DSL.field("canonicalUrl"),
				DSL.field("channelId"),
				DSL.field("eventDate"),
				DSL.coalesce(
					DSL.lag(
						DSL.field("canonicalUrl")
					).over(
						DSL.partitionBy(
							DSL.field("channelId"),
							DSL.field("sessionId"),
							DSL.field("userId")
						).orderBy(
							DSL.field("eventDate")
						)
					),
					DSL.nullif(DSL.field("referrer"), "")
				).as("previousCanonicalUrl"),
				DSL.lag(
					DSL.field("title")
				).over(
					DSL.partitionBy(
						DSL.field("channelId"),
						DSL.field("sessionId"),
						DSL.field("userId")
					).orderBy(
						DSL.field("eventDate")
					)
				).as("previousTitle"),
				DSL.field("title"),
				DSL.field("userId")
			).from(
				"BQEvent"
			).where(
				DSL.field("applicationId").eq("Page"),
				DSL.field("eventId").eq("pageViewed")
			)

		).with(
			"FollowingPages"
		).as(
			_dslContext.select(
				DSL.field("canonicalUrl"),
				DSL.val(
					1
				).as(
					"views"
				)
			).from(
				"PagePath"
			).where(
				DSL.field(
					"previousCanonicalUrl"
				).eq(
					canonicalUrl
				)
			)
		).with(
			"TopFollowingPages"
		).as(
			_dslContext.select(
				DSL.when(
					DSL.field(
						"rowNumber", Long.class
					).greaterThan(
						3L
					),
					DSL.val("other")
				).otherwise(
					DSL.field("canonicalUrl", String.class)
				).as(
					"canonicalUrl"
				),
				DSL.sum(
					DSL.field("views", Long.class)
				).as(
					"views"
				),
				DSL.val(
					Boolean.FALSE
				).as(
					"previous"
				)
			).from(
				_dslContext.select(
					DSL.field("canonicalUrl"),
					DSL.sum(
						DSL.field("views", Long.class)
					).as(
						"views"
					),
					DSL.rowNumber(
					).over(
						DSL.orderBy(
							DSL.sum(
								DSL.field("views", Long.class)
							).desc())
					).as(
						"rowNumber"
					)
				).from(
					"FollowingPages"
				).groupBy(
					DSL.field("canonicalUrl")
				)
			).groupBy(
				DSL.field("canonicalUrl", String.class)
			)
		).with(
			"PreviousPages"
		).as(
			_dslContext.select(
				DSL.coalesce(
					DSL.field("previousCanonicalUrl"), "direct"
				).as(
					"canonicalUrl"
				),
				DSL.val(
					1
				).as(
					"views"
				)
			).from(
				"PagePath"
			).where(
				DSL.field(
					"canonicalUrl"
				).eq(
					canonicalUrl
				)
			)
		).with(
			"TopPreviousPages"
		).as(
			_dslContext.select(
				DSL.when(
					DSL.field(
						"rowNumber", Long.class
					).greaterThan(
						3L
					),
					DSL.val("other")
				).otherwise(
					DSL.field("canonicalUrl", String.class)
				).as(
					"canonicalUrl"
				),
				DSL.sum(
					DSL.field("views", Long.class)
				).as(
					"views"
				),
				DSL.val(
					Boolean.TRUE
				).as(
					"previous"
				)
			).from(
				_dslContext.select(
					DSL.field("canonicalUrl"),
					DSL.sum(
						DSL.field("views", Long.class)
					).as(
						"views"
					),
					DSL.rowNumber(
					).over(
						DSL.orderBy(
							DSL.sum(
								DSL.field("views", Long.class)
							).desc())
					).as(
						"rowNumber"
					)
				).from(
					"PreviousPages"
				).where(
					DSL.field(
						"canonicalUrl"
					).notEqual(
						"direct"
					)
				).groupBy(
					DSL.field("canonicalUrl")
				)
			).groupBy(
				DSL.field("canonicalUrl", String.class)
			)
		).with(
			"Result"
		).as(
			_dslContext.select(
				DSL.field("canonicalUrl", String.class),
				DSL.field("views", BigDecimal.class),
				DSL.field("previous", Boolean.class)
			).from(
				"TopFollowingPages"
			).unionAll(
				_dslContext.select(
					DSL.field("canonicalUrl", String.class),
					DSL.field("views", BigDecimal.class),
					DSL.field("previous", Boolean.class)
				).from(
					"TopPreviousPages"
				)
			).unionAll(
				_dslContext.select(
					DSL.val(
						"direct"
					).as(
						"canonicalUrl"
					),
					DSL.sum(
						DSL.field("views", Long.class)
					).as(
						"views"
					),
					DSL.val(
						Boolean.TRUE
					).as(
						"previous"
					)
				).from(
					"PreviousPages"
				).where(
					DSL.field(
						"canonicalUrl"
					).eq(
						"direct"
					)
				)
			)
		).select(
			DSL.asterisk()
		).from(
			"Result"
		);

		return _queryExecutor.queryForList(
			AdjacentPageViewsMetric::new, select);
	}

	//
	//
	//
	//
	//		Field<BigDecimal> accessesField = DSL.sum(
	//			DSL.field("access", Long.class)
	//		).as(

	// 			"accesses"

	//		);
	//
	//		Field<String> referrerField = DSL.coalesce(DSL.field("referrer", String.class), "");

	//
	//		SelectJoinStep<Record2<String, BigDecimal>> selectJoinStep = _dslContext.select(

	// 			referrerField, accessesField

	//		).from(
	//			DSL.table(

	// 				"BQPageReferrers"

	//			).as(

	// 				"PageReferrers"

	//			)
	//		);
	//
	//		if (segmentId != null) {
	//			selectJoinStep = selectJoinStep.leftJoin(
	//				DSL.table(

	// 					"BQMembership"

	//				).as(

	// 					"Membership"

	//				)
	//			).on(
	//				DSL.field(
	//					"PageReferrers.userId"
	//				).eq(
	//					DSL.field("Membership.identityId")
	//				)
	//			);
	//		}
	//
	//
	//		return _queryExecutor.queryForMap(
	//			GetterUtil::getString,
	//			selectJoinStep.where(
	//				_createWhereClauseConditions(
	//					canonicalUrl, channelId, segmentId, timeRange, title, zoneId)
	//			).groupBy(

	// 				referrerField

	//			).orderBy(
	//				accessesField.desc()
	//			).limit(

	// 				3

	//			),
	//			value -> {
	//				BigDecimal bigDecimalValue = (BigDecimal)value;

	//
	//				return bigDecimalValue.longValue();
	//			});
	//	}

	private List<Condition> _createWhereClauseConditions(
		String canonicalUrl, @Nullable Long channelId, @Nullable Long segmentId,
		TimeRange timeRange, @Nullable String title, ZoneId zoneId) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"PageReferrers.canonicalUrl"
			).eq(
				canonicalUrl
			));
		conditions.add(
			DSL.field(
				"PageReferrers.eventDate"
			).between(
				DateUtil.toUTCLocalDateTime(
					timeRange.getStartLocalDateTime(), zoneId),
				DateUtil.toUTCLocalDateTime(
					timeRange.getEndLocalDateTime(), zoneId)
			));
		conditions.add(
			DSL.field(
				"PageReferrers.channelId"
			).eq(
				channelId
			));

		if (StringUtils.isNotBlank(title)) {
			conditions.add(
				DSL.field(
					"PageReferrers.title"
				).eq(
					title
				));
		}

		if (segmentId != null) {
			conditions.add(
				DSL.field(
					"Membership.channelId"
				).eq(
					channelId
				));
			conditions.add(
				DSL.field(
					"Membership.segmentId"
				).eq(
					segmentId
				));
		}

		return conditions;
	}

	@Autowired
	private DSLContext _dslContext;

	@Autowired
	private QueryExecutor _queryExecutor;

}