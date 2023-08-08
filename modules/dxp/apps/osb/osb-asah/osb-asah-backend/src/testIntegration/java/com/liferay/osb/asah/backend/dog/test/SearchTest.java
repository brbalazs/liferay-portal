/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.CustomAssetDashboardDog;
import com.liferay.osb.asah.backend.dog.MetricDog;
import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.model.AssetType;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQBlogRepository;
import com.liferay.osb.asah.test.util.repository.CrudBQPageRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author André Miranda
 */
public class SearchTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQBlogRepository.class,
		resourcePath = "osbasahcerebroinfo/search_blogs_info.json"
	)
	@Test
	public void testAssetSearch() {
		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setAssetType(AssetType.BLOG);
		searchQueryContext.setKeywords("titul");
		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			2, _metricDog.getAssetMetricsCount(searchQueryContext));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/search_custom_asset_dashboards_info.json"
	)
	@Test
	public void testCustomAssetDashboardSearch() {
		Page<CustomAssetDashboard> customAssetDashboardPage =
			_customAssetDashboardDog.getCustomAssetDashboardPage(
				1L, "ASSET", 0, 10, Sort.asc("assetTitle"));

		Assertions.assertEquals(2, customAssetDashboardPage.getTotalElements());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = CrudBQPageRepository.class,
		resourcePath = "osbasahcerebroinfo/search_pages_info.json"
	)
	@Test
	public void testPageSearch() {
		SearchQueryContext searchQueryContext = new SearchQueryContext();

		searchQueryContext.setAssetType(AssetType.PAGE);
		searchQueryContext.setKeywords("pag");
		searchQueryContext.setTimeRange(TimeRange.LAST_7_DAYS);

		Assertions.assertEquals(
			3, _metricDog.getAssetMetricsCount(searchQueryContext));
	}

	@Autowired
	private CustomAssetDashboardDog _customAssetDashboardDog;

	@Autowired
	private MetricDog _metricDog;

}