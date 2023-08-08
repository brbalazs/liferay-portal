/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.repository.CrudBQDocumentLibraryRepository;

import org.junit.jupiter.api.Disabled;

/**
 * @author André Miranda
 */
@Disabled
@RepositoryResource(
	repositoryClass = CrudBQDocumentLibraryRepository.class,
	resourcePath = "osbasahcerebroinfo/document_library_info.json"
)
public class DocumentLibraryMetricGraphQLRestControllerTest
	extends BaseGraphQLRestControllerTestCase {

	@Override
	public String getBodyPath() {
		return "document_library_metric_body.json";
	}

	@Override
	public String getExpectedResultPath() {
		return "document_library_metric_expected_result.json";
	}

	@Override
	public String getQueryPath() {
		return "document_library_metric_query.graphql";
	}

}