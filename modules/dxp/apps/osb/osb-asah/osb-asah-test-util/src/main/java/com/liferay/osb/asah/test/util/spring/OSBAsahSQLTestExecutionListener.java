/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.spring;

import com.liferay.osb.asah.common.util.ResourceTemplateUtil;
import com.liferay.osb.asah.test.util.annotation.SQLResource;

import java.io.File;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Objects;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.test.context.TestContext;

/**
 * @author Leslie Wong
 */
@TestComponent
public class OSBAsahSQLTestExecutionListener
	extends BaseOSBAsahTestExecutionListener {

	@Override
	public void afterTestMethod(TestContext testContext) throws SQLException {
		if (!_isTestExecutionListenerEnabled()) {
			return;
		}

		if (_postgreSQLDataSource != null) {
			try (Connection connection =
					_postgreSQLDataSource.getConnection()) {

				DatabaseMetaData databaseMetaData = connection.getMetaData();

				ResultSet resultSet = databaseMetaData.getTables(
					null, connection.getSchema(), null, new String[] {"TABLE"});

				while (resultSet.next()) {
					try (PreparedStatement preparedStatement =
							connection.prepareStatement(
								"TRUNCATE TABLE " +
									resultSet.getString("TABLE_NAME") +
										" CASCADE")) {

						preparedStatement.execute();
					}
				}

				DatabasePopulatorUtils.execute(
					new ResourceDatabasePopulator(
						new ClassPathResource("data.sql")),
					_postgreSQLDataSource);
			}
		}

		for (String cacheName : _cacheManager.getCacheNames()) {
			Cache cache = _cacheManager.getCache(cacheName);

			if (cache != null) {
				cache.invalidate();
			}
		}
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		super.beforeTestClass(testContext);

		if (!_isTestExecutionListenerEnabled()) {
			return;
		}

		Class<?> clazz = testContext.getTestClass();

		SQLResource[] sqlResources = clazz.getAnnotationsByType(
			SQLResource.class);

		if (sqlResources.length == 0) {
			return;
		}

		if (sqlResources.length > 1) {
			throw new IllegalArgumentException(
				"Only 1 SQLResource annotation allowed");
		}

		_prepareTables(clazz, sqlResources[0]);
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		if (!_isTestExecutionListenerEnabled()) {
			return;
		}

		SQLResource sqlResource = AnnotatedElementUtils.getMergedAnnotation(
			testContext.getTestMethod(), SQLResource.class);

		if (sqlResource != null) {
			_prepareTables(testContext.getTestClass(), sqlResource);
		}
	}

	private String _getResourcePath(SQLResource sqlResource) {
		if (StringUtils.startsWith(sqlResource.resourcePath(), "/")) {
			return sqlResource.resourcePath();
		}

		return "dependencies/" + sqlResource.resourcePath();
	}

	private boolean _isTestExecutionListenerEnabled() {
		if (_postgreSQLDataSource != null) {
			return true;
		}

		return false;
	}

	private void _prepareTables(Class<?> clazz, SQLResource sqlResource)
		throws Exception {

		if (!Objects.equals(sqlResource.resourcePath(), "")) {
			ClassPathResource classPathResource = new ClassPathResource(
				_getResourcePath(sqlResource), clazz);

			if (!classPathResource.isFile()) {
				DatabasePopulatorUtils.execute(
					new ResourceDatabasePopulator(classPathResource),
					_postgreSQLDataSource);

				return;
			}

			File file = classPathResource.getFile();

			String replaceSQLVariables =
				ResourceTemplateUtil.replaceSQLVariables(
					new String(
						Files.readAllBytes(file.toPath()),
						StandardCharsets.UTF_8));

			DatabasePopulatorUtils.execute(
				new ResourceDatabasePopulator(
					new ByteArrayResource(
						replaceSQLVariables.getBytes(StandardCharsets.UTF_8))),
				_postgreSQLDataSource);
		}
	}

	@Autowired
	private CacheManager _cacheManager;

	@Autowired(required = false)
	@Qualifier("postgreSQLDataSource")
	private DataSource _postgreSQLDataSource;

}