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
import java.util.Set;

import org.jooq.CommonTableExpression;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
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
	public Set<AdjacentPageViewsMetric> getAdjacentPagesViewsMetric(
		String canonicalUrl, @Nullable Long channelId, @Nullable Long segmentId,
		TimeRange timeRange, @Nullable String title, ZoneId zoneId) {

		return _queryExecutor.queryForSet(
			AdjacentPageViewsMetric::new,
			_dslContext.with(
				_getPagePathCTE(channelId, segmentId, timeRange, zoneId)
			).with(
				_getFollowingPagesCTE(canonicalUrl)
			).with(
				_getTopFollowingPagesCTE()
			).with(
				_getPreviousPagesCTE(canonicalUrl)
			).with(
				_getTopPreviousPagesCTE()
			).select(
				_canonicalUrlField, _previousField, _titleField, _viewsField
			).from(
				"TopFollowingPages"
			).unionAll(
				_dslContext.select(
					_canonicalUrlField, _previousField, _titleField, _viewsField
				).from(
					"TopPreviousPages"
				)
			).unionAll(
				_dslContext.select(
					_canonicalUrlField,
					DSL.val(
						Boolean.TRUE
					).as(
						"previous"
					),
					_titleField,
					DSL.sum(
						_viewsField
					).as(
						"views"
					)
				).from(
					"PreviousPages"
				).where(
					_canonicalUrlField.eq("direct")
				).groupBy(
					_canonicalUrlField, _previousField, _titleField
				)
			));
	}

	private CommonTableExpression<?> _getFollowingPagesCTE(
		String canonicalUrl) {

		return DSL.name(
			"FollowingPages"
		).as(
			_dslContext.select(
				_canonicalUrlField, _titleField,
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
		);
	}

	private CommonTableExpression<?> _getPagePathCTE(
		Long channelId, @Nullable Long segmentId, TimeRange timeRange,
		ZoneId zoneId) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.field(
				"applicationId"
			).eq(
				"Page"
			));
		conditions.add(
			DSL.field(
				"channelId"
			).eq(
				channelId
			));
		conditions.add(
			DSL.field(
				"eventId"
			).eq(
				"pageViewed"
			));
		conditions.add(
			DSL.field(
				"eventDate"
			).between(
				DateUtil.toUTCLocalDateTime(
					timeRange.getStartLocalDateTime(), zoneId),
				DateUtil.toUTCLocalDateTime(
					timeRange.getEndLocalDateTime(), zoneId)
			));

		if (segmentId != null) {
			conditions.add(
				DSL.field(
					"userId"
				).in(
					_dslContext.select(
						DSL.field("identityId")
					).from(
						"BQMembership"
					).where(
						DSL.field(
							"segmentId"
						).eq(
							segmentId
						)
					)
				));
		}

		return DSL.name(
			"PagePath"
		).as(
			_dslContext.select(
				DSL.field("canonicalUrl"), DSL.field("channelId"),
				DSL.field("eventDate"),
				DSL.coalesce(
					DSL.lag(
						DSL.field("canonicalUrl")
					).over(
						DSL.partitionBy(
							DSL.field("channelId"), DSL.field("sessionId"),
							DSL.field("userId")
						).orderBy(
							DSL.field("eventDate")
						)
					),
					DSL.nullif(DSL.field("referrer"), "")
				).as(
					"previousCanonicalUrl"
				),
				DSL.lag(
					DSL.field("title")
				).over(
					DSL.partitionBy(
						DSL.field("channelId"), DSL.field("sessionId"),
						DSL.field("userId")
					).orderBy(
						DSL.field("eventDate")
					)
				).as(
					"previousTitle"
				),
				DSL.field("title"), DSL.field("userId")
			).from(
				"BQEvent"
			).where(
				conditions
			)
		);
	}

	private CommonTableExpression<?> _getPreviousPagesCTE(String canonicalUrl) {
		return DSL.name(
			"PreviousPages"
		).as(
			_dslContext.select(
				DSL.coalesce(
					DSL.field("previousCanonicalUrl"), "direct"
				).as(
					"canonicalUrl"
				),
				DSL.coalesce(
					DSL.field("previousTitle"), "direct"
				).as(
					"title"
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
		);
	}

	private CommonTableExpression<?> _getTopFollowingPagesCTE() {
		return DSL.name(
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
					_canonicalUrlField
				).as(
					"canonicalUrl"
				),
				DSL.when(
					DSL.field(
						"rowNumber", Long.class
					).greaterThan(
						3L
					),
					DSL.val("other")
				).otherwise(
					_titleField
				).as(
					"title"
				),
				DSL.sum(
					_viewsField
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
					_canonicalUrlField, _titleField,
					DSL.sum(
						_viewsField
					).as(
						"views"
					),
					DSL.rowNumber(
					).over(
						DSL.orderBy(
							DSL.sum(
								_viewsField
							).desc())
					).as(
						"rowNumber"
					)
				).from(
					"FollowingPages"
				).groupBy(
					_canonicalUrlField, _titleField
				)
			).groupBy(
				_canonicalUrlField, _titleField
			)
		);
	}

	private CommonTableExpression<?> _getTopPreviousPagesCTE() {
		return DSL.name(
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
					_canonicalUrlField
				).as(
					"canonicalUrl"
				),
				DSL.when(
					DSL.field(
						"rowNumber", Long.class
					).greaterThan(
						3L
					),
					DSL.val("other")
				).otherwise(
					_titleField
				).as(
					"title"
				),
				DSL.sum(
					_viewsField
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
					_canonicalUrlField, _titleField,
					DSL.sum(
						_viewsField
					).as(
						"views"
					),
					DSL.rowNumber(
					).over(
						DSL.orderBy(
							DSL.sum(
								_viewsField
							).desc())
					).as(
						"rowNumber"
					)
				).from(
					"PreviousPages"
				).where(
					_canonicalUrlField.notEqual("direct")
				).groupBy(
					_canonicalUrlField, _titleField
				)
			).groupBy(
				_canonicalUrlField, _titleField
			)
		);
	}

	private final Field<String> _canonicalUrlField = DSL.field(
		"canonicalUrl", String.class);

	@Autowired
	private DSLContext _dslContext;

	private final Field<Boolean> _previousField = DSL.field(
		"previous", Boolean.class);

	@Autowired
	private QueryExecutor _queryExecutor;

	private final Field<String> _titleField = DSL.field("title", String.class);
	private final Field<BigDecimal> _viewsField = DSL.field(
		"views", BigDecimal.class);

}