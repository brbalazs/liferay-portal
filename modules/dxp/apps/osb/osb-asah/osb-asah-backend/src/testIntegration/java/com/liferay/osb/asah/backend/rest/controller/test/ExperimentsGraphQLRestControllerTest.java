/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

/**
 * @author Geyson Silva
 */
@RepositoryResource(
	repositoryClass = ChannelRepository.class,
	resourcePath = "osbasahfaroinfo/channels_2.json"
)
@RepositoryResource(
	repositoryClass = ExperimentRepository.class,
	resourcePath = "osbasahfaroinfo/experiments.json"
)
public class ExperimentsGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "experiments_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "experiments_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "experiments_query.graphql";
	}

}