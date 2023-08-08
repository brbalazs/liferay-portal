/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration;

import javax.sql.DataSource;

import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.RenderOptionalKeyword;
import org.jooq.conf.RenderQuotedNames;
import org.jooq.conf.Settings;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
 * @author Inácio Nery
 */
@Configuration
public class JooqConfiguration {

	@Bean
	@Primary
	public org.jooq.Configuration defaultConfiguration(
		ConnectionProvider connectionProvider) {

		DefaultConfiguration defaultConfiguration = new DefaultConfiguration();

		defaultConfiguration.set(connectionProvider);
		defaultConfiguration.setSQLDialect(SQLDialect.POSTGRES);

		Settings settings = defaultConfiguration.settings();

		settings.setRenderOptionalAsKeywordForFieldAliases(
			RenderOptionalKeyword.ON);
		settings.setRenderOptionalAsKeywordForTableAliases(
			RenderOptionalKeyword.ON);
		settings.setRenderQuotedNames(
			RenderQuotedNames.EXPLICIT_DEFAULT_UNQUOTED);

		return defaultConfiguration;
	}

	@Bean
	@Primary
	public ConnectionProvider defaultConnectionProvider(DataSource dataSource) {
		return new DataSourceConnectionProvider(
			new TransactionAwareDataSourceProxy(dataSource));
	}

	@Bean
	@Primary
	public DSLContext defaultDSLContext(org.jooq.Configuration configuration) {
		return new DefaultDSLContext(configuration);
	}

}