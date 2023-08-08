/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.CustomAssetMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.CustomAssetMetricType;
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
 * @author Marcellus Tavares
 */
@Repository("CustomAssetMetricRepository")
public class CustomAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<CustomAssetMetric> {

	@Override
	public AssetType getAssetType() {
		return AssetType.CUSTOM;
	}

	@Override
	protected CustomAssetMetric createAssetMetric() {
		return new CustomAssetMetric();
	}

	@Override
	protected String getAssetIdFieldName() {
		return "assetPrimaryKey";
	}

	@Override
	protected Map<String, BiConsumer<CustomAssetMetric, Metric>>
		getAssetMetricSetters() {

		return new HashMap<String, BiConsumer<CustomAssetMetric, Metric>>() {
			{
				put(
					CustomAssetMetricType.ABANDONMENTS.getName(),
					CustomAssetMetric::setAbandonmentsMetric);
				put(
					CustomAssetMetricType.CLICKS.getName(),
					CustomAssetMetric::setClicksMetric);
				put(
					CustomAssetMetricType.COMPLETION_TIME.getName(),
					CustomAssetMetric::setCompletionTimeMetric);
				put(
					CustomAssetMetricType.DOWNLOADS.getName(),
					CustomAssetMetric::setDownloadsMetric);
				put(
					CustomAssetMetricType.READING_TIME.getName(),
					CustomAssetMetric::setReadingTimeMetric);
				put(
					CustomAssetMetricType.SUBMISSIONS.getName(),
					CustomAssetMetric::setSubmissionsMetric);
				put(
					CustomAssetMetricType.VIEWS.getName(),
					CustomAssetMetric::setViewsMetric);
			}
		};
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		if (metricType == CustomAssetMetricType.ABANDONMENTS) {
			return DSL.sum(
				DSL.field(
					CustomAssetMetricType.ABANDONMENTS.getFieldName(),
					Long.class)
			).div(
				DSL.sum(
					DSL.field(
						CustomAssetMetricType.VIEWS.getFieldName(), Long.class))
			);
		}

		if ((metricType == CustomAssetMetricType.CLICKS) ||
			(metricType == CustomAssetMetricType.DOWNLOADS) ||
			(metricType == CustomAssetMetricType.SUBMISSIONS) ||
			(metricType == CustomAssetMetricType.VIEWS)) {

			Field<Long> longField = DSL.field(
				metricType.getFieldName(), Long.class);

			return DSL.sum(longField);
		}

		return DSL.sum(
			DSL.field(metricType.getFieldName(), Long.class)
		).div(
			DSL.sum(
				DSL.field(
					CustomAssetMetricType.SESSIONS.getFieldName(), Long.class))
		);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return CustomAssetMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return CustomAssetMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if (!dslHelper.isBigQueryDialect()) {
			return "BQCustomAsset";
		}

		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "CustomAssetHourly";
		}

		return "CustomAssetDaily";
	}

}