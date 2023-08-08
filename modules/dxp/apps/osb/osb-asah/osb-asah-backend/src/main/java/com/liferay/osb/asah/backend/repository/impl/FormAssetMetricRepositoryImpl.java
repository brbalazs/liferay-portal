/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.impl;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.FormMetric;
import com.liferay.osb.asah.backend.model.FormMetricType;
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
@Repository("FormAssetMetricRepository")
public class FormAssetMetricRepositoryImpl
	extends BaseAssetMetricRepository<FormMetric> {

	@Override
	public AssetType getAssetType() {
		return AssetType.FORM;
	}

	@Override
	protected FormMetric createAssetMetric() {
		return new FormMetric();
	}

	@Override
	protected Map<String, BiConsumer<FormMetric, Metric>>
		getAssetMetricSetters() {

		return new HashMap<String, BiConsumer<FormMetric, Metric>>() {
			{
				put(
					FormMetricType.ABANDONMENTS.getName(),
					FormMetric::setAbandonmentsMetric);
				put(
					FormMetricType.COMPLETION_TIME.getName(),
					FormMetric::setCompletionTimeMetric);
				put(
					FormMetricType.SUBMISSIONS.getName(),
					FormMetric::setSubmissionsMetric);
				put(FormMetricType.VIEWS.getName(), FormMetric::setViewsMetric);
			}
		};
	}

	@Override
	protected Field<BigDecimal> getMetricField(
		MetricType metricType, TimeRange timeRange) {

		if (metricType == FormMetricType.ABANDONMENTS) {
			return DSL.sum(
				DSL.field(
					FormMetricType.ABANDONMENTS.getFieldName(), Long.class)
			).div(
				DSL.greatest(
					DSL.sum(DSL.field("finalizedFormViews", Long.class)),
					DSL.one())
			);
		}

		Field<Long> longField = DSL.field(
			metricType.getFieldName(), Long.class);

		if (metricType == FormMetricType.COMPLETION_TIME) {
			return DSL.avg(longField);
		}

		return DSL.sum(longField);
	}

	@Override
	protected MetricType getMetricType(String metricTypeName) {
		return FormMetricType.of(metricTypeName);
	}

	@Override
	protected MetricType[] getMetricTypes() {
		return FormMetricType.values();
	}

	@Override
	protected String getTableName(TimeRange timeRange) {
		if ((timeRange == TimeRange.LAST_24_HOURS) ||
			(timeRange == TimeRange.YESTERDAY)) {

			return "FormHourly";
		}

		return "FormDaily";
	}

}