/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.DefinitionsRestController;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.json.JSONObject;

import org.junit.jupiter.api.Test;

import org.skyscreamer.jsonassert.JSONAssert;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Rachael Koestartyo
 */
public class DefinitionsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_1.json"
	)
	@Test
	public void test() throws Exception {
		JSONAssert.assertEquals(
			ResourceUtil.readResourceToJSONObject(
				"dependencies/expected_definitions_individual_attributes.json",
				this),
			_objectMapper.convertValue(
				_definitionsRestController.
					getIndividualBQFieldMappingDTOPageDTO(null),
				JSONObject.class),
			false);
	}

	@Autowired
	private DefinitionsRestController _definitionsRestController;

	@Autowired
	private ObjectMapper _objectMapper;

}