/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetric;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
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
@Repository("DocumentLibraryAssetMetricRepository")
public class DocumentLibraryAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<DocumentLibraryMetric> {

	@Override
	public AssetType getAssetType() {
		return AssetType.DOCUMENT;
	}

	@Override
	protected DocumentLibraryMetric createAssetMetric() {
		return new DocumentLibraryMetric();
	}

	@Override
	protected Map<String, BiConsumer<DocumentLibraryMetric, Metric>>
		getAssetMetricSetters() {

		return new HashMap
			<String, BiConsumer<DocumentLibraryMetric, Metric>>() {

			{
				put(
					DocumentLibraryMetricType.COMMENTS.getName(),
					DocumentLibraryMetric::setCommentsMetric);
				put(
					DocumentLibraryMetricType.DOWNLOADS.getName(),
					DocumentLibraryMetric::setDownloadsMetric);
				put(
					DocumentLibraryMetricType.PREVIEWS.getName(),
					DocumentLibraryMetric::setPreviewsMetric);
				put(
					DocumentLibraryMetricType.RATINGS.getName(),
					DocumentLibraryMetric::setRatingsMetric);
			}
		};
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		Field<Long> longField = DSL.field(
			metricType.getFieldName(), Long.class);

		if (metricType == DocumentLibraryMetricType.RATINGS) {
			return DSL.avg(longField);
		}

		return DSL.sum(longField);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return DocumentLibraryMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return DocumentLibraryMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "DocumentLibraryHourly";
		}

		return "DocumentLibraryDaily";
	}

}