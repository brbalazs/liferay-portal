/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.MetricHelper;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.CohortHeatMapMetric;
import com.liferay.osb.asah.backend.model.CohortMetric;
import com.liferay.osb.asah.backend.model.HeatMapMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.SiteMetric;
import com.liferay.osb.asah.backend.model.SiteMetricType;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.AcquisitionType;
import com.liferay.osb.asah.common.model.Composition;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.SiteVisitorBehaviorMetric;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.common.util.StringUtil;

import java.math.BigDecimal;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import org.jetbrains.annotations.NotNull;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Marcos Martins
 * @author Leilany Ulisses
 * @author Regisson Cesar
 * @author Thiago Buarque
 */
@Component
public class SiteMetricDog {

	public CompositionResultBag getAcquisitionsMetricsCompositionResultBag(
		AcquisitionType acquisitionType, String channelId, int size, int start,
		TimeRange timeRange) {

		long acquisitionsMetricsCount =
			_bqSessionRepository.getAcquisitionsMetricsCount(
				acquisitionType, Long.parseLong(channelId), timeRange,
				_timeZoneDog.getZoneId());

		if (acquisitionsMetricsCount == 0) {
			return new CompositionResultBag(Collections.emptyList(), 0, 0);
		}

		Map<String, BigDecimal> acquisitionsMetrics =
			_bqSessionRepository.getAcquisitionsMetrics(
				acquisitionType, Long.parseLong(channelId),
				PageRequest.of(start, size), timeRange,
				_timeZoneDog.getZoneId());

		if (acquisitionsMetrics.isEmpty()) {
			return new CompositionResultBag(Collections.emptyList(), 0, 0);
		}

		List<Composition> compositions = new ArrayList<>();

		for (Map.Entry<String, BigDecimal> entry :
				acquisitionsMetrics.entrySet()) {

			BigDecimal count = entry.getValue();

			compositions.add(
				new Composition(count.longValue(), entry.getKey()));
		}

		return new CompositionResultBag(
			compositions, compositions.size(), acquisitionsMetricsCount);
	}

	public List<Metric> getBrowserMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		Map<String, BigDecimal> sessionsCountGroupedByBrowserName =
			_bqSessionRepository.getSessionsCountGroupedByBrowserName(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());

		Set<Map.Entry<String, BigDecimal>> set =
			sessionsCountGroupedByBrowserName.entrySet();

		Stream<Map.Entry<String, BigDecimal>> stream = set.stream();

