/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.test;

import com.liferay.osb.asah.backend.model.AudienceReport;
import com.liferay.osb.asah.backend.model.FormMetric;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.Metric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.Tuple2;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
public class FormAssetMetricRepositoryTest
	extends BaseAssetMetricRepositoryTestCase<FormMetric> {

	@BQSQLResource(
		resourcePath = "form_asset_metric_canonical_urls_last_7_days.sql"
	)
	@Test
	public void testGetAppearsOnMetricLast7Days() {
		super.assertAppearsOnMetric(
			FormMetricType.SUBMISSIONS, TimeRange.LAST_7_DAYS);
	}

	@BQSQLResource(resourcePath = "form_asset_metric_audience_report.sql")
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
				"e131fabc", null, 1L, FormMetricType.SUBMISSIONS,
				TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(resourcePath = "form_asset_metric_audience_report.sql")
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
				"e131fabc", null, 1L, FormMetricType.VIEWS,
				TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_browser_last_30_days.sql"
	)
	@Test
	public void testGetBrowserMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Firefox", 14D), new Tuple2("Chrome", 9D),
				new Tuple2("Opera Desktop", 3D)),
			_assetMetricRepository.getBrowserMetrics(
				"e131fabc", null, 1L, FormMetricType.VIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Desktop", 22D), new Tuple2("Tablet", 18D),
				new Tuple2("Mobile", 12D), new Tuple2("Phone", 9D)),
			_assetMetricRepository.getDeviceMetrics(
				"e131fabc", null, 1L, FormMetricType.VIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsOrdering() {
		assertDeviceMetricsOrdering(
			_assetMetricRepository.getDeviceMetrics(
				"e131fabc", null, 1L, FormMetricType.VIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_geolocation_last_30_days.sql"
	)
	@Test
	public void testGetGeolocationMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("France", 9D), new Tuple2("Japan", 7D),
				new Tuple2("United States", 5D)),
			_assetMetricRepository.getGeolocationMetrics(
				"e131fabc", null, 1L, FormMetricType.VIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetric() {
		FormMetric formMetric = _assetMetricRepository.getAssetMetric(
			"e131fabc", null, 1L, SetUtil.of(FormMetricType.VIEWS.getName()),
			TimeRange.LAST_24_HOURS);

		Assertions.assertNotNull(formMetric);

		Metric viewsMetric = formMetric.getViewsMetric();

		Assertions.assertEquals(7D, viewsMetric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsAssetMetrics() {
		assertAssetMetrics(
			new Double[] {7D, 6D},
			_assetMetricRepository.getAssetMetrics(
				1L, null, null, PageRequest.of(0, 10),
				SetUtil.of(FormMetricType.VIEWS.getName()),
				TimeRange.LAST_24_HOURS),
			FormMetric::getViewsMetric);
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_7_days.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast7Days() {
		assertHistogramMetrics(
			SetUtil.of((double)3),
			_assetMetricRepository.getHistogramMetrics(
				"e131fabc", null, 1L, false, Interval.DAY, FormMetricType.VIEWS,
				TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24Hours() {
		assertHistogramMetrics(
			SetUtil.of((double)1, (double)2, (double)4),
			_assetMetricRepository.getHistogramMetrics(
				"e131fabc", null, 1L, false, Interval.HOUR,
				FormMetricType.VIEWS, TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "form_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24HoursDifferentTimezone() {
		assertHistogramMetricsDifferentTimezone(
			"e131fabc", 1L, FormMetricType.VIEWS, -3, "America/Fortaleza");
	}

	@Override
	protected AssetMetricRepository getAssetMetricRepository() {
		return _assetMetricRepository;
	}

	@Autowired
	@Qualifier("FormAssetMetricRepository")
	private AssetMetricRepository<FormMetric> _assetMetricRepository;

}