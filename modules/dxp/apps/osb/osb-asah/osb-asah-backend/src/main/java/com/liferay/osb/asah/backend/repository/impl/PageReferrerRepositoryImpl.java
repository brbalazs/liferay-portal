/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.repository.PageReferrerRepository;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.util.GetterUtil;

import java.math.BigDecimal;

import java.time.ZoneId;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

/**
 * @author Marcellus Tavares
 */
@Repository
public class PageReferrerRepositoryImpl implements PageReferrerRepository {

	@Override
	public Map<String, Double> getAcquisitionChannelAccesses(
		String canonicalUrl, @Nullable Long channelId, TimeRange timeRange,
		ZoneId zoneId) {

		Field<String> acquisitionChannelField = DSL.coalesce(
			DSL.field("acquisitionChannel", String.class), DSL.val("direct")
		).as(
			"acquisitionChannel"
		);

		Field<BigDecimal> accessesField = DSL.sum(
			DSL.field("access", Long.class)
		).as(
			"accesses"
		);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			dslContext.select(
				acquisitionChannelField, accessesField
			).from(
				"BQPageReferrers"
			).where(
				_createWhereClauseCondition(
					canonicalUrl, channelId, timeRange, null, zoneId)
			).groupBy(
				acquisitionChannelField
			).orderBy(
				accessesField
			),
			value -> {
				BigDecimal bigDecimalValue = (BigDecimal)value;

				return bigDecimalValue.doubleValue();
			});
	}

	@Override
	public Map<String, Double> getPageReferrerAccesses(
		String canonicalUrl, @Nullable Long channelId, TimeRange timeRange,
		@Nullable String title, ZoneId zoneId) {

		Field<BigDecimal> accessesField = DSL.sum(
			DSL.field("access", Long.class)
		).as(
			"accesses"
		);

		Field<String> referrerField = DSL.field("referrer", String.class);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			dslContext.select(
				referrerField, accessesField
			).from(
				"BQPageReferrers"
			).where(
				DSL.and(
					_createWhereClauseCondition(
						canonicalUrl, channelId, timeRange, title, zoneId),
					DSL.field(
						"referrer"
					).ne(
						""
					))
			).groupBy(
				referrerField
			).orderBy(
				accessesField.desc()
			).limit(
				3
			),
			value -> {
				BigDecimal bigDecimalValue = (BigDecimal)value;

				return bigDecimalValue.doubleValue();
			});
	}

	@Override
	public Map<String, Double>
		getSocialPageReferrerAccessesByReferrerCanonicalUrl(
			String canonicalUrl, @Nullable Long channelId, Pageable pageable,
			TimeRange timeRange, ZoneId zoneId) {

		Field<String> referrerCanonicalUrl = DSL.field(
			"referrerCanonicalUrl", String.class);

		Field<BigDecimal> accessesField = DSL.sum(
			DSL.field("access", Long.class)
		).as(
			"accesses"
		);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			dslContext.select(
				referrerCanonicalUrl, accessesField
			).from(
				"BQPageReferrers"
			).where(
				DSL.and(
					_createWhereClauseCondition(
						canonicalUrl, channelId, timeRange, null, zoneId),
					DSL.field(
						"acquisitionChannel"
					).isNotNull(),
					DSL.field(
						"acquisitionChannel"
					).notIn(
						"organic", "social"
					),
					referrerCanonicalUrl.ne(""),
					referrerCanonicalUrl.isNotNull())
			).groupBy(
				referrerCanonicalUrl
			).orderBy(
				accessesField
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			),
			value -> {
				BigDecimal bigDecimalValue = (BigDecimal)value;

				return bigDecimalValue.doubleValue();
			});
	}

	@Override
	public Map<String, Double> getSocialPageReferrerAccessesByReferrerHost(
		String canonicalUrl, @Nullable Long channelId, Pageable pageable,
		TimeRange timeRange, ZoneId zoneId) {

		Field<String> referrerHostField = DSL.field(
			"referrerHost", String.class);

		Field<BigDecimal> accessesField = DSL.sum(
			DSL.field("access", Long.class)
		).as(
			"accesses"
		);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			dslContext.select(
				referrerHostField, accessesField
			).from(
				"BQPageReferrers"
			).where(
				DSL.and(
					_createWhereClauseCondition(
						canonicalUrl, channelId, timeRange, null, zoneId),
					DSL.field(
						"acquisitionChannel"
					).isNotNull(),
					DSL.field(
						"acquisitionChannel"
					).notIn(
						"organic", "social"
					),
					referrerHostField.ne(""), referrerHostField.isNotNull())
			).groupBy(
				referrerHostField
			).orderBy(
				accessesField
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			),
			value -> {
				BigDecimal bigDecimalValue = (BigDecimal)value;

				return bigDecimalValue.doubleValue();
			});
	}

	@Autowired
	protected DSLContext dslContext;

	private Condition _createWhereClauseCondition(
		String canonicalUrl, @Nullable Long channelId, TimeRange timeRange,
		String title, ZoneId zoneId) {

		Condition condition = DSL.and(
			DSL.field(
				"canonicalUrl"
			).eq(
				canonicalUrl
			),
			DSL.field(
				"eventDate"
			).between(
				DateUtil.toUTCLocalDateTime(
					timeRange.getStartLocalDateTime(), zoneId),
				DateUtil.toUTCLocalDateTime(
					timeRange.getEndLocalDateTime(), zoneId)
			));

		if (channelId != null) {
			condition = condition.and(
				DSL.field(
					"channelId"
				).eq(
					channelId
				));
		}

		if (StringUtils.isNotBlank(title)) {
			condition = condition.and(
				DSL.field(
					"title"
				).eq(
					title
				));
		}

		return condition;
	}

	@Autowired
	private QueryExecutor _queryExecutor;

}