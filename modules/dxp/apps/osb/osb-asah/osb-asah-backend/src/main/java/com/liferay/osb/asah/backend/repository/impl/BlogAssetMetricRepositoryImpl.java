/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetric;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jooq.Field;
import org.jooq.impl.DSL;

import org.springframework.stereotype.Repository;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Repository("BlogAssetMetricRepository")
public class BlogAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<BlogMetric> {

	@Override
	public AssetType getAssetType() {
		return AssetType.BLOG;
	}

	@Override
	protected BlogMetric createAssetMetric() {
		return new BlogMetric();
	}

	@Override
	protected Map<String, BiConsumer<BlogMetric, Metric>>
		getAssetMetricSetters() {

		return new HashMap<String, BiConsumer<BlogMetric, Metric>>() {
			{
				put(
					BlogMetricType.CLICKS.getName(),
					BlogMetric::setClicksMetric);
				put(
					BlogMetricType.COMMENTS.getName(),
					BlogMetric::setCommentsMetric);
				put(
					BlogMetricType.RATINGS.getName(),
					BlogMetric::setRatingsMetric);
				put(
					BlogMetricType.READING_TIME.getName(),
					BlogMetric::setReadingTimeMetric);
				put(BlogMetricType.VIEWS.getName(), BlogMetric::setViewsMetric);
			}
		};
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		if (metricType == BlogMetricType.RATINGS) {
			return DSL.coalesce(
				DSL.sum(
					DSL.field("ratingsScore", Float.class)
				).div(
					DSL.sum(DSL.field("ratings", Long.class))
				),
				BigDecimal.ZERO);
		}

		if (metricType == BlogMetricType.READING_TIME) {
			return DSL.coalesce(
				DSL.sum(
					DSL.field(metricType.getFieldName(), Long.class)
				).div(
					DSL.sum(DSL.field("sessions", Long.class))
				),
				BigDecimal.ZERO);
		}

		Field<Long> longField = DSL.field(
			metricType.getFieldName(), Long.class);

		return DSL.sum(longField);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return BlogMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return BlogMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "BlogHourly";
		}

		return "BlogDaily";
	}

}