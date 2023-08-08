/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.MetricHelper;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.petra.string.StringPool;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class IndividualHistogramDog {

	public HistogramMetricBag getHistogramMetricBag(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		HistogramMetricBag histogramMetricBag =
			_metricHelper.createHistogramMetricBag(
				Clock.system(_timeZoneDog.getZoneId()),
				searchQueryContext.isIncludePrevious(),
				searchQueryContext.getInterval(), metricType,
				searchQueryContext.getTimeRange());

		for (HistogramMetric histogramMetric :
				histogramMetricBag.getMetrics()) {

			histogramMetric.setValue(
				_bqIndividualMetricDog.getBQIndividualsCount(
					_getLocalDate(histogramMetric), metricType,
					searchQueryContext));
		}

		return histogramMetricBag;
	}

	private LocalDate _getLocalDate(Metric metric) {
		String key = metric.getValueKey();

		if (key.contains(StringPool.SLASH)) {
			String[] parts = key.split(StringPool.SLASH);

			return LocalDate.parse(parts[1]);
		}

		LocalDateTime localDateTime = LocalDateTime.parse(key);

		return localDateTime.toLocalDate();
	}

	@Autowired
	private BQIndividualMetricDog _bqIndividualMetricDog;

	@Autowired
	private MetricHelper _metricHelper;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}