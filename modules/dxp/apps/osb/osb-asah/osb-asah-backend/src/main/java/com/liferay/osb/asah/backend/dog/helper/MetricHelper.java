/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.helper;

import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.petra.string.StringPool;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 * @author Rachael Koestartyo
 */
@Component
public class MetricHelper {

	public HistogramMetricBag createHistogramMetricBag(
		Clock clock, boolean includePrevious, Interval interval,
		MetricType metricType, TimeRange timeRange) {

		Map<String, Metric> metrics = new LinkedHashMap<>();

		LocalDate localDate = timeRange.getEndLocalDate();

		LocalDateTime localDateTime = localDate.atStartOfDay();

		if (TimeRange.LAST_24_HOURS.equals(timeRange)) {
			localDateTime = LocalDateTime.now(clock);

			localDateTime = localDateTime.withMinute(0);
			localDateTime = localDateTime.withNano(0);
			localDateTime = localDateTime.withSecond(0);

			for (int i = 23; i >= 0; i--) {
				LocalDateTime periodLocalDateTime = localDateTime.minusHours(i);

				metrics.put(
					String.valueOf(periodLocalDateTime),
					_createMetric(
						periodLocalDateTime, i, interval, metricType,
						timeRange));
			}
		}
		else if (TimeRange.YESTERDAY.equals(timeRange)) {
			for (int i = 0; i < 24; i++) {
				LocalDateTime periodLocalDateTime = localDateTime.plusHours(i);

				metrics.put(
					String.valueOf(periodLocalDateTime),
					_createMetric(
						periodLocalDateTime, i, interval, metricType,
						timeRange));
			}
		}
		else {
			_addMetric(
				includePrevious, interval, localDateTime, metricType, metrics,
				timeRange);
		}

		Set<Map.Entry<String, Metric>> entries = metrics.entrySet();

		Stream<Map.Entry<String, Metric>> stream = entries.stream();

		List<HistogramMetric> histogramMetrics = stream.map(
			entry -> new HistogramMetric(entry.getKey(), entry.getValue())
		).collect(
			Collectors.toList()
		);

		return new HistogramMetricBag(
			_isAsymmetricComparison(interval, timeRange), histogramMetrics,
			histogramMetrics.size());
	}

	public List<String> getCohortMetricIntervals(
		Clock clock, Interval interval) {

		List<String> intervals = new ArrayList<>();

		LocalDateTime localDateTime = LocalDateTime.of(
			LocalDate.now(clock), LocalTime.MIDNIGHT);

		if (Interval.DAY.equals(interval)) {
			for (int i = 7; i >= 0; i--) {
				ZonedDateTime zonedDateTime = ZonedDateTime.of(
					localDateTime, clock.getZone());

				zonedDateTime = zonedDateTime.minusDays(i);

				intervals.add(String.valueOf(zonedDateTime.toLocalDate()));
			}
		}
		else if (Interval.MONTH.equals(interval)) {
			localDateTime = localDateTime.withDayOfMonth(1);

			for (int i = 6; i >= 0; i--) {
				ZonedDateTime zonedDateTime = ZonedDateTime.of(
					localDateTime, clock.getZone());

				zonedDateTime = zonedDateTime.minusMonths(i);

				intervals.add(String.valueOf(zonedDateTime.toLocalDate()));
			}
		}
		else if (Interval.WEEK.equals(interval)) {
			localDateTime = localDateTime.with(DayOfWeek.MONDAY);
			localDateTime = localDateTime.minusDays(1);

			for (int i = 6; i >= 0; i--) {
				ZonedDateTime zonedDateTime = ZonedDateTime.of(
					localDateTime, clock.getZone());

				zonedDateTime = zonedDateTime.minusWeeks(i);

				intervals.add(String.valueOf(zonedDateTime.toLocalDate()));
			}
		}

		return intervals;
	}

	private void _addMetric(
		boolean includePrevious, Interval interval, LocalDateTime localDateTime,
		MetricType metricType, Map<String, Metric> metrics,
		TimeRange timeRange) {

		int metricsCount = _getMetricsCount(
			includePrevious, interval, timeRange);

		if (Interval.WEEK.equals(interval)) {
			if (localDateTime.getDayOfWeek() != DayOfWeek.SUNDAY) {
				localDateTime = localDateTime.minusWeeks(1);
				localDateTime = localDateTime.with(DayOfWeek.SUNDAY);
			}

			for (int i = metricsCount - 1; i >= 0; i--) {
				LocalDateTime periodLocalDateTime = localDateTime.minusWeeks(i);

				metrics.put(
					String.valueOf(periodLocalDateTime),
					_createMetric(
						periodLocalDateTime, i, interval, metricType,
						timeRange));
			}
		}
		else if (Interval.MONTH.equals(interval)) {
			if (localDateTime.getDayOfMonth() != 1) {
				localDateTime = localDateTime.withDayOfMonth(1);
			}

			for (int i = metricsCount - 1; i >= 0; i--) {
				LocalDateTime periodLocalDateTime = localDateTime.minusMonths(
					i);

				metrics.put(
					String.valueOf(periodLocalDateTime),
					_createMetric(
						periodLocalDateTime, i, interval, metricType,
						timeRange));
			}
		}
		else {
			LocalDate periodLocalDate = timeRange.getStartLocalDate();

			LocalDateTime periodLocalDateTime = periodLocalDate.atStartOfDay();

			LocalDateTime endLocalDateTime = timeRange.getEndLocalDateTime();

			long deltaDays = timeRange.getDeltaDays();

			while (!periodLocalDateTime.isAfter(endLocalDateTime)) {
				metrics.put(
					String.valueOf(periodLocalDateTime),
					_createMetric(
						periodLocalDateTime, deltaDays, interval, metricType,
						timeRange));

				periodLocalDateTime = periodLocalDateTime.plusDays(1);

				deltaDays--;
			}
		}
	}

