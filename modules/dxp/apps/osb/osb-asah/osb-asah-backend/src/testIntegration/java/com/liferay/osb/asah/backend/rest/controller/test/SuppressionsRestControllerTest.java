/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.SuppressionsRestController;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

/**
 * @author Leslie Wong
 */
public class SuppressionsRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "suppressions_rest_controller_test.sql")
	@Test
	public void testDownloadLogs() throws Exception {
		ResponseEntity responseEntity =
			_suppressionsRestController.downloadLogs(
				"(createDate ge '2023-08-02' and createDate le '2023-08-05')");

		FileSystemResource fileSystemResource =
			(FileSystemResource)responseEntity.getBody();

		Assertions.assertNotNull(fileSystemResource);
		Assertions.assertEquals(
			StringUtils.trim(
				ResourceUtil.readResourceToString(
					"dependencies/suppressions_log.csv", this)),
			StringUtils.trim(
				IOUtils.toString(
					fileSystemResource.getInputStream(),
					StandardCharsets.UTF_8)));
	}

	@Autowired
	private SuppressionsRestController _suppressionsRestController;

}