/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQBlogRepository;

import org.junit.jupiter.api.Disabled;

/**
 * @author André Miranda
 */
@Disabled
@RepositoryResource(
	repositoryClass = CrudBQBlogRepository.class,
	resourcePath = "osbasahcerebroinfo/blog_info.json"
)
public class BlogMetricGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "blog_metric_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "blog_metric_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "blog_metric_query.graphql";
	}

}