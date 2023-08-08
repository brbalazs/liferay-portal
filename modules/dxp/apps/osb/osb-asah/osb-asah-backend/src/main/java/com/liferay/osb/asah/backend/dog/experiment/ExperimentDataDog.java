/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.experiment;

import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.repository.PageAssetMetricRepository;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class ExperimentDataDog {

	public ExperimentDataPoint<Double> fetchDichotomousDataPoint(
		Long experimentId, PageMetricType goalPageMetricType,
		TimeRange timeRange, String variantId) {

		Optional<PageMetric> pageMetricOptional =
			_pageAssetMetricRepository.getExperimentPageMetric(
				experimentId,
				SetUtil.of(goalPageMetricType, PageMetricType.SESSIONS),
				timeRange, variantId);

		return _toExperimentDataPoint(goalPageMetricType, pageMetricOptional);
	}

	public ExperimentDataPoint<Double> fetchDichotomousDataPoint(
		String canonicalUrl, PageMetricType goalPageMetricType,
		TimeRange timeRange) {

		Optional<PageMetric> pageMetricOptional =
			_pageAssetMetricRepository.getExperimentPageMetric(
				canonicalUrl,
				SetUtil.of(goalPageMetricType, PageMetricType.SESSIONS),
				timeRange);

		return _toExperimentDataPoint(goalPageMetricType, pageMetricOptional);
	}

	private double _getMetricValue(Metric metric) {
		return metric.getValue();
	}

	private long _getMetricValueAsLong(Metric metric) {
		Double value = metric.getValue();

		return value.longValue();
	}

	private ExperimentDataPoint<Double> _toExperimentDataPoint(
		PageMetricType goalPageMetricType,
		Optional<PageMetric> pageMetricOptional) {

		if (!pageMetricOptional.isPresent()) {
			return null;
		}

		PageMetric pageMetric = pageMetricOptional.get();

		if (goalPageMetricType == PageMetricType.CTA_CLICKS) {
			return new ExperimentDataPoint<>(
				_getMetricValueAsLong(pageMetric.getSessionsMetric()),
				_getMetricValue(pageMetric.getCTAClicksMetric()));
		}

		return new ExperimentDataPoint<>(
			_getMetricValueAsLong(pageMetric.getSessionsMetric()),
			_getMetricValue(pageMetric.getBounceMetric()));
	}

	@Autowired
	private PageAssetMetricRepository _pageAssetMetricRepository;

}