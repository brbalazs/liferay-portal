/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import org.junit.jupiter.api.Disabled;

/**
 * @author Geyson Silva
 */
@Disabled
@RepositoryResource(
	repositoryClass = BQSessionRepository.class,
	resourcePath = "osbasahcerebroinfo/user_sessions_info.json"
)
public class AcquisitionGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "acquisition_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "acquisition_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "acquisition_query.graphql";
	}

}