/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.backend.model.BlogMetricType;
import com.liferay.osb.asah.backend.model.DocumentLibraryMetricType;
import com.liferay.osb.asah.backend.model.FormMetricType;
import com.liferay.osb.asah.backend.model.JournalMetricType;
import com.liferay.osb.asah.backend.model.SiteMetricType;
import com.liferay.osb.asah.common.model.CustomAssetMetricType;
import com.liferay.osb.asah.common.model.IndividualMetricType;
import com.liferay.osb.asah.common.model.MetricType;
import com.liferay.osb.asah.common.model.PageMetricType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class MetricTypeDog {

	public MetricType getDefaultMetricType(AssetType assetType) {
		return Optional.ofNullable(
			_defaultMetricTypes.get(assetType)
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	public MetricType getMetricType(AssetType assetType, String name) {
		return Optional.ofNullable(
			_metricTypeSuppliers.get(assetType)
		).map(
			metricTypeResolver -> metricTypeResolver.apply(name)
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	private final Map<AssetType, MetricType> _defaultMetricTypes =
		new HashMap<AssetType, MetricType>() {
			{
				put(AssetType.BLOG, BlogMetricType.VIEWS);
				put(AssetType.CUSTOM, CustomAssetMetricType.VIEWS);
				put(AssetType.DOCUMENT, DocumentLibraryMetricType.DOWNLOADS);
				put(AssetType.FORM, FormMetricType.SUBMISSIONS);
				put(
					AssetType.INDIVIDUAL_METRIC,
					IndividualMetricType.TOTAL_INDIVIDUALS);
				put(AssetType.JOURNAL, JournalMetricType.VIEWS);
				put(AssetType.PAGE, PageMetricType.VISITORS);
				put(AssetType.SITE, SiteMetricType.VISITORS);
			}
		};
	private final Map<AssetType, Function<String, MetricType>>
		_metricTypeSuppliers =
			new HashMap<AssetType, Function<String, MetricType>>() {
				{
					put(AssetType.BLOG, BlogMetricType::of);
					put(AssetType.CUSTOM, CustomAssetMetricType::of);
					put(AssetType.DOCUMENT, DocumentLibraryMetricType::of);
					put(AssetType.FORM, FormMetricType::of);
					put(AssetType.INDIVIDUAL_METRIC, IndividualMetricType::of);
					put(AssetType.JOURNAL, JournalMetricType::of);
					put(AssetType.PAGE, PageMetricType::of);
					put(AssetType.SITE, SiteMetricType::of);
				}
			};

}