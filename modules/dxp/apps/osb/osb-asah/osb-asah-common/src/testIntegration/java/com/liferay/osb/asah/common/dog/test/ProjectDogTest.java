/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.ProjectDog;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.postgresql.PostgreSQLSchemaManager;
import com.liferay.osb.asah.common.repository.ProjectRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author André Miranda
 */
public class ProjectDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BeforeEach
	public void setUp() {
		ReflectionTestUtils.setField(_projectDog, "_nanitesHttp", _nanitesHttp);
		ReflectionTestUtils.setField(
			_projectDog, "_postgreSQLSchemaManager", _postgreSQLSchemaManager);
	}

	@AfterEach
	public void tearDown() {
		_projectRepository.deleteAll();

		ProjectIdThreadLocal.setGlobalContext(false);
	}

	@Test
	public void testAddProject() {
		_projectDog.addProject("project4");

		Mockito.verify(
			_nanitesHttp, Mockito.times(1)
		).rescheduleNanites();

		ProjectIdThreadLocal.setGlobalContext(true);

		Assertions.assertTrue(_projectRepository.existsById("project4"));
	}

	@SQLResource(resourcePath = "test_projects.sql")
	@Test
	public void testDeleteProject() {
		_projectDog.deleteProject(false, "project2");

		Mockito.verify(
			_nanitesHttp, Mockito.times(1)
		).removeSchedule();

		ProjectIdThreadLocal.setGlobalContext(true);

		Assertions.assertTrue(_projectRepository.existsById("project1"));
		Assertions.assertFalse(_projectRepository.existsById("project2"));
		Assertions.assertTrue(_projectRepository.existsById("project3"));
	}

	@SQLResource(resourcePath = "test_projects.sql")
	@Test
	public void testGetProjects() {
		List<Project> projects = _projectDog.getProjects();

		Stream<Project> stream = projects.stream();

		Assertions.assertArrayEquals(
			new String[] {"project1", "project2", "project3"},
			stream.map(
				Project::getId
			).sorted(
			).toArray());
	}

	@SQLResource(resourcePath = "test_projects.sql")
	@Test
	public void testUpdateVersion() {
		_projectDog.updateVersion("project1", "5.0.0");

		Project project1 = _projectDog.getProject("project1");

		Assertions.assertEquals("5.0.0", project1.getVersion());

		Project project2 = _projectDog.getProject("project2");

		Assertions.assertEquals("4.0.0", project2.getVersion());
	}

	@MockBean
	private NanitesHttp _nanitesHttp;

	@MockBean
	private PostgreSQLSchemaManager _postgreSQLSchemaManager;

	@Autowired
	private ProjectDog _projectDog;

	@Autowired
	private ProjectRepository _projectRepository;

}