	private int _countMonths(long days, LocalDate localDate) {
		LocalDate startLocalDate = localDate.minusDays(days - 1);

		return DateUtil.getDeltaMonths(startLocalDate, localDate);
	}

	private int _countWeeks(long days, LocalDate localDate) {
		LocalDate startLocalDate = localDate.minusDays(days - 1);

		return DateUtil.getDeltaWeeks(startLocalDate, localDate);
	}

	private Metric _createMetric(
		LocalDateTime currentPeriodLocalDateTime, long intervalDelta,
		Interval interval, MetricType metricType, TimeRange timeRange) {

		Metric metric = new Metric(metricType);

		metric.setPreviousValue(0.0);
		metric.setPreviousValueKey(
			_getPreviousValueKey(
				currentPeriodLocalDateTime, intervalDelta, interval,
				timeRange));
		metric.setValue(0.0);
		metric.setValueKey(_getValueKey(interval, currentPeriodLocalDateTime));

		return metric;
	}

	private int _getMetricsCount(
		boolean includePrevious, Interval interval, TimeRange timeRange) {

		int deltaDays = timeRange.getDeltaDays();

		if (Interval.WEEK.equals(interval)) {
			if (includePrevious) {
				LocalDate startLocalDate = timeRange.getStartLocalDate();

				return Integer.max(
					_countWeeks(deltaDays, timeRange.getEndLocalDate()),
					_countWeeks(deltaDays, startLocalDate.minusDays(1)));
			}

			return _countWeeks(deltaDays, timeRange.getEndLocalDate());
		}

		if (Interval.MONTH.equals(interval)) {
			if (includePrevious) {
				LocalDate startLocalDate = timeRange.getStartLocalDate();

				return Integer.max(
					_countMonths(deltaDays, timeRange.getEndLocalDate()),
					_countMonths(deltaDays, startLocalDate.minusDays(1)));
			}

			return _countMonths(deltaDays, timeRange.getEndLocalDate());
		}

		return deltaDays;
	}

	private String _getPreviousValueKey(
		LocalDateTime currentPeriodLocalDateTime, long intervalDelta,
		Interval interval, TimeRange timeRange) {

		if (Interval.WEEK.equals(interval)) {
			LocalDate currentPeriodStartLocalDate =
				timeRange.getStartLocalDate();

			LocalDate firstWeekdayLocalDate =
				currentPeriodStartLocalDate.minusDays((intervalDelta * 7) + 1);

			if (firstWeekdayLocalDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
				firstWeekdayLocalDate = firstWeekdayLocalDate.minusWeeks(1);

				firstWeekdayLocalDate = firstWeekdayLocalDate.with(
					DayOfWeek.SUNDAY);
			}

			LocalDate lastWeekdayLocalDate = firstWeekdayLocalDate.plusDays(6);

			return firstWeekdayLocalDate + StringPool.SLASH +
				lastWeekdayLocalDate;
		}

		if (Interval.MONTH.equals(interval)) {
			LocalDate currentPeriodStartLocalDate =
				timeRange.getStartLocalDate();

			LocalDate firstMonthDayLocalDate =
				currentPeriodStartLocalDate.minusDays(1);

			firstMonthDayLocalDate = firstMonthDayLocalDate.minusMonths(
				intervalDelta);

			firstMonthDayLocalDate = firstMonthDayLocalDate.withDayOfMonth(1);

			LocalDate lastMonthDayLocalDate =
				firstMonthDayLocalDate.withDayOfMonth(
					firstMonthDayLocalDate.lengthOfMonth());

			return firstMonthDayLocalDate + StringPool.SLASH +
				lastMonthDayLocalDate;
		}

		LocalDateTime previousPeriodLocalDateTime = LocalDateTime.from(
			currentPeriodLocalDateTime);

		previousPeriodLocalDateTime = previousPeriodLocalDateTime.minusDays(
			timeRange.getDeltaDays());

		return previousPeriodLocalDateTime.toString();
	}

	private String _getValueKey(
		Interval interval, LocalDateTime localDateTime) {

		if (Interval.WEEK.equals(interval)) {
			LocalDate startLocalDate = localDateTime.toLocalDate();

			LocalDate endLocalDate = startLocalDate.plusDays(6);

			return startLocalDate + StringPool.SLASH + endLocalDate;
		}

		if (Interval.MONTH.equals(interval)) {
			LocalDate localDate = localDateTime.toLocalDate();

			LocalDate startLocalDate = localDate.withDayOfMonth(1);
			LocalDate endLocalDate = localDate.withDayOfMonth(
				localDate.lengthOfMonth());

			return startLocalDate + StringPool.SLASH + endLocalDate;
		}

		return localDateTime.toString();
	}

	private boolean _isAsymmetricComparison(
		Interval interval, TimeRange timeRange) {

		int deltaDays = timeRange.getDeltaDays();

		LocalDate startLocalDate = timeRange.getStartLocalDate();

		if (Interval.WEEK.equals(interval)) {
			int currentNumOfBins = _countWeeks(
				deltaDays, timeRange.getEndLocalDate());
			int previousNumOfBins = _countWeeks(
				deltaDays, startLocalDate.minusDays(1));

			if (previousNumOfBins > currentNumOfBins) {
				return true;
			}
		}
		else if (Interval.MONTH.equals(interval)) {
			int currentNumOfBins = _countMonths(
				deltaDays, timeRange.getEndLocalDate());
			int previousNumOfBins = _countMonths(
				deltaDays, startLocalDate.minusDays(1));

			if (previousNumOfBins > currentNumOfBins) {
				return true;
			}
		}

		return false;
	}

}