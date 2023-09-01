/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.test;

import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.model.PageMetric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.PageMetricType;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.Tuple2;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leslie Wong
 */
public class PageAssetMetricRepositoryTest
	extends BaseAssetMetricRepositoryTestCase<PageMetric> {

	@BQSQLResource(resourcePath = "page_asset_metric_audience_report.sql")
	@Test
	public void testGetAudienceReportLast7Days() {
		AudienceReport audienceReport = new AudienceReport();

		audienceReport.setAnonymousIndividualsCount(1L);
		audienceReport.setKnownIndividualsCount(2L);
		audienceReport.setNonsegmentedIndividualsCount(0L);
		audienceReport.setSegmentedIndividualsCount(2L);

		Assertions.assertEquals(
			audienceReport,
			_assetMetricRepository.getAudienceReport(
				"https://www.beryl.com/delivery", null, 1L,
				PageMetricType.VIEWS, TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(resourcePath = "page_asset_metric_audience_report.sql")
	@Test
	public void testGetAudienceReportLast24Hours() {
		AudienceReport audienceReport = new AudienceReport();

		audienceReport.setAnonymousIndividualsCount(2L);
		audienceReport.setKnownIndividualsCount(2L);
		audienceReport.setNonsegmentedIndividualsCount(0L);
		audienceReport.setSegmentedIndividualsCount(2L);

		Assertions.assertEquals(
			audienceReport,
			_assetMetricRepository.getAudienceReport(
				"https://www.beryl.com/delivery", null, 1L,
				PageMetricType.VIEWS, TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_browser_last_30_days.sql"
	)
	@Test
	public void testGetBrowserMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Firefox", 14D), new Tuple2("Chrome", 9D),
				new Tuple2("Unknown", 3D)),
			_assetMetricRepository.getBrowserMetrics(
				"https://www.beryl.com/delivery", "Beryl Delivery", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Desktop", 22D), new Tuple2("Tablet", 18D),
				new Tuple2("Mobile", 12D), new Tuple2("Phone", 9D)),
			_assetMetricRepository.getDeviceMetrics(
				"https://www.beryl.com/delivery", "Beryl Delivery", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsOrdering() {
		assertDeviceMetricsOrdering(
			_assetMetricRepository.getDeviceMetrics(
				"https://www.beryl.com/delivery", "Beryl Delivery", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_geolocation_last_30_days.sql"
	)
	@Test
	public void testGetGeolocationMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("France", 9D), new Tuple2("Japan", 7D),
				new Tuple2("United States", 5D)),
			_assetMetricRepository.getGeolocationMetrics(
				"https://www.beryl.com/delivery", "Beryl Delivery", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(resourcePath = "page_asset_metric_segment.sql")
	@Test
	public void testGetSegmentMetricsLast7Days() {
		assertMetrics(
			Collections.singletonList(new Tuple2("192837465", 2D)),
			_assetMetricRepository.getSegmentMetrics(
				"https://www.beryl.com/delivery", "Asset Title 2", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(resourcePath = "page_asset_metric_segment.sql")
	@Test
	public void testGetSegmentMetricsLast24Hours() {
		assertMetrics(
			Collections.singletonList(new Tuple2("192837465", 2D)),
			_assetMetricRepository.getSegmentMetrics(
				"https://www.beryl.com/delivery", "Title 2", 1L,
				PageMetricType.VIEWS, TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetric() {
		PageMetric pageMetric = _assetMetricRepository.getAssetMetric(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100",
			"Title 1", 1L, SetUtil.of(PageMetricType.VIEWS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertNotNull(pageMetric);

		Metric viewsMetric = pageMetric.getViewsMetric();

		Assertions.assertEquals(7D, viewsMetric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetrics() {
		assertAssetMetrics(
			new Double[] {7D, 6D},
			_assetMetricRepository.getAssetMetrics(
				1L, null, null, PageRequest.of(0, 10),
				SetUtil.of(PageMetricType.VIEWS.getName()),
				TimeRange.LAST_24_HOURS),
			PageMetric::getViewsMetric);
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetricsFilteringByTerms() {
		List<PageMetric> pageMetrics = _assetMetricRepository.getAssetMetrics(
			1L, null, null, PageRequest.of(0, 10),
			SetUtil.of(PageMetricType.VIEWS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(2, pageMetrics.size());

		pageMetrics = _assetMetricRepository.getAssetMetrics(
			1L, null, "title 1", PageRequest.of(0, 10),
			SetUtil.of(PageMetricType.VIEWS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, pageMetrics.size());

		pageMetrics = _assetMetricRepository.getAssetMetrics(
			1L, null, "description 1", PageRequest.of(0, 10),
			SetUtil.of(PageMetricType.VIEWS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertEquals(1, pageMetrics.size());
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetricsWithKeywords1() {
		assertAssetMetrics(
			new Double[] {7D},
			_assetMetricRepository.getAssetMetrics(
				1L, "Title 1", null, PageRequest.of(0, 10),
				SetUtil.of(PageMetricType.VIEWS.getName()),
				TimeRange.LAST_24_HOURS),
			PageMetric::getViewsMetric);
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetricsWithKeywords2() {
		assertAssetMetrics(
			new Double[] {6D},
			_assetMetricRepository.getAssetMetrics(
				1L, "delivery", null, PageRequest.of(0, 10),
				SetUtil.of(PageMetricType.VIEWS.getName()),
				TimeRange.LAST_24_HOURS),
			PageMetric::getViewsMetric);
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_7_days.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast7Days() {
		assertHistogramMetrics(
			SetUtil.of(3.0),
			_assetMetricRepository.getHistogramMetrics(
				"https://www.beryl.com/delivery", "Beryl Delivery", 1L, false,
				Interval.DAY, PageMetricType.VIEWS, TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24Hours() {
		assertHistogramMetrics(
			SetUtil.of((double)1, (double)2, (double)4),
			_assetMetricRepository.getHistogramMetrics(
				"https://www.beryl.com/products/commercial/irrigation/FF-2100",
				"Title 1", 1L, false, Interval.HOUR, PageMetricType.VIEWS,
				TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24HoursDifferentTimezone() {
		assertHistogramMetricsDifferentTimezone(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100", 1L,
			PageMetricType.VIEWS, -3, "America/Fortaleza");
	}

	@BQSQLResource(resourcePath = "page_asset_metric_visitors_last_7_days.sql")
	@Test
	public void testGetVisitorMetricsLast7Days() {
		PageMetric pageMetric = _assetMetricRepository.getAssetMetric(
			"https://www.beryl.com/delivery", "Beryl Delivery", 1L,
			SetUtil.of(PageMetricType.VISITORS.getName()),
			TimeRange.LAST_7_DAYS);

		Assertions.assertNotNull(pageMetric);

		Metric visitorsMetric = pageMetric.getVisitorsMetric();

		Assertions.assertEquals(2D, visitorsMetric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "page_asset_metric_visitors_last_24_hours.sql"
	)
	@Test
	public void testGetVisitorMetricsLast24Hours() {
		PageMetric pageMetric = _assetMetricRepository.getAssetMetric(
			"https://www.beryl.com/products/commercial/irrigation/FF-2100",
			"Title 1", 1L, SetUtil.of(PageMetricType.VISITORS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertNotNull(pageMetric);

		Metric visitorsMetric = pageMetric.getVisitorsMetric();

		Assertions.assertEquals(2D, visitorsMetric.getValue(), 0);
	}

	@Override
	protected AssetMetricRepository<PageMetric> getAssetMetricRepository() {
		return _assetMetricRepository;
	}

	@Autowired
	@Qualifier("PageAssetMetricRepository")
	private AssetMetricRepository<PageMetric> _assetMetricRepository;

}