/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.IndividualMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQIdentityRepository;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class BQIndividualMetricDog {

	public BQIndividualMetricDog(
		BQIdentityRepository bqIdentityRepository, TimeZoneDog timeZoneDog) {

		_bqIdentityRepository = bqIdentityRepository;
		_timeZoneDog = timeZoneDog;
	}

	public double getBQIndividualsCount(
		LocalDate localDate, MetricType metricType,
		SearchQueryContext searchQueryContext) {

		return _bqIdentityRepository.getBQIndividualsCount(
			searchQueryContext.isActive(),
			searchQueryContext.getChannelIdAsLong(), localDate, metricType,
			_timeZoneDog.getZoneId());
	}

	public IndividualMetric getIndividualMetric(
		SearchQueryContext searchQueryContext, Set<String> selectedMetrics) {

		IndividualMetric individualMetric = new IndividualMetric();

		List<Consumer<Metric>> consumers = new ArrayList<>();
		List<MetricType> metricTypes = new ArrayList<>();

		if (selectedMetrics.contains(
				IndividualMetricType.ANONYMOUS_INDIVIDUALS.getName())) {

			consumers.add(individualMetric::setAnonymousIndividualsMetric);
			metricTypes.add(IndividualMetricType.ANONYMOUS_INDIVIDUALS);
		}

		if (selectedMetrics.contains(
				IndividualMetricType.KNOWN_INDIVIDUALS.getName()) ||
			selectedMetrics.contains("defaultMetric")) {

			consumers.add(individualMetric::setKnownIndividualsMetric);
			metricTypes.add(IndividualMetricType.KNOWN_INDIVIDUALS);
		}

		if (selectedMetrics.contains(
				IndividualMetricType.TOTAL_INDIVIDUALS.getName()) &&
			(!selectedMetrics.contains(
				IndividualMetricType.ANONYMOUS_INDIVIDUALS.getName()) ||
			 !selectedMetrics.contains(
				 IndividualMetricType.KNOWN_INDIVIDUALS.getName()))) {

			consumers.add(individualMetric::setTotalIndividualsMetric);
			metricTypes.add(IndividualMetricType.TOTAL_INDIVIDUALS);
		}

		List<Metric> bqIndividualCountMetrics = _getBQIndividualCountMetrics(
			metricTypes, searchQueryContext);

		for (int i = 0; i < bqIndividualCountMetrics.size(); i++) {
			Consumer<Metric> metricConsumer = consumers.get(i);

			metricConsumer.accept(bqIndividualCountMetrics.get(i));
		}

		if (selectedMetrics.contains(
				IndividualMetricType.ANONYMOUS_INDIVIDUALS.getName()) &&
			selectedMetrics.contains(
				IndividualMetricType.KNOWN_INDIVIDUALS.getName()) &&
			selectedMetrics.contains(
				IndividualMetricType.TOTAL_INDIVIDUALS.getName())) {

			Metric totalIndividualsMetric = new Metric(
				IndividualMetricType.TOTAL_INDIVIDUALS);

			Double previousValue = 0.0;
			Double value = 0.0;

			for (Metric bqIndividualCountMetric : bqIndividualCountMetrics) {
				if ((bqIndividualCountMetric.getMetricType() !=
						IndividualMetricType.ANONYMOUS_INDIVIDUALS) &&
					(bqIndividualCountMetric.getMetricType() !=
						IndividualMetricType.KNOWN_INDIVIDUALS)) {

					continue;
				}

				previousValue += bqIndividualCountMetric.getPreviousValue();

				totalIndividualsMetric.setPreviousValue(previousValue);

				totalIndividualsMetric.setPreviousValueKey(
					bqIndividualCountMetric.getPreviousValueKey());

				value += bqIndividualCountMetric.getValue();

				totalIndividualsMetric.setValue(value);

				totalIndividualsMetric.setValueKey(
					bqIndividualCountMetric.getValueKey());
			}

			individualMetric.setTotalIndividualsMetric(totalIndividualsMetric);
		}

		return individualMetric;
	}

	private List<Metric> _getBQIndividualCountMetrics(
		List<MetricType> metricTypes, SearchQueryContext searchQueryContext) {

		List<Metric> bqIndividualCountMetrics = new ArrayList<>();

		LocalDate localDate = LocalDate.now(_timeZoneDog.getZoneId());

		LocalDate previousLocalDate = _getPreviousLocalDate(
			localDate, searchQueryContext.getTimeRange());

		List<Long> bqIndividualsCounts =
			_bqIdentityRepository.getBQIndividualsCounts(
				searchQueryContext.isActive(),
				searchQueryContext.getChannelIdAsLong(),
				Arrays.asList(localDate, previousLocalDate), metricTypes,
				_timeZoneDog.getZoneId());

		for (int i = 0; i < metricTypes.size(); i++) {
			Metric metric = new Metric(metricTypes.get(i));

			Long previousValue = bqIndividualsCounts.get(
				i + metricTypes.size());

			metric.setPreviousValue(previousValue.doubleValue());

			metric.setPreviousValueKey(previousLocalDate.toString());

			Long value = bqIndividualsCounts.get(i);

			metric.setValue(value.doubleValue());

			metric.setValueKey(localDate.toString());

			bqIndividualCountMetrics.add(metric);
		}

		return bqIndividualCountMetrics;
	}

	private LocalDate _getPreviousLocalDate(
		LocalDate localDate, TimeRange timeRange) {

		LocalDate previousLocalDate = LocalDate.from(localDate);

		return previousLocalDate.minusDays(timeRange.getDeltaDays());
	}

	private final BQIdentityRepository _bqIdentityRepository;
	private final TimeZoneDog _timeZoneDog;

}