/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dog.CustomAssetDashboardDog;
import com.liferay.osb.asah.common.entity.CustomAssetDashboard;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author André Miranda
 */
public class CustomAssetDashboardDogTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
	)
	@Test
	public void testCustomAssetDashboardNotFound() {
		Assertions.assertNull(
			_customAssetDashboardDog.fetchCustomAssetDashboard("0"));
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
	)
	@Test
	public void testGetCustomAssetDashboard() {
		String customAssetDashboardId =
			"e131fabc648f00a7ccb6601acf6bfa831ee195d84126ca2f90eae1d4e9d863a9";

		CustomAssetDashboard customAssetDashboard =
			_customAssetDashboardDog.fetchCustomAssetDashboard(
				customAssetDashboardId);

		Assertions.assertNotNull(customAssetDashboard);
		Assertions.assertEquals(
			"Asset Title 1", customAssetDashboard.getAssetTitle());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
	)
	@Test
	public void testGetCustomAssetDashboardPageAll() {
		Page<CustomAssetDashboard> customAssetDashboardPage =
			_customAssetDashboardDog.getCustomAssetDashboardPage(
				1L, null, 0, 10, Sort.asc("assetTitle"));

		Assertions.assertNotNull(customAssetDashboardPage);
		Assertions.assertEquals(3, customAssetDashboardPage.getTotalElements());

		List<CustomAssetDashboard> customAssetDashboards =
			customAssetDashboardPage.getContent();

		Assertions.assertEquals(
			3, customAssetDashboards.size(), customAssetDashboards.toString());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
	)
	@Test
	public void testGetCustomAssetDashboardPagePaginated() {
		Page<CustomAssetDashboard> customAssetDashboardPage =
			_customAssetDashboardDog.getCustomAssetDashboardPage(
				1L, null, 1, 1, Sort.asc("assetTitle"));

		Assertions.assertNotNull(customAssetDashboardPage);
		Assertions.assertEquals(3, customAssetDashboardPage.getTotalElements());

		List<CustomAssetDashboard> customAssetDashboards =
			customAssetDashboardPage.getContent();

		Assertions.assertEquals(
			1, customAssetDashboards.size(), customAssetDashboards.toString());

		CustomAssetDashboard customAssetDashboard = customAssetDashboards.get(
			0);

		Assertions.assertEquals(
			"Asset Title 2", customAssetDashboard.getAssetTitle());
	}

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
	)
	@RepositoryResource(
		repositoryClass = CustomAssetDashboardRepository.class,
		resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
	)
	@Test
	public void testUpdateCustomAssetDashboard() {
		String customAssetDashboardId =
			"e131fabc648f00a7ccb6601acf6bfa831ee195d84126ca2f90eae1d4e9d863a9";

		CustomAssetDashboard customAssetDashboard =
			_customAssetDashboardDog.updateCustomAssetDashboard(
				customAssetDashboardId,
				"{\"rows\": [{\"panels\":[{\"title\":\"MyPanel\"," +
					"\"width\":100,\"metric\":\"assetViewed\"," +
						"\"chartType\":\"line\"}]}]}",
				"123", "Pedro");

		Assertions.assertNotNull(customAssetDashboard);
		Assertions.assertEquals("1", customAssetDashboard.getAssetId());
		Assertions.assertEquals(
			"Asset Title 1", customAssetDashboard.getAssetTitle());
		Assertions.assertEquals("default", customAssetDashboard.getCategory());
		Assertions.assertEquals(
			"e131fabc648f00a7ccb6601acf6bfa831ee195d84126ca2f90eae1d4e9d863a9",
			customAssetDashboard.getId());
		Assertions.assertEquals(
			"123", customAssetDashboard.getModifiedByUserId());
		Assertions.assertEquals(
			"Pedro", customAssetDashboard.getModifiedByUserName());
	}

	@Autowired
	private CustomAssetDashboardDog _customAssetDashboardDog;

}