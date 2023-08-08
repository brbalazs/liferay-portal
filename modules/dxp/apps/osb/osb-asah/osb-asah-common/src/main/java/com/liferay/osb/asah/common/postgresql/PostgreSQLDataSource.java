/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.postgresql;

import com.liferay.osb.asah.common.constants.CredentialConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * @author Inácio Nery
 */
public class PostgreSQLDataSource extends AbstractRoutingDataSource {

	public PostgreSQLDataSource(
		int hikariConnectionTimeout, int hikariIdleTimeout,
		int hikariLeakDetectionThreshold, int hikariMaxLifetime,
		int hikariMaximumPoolSize, int hikariMinimumIdleSize) {

		_hikariConnectionTimeout = hikariConnectionTimeout;
		_hikariIdleTimeout = hikariIdleTimeout;
		_hikariLeakDetectionThreshold = hikariLeakDetectionThreshold;
		_hikariMaxLifetime = hikariMaxLifetime;
		_hikariMaximumPoolSize = hikariMaximumPoolSize;
		_hikariMinimumIdleSize = hikariMinimumIdleSize;
	}

	@Override
	public void afterPropertiesSet() {
	}

	@Override
	protected Object determineCurrentLookupKey() {
		return StringUtils.replace(
			ProjectIdThreadLocal.getProjectId(), "-", "_");
	}

	@Override
	protected DataSource determineTargetDataSource() {
		return _resolvedDataSources.computeIfAbsent(
			determineCurrentLookupKey(), this::resolveSpecifiedDataSource);
	}

	@Override
	protected DataSource resolveSpecifiedDataSource(Object dataSource)
		throws IllegalArgumentException {

		if (dataSource == null) {
			dataSource = "global";
		}

		if (dataSource instanceof DataSource) {
			return (DataSource)dataSource;
		}

		HikariDataSource hikariDataSource = new HikariDataSource();

		hikariDataSource.setConnectionInitSql("SET TIME ZONE 'UTC';");
		hikariDataSource.setConnectionTimeout(
			TimeUnit.SECONDS.toMillis(_hikariConnectionTimeout));
		hikariDataSource.setIdleTimeout(
			TimeUnit.SECONDS.toMillis(_hikariIdleTimeout));
		hikariDataSource.setJdbcUrl(_buildJdbcUrl(dataSource));
		hikariDataSource.setLeakDetectionThreshold(
			TimeUnit.SECONDS.toMillis(_hikariLeakDetectionThreshold));
		hikariDataSource.setMaximumPoolSize(_hikariMaximumPoolSize);
		hikariDataSource.setMaxLifetime(
			TimeUnit.SECONDS.toMillis(_hikariMaxLifetime));
		hikariDataSource.setMinimumIdle(_hikariMinimumIdleSize);
		hikariDataSource.setPassword(CredentialConstants.POSTGRESQL_PASSWORD);
		hikariDataSource.setUsername(CredentialConstants.POSTGRESQL_USER);

		if (Objects.equals(dataSource, "global")) {
			hikariDataSource.setMaximumPoolSize(2);
		}

		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"DataSource %s created for %s", hikariDataSource,
					dataSource));
		}

		return hikariDataSource;
	}

	private String _buildJdbcUrl(Object dataSource) {
		StringBuilder sb = new StringBuilder("jdbc:postgresql://");

		String[] transportAddressParts = StringUtils.split(
			ServiceConstants.POSTGRESQL_SERVER_IP, ':');

		sb.append(transportAddressParts[0]);

		sb.append(":");

		int port = 5432;

		if (transportAddressParts.length == 2) {
			port = Integer.parseInt(transportAddressParts[1]);
		}

		sb.append(port);
		sb.append("/");
		sb.append(CredentialConstants.POSTGRESQL_DB);
		sb.append("?currentSchema=");
		sb.append(dataSource);

		return sb.toString();
	}

	private static final Log _log = LogFactory.getLog(
		PostgreSQLDataSource.class);

	private final int _hikariConnectionTimeout;
	private final int _hikariIdleTimeout;
	private final int _hikariLeakDetectionThreshold;
	private final int _hikariMaximumPoolSize;
	private final int _hikariMaxLifetime;
	private final int _hikariMinimumIdleSize;
	private final Map<Object, DataSource> _resolvedDataSources =
		new ConcurrentHashMap<>();

}