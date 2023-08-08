/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.test;

import com.liferay.osb.asah.backend.model.DocumentLibraryMetric;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
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
public class DocumentLibraryAssetMetricRepositoryTest
	extends BaseAssetMetricRepositoryTestCase<DocumentLibraryMetric> {

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_canonical_urls_last_7_days.sql"
	)
	@Test
	public void testGetAppearsOnMetricLast7Days() {
		super.assertAppearsOnMetric(
			DocumentLibraryMetricType.DOWNLOADS, TimeRange.LAST_7_DAYS);
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_browser_last_30_days.sql"
	)
	@Test
	public void testGetBrowserMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Firefox", 14D), new Tuple2("Chrome", 9D),
				new Tuple2("Opera Desktop", 3D)),
			_assetMetricRepository.getBrowserMetrics(
				"e131fabc", null, 1L, DocumentLibraryMetricType.PREVIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("Desktop", 22D), new Tuple2("Tablet", 18D),
				new Tuple2("Mobile", 12D), new Tuple2("Phone", 9D)),
			_assetMetricRepository.getDeviceMetrics(
				"e131fabc", null, 1L, DocumentLibraryMetricType.PREVIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_device_last_30_days.sql"
	)
	@Test
	public void testGetDeviceMetricsOrdering() {
		assertDeviceMetricsOrdering(
			_assetMetricRepository.getDeviceMetrics(
				"e131fabc", null, 1L, DocumentLibraryMetricType.PREVIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_geolocation_last_30_days.sql"
	)
	@Test
	public void testGetGeolocationMetricsLast30Days() {
		assertMetrics(
			Arrays.asList(
				new Tuple2("France", 9D), new Tuple2("Japan", 7D),
				new Tuple2("United States", 5D)),
			_assetMetricRepository.getGeolocationMetrics(
				"e131fabc", null, 1L, DocumentLibraryMetricType.PREVIEWS,
				TimeRange.LAST_30_DAYS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetPreviewsAssetMetric() {
		DocumentLibraryMetric documentLibraryMetric =
			_assetMetricRepository.getAssetMetric(
				"e131fabc", null, 1L,
				SetUtil.of(DocumentLibraryMetricType.PREVIEWS.getName()),
				TimeRange.LAST_24_HOURS);

		Assertions.assertNotNull(documentLibraryMetric);

		Metric previewsMetric = documentLibraryMetric.getPreviewsMetric();

		Assertions.assertEquals(7D, previewsMetric.getValue(), 0);
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetPreviewsAssetMetrics() {
		assertAssetMetrics(
			new Double[] {7D, 6D},
			_assetMetricRepository.getAssetMetrics(
				1L, null, null, PageRequest.of(0, 10),
				SetUtil.of(DocumentLibraryMetricType.PREVIEWS.getName()),
				TimeRange.LAST_24_HOURS),
			DocumentLibraryMetric::getPreviewsMetric);
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_7_days.sql"
	)
	@Test
	public void testGetPreviewsHistogramMetricsLast7Days() {
		assertHistogramMetrics(
			SetUtil.of((double)3),
			_assetMetricRepository.getHistogramMetrics(
				"e131fabc", null, 1L, false, Interval.DAY,
				DocumentLibraryMetricType.PREVIEWS, TimeRange.LAST_7_DAYS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetPreviewsHistogramMetricsLast24Hours() {
		assertHistogramMetrics(
			SetUtil.of((double)1, (double)2, (double)4),
			_assetMetricRepository.getHistogramMetrics(
				"e131fabc", null, 1L, false, Interval.HOUR,
				DocumentLibraryMetricType.PREVIEWS, TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_previews_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetPreviewsHistogramMetricsLast24HoursDifferentTimezone() {
		assertHistogramMetricsDifferentTimezone(
			"e131fabc", 1L, DocumentLibraryMetricType.PREVIEWS, -3,
			"America/Fortaleza");
	}

	@BQSQLResource(
		resourcePath = "document_library_asset_metric_ratings_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetRatingsAssetMetric() {
		DocumentLibraryMetric documentLibraryMetric =
			_assetMetricRepository.getAssetMetric(
				"e131fabc", null, 1L,
				SetUtil.of(DocumentLibraryMetricType.RATINGS.getName()),
				TimeRange.LAST_24_HOURS);

		Assertions.assertNotNull(documentLibraryMetric);

		Metric ratingsMetric = documentLibraryMetric.getRatingsMetric();

		Assertions.assertEquals(0.9, ratingsMetric.getValue(), 0.01);
	}

	@Override
	protected AssetMetricRepository getAssetMetricRepository() {
		return _assetMetricRepository;
	}

	@Autowired
	@Qualifier("DocumentLibraryAssetMetricRepository")
	private AssetMetricRepository<DocumentLibraryMetric> _assetMetricRepository;

}