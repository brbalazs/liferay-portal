/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository.test;

import com.liferay.osb.asah.backend.model.CustomAssetMetric;
import com.liferay.osb.asah.backend.repository.AssetMetricRepository;
import com.liferay.osb.asah.common.model.CustomAssetMetricType;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;

import org.apache.commons.codec.digest.DigestUtils;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author Marcellus Tavares
 */
public class CustomAssetMetricRepositoryTest
	extends BaseAssetMetricRepositoryTestCase {

	@BQSQLResource(
		resourcePath = "custom_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24Hours() {
		assertHistogramMetrics(
			SetUtil.of(1D, 2D, 4D, 7D),
			_assetMetricRepository.getHistogramMetrics(
				DigestUtils.sha256Hex("Adefault1"), null, 1L, false,
				Interval.HOUR, CustomAssetMetricType.VIEWS,
				TimeRange.LAST_24_HOURS));
	}

	@BQSQLResource(
		resourcePath = "custom_asset_metric_views_histogram_last_24_hours.sql"
	)
	@Test
	public void testGetViewsHistogramMetricsLast24HoursDifferentTimezone() {
		assertHistogramMetricsDifferentTimezone(
			DigestUtils.sha256Hex("Adefault1"), 1L, CustomAssetMetricType.VIEWS,
			-3, "America/Fortaleza");
	}

	@Override
	protected AssetMetricRepository getAssetMetricRepository() {
		return _assetMetricRepository;
	}

	@Autowired
	@Qualifier("CustomAssetMetricRepository")
	private AssetMetricRepository<CustomAssetMetric> _assetMetricRepository;

}