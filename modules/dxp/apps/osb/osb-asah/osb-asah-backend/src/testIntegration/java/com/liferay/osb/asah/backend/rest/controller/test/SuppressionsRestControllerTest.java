/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
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