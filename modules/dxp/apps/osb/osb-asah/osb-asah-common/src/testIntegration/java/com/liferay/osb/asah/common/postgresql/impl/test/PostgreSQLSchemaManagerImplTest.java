/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql.impl.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.constants.CredentialConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.entity.Project;
import com.liferay.osb.asah.common.postgresql.PostgreSQLSchemaManager;
import com.liferay.osb.asah.test.util.configuration.JDBCTestConfiguration;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.postgresql.ds.PGSimpleDataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.util.InMemoryResource;

/**
 * @author Rachael Koestartyo
 */
@Import(JDBCTestConfiguration.class)
public class PostgreSQLSchemaManagerImplTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void test() {
		Project project = new Project("test");

		Assertions.assertTrue(
			_postgreSQLSchemaManager.existsTable(project, "EventAnalysis"));
		Assertions.assertTrue(
			_postgreSQLSchemaManager.existsTable(project, "EventDefinition"));
		Assertions.assertTrue(
			_postgreSQLSchemaManager.existsTable(project, "Project"));
	}

	@Test
	public void testDeleteSchema() {
		Project project = new Project("deletetest");

		_createSchema(project);

		Assertions.assertTrue(_postgreSQLSchemaManager.existsSchema(project));

		_postgreSQLSchemaManager.deleteSchema(project.getId());

		Assertions.assertFalse(_postgreSQLSchemaManager.existsSchema(project));

		Assertions.assertTrue(
			_postgreSQLSchemaManager.existsSchema(new Project("test")));
	}

	private void _createSchema(Project project) {
		PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();

		pgSimpleDataSource.setCurrentSchema(project.getId());
		pgSimpleDataSource.setDatabaseName(CredentialConstants.POSTGRESQL_DB);
		pgSimpleDataSource.setPassword(CredentialConstants.POSTGRESQL_PASSWORD);
		pgSimpleDataSource.setPortNumber(5432);
		pgSimpleDataSource.setServerName(ServiceConstants.POSTGRESQL_SERVER_IP);
		pgSimpleDataSource.setUser(CredentialConstants.POSTGRESQL_USER);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new InMemoryResource(
					"SET TIME ZONE 'UTC'; CREATE SCHEMA IF NOT EXISTS " +
						project.getId())),
			pgSimpleDataSource);
	}

	@Autowired
	private PostgreSQLSchemaManager _postgreSQLSchemaManager;

}