		return stream.map(
			entry -> {
				Metric metric = new Metric(metricType);

				BigDecimal value = entry.getValue();

				metric.setValue(value.doubleValue());

				metric.setValueKey(entry.getKey());

				return metric;
			}
		).collect(
			Collectors.toList()
		);
	}

	public CohortMetric getCohortMetric(SearchQueryContext searchQueryContext) {
		Map<String, Map<String, Object>> cohortHeatMapTuples = new HashMap<>();

		Interval interval = searchQueryContext.getInterval();

		List<String> cohortMetricIntervals =
			_metricHelper.getCohortMetricIntervals(
				Clock.system(_timeZoneDog.getZoneId()), interval);

		LocalDate startLocalDate = LocalDate.parse(
			cohortMetricIntervals.get(0));

		for (Map<String, Object> cohortHeatMapTuple :
				_bqSessionRepository.getCohortHeatMapTuples(
					searchQueryContext.getChannelIdAsLong(), interval,
					TimeRange.of(
						LocalDateTime.now(), startLocalDate.atStartOfDay()),
					_timeZoneDog.getZoneId())) {

			cohortHeatMapTuples.put(
				(String)cohortHeatMapTuple.get("cohortDate"),
				cohortHeatMapTuple);
		}

		CohortMetric cohortMetric = new CohortMetric();

		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics =
			_getCohortHeatMapMetrics(
				cohortHeatMapTuples, cohortMetricIntervals,
				SiteMetricType.ANONYMOUS_VISITORS);

		cohortMetric.setAnonymousCohortHeatMapMetrics(
			anonymousCohortHeatMapMetrics);

		List<CohortHeatMapMetric> knownCohortHeatMapMetrics =
			_getCohortHeatMapMetrics(
				cohortHeatMapTuples, cohortMetricIntervals,
				SiteMetricType.KNOWN_VISITORS);

		cohortMetric.setKnownCohortHeatMapMetrics(knownCohortHeatMapMetrics);

		cohortMetric.setVisitorsCohortHeatMapMetrics(
			_getCohortHeatMapMetrics(
				anonymousCohortHeatMapMetrics, knownCohortHeatMapMetrics));

		return cohortMetric;
	}

	public List<Metric> getDeviceMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		Map<String, Metric> metrics = new LinkedHashMap<>();

		List<Map<String, Object>> sessionsCountGroupedByDeviceName =
			_bqSessionRepository.getSessionsCountGroupedByDeviceName(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());

		Stream<Map<String, Object>> stream =
			sessionsCountGroupedByDeviceName.stream();

		stream.forEach(
			recordMap -> {
				Metric deviceTypeMetric = new Metric(metricType);

				String deviceType = (String)recordMap.get("deviceType");

				deviceTypeMetric.setValueKey(deviceType);

				metrics.putIfAbsent(deviceType, deviceTypeMetric);

				deviceTypeMetric = metrics.get(deviceType);

				Metric platformTypeMetric = new Metric(metricType);

				platformTypeMetric.setValue(
					Double.valueOf(String.valueOf(recordMap.get("count"))));
				platformTypeMetric.setValueKey(
					(String)recordMap.get("platformName"));

				deviceTypeMetric.addMetric(platformTypeMetric);
				deviceTypeMetric.setValue(
					deviceTypeMetric.getValue() +
						platformTypeMetric.getValue());
			});

		return new ArrayList<>(metrics.values());
	}

	public List<Metric> getGeolocationMetrics(
		MetricType metricType, SearchQueryContext searchQueryContext) {

		Map<String, BigDecimal> sessionsCountGroupedByGeolocation =
			_bqSessionRepository.getSessionsCountGroupedByGeolocation(
				searchQueryContext.getChannelIdAsLong(),
				searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());

		Set<Map.Entry<String, BigDecimal>> set =
			sessionsCountGroupedByGeolocation.entrySet();

		Stream<Map.Entry<String, BigDecimal>> stream = set.stream();

		return stream.map(
			entry -> {
				Metric metric = new Metric(metricType);

				BigDecimal value = entry.getValue();

				metric.setValue(value.doubleValue());

				metric.setValueKey(entry.getKey());

				return metric;
			}
		).collect(
			Collectors.toList()
		);
	}

	public List<HeatMapMetric> getHeatMapMetrics(
		Long channelId, TimeRange timeRange) {

		List<Map<String, BigDecimal>> visitorsCountGroupedByDayAndTime =
			_bqSessionRepository.getVisitorsCountGroupedByDayAndTime(
				channelId, timeRange, _timeZoneDog.getZoneId());

		Map<Pair<Integer, Integer>, HeatMapMetric> heatMapMetrics =
			new TreeMap<>();

		for (int day = 0; day < 7; day++) {
			for (int hour = 0; hour < 24; hour++) {
				Metric metric = new Metric(SiteMetricType.VISITORS);

				metric.setValue(0D);

				heatMapMetrics.put(
					Pair.of(day, hour),
					new HeatMapMetric(
						String.valueOf(day), metric, String.valueOf(hour)));
			}
		}

		if (visitorsCountGroupedByDayAndTime.isEmpty()) {
			return new ArrayList<>(heatMapMetrics.values());
		}

		for (Map<String, BigDecimal> visitorCount :
				visitorsCountGroupedByDayAndTime) {

			BigDecimal dayOfWeek = visitorCount.get("dayOfWeek");
			BigDecimal hourOfDay = visitorCount.get("hourOfDay");
			BigDecimal visitors = visitorCount.get("visitors");

			Metric metric = new Metric(SiteMetricType.VISITORS);

			metric.setValue(Double.valueOf(String.valueOf(visitors)));

			heatMapMetrics.put(
				Pair.of(dayOfWeek.intValue() - 1, hourOfDay.intValue()),
				new HeatMapMetric(
					String.valueOf(dayOfWeek.intValue() - 1), metric,
					String.valueOf(hourOfDay.intValue())));
		}

		return new ArrayList<>(heatMapMetrics.values());
	}

	public CompositionResultBag getSearchTermsCompositionResultBag(
		Long channelId, int size, int start, TimeRange timeRange) {

		Map<String, BigDecimal> searchTerms = _bqEventRepository.getSearchTerms(
			channelId, _getSearchQueryParams(), size, start, timeRange,
			_timeZoneDog.getTimeZoneId());

		if (searchTerms.isEmpty()) {
			return new CompositionResultBag(Collections.emptyList(), 0, 0);
		}

		List<Composition> compositions = new ArrayList<>();

		for (Map.Entry<String, BigDecimal> entry : searchTerms.entrySet()) {
			String key = entry.getKey();

			BigDecimal count = entry.getValue();

			compositions.add(new Composition(count.longValue(), key));
		}

		return new CompositionResultBag(
			compositions, compositions.size(),
			_bqEventRepository.getSearchTermsCount(
				channelId, _getSearchQueryParams(), timeRange,
				_timeZoneDog.getTimeZoneId()));
	}

	public SiteMetric getSiteMetric(SearchQueryContext searchQueryContext) {
		SiteMetric siteMetric = new SiteMetric();

		boolean includePrevious = searchQueryContext.isIncludePrevious();

		List<SiteVisitorBehaviorMetric> sessionSiteVisitorBehaviorMetrics =
			_bqSessionRepository.getSiteVisitorBehaviorMetrics(
				searchQueryContext.getChannelIdAsLong(), includePrevious,
				searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());

		Stream<SiteVisitorBehaviorMetric> stream =
			sessionSiteVisitorBehaviorMetrics.stream();

		Map<String, SiteVisitorBehaviorMetric>
			sessionSiteVisitorBehaviorMetricsMap = stream.collect(
				Collectors.toMap(
					sessionSiteVisitorBehaviorMetric -> {
						if (sessionSiteVisitorBehaviorMetric.isPrevious()) {
							return "previous";
						}

						return "current";
					},
					Function.identity()));

		_setMetricValues(
			sessionSiteVisitorBehaviorMetricsMap.get("current"),
			sessionSiteVisitorBehaviorMetricsMap.get("previous"), siteMetric);

		return siteMetric;
	}

	private List<CohortHeatMapMetric> _getCohortHeatMapMetrics(
		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics,
		List<CohortHeatMapMetric> knownCohortHeatMapMetrics) {

		List<CohortHeatMapMetric> cohortHeatMapMetrics = new ArrayList<>();

		Map<String, Double> intervalValues = new Hashtable<>();

		for (int i = 0; i < anonymousCohortHeatMapMetrics.size(); i++) {
			Metric metric = new Metric(SiteMetricType.VISITORS);

			CohortHeatMapMetric anonymousCohortHeatMapMetric =
				anonymousCohortHeatMapMetrics.get(i);

			metric.setValueKey(anonymousCohortHeatMapMetric.getValueKey());

			CohortHeatMapMetric knownCohortHeatMapMetric =
				knownCohortHeatMapMetrics.get(i);

			double metricValue =
				anonymousCohortHeatMapMetric.getValue() +
					knownCohortHeatMapMetric.getValue();

			metric.setValue(metricValue);

			String colDimension =
				anonymousCohortHeatMapMetric.getColDimension();

			if (colDimension.equals("0")) {
				intervalValues.put(
					anonymousCohortHeatMapMetric.getRowDimension(),
					metricValue);
			}

			Double intervalValue = intervalValues.get(
				anonymousCohortHeatMapMetric.getRowDimension());

			double retention = 0;

			if (intervalValue != 0) {
				retention = (metricValue / intervalValue) * 100;
			}

			cohortHeatMapMetrics.add(
				new CohortHeatMapMetric(
					colDimension, metric, retention,
					anonymousCohortHeatMapMetric.getRowDimension(),
					anonymousCohortHeatMapMetric.getRowKey()));
		}

		return cohortHeatMapMetrics;
	}

	@NotNull
	private List<CohortHeatMapMetric> _getCohortHeatMapMetrics(
		Map<String, Map<String, Object>> cohortHeatMapTuples,
		List<String> cohortMetricIntervals, SiteMetricType siteMetricType) {

		String visitorType = "Known";

		if (siteMetricType.equals(SiteMetricType.ANONYMOUS_VISITORS)) {
			visitorType = "Unknown";
		}

		List<CohortHeatMapMetric> cohortHeatMapMetrics = new ArrayList<>();

		double totalVisitors = 0;

		for (int i = 0; i < cohortMetricIntervals.size(); i++) {
			double curTotalVisitors = 0;
			int intervalIndex = 0;

			for (int j = 0; j <= (cohortMetricIntervals.size() - 1 - i); j++) {
				if (j == 0) {
					intervalIndex = cohortHeatMapMetrics.size();
				}

				if ((j == (cohortMetricIntervals.size() - 1)) && (i == 0)) {
					break;
				}

				String rowKey = cohortMetricIntervals.get(j);

				Map<String, Object> cohortHeatMapTuple =
					cohortHeatMapTuples.get(rowKey);

				Metric metric = new Metric(siteMetricType);

				double retention = 0;
				double visitors = 0;

				if (cohortHeatMapTuple != null) {
					metric.setValueKey(rowKey);

					BigDecimal intervalTotal =
						(BigDecimal)cohortHeatMapTuple.get(
							String.format("interval%dTotal%s", i, visitorType));

					visitors = intervalTotal.doubleValue();

					metric.setValue(visitors);

					BigDecimal intervalRetention =
						(BigDecimal)cohortHeatMapTuple.get(
							String.format(
								"interval%dRetention%s", i, visitorType));

					retention = intervalRetention.doubleValue() * 100;
				}

				cohortHeatMapMetrics.add(
					new CohortHeatMapMetric(
						String.valueOf(i), metric, retention,
						String.valueOf(j + 1), rowKey));

				if (i == 0) {
					totalVisitors += visitors;
				}

				curTotalVisitors += visitors;
			}

			Metric metric = new Metric(siteMetricType);

			metric.setValue(curTotalVisitors);

			double retention = 0;

			if (totalVisitors != 0) {
				retention = (curTotalVisitors / totalVisitors) * 100;
			}

			cohortHeatMapMetrics.add(
				intervalIndex,
				new CohortHeatMapMetric(
					String.valueOf(i), metric, retention, "0", null));
		}

		return cohortHeatMapMetrics;
	}

	private String[] _getSearchQueryParams() {
		Set<String> searchQueryParams = new HashSet<>();

		Preference preference = _preferenceDog.getPreference(
			"search-query-strings");

		String preferenceValue = preference.getValue();

		if (!StringUtil.isNull(preferenceValue)) {
			searchQueryParams = JSONUtil.toStringSet(
				new JSONArray(preferenceValue));
		}

		searchQueryParams.add("q");

		return searchQueryParams.toArray(new String[0]);
	}

	private void _setMetricPreviousValue(Metric metric, Double value) {
		metric.setPreviousValue(value);
	}

	private void _setMetricValue(Metric metric, Double value) {
		metric.setValue(value);
	}

	private void _setMetricValues(
		SiteVisitorBehaviorMetric currentSiteVisitorBehaviorMetric,
		SiteVisitorBehaviorMetric previousSiteVisitorBehaviorMetric,
		SiteMetric siteMetric) {

		for (Map.Entry
				<Function<SiteMetric, Metric>,
				 Function<SiteVisitorBehaviorMetric, Object>> entry :
					_sessionMetricsFunctionMap.entrySet()) {

			Function<SiteMetric, Metric> metricFunction = entry.getKey();
			Function<SiteVisitorBehaviorMetric, Object> metricValueFunction =
				entry.getValue();

			if (currentSiteVisitorBehaviorMetric != null) {
				_setMetricValue(
					metricFunction.apply(siteMetric),
					Double.parseDouble(
						String.valueOf(
							metricValueFunction.apply(
								currentSiteVisitorBehaviorMetric))));
			}

			if (previousSiteVisitorBehaviorMetric != null) {
				_setMetricPreviousValue(
					metricFunction.apply(siteMetric),
					Double.parseDouble(
						String.valueOf(
							metricValueFunction.apply(
								previousSiteVisitorBehaviorMetric))));
			}
		}
	}

	private static final Map
		<Function<SiteMetric, Metric>,
		 Function<SiteVisitorBehaviorMetric, Object>>
			_sessionMetricsFunctionMap =
				new HashMap
					<Function<SiteMetric, Metric>,
					 Function<SiteVisitorBehaviorMetric, Object>>() {

					{
						put(
							SiteMetric::getAnonymousVisitorsMetric,
							SiteVisitorBehaviorMetric::getAnonymousVisitors);
						put(
							SiteMetric::getBounceRateMetric,
							SiteVisitorBehaviorMetric::getBounceRate);
						put(
							SiteMetric::getKnownVisitorsMetric,
							SiteVisitorBehaviorMetric::getKnownVisitors);
						put(
							SiteMetric::getSessionDurationMetric,
							SiteVisitorBehaviorMetric::
								getAverageSessionDuration);
						put(
							SiteMetric::getSessionsMetric,
							SiteVisitorBehaviorMetric::getSessions);
						put(
							SiteMetric::getSessionsPerVisitorMetric,
							SiteVisitorBehaviorMetric::getSessionsPerVisitor);
						put(
							SiteMetric::getVisitorsMetric,
							SiteVisitorBehaviorMetric::getVisitors);
					}
				};

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private BQSessionRepository _bqSessionRepository;

	@Autowired
	private MetricHelper _metricHelper;

	@Autowired
	private PreferenceDog _preferenceDog;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}