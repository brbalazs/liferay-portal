/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.rest.controller.DataControlTasksRestController;
import com.liferay.osb.asah.common.repository.DataControlTaskRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;

/**
 * @author Robson Pastor
 */
public class DataControlTasksRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = DataControlTaskRepository.class,
		resourcePath = "osbasahfaroinfo/data_control_tasks.json"
	)
	@Test
	public void testDownloadLogs() throws Exception {
		ResponseEntity responseEntity =
			_dataControlTasksRestController.downloadLogs("");

		FileSystemResource fileSystemResource =
			(FileSystemResource)responseEntity.getBody();

		Assertions.assertNotNull(fileSystemResource);
		Assertions.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/data_control_tasks.csv", this),
			IOUtils.toString(
				fileSystemResource.getInputStream(), StandardCharsets.UTF_8));
	}

	@Autowired
	private DataControlTasksRestController _dataControlTasksRestController;

}