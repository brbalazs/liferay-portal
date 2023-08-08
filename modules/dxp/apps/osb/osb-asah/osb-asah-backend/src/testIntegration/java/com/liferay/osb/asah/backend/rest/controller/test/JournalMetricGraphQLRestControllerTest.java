/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQJournalRepository;

import org.junit.jupiter.api.Disabled;

/**
 * @author André Miranda
 */
@Disabled
@RepositoryResource(
	repositoryClass = CrudBQJournalRepository.class,
	resourcePath = "osbasahcereroinfo/journal_info.json"
)
public class JournalMetricGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "journal_metric_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "journal_metric_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "journal_metric_query.graphql";
	}

}