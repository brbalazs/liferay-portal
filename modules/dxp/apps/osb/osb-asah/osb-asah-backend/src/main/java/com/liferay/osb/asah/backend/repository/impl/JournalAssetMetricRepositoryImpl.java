/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.JournalMetric;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;

import org.jooq.Field;
import org.jooq.impl.DSL;

import org.springframework.stereotype.Repository;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@Repository("JournalAssetMetricRepository")
public class JournalAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<JournalMetric> {

	@Override
	public AssetType getAssetType() {
		return AssetType.JOURNAL;
	}

	@Override
	protected JournalMetric createAssetMetric() {
		return new JournalMetric();
	}

	@Override
	protected Map<String, BiConsumer<JournalMetric, Metric>>
		getAssetMetricSetters() {

		return Collections.singletonMap(
			JournalMetricType.VIEWS.getName(), JournalMetric::setViewsMetric);
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		Field<Long> longField = DSL.field(
			metricType.getFieldName(), Long.class);

		return DSL.sum(longField);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return JournalMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return JournalMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "JournalHourly";
		}

		return "JournalDaily";
	}

}