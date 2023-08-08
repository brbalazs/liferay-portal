/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.configuration;

import com.liferay.osb.asah.common.configuration.JDBCConfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * @author Marcellus Tavares
 */
@EnableJdbcRepositories(
	basePackages = {
		"com.liferay.osb.asah.common.repository",
		"com.liferay.osb.asah.test.util.repository"
	},
	namedQueriesLocation = "classpath*:com/liferay/osb/asah/common/repository/*-sql.xml"
)
@TestConfiguration
public class JDBCTestConfiguration extends JDBCConfiguration {
}