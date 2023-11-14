/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.http.NanitesHttp;
import com.liferay.osb.asah.common.postgresql.PostgreSQLSchemaManager;
import com.liferay.osb.asah.common.repository.ProjectRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.ReleaseInfo;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.IterableUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author André Miranda
 */
@Component
public class ProjectDog {

	public void addProject(String projectId) {
		Project project = new Project(projectId, ReleaseInfo.getVersion());

		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			_projectRepository.save(project);
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}

		_postgreSQLSchemaManager.createSchema(project);

		_bigQuerySchemaManager.createSchema(projectId);

		ProjectIdThreadLocal.forProject(
			project, _nanitesHttp::rescheduleNanites);
	}

	public void deleteProject(boolean deleteData, String projectId) {
		if (deleteData) {
			_bigQuerySchemaManager.deleteSchema(projectId);
		}

		ProjectIdThreadLocal.forProject(
			projectId, _nanitesHttp::removeSchedule);

		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			_projectRepository.deleteById(projectId);
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}

		if (deleteData) {
			_postgreSQLSchemaManager.deleteSchema(projectId);
		}
	}

	public Project getProject(String projectId) {
		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			Optional<Project> projectOptional = _projectRepository.findById(
				projectId);

			return projectOptional.orElseThrow(
				() -> new OSBAsahException(
					HttpStatus.BAD_REQUEST,
					"There is no Project with ID " + projectId));
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}
	}

	public List<Project> getProjects() {
		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			return IterableUtils.toList(_projectRepository.findAll());
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}
	}

	@PostConstruct
	public void init() {
		_postgreSQLSchemaManager.createGlobalSchema();
	}

	public void updateVersion(String projectId, String version) {
		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			_projectRepository.updateVersion(projectId, version);
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}
	}

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

	@Autowired
	private NanitesHttp _nanitesHttp;

	@Autowired
	private PostgreSQLSchemaManager _postgreSQLSchemaManager;

	@Autowired
	private ProjectRepository _projectRepository;

}