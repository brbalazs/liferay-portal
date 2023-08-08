/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.dog.HistogramDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.HistogramMetric;
import com.liferay.osb.asah.backend.model.HistogramMetricBag;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.Trend;
import com.liferay.osb.asah.backend.spring.OSBAsahBackendSpringBootApplication;
import com.liferay.osb.asah.common.model.CustomAssetMetricType;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.TrendClassification;
import com.liferay.osb.asah.common.repository.PreferenceRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQJournalRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.math.BigDecimal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Lino Alves
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(
	classes = {HistogramDog.class, OSBAsahBackendSpringBootApplication.class}
)
public class HistogramDogTest implements OSBAsahTestExecutionListenersContext {

	@BQSQLResource(
		resourcePath = "blog_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testGetBlogViewsHistogramMetricsLast7Days() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				BlogMetricType.VIEWS,
				new SearchQueryContext() {
					{
						setAssetId("e131fabc");
						setAssetType(AssetType.BLOG);
						setChannelId("1");
						setIncludePrevious(Boolean.TRUE);
						setInterval("D");
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		HistogramMetric lastHistogramMetric = histogramMetrics.get(
			histogramMetrics.size() - 1);

		Assertions.assertEquals(2, lastHistogramMetric.getPreviousValue());
		Assertions.assertEquals(1, lastHistogramMetric.getValue());
	}

	@BQSQLResource(
		resourcePath = "custom_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testGetCustomAssetViewsHistogramMetricsLast7Days() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				CustomAssetMetricType.VIEWS,
				new SearchQueryContext() {
					{
						setAssetId(DigestUtils.sha256Hex("Adefault1"));
						setAssetType(AssetType.CUSTOM);
						setChannelId("1");
						setIncludePrevious(Boolean.TRUE);
						setInterval("D");
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		HistogramMetric lastHistogramMetric = histogramMetrics.get(
			histogramMetrics.size() - 1);

		Assertions.assertEquals(0, lastHistogramMetric.getPreviousValue());
		Assertions.assertEquals(2, lastHistogramMetric.getValue());
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_14_days.sql"
	)
	@Test
	public void testGetDocumentPreviewsHistogramMetricsLast7Days() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				DocumentLibraryMetricType.PREVIEWS,
				new SearchQueryContext() {
					{
						setAssetId("e131fabc");
						setAssetType(AssetType.DOCUMENT);
						setChannelId("1");
						setIncludePrevious(Boolean.TRUE);
						setInterval("D");
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		HistogramMetric lastHistogramMetric = histogramMetrics.get(
			histogramMetrics.size() - 1);

		Assertions.assertEquals(2, lastHistogramMetric.getPreviousValue());
		Assertions.assertEquals(1, lastHistogramMetric.getValue());
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testGetFormViewsHistogramMetricsLast7Days() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				FormMetricType.VIEWS,
				new SearchQueryContext() {
					{
						setAssetId("e131fabc");
						setAssetType(AssetType.FORM);
						setChannelId("1");
						setIncludePrevious(Boolean.TRUE);
						setInterval("D");
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		HistogramMetric lastHistogramMetric = histogramMetrics.get(
			histogramMetrics.size() - 1);

		Assertions.assertEquals(2, lastHistogramMetric.getPreviousValue());
		Assertions.assertEquals(1, lastHistogramMetric.getValue());
	}

	@BQSQLResource(
		resourcePath = "journal_asset_metric_views_histogram_last_14_days.sql"
	)
	@Test
	public void testGetJournalViewsHistogramMetricsLast7Days() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				JournalMetricType.VIEWS,
				new SearchQueryContext() {
					{
						setAssetId("e131fabc");
						setAssetType(AssetType.JOURNAL);
						setChannelId("1");
						setIncludePrevious(Boolean.TRUE);
						setInterval("D");
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		HistogramMetric lastHistogramMetric = histogramMetrics.get(
			histogramMetrics.size() - 1);

		Assertions.assertEquals(2, lastHistogramMetric.getPreviousValue());
		Assertions.assertEquals(1, lastHistogramMetric.getValue());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_90_days_info.json"
	)
	@Test
	public void testHistogramMetricsCustomRange() {
		double[] expectedValues = {
			0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1
		};

		LocalDate localDate = LocalDate.now(ZoneOffset.UTC);

		Assertions.assertArrayEquals(
			expectedValues,
			_getActualValues(
				_getHistogramMetrics(
					Interval.DAY,
					TimeRange.of(
						localDate.minusDays(85), localDate.minusDays(105)))),
			0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_7_days_info.json"
	)
	@Test
	public void testHistogramMetricsLast7Days() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			7, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {6, 6, 5, 4, 3, 2, 1};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_24_hours_info.json"
	)
	@Test
	public void testHistogramMetricsLast24Hours() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(
			24, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 1, 1, 1, 1, 2, 1, 2, 1, 2, 0, 3, 1, 1, 4, 6, 4, 2, 4, 3, 2, 1, 2,
			0
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_28_days_info.json"
	)
	@Test
	public void testHistogramMetricsLast28Days() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_28_DAYS);

		Assertions.assertEquals(
			28, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			28, 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12,
			11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_90_days_info.json"
	)
	@Test
	public void testHistogramMetricsLast90Days() {
		double[] expectedValues = DogTestUtil.create90DaysHistogramBuckets();

		Assertions.assertArrayEquals(
			expectedValues,
			_getActualValues(
				_getHistogramMetrics(Interval.WEEK, TimeRange.LAST_90_DAYS)),
			0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_day_interval_time_zone_info.json"
	)
	@RepositoryResource(
		repositoryClass = PreferenceRepository.class,
		resourcePath = "osbasahfaroinfo/preference_time_zone_america_los_angeles.json"
	)
	@Test
	public void testHistogramMetricsTimeZoneDayInterval() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY,
			TimeRange.of(
				LocalDateTime.parse("2021-03-13T00:00:00"),
				LocalDateTime.parse("2021-03-08T00:00:00")));

		Assertions.assertEquals(
			6, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {4, 0, 4, 2, 2, 1};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_month_interval_time_zone_info.json"
	)
	@RepositoryResource(
		repositoryClass = PreferenceRepository.class,
		resourcePath = "osbasahfaroinfo/preference_time_zone_america_los_angeles.json"
	)
	@Test
	public void testHistogramMetricsTimeZoneMonthInterval() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.MONTH,
			TimeRange.of(
				LocalDateTime.parse("2021-03-31T23:59:59"),
				LocalDateTime.parse("2021-01-01T00:00:00")));

		Assertions.assertEquals(
			3, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {24, 15, 22};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_week_interval_time_zone_info.json"
	)
	@RepositoryResource(
		repositoryClass = PreferenceRepository.class,
		resourcePath = "osbasahfaroinfo/preference_time_zone_america_los_angeles.json"
	)
	@Test
	public void testHistogramMetricsTimeZoneWeekInterval() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.WEEK,
			TimeRange.of(
				LocalDateTime.parse("2021-03-13T23:59:59"),
				LocalDateTime.parse("2021-02-07T00:00:00")));

		Assertions.assertEquals(
			5, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {8, 12, 7, 7, 9};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_last_7_days_info.json"
	)
	@Test
	public void testHistogramMetricsTrendLast7Days() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			7, histogramMetrics.size(), histogramMetrics.toString());

		_assertHistogramMetric(
			histogramMetrics.get(0), 1, TrendClassification.POSITIVE,
			BigDecimal.valueOf(500.0), 6);
		_assertHistogramMetric(
			histogramMetrics.get(1), 1, TrendClassification.POSITIVE,
			BigDecimal.valueOf(500.0), 6);
		_assertHistogramMetric(
			histogramMetrics.get(2), 1, TrendClassification.POSITIVE,
			BigDecimal.valueOf(400.0), 5);
		_assertHistogramMetric(
			histogramMetrics.get(3), 2, TrendClassification.POSITIVE,
			BigDecimal.valueOf(100.0), 4);
		_assertHistogramMetric(
			histogramMetrics.get(4), 3, TrendClassification.NEUTRAL,
			BigDecimal.valueOf(0.0), 3);
		_assertHistogramMetric(
			histogramMetrics.get(5), 4, TrendClassification.NEGATIVE,
			BigDecimal.valueOf(-50.0), 2);
		_assertHistogramMetric(
			histogramMetrics.get(6), 5, TrendClassification.NEGATIVE,
			BigDecimal.valueOf(-80.0), 1);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQJournalRepository.class,
		resourcePath = "osbasahcereroinfo/histogram_journal_yesterday_info.json"
	)
	@Test
	public void testHistogramMetricsYesterday() {
		List<HistogramMetric> histogramMetrics = _getHistogramMetrics(
			Interval.DAY, TimeRange.YESTERDAY);

		Assertions.assertEquals(
			24, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {
			0, 1, 1, 1, 2, 3, 2, 1, 7, 0, 1, 0, 3, 0, 1, 1, 2, 2, 1, 2, 4, 2, 3,
			2
		};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/histogram_pages_info.json"
	)
	@Test
	public void testHistogramPagesMetricsMissingSessionId() {
		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				PageMetricType.VIEWS,
				new SearchQueryContext(null, AssetType.PAGE) {
					{
						setIncludePrevious(Boolean.TRUE);
						setInterval(Interval.DAY.getKey());
						setTimeRange(TimeRange.LAST_7_DAYS);
					}
				});

		List<HistogramMetric> histogramMetrics =
			histogramMetricBag.getMetrics();

		Assertions.assertEquals(
			7, histogramMetrics.size(), histogramMetrics.toString());

		double[] expectedValues = {0, 5, 0, 1, 4, 3, 2};

		Assertions.assertArrayEquals(
			expectedValues, _getActualValues(histogramMetrics), 0);
	}

	private void _assertHistogramMetric(
		HistogramMetric actualHistogramMetric, double expectedPreviousValue,
		TrendClassification expectedTrendClassification,
		BigDecimal expectedTrendPercentage, double expectedValue) {

		Assertions.assertEquals(
			expectedValue, actualHistogramMetric.getValue(), 0.01);
		Assertions.assertEquals(
			expectedPreviousValue, actualHistogramMetric.getPreviousValue(),
			0.01);

		Trend actualTrend = actualHistogramMetric.getTrend();

		Assertions.assertEquals(
			expectedTrendPercentage, actualTrend.getPercentage());
		Assertions.assertEquals(
			expectedTrendClassification, actualTrend.getTrendClassification());
	}

	private double[] _getActualValues(List<HistogramMetric> histogramMetrics) {
		double[] actualValues = new double[histogramMetrics.size()];

		for (int i = 0; i < histogramMetrics.size(); i++) {
			HistogramMetric histogramMetric = histogramMetrics.get(i);

			actualValues[i] = histogramMetric.getValue();
		}

		return actualValues;
	}

	private List<HistogramMetric> _getHistogramMetrics(
		Interval interval, TimeRange timeRange) {

		HistogramMetricBag histogramMetricBag =
			_histogramDog.getHistogramMetricBag(
				JournalMetricType.VIEWS,
				new SearchQueryContext("1", AssetType.JOURNAL) {
					{
						setAssetType(AssetType.JOURNAL);
						setIncludePrevious(Boolean.TRUE);
						setInterval(interval.getKey());
						setTimeRange(timeRange);
					}
				});

		return histogramMetricBag.getMetrics();
	}

	@Autowired
	private HistogramDog _histogramDog;

}