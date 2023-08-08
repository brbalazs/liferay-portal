/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.CustomAssetDashboardRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

/**
 * @author André Miranda
 */
@RepositoryResource(
	repositoryClass = ChannelRepository.class,
	resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_channel_info.json"
)
@RepositoryResource(
	repositoryClass = CustomAssetDashboardRepository.class,
	resourcePath = "osbasahcerebroinfo/custom_asset_dashboards_info.json"
)
public class DashboardGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "dashboard_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "dashboard_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "dashboard_query.graphql";
	}

}