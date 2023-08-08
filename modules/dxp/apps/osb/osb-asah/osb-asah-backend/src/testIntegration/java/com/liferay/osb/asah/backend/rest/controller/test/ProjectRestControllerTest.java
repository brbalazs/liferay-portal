/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.backend.dto.ProjectDTO;
import com.liferay.osb.asah.backend.dto.ProjectDetailDTO;
import com.liferay.osb.asah.backend.rest.controller.ProjectsRestController;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.ReleaseInfo;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Riccardo Ferrari
 */
public class ProjectRestControllerTest
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources_6.json"
	)
	@Test
	public void testGetProjectDetails() {
		List<ProjectDetailDTO> projectDetailDTOs =
			_projectsRestController.getProjectDetailDTOs();

		Assertions.assertEquals(1, projectDetailDTOs.size());

		ProjectDetailDTO projectDetailDTO = projectDetailDTOs.get(0);

		Assertions.assertTrue(projectDetailDTO.getAccountsSelected());
		Assertions.assertFalse(projectDetailDTO.getCommerceChannelsSelected());
		Assertions.assertTrue(projectDetailDTO.getContactsSelected());
		Assertions.assertTrue(projectDetailDTO.getSitesSelected());
	}

	@Test
	public void testGetProjectDTO() {
		List<ProjectDTO> projectDTOs = _projectsRestController.getProjectDTOs();

		Assertions.assertEquals(1, projectDTOs.size());

		ProjectDTO projectDTO = projectDTOs.get(0);

		Assertions.assertEquals("test", projectDTO.getId());
		Assertions.assertEquals(
			ReleaseInfo.getVersion(), projectDTO.getVersion());
	}

	@BeforeEach
	protected void setUp() {
		_projectDog.addProject("test");

		ProjectIdThreadLocal.setProjectId("test");
	}

	@Autowired
	private ProjectDog _projectDog;

	@Autowired
	private ProjectsRestController _projectsRestController;

}