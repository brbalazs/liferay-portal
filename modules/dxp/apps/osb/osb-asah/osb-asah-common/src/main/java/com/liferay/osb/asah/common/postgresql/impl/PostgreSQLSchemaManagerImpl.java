/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.impl;

import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.postgresql.PostgreSQLDataSource;
import com.liferay.osb.asah.common.postgresql.PostgreSQLSchemaManager;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.util.InMemoryResource;
import org.springframework.stereotype.Component;

/**
 * @author Rachael Koestartyo
 */
@Component
public class PostgreSQLSchemaManagerImpl implements PostgreSQLSchemaManager {

	@Override
	public void createGlobalSchema() {
		if (!(_dataSource instanceof PostgreSQLDataSource)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"createGlobalSchema has no effect on data source " +
						_dataSource);
			}

			return;
		}

		try {
			ProjectIdThreadLocal.setGlobalContext(true);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new InMemoryResource(
						"CREATE SCHEMA IF NOT EXISTS " +
							ProjectIdThreadLocal.getProjectId())),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ClassPathResource("tables_global.sql")),
				_dataSource);

			if (_log.isInfoEnabled()) {
				_log.info("Global schema created successfully");
			}
		}
		finally {
			ProjectIdThreadLocal.setGlobalContext(false);
		}
	}

	@Override
	public void createSchema(Project project) {
		if (!(_dataSource instanceof PostgreSQLDataSource)) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"createSchema has no effect on data source " + _dataSource);
			}

			return;
		}

		try {
			ProjectIdThreadLocal.setProject(project);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new InMemoryResource(
						"CREATE SCHEMA IF NOT EXISTS " +
							ProjectIdThreadLocal.getProjectId())),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ClassPathResource("functions.sql")),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ClassPathResource("tables.sql")),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					true, true, null, new ClassPathResource("constraints.sql")),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ClassPathResource("indexes.sql")),
				_dataSource);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ClassPathResource("data.sql")),
				_dataSource);

			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"Schema for project %s created successfully",
						project.getId()));
			}
		}
		finally {
			ProjectIdThreadLocal.remove();
		}
	}

	@Override
	public void deleteSchema(String projectId) {
		try {
			ProjectIdThreadLocal.setProjectId(projectId);

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new InMemoryResource(
						"DROP SCHEMA IF EXISTS " +
							ProjectIdThreadLocal.getProjectId() + " CASCADE")),
				_dataSource);
		}
		finally {
			ProjectIdThreadLocal.remove();
		}
	}

	@Override
	public boolean existsSchema(Project project) {
		try {
			ProjectIdThreadLocal.setProject(project);

			try (Connection connection = _dataSource.getConnection()) {
				DatabaseMetaData databaseMetaData = connection.getMetaData();

				ResultSet resultSet = databaseMetaData.getSchemas(
					null, StringUtils.lowerCase(project.getId()));

				return resultSet.next();
			}
			catch (SQLException sqlException) {
				return false;
			}
		}
		finally {
			ProjectIdThreadLocal.remove();
		}
	}

	@Override
	public boolean existsTable(Project project, String tableName) {
		try {
			ProjectIdThreadLocal.setProject(project);

			try (Connection connection = _dataSource.getConnection()) {
				DatabaseMetaData databaseMetaData = connection.getMetaData();

				ResultSet resultSet = databaseMetaData.getTables(
					null, null, StringUtils.lowerCase(tableName),
					new String[] {"TABLE"});

				return resultSet.next();
			}
			catch (SQLException sqlException) {
				return false;
			}
		}
		finally {
			ProjectIdThreadLocal.remove();
		}
	}

	private static final Log _log = LogFactory.getLog(
		PostgreSQLSchemaManagerImpl.class);

	@Autowired
	@Qualifier("postgreSQLDataSource")
	private DataSource _dataSource;

}