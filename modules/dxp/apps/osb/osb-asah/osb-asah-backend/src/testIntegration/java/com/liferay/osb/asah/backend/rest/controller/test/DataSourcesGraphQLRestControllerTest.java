/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

/**
 * @author Leslie Wong
 */
@RepositoryResource(
	repositoryClass = DataSourceRepository.class,
	resourcePath = "osbasahfaroinfo/data_sources_5.json"
)
public class DataSourcesGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "data_sources_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "data_sources_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "data_sources_query.graphql";
	}

}