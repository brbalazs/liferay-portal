/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.SiteMetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.CohortHeatMapMetric;
import com.liferay.osb.asah.backend.model.CohortMetric;
import com.liferay.osb.asah.backend.model.HeatMapMetric;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.SiteMetric;
import com.liferay.osb.asah.backend.model.SiteMetricType;
import com.liferay.osb.asah.common.model.AcquisitionType;
import com.liferay.osb.asah.common.model.Composition;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.test.context.BootstrapWith;

/**
 * @author Marcos Martins
 * @author Regisson Cesar
 */
@BootstrapWith(SpringBootTestContextBootstrapper.class)
public class SiteMetricDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_sessions_acquisition.sql")
	@Test
	public void testAcquisitionChannel() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getAcquisitionsMetricsCompositionResultBag(
				AcquisitionType.CHANNEL, "1", 5, 0,
				TimeRange.of(
					LocalDate.parse("2023-02-01"),
					LocalDate.parse("2023-01-01")));

		List<Composition> results = compositionResultBag.getResults();

		Assertions.assertEquals(1, results.size());

		Composition composition = results.get(0);

		Assertions.assertEquals("testchannel1", composition.getName());
		Assertions.assertEquals(2, composition.getCount());
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_acquisition.sql")
	@Test
	public void testAcquisitionReferrers() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getAcquisitionsMetricsCompositionResultBag(
				AcquisitionType.REFERRER, "1", 5, 0, TimeRange.LAST_24_HOURS);

		List<Composition> results = compositionResultBag.getResults();

		Assertions.assertEquals(1, results.size());

		Composition composition = results.get(0);

		Assertions.assertEquals("testReferrers3", composition.getName());
		Assertions.assertEquals(1, composition.getCount());
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_acquisition.sql")
	@Test
	public void testAcquisitionSourceMedium() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getAcquisitionsMetricsCompositionResultBag(
				AcquisitionType.SOURCE_MEDIUM, "1", 5, 0,
				TimeRange.LAST_24_HOURS);

		List<Composition> results = compositionResultBag.getResults();

		Assertions.assertEquals(1, results.size());

		Composition composition = results.get(0);

		Assertions.assertEquals(
			"testsource3 / testmedium3", composition.getName());
		Assertions.assertEquals(1, composition.getCount());
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testBrowserMetricsLast7Days() {
		List<Metric> browserMetrics = _siteMetricDog.getBrowserMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_7_DAYS));

		DogTestUtil.assertMetric(1, browserMetrics, "Chrome");
		DogTestUtil.assertMetric(1, browserMetrics, "Firefox");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testBrowserMetricsLast24Hours() {
		List<Metric> browserMetrics = _siteMetricDog.getBrowserMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_24_HOURS));

		DogTestUtil.assertMetric(2, browserMetrics, "Chrome");
		DogTestUtil.assertMetric(1, browserMetrics, "Firefox");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testBrowserMetricsLast30Days() {
		List<Metric> browserMetrics = _siteMetricDog.getBrowserMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_30_DAYS));

		DogTestUtil.assertMetric(2, browserMetrics, "Chrome");
		DogTestUtil.assertMetric(2, browserMetrics, "Firefox");
	}

	@BQSQLResource(
		resourcePath = "test_visitor_cohort_heatmap_day_interval.sql"
	)
	@Disabled
	@Test
	public void testCohortHeatMapMetricsDayInterval() {
		CohortMetric cohortMetric = _siteMetricDog.getCohortMetric(
			_getVisitorCohortSearchQueryContext(Interval.DAY));

		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics =
			cohortMetric.getAnonymousCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 33.33333333333333);
						put(Pair.of(0, 3), 100.0);
						put(Pair.of(1, 3), 50.0);
						put(Pair.of(0, 7), 100.0);
					}
				},
				anonymousCohortHeatMapMetrics.size(), 8),
			_getActualCohortRetentions(anonymousCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> knownCohortHeatMapMetrics =
			cohortMetric.getKnownCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 100.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 3), 100.0);
						put(Pair.of(1, 3), 100.0);
						put(Pair.of(0, 7), 0.0);
						put(Pair.of(1, 7), 0.0);
					}
				},
				knownCohortHeatMapMetrics.size(), 8),
			_getActualCohortRetentions(knownCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> visitorsCohortHeatMapMetrics =
			cohortMetric.getVisitorsCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 60.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 3), 100.0);
						put(Pair.of(1, 3), 75.0);
						put(Pair.of(0, 7), 100.0);
						put(Pair.of(1, 7), 0.0);
					}
				},
				visitorsCohortHeatMapMetrics.size(), 8),
			_getActualCohortRetentions(visitorsCohortHeatMapMetrics), 0);
	}

	@BQSQLResource(
		resourcePath = "test_visitor_cohort_heatmap_month_interval.sql"
	)
	@Disabled
	@Test
	public void testCohortHeatMapMetricsMonthInterval() {
		CohortMetric cohortMetric = _siteMetricDog.getCohortMetric(
			_getVisitorCohortSearchQueryContext(Interval.MONTH));

		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics =
			cohortMetric.getAnonymousCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 33.33333333333333);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 50.0);
						put(Pair.of(0, 6), 100.0);
					}
				},
				anonymousCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(anonymousCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> knownCohortHeatMapMetrics =
			cohortMetric.getKnownCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 100.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 100.0);
						put(Pair.of(1, 5), 0.0);
						put(Pair.of(0, 6), 0.0);
					}
				},
				knownCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(knownCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> visitorsCohortHeatMapMetrics =
			cohortMetric.getVisitorsCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 60.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 75.0);
						put(Pair.of(0, 6), 100.0);
						put(Pair.of(1, 6), 0.0);
					}
				},
				visitorsCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(visitorsCohortHeatMapMetrics), 0);
	}

	@BQSQLResource(
		resourcePath = "test_visitor_cohort_heatmap_week_interval.sql"
	)
	@Test
	public void testCohortHeatMapMetricsWeekInterval() {
		CohortMetric cohortMetric = _siteMetricDog.getCohortMetric(
			_getVisitorCohortSearchQueryContext(Interval.WEEK));

		List<CohortHeatMapMetric> anonymousCohortHeatMapMetrics =
			cohortMetric.getAnonymousCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 33.33333333333333);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 50.0);
						put(Pair.of(0, 6), 100.0);
					}
				},
				anonymousCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(anonymousCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> knownCohortHeatMapMetrics =
			cohortMetric.getKnownCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 100.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 100.0);
						put(Pair.of(1, 5), 0.0);
						put(Pair.of(0, 6), 0.0);
					}
				},
				knownCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(knownCohortHeatMapMetrics), 0);

		List<CohortHeatMapMetric> visitorsCohortHeatMapMetrics =
			cohortMetric.getVisitorsCohortHeatMapMetrics();

		Assertions.assertArrayEquals(
			_getExpectedCohortRetentions(
				new HashMap<Pair<Integer, Integer>, Double>() {
					{
						put(Pair.of(0, 0), 100.0);
						put(Pair.of(1, 0), 60.0);
						put(Pair.of(2, 0), 0.0);
						put(Pair.of(0, 2), 100.0);
						put(Pair.of(1, 2), 75.0);
						put(Pair.of(0, 6), 100.0);
						put(Pair.of(1, 6), 0.0);
					}
				},
				visitorsCohortHeatMapMetrics.size(), 7),
			_getActualCohortRetentions(visitorsCohortHeatMapMetrics), 0);
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testDeviceMetricsLast24Hours() {
		List<Metric> browserMetrics = _siteMetricDog.getDeviceMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_24_HOURS));

		DogTestUtil.assertMetric(3, browserMetrics, "Desktop");
		DogTestUtil.assertMetric(2, browserMetrics, "Desktop", "Linux");
		DogTestUtil.assertMetric(1, browserMetrics, "Desktop", "Win10");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testDeviceMetricsLast30Days() {
		List<Metric> browserMetrics = _siteMetricDog.getDeviceMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_30_DAYS));

		DogTestUtil.assertMetric(1, browserMetrics, "Desktop");
		DogTestUtil.assertMetric(1, browserMetrics, "Desktop", "Linux");
		DogTestUtil.assertMetric(3, browserMetrics, "Phone");
		DogTestUtil.assertMetric(1, browserMetrics, "Phone", "Android");
		DogTestUtil.assertMetric(2, browserMetrics, "Phone", "IOS");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_technology.sql")
	@Test
	public void testDeviceMetricsOrdering() {
		List<Metric> deviceMetrics = _siteMetricDog.getDeviceMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_30_DAYS));

		double previousDeviceCount = -1;
		String previousDeviceType = null;

		for (Metric deviceMetric : deviceMetrics) {
			Double deviceCount = deviceMetric.getValue();
			String deviceType = deviceMetric.getValueKey();

			if (previousDeviceType != null) {
				Assertions.assertTrue(deviceCount <= previousDeviceCount);

				if (deviceCount == previousDeviceCount) {
					Assertions.assertTrue(
						deviceType.compareTo(previousDeviceType) >= 0);
				}
			}

			double previousPlatformCount = -1;
			String previousPlatformName = null;

			for (Metric platformMetric : deviceMetric.getMetrics()) {
				Double platformCount = platformMetric.getValue();

				String platformName = platformMetric.getValueKey();

				if (previousPlatformName != null) {
					Assertions.assertTrue(
						platformCount <= previousPlatformCount);

					if (platformCount == previousPlatformCount) {
						Assertions.assertTrue(
							platformName.compareTo(previousPlatformName) >= 0);
					}
				}

				previousPlatformCount = platformCount;
				previousPlatformName = platformName;
			}

			previousDeviceCount = deviceCount;
			previousDeviceType = deviceType;
		}
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_geolocation.sql")
	@Test
	public void testGeolocationMetrics1() {
		List<Metric> geolocationMetrics = _siteMetricDog.getGeolocationMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_24_HOURS));

		Assertions.assertEquals(
			2, geolocationMetrics.size(), geolocationMetrics.toString());

		DogTestUtil.assertMetric(2, geolocationMetrics, "Brazil");
		DogTestUtil.assertMetric(1, geolocationMetrics, "Spain");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_geolocation.sql")
	@Test
	public void testGeolocationMetrics2() {
		List<Metric> geolocationMetrics = _siteMetricDog.getGeolocationMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_7_DAYS));

		Assertions.assertEquals(
			2, geolocationMetrics.size(), geolocationMetrics.toString());

		DogTestUtil.assertMetric(1, geolocationMetrics, "Brazil");
		DogTestUtil.assertMetric(1, geolocationMetrics, "Spain");
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_site_geolocation.sql")
	@Test
	public void testGeolocationMetrics3() {
		List<Metric> geolocationMetrics = _siteMetricDog.getGeolocationMetrics(
			SiteMetricType.SESSIONS,
			_getSearchQueryContext(TimeRange.LAST_30_DAYS));

		Assertions.assertEquals(
			2, geolocationMetrics.size(), geolocationMetrics.toString());

		DogTestUtil.assertMetric(2, geolocationMetrics, "Brazil");
		DogTestUtil.assertMetric(2, geolocationMetrics, "Spain");
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testGetSiteMetric() {
		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			_getSearchQueryContext());

		Metric anonymousVisitorsMetric =
			siteMetric.getAnonymousVisitorsMetric();

		Assertions.assertEquals(0, anonymousVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(1, anonymousVisitorsMetric.getValue());

		Metric knownVisitorsMetric = siteMetric.getKnownVisitorsMetric();

		Assertions.assertEquals(1, knownVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(1, knownVisitorsMetric.getValue());

		Metric visitorsMetric = siteMetric.getVisitorsMetric();

		Assertions.assertEquals(1, visitorsMetric.getPreviousValue());
		Assertions.assertEquals(2, visitorsMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_1.sql")
	@Test
	public void testGetSiteMetricBounceRateLast7Days() {
		SearchQueryContext searchQueryContext = _getSearchQueryContext();

		searchQueryContext.setIncludePrevious(false);
		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			searchQueryContext);

		Metric bounceRateMetric = siteMetric.getBounceRateMetric();

		Assertions.assertEquals(null, bounceRateMetric.getPreviousValue());
		Assertions.assertEquals(0.5, bounceRateMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_1.sql")
	@Test
	public void testGetSiteMetricBounceRateLast7DaysIncludePrevious() {
		SearchQueryContext searchQueryContext = _getSearchQueryContext();

		searchQueryContext.setIncludePrevious(true);
		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			searchQueryContext);

		Metric bounceRateMetric = siteMetric.getBounceRateMetric();

		Assertions.assertEquals(1, bounceRateMetric.getPreviousValue());
		Assertions.assertEquals(0.5, bounceRateMetric.getValue());
	}

	@Test
	public void testGetSiteMetricEmptyState() {
		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			_getSearchQueryContext());

		Metric anonymousVisitorsMetric =
			siteMetric.getAnonymousVisitorsMetric();

		Assertions.assertNull(anonymousVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(0, anonymousVisitorsMetric.getValue());

		Metric knownVisitorsMetric = siteMetric.getKnownVisitorsMetric();

		Assertions.assertNull(knownVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(0, knownVisitorsMetric.getValue());

		Metric visitorsMetric = siteMetric.getVisitorsMetric();

		Assertions.assertNull(visitorsMetric.getPreviousValue());
		Assertions.assertEquals(0, visitorsMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_2.sql")
	@Test
	public void testGetSiteMetricSessionDuration() {
		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			_getSearchQueryContext());

		Metric sessionDurationMetric = siteMetric.getSessionDurationMetric();

		Assertions.assertEquals(8.64E7, sessionDurationMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_2.sql")
	@Test
	public void testGetSiteMetricSessionDurationLast24Hours() {
		SearchQueryContext searchQueryContext = _getSearchQueryContext();

		searchQueryContext.setTimeRange(TimeRange.LAST_24_HOURS);

		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			searchQueryContext);

		Metric sessionDurationMetric = siteMetric.getSessionDurationMetric();

		Assertions.assertEquals(3600000.0, sessionDurationMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events.sql")
	@Test
	public void testGetSiteMetricSessionPerVisitor() {
		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			_getSearchQueryContext());

		Metric sessionsPerVisitorMetric =
			siteMetric.getSessionsPerVisitorMetric();

		Assertions.assertEquals(1, sessionsPerVisitorMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_suppressed_individual.sql")
	@Test
	public void testGetSiteMetricSuppressedIndividual() {
		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			_getSearchQueryContext());

		Metric anonymousVisitorsMetric =
			siteMetric.getAnonymousVisitorsMetric();

		Assertions.assertEquals(2, anonymousVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(2, anonymousVisitorsMetric.getValue());

		Metric knownVisitorsMetric = siteMetric.getKnownVisitorsMetric();

		Assertions.assertEquals(0, knownVisitorsMetric.getPreviousValue());
		Assertions.assertEquals(0, knownVisitorsMetric.getValue());

		Metric visitorsMetric = siteMetric.getVisitorsMetric();

		Assertions.assertEquals(2, visitorsMetric.getPreviousValue());
		Assertions.assertEquals(2, visitorsMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_3.sql")
	@Test
	public void testGetSiteMetricVisitorPreviousValueOnly() {
		SearchQueryContext searchQueryContext = _getSearchQueryContext();

		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			searchQueryContext);

		Metric visitorsMetric = siteMetric.getVisitorsMetric();

		Assertions.assertEquals(1, visitorsMetric.getPreviousValue());
		Assertions.assertEquals(0, visitorsMetric.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_4.sql")
	@Test
	public void testGetSiteMetricVisitors() {
		SearchQueryContext searchQueryContext = _getSearchQueryContext();

		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		SiteMetric siteMetric = _siteMetricDog.getSiteMetric(
			searchQueryContext);

		Metric anonymousVisitorsMetrics =
			siteMetric.getAnonymousVisitorsMetric();

		Assertions.assertEquals(2, anonymousVisitorsMetrics.getPreviousValue());
		Assertions.assertEquals(2, anonymousVisitorsMetrics.getValue());

		Metric knownVisitorsMetrics = siteMetric.getKnownVisitorsMetric();

		Assertions.assertEquals(2, knownVisitorsMetrics.getPreviousValue());
		Assertions.assertEquals(2, knownVisitorsMetrics.getValue());

		Metric visitorsMetrics = siteMetric.getVisitorsMetric();

		Assertions.assertEquals(4, visitorsMetrics.getPreviousValue());
		Assertions.assertEquals(4, visitorsMetrics.getValue());
	}

	@BQSQLResource(resourcePath = "test_bq_events_search_terms.sql")
	@Test
	public void testSearchTerms7Days() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getSearchTermsCompositionResultBag(
				1L, 5, 0, TimeRange.LAST_7_DAYS);

		LinkedHashMap<String, Long> expectedResults =
			new LinkedHashMap<String, Long>() {
				{
					put("liferay", 1L);
					put("test", 1L);
				}
			};

		List<Composition> results = compositionResultBag.getResults();

		Stream<Composition> stream = results.stream();

		Assertions.assertEquals(
			expectedResults,
			stream.collect(
				Collectors.toMap(
					Composition::getName, Composition::getCount,
					(name, count) -> name, LinkedHashMap::new)));

		Assertions.assertEquals(1, compositionResultBag.getMaxCount());
		Assertions.assertEquals(2, compositionResultBag.getTotal());
		Assertions.assertEquals(2, compositionResultBag.getTotalCount());
	}

	@BQSQLResource(resourcePath = "test_bq_events_search_terms.sql")
	@Test
	public void testSearchTerms24Hours() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getSearchTermsCompositionResultBag(
				1L, 5, 0, TimeRange.LAST_24_HOURS);

		LinkedHashMap<String, Long> expectedResults =
			new LinkedHashMap<String, Long>() {
				{
					put("test", 2L);
				}
			};

		List<Composition> results = compositionResultBag.getResults();

		Stream<Composition> stream = results.stream();

		Assertions.assertEquals(
			expectedResults,
			stream.collect(
				Collectors.toMap(
					Composition::getName, Composition::getCount,
					(name, count) -> name, LinkedHashMap::new)));

		Assertions.assertEquals(2, compositionResultBag.getMaxCount());
		Assertions.assertEquals(1, compositionResultBag.getTotal());
		Assertions.assertEquals(2, compositionResultBag.getTotalCount());
	}

	@BQSQLResource(resourcePath = "test_bq_events_search_terms.sql")
	@Test
	public void testSearchTerms30Days() {
		CompositionResultBag compositionResultBag =
			_siteMetricDog.getSearchTermsCompositionResultBag(
				1L, 5, 0, TimeRange.LAST_30_DAYS);

		LinkedHashMap<String, Long> expectedResults =
			new LinkedHashMap<String, Long>() {
				{
					put("liferay", 2L);
					put("documents and media", 1L);
					put("forms", 1L);
					put("test", 1L);
				}
			};

		List<Composition> results = compositionResultBag.getResults();

		Stream<Composition> stream = results.stream();

		Assertions.assertEquals(
			expectedResults,
			stream.collect(
				Collectors.toMap(
					Composition::getName, Composition::getCount,
					(name, count) -> name, LinkedHashMap::new)));

		Assertions.assertEquals(2, compositionResultBag.getMaxCount());
		Assertions.assertEquals(4, compositionResultBag.getTotal());
		Assertions.assertEquals(5, compositionResultBag.getTotalCount());
	}

	@BQSQLResource(
		resourcePath = "test_bq_sessions_visitors_by_day_and_time.sql"
	)
	@Test
	public void testVisitorHeatMapMetrics30Days() {
		List<HeatMapMetric> heatMapMetrics = _siteMetricDog.getHeatMapMetrics(
			1L,
			TimeRange.of(
				LocalDate.parse("2022-10-06"), LocalDate.parse("2022-09-06")));

		HeatMapMetric heatMapMetric = heatMapMetrics.get(63);

		Assertions.assertEquals(1.0, heatMapMetric.getValue());

		heatMapMetric = heatMapMetrics.get(87);

		Assertions.assertEquals(1.0, heatMapMetric.getValue());
	}

	private double[] _getActualCohortRetentions(
		List<CohortHeatMapMetric> cohortHeatMapMetrics) {

		Stream<CohortHeatMapMetric> cohortHeatMapMetricsStream =
			cohortHeatMapMetrics.stream();

		return cohortHeatMapMetricsStream.mapToDouble(
			CohortHeatMapMetric::getRetention
		).toArray();
	}

	private double[] _getExpectedCohortRetentions(
		Map<Pair<Integer, Integer>, Double> expectedRetentionsMap, int size,
		int totalLines) {

		double[] expectedRetentions = new double[size];

		int totalLinesMissed = 0;

		for (Map.Entry<Pair<Integer, Integer>, Double> entry :
				expectedRetentionsMap.entrySet()) {

			Pair<Integer, Integer> key = entry.getKey();

			int col = key.getLeft();

			int row = key.getRight();

			int index = (col * totalLines) + row;

			if ((col > 2) && (row == 0)) {
				totalLinesMissed += col - 2;
			}

			if ((totalLinesMissed > 0) && (col > 2)) {
				index -= totalLinesMissed;
			}

			expectedRetentions[index] = entry.getValue();
		}

		return expectedRetentions;
	}

	private SearchQueryContext _getSearchQueryContext() {
		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setChannelId("1");
		searchQueryContext.setIncludePrevious(true);
		searchQueryContext.setTimeRange(TimeRange.LAST_30_DAYS);

		return searchQueryContext;
	}

	private SearchQueryContext _getSearchQueryContext(TimeRange timeRange) {
		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setAssetType(AssetType.SITE);
		searchQueryContext.setChannelId("1");
		searchQueryContext.setIncludePrevious(true);
		searchQueryContext.setTimeRange(timeRange);

		return searchQueryContext;
	}

	private SearchQueryContext _getVisitorCohortSearchQueryContext(
		Interval interval) {

		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setChannelId("1");
		searchQueryContext.setInterval(interval.getKey());

		return searchQueryContext;
	}

	@Autowired
	private SiteMetricDog _siteMetricDog;

}