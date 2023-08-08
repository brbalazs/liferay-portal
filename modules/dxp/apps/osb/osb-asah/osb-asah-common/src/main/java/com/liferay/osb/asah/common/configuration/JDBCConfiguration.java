/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration;

import com.liferay.osb.asah.common.constants.CredentialConstants;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.postgresql.PostgreSQLDataSource;
import com.liferay.osb.asah.common.postgresql.converter.JSONArrayToPGobjectConverter;
import com.liferay.osb.asah.common.postgresql.converter.JSONObjectToPGobjectConverter;
import com.liferay.osb.asah.common.postgresql.converter.PGobjectToJSONArrayConverter;
import com.liferay.osb.asah.common.postgresql.converter.PGobjectToJSONObjectConverter;

import java.util.Arrays;

import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.dialect.PostgresDialect;
import org.springframework.data.relational.core.mapping.NamingStrategy;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.security.util.InMemoryResource;
import org.springframework.transaction.TransactionManager;

/**
 * @author Inácio Nery
 */
@Configuration
@EnableJdbcRepositories(
	basePackages = "com.liferay.osb.asah.common.repository",
	excludeFilters = @ComponentScan.Filter(Primary.class),
	namedQueriesLocation = "classpath*:com/liferay/osb/asah/common/repository/*-sql.xml"
)
public class JDBCConfiguration extends AbstractJdbcConfiguration {

	@Bean
	@Override
	public JdbcCustomConversions jdbcCustomConversions() {
		return new JdbcCustomConversions(
			Arrays.asList(
				new JSONArrayToPGobjectConverter(),
				new JSONObjectToPGobjectConverter(),
				new PGobjectToJSONObjectConverter(),
				new PGobjectToJSONArrayConverter()));
	}

	@Bean
	@Override
	public Dialect jdbcDialect(
		NamedParameterJdbcOperations namedParameterJdbcOperations) {

		return PostgresDialect.INSTANCE;
	}

	@Bean
	public NamedParameterJdbcOperations namedParameterJdbcOperations(
		DataSource dataSource) {

		return new NamedParameterJdbcTemplate(new JdbcTemplate(dataSource));
	}

	@Bean
	public NamingStrategy namingStrategy() {
		return new NamingStrategy() {

			@Override
			public String getColumnName(
				RelationalPersistentProperty relationalPersistentProperty) {

				String columnName = relationalPersistentProperty.getName();

				columnName = columnName.replace("_", "");

				return columnName.toLowerCase();
			}

			@Override
			public String getTableName(Class<?> clazz) {
				String tableName = clazz.getSimpleName();

				tableName = tableName.replace("_", "");

				return tableName.toLowerCase();
			}

		};
	}

	@Bean("postgreSQLDataSource")
	@Primary
	@Profile({"dev", "prod"})
	public DataSource postgreSQLDataSource() {
		return new PostgreSQLDataSource(
			_hikariConnectionTimeout, _hikariIdleTimeout,
			_hikariLeakDetectionThreshold, _hikariMaxLifetime,
			_hikariMaximumPoolSize, _hikariMinimumIdleSize);
	}

	@Bean("postgreSQLDataSource")
	@Primary
	@Profile("test")
	public DataSource testPostgreSQLDataSource() {
		PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();

		pgSimpleDataSource.setCurrentSchema("test");
		pgSimpleDataSource.setDatabaseName(CredentialConstants.POSTGRESQL_DB);
		pgSimpleDataSource.setPassword(CredentialConstants.POSTGRESQL_PASSWORD);
		pgSimpleDataSource.setPortNumber(5432);
		pgSimpleDataSource.setServerName(ServiceConstants.POSTGRESQL_SERVER_IP);
		pgSimpleDataSource.setUser(CredentialConstants.POSTGRESQL_USER);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new InMemoryResource(
					"SET TIME ZONE 'UTC'; CREATE SCHEMA IF NOT EXISTS test")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new ClassPathResource("functions.sql")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new ClassPathResource("tables.sql"),
				new ClassPathResource("tables_global.sql")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				new ClassPathResource("sequences.sql")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(
				true, true, null, new ClassPathResource("constraints.sql")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(new ClassPathResource("indexes.sql")),
			pgSimpleDataSource);

		DatabasePopulatorUtils.execute(
			new ResourceDatabasePopulator(new ClassPathResource("data.sql")),
			pgSimpleDataSource);

		return pgSimpleDataSource;
	}

	@Bean
	public TransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	@Value("${spring.datasource.hikari.connection-timeout:240}")
	private int _hikariConnectionTimeout;

	@Value("${spring.datasource.hikari.idle-timeout:60}")
	private int _hikariIdleTimeout;

	@Value("${spring.datasource.hikari.leak-detection-threshold:60}")
	private int _hikariLeakDetectionThreshold;

	@Value("${spring.datasource.hikari.maximum-pool-size:10}")
	private int _hikariMaximumPoolSize;

	@Value("${spring.datasource.hikari.max-lifetime:360}")
	private int _hikariMaxLifetime;

	@Value("${spring.datasource.hikari.minimum-idle-size:1}")
	private int _hikariMinimumIdleSize;

}