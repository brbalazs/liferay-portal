/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.common.repository.AssetRepository;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import org.junit.jupiter.api.Disabled;

/**
 * @author Geyson Silva
 */
@Disabled
@RepositoryResource(
	repositoryClass = BQEventRepository.class,
	resourcePath = "osbasahfaroinfo/events.json"
)
@RepositoryResource(
	repositoryClass = AssetRepository.class,
	resourcePath = "osbasahfaroinfo/assets_info.json"
)
public class SiteInterestGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "site_interest_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "site_interest_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "site_interest_query.graphql";
	}

}