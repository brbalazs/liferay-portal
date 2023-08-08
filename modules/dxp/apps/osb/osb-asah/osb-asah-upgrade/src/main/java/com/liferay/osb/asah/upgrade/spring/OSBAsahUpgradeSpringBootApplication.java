/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Shinn Lok
 */
@ComponentScan(
	basePackages = {
		"com.liferay.osb.asah.common.bigquery",
		"com.liferay.osb.asah.common.configuration",
		"com.liferay.osb.asah.common.date.dog",
		"com.liferay.osb.asah.common.dog", "com.liferay.osb.asah.common.dxp",
		"com.liferay.osb.asah.common.elasticsearch",
		"com.liferay.osb.asah.common.faro.info",
		"com.liferay.osb.asah.common.http",
		"com.liferay.osb.asah.common.messaging",
		"com.liferay.osb.asah.common.postgresql",
		"com.liferay.osb.asah.common.repository",
		"com.liferay.osb.asah.common.security",
		"com.liferay.osb.asah.common.spring.cache",
		"com.liferay.osb.asah.common.spring.http",
		"com.liferay.osb.asah.common.wedeploy", "com.liferay.osb.asah.upgrade"
	}
)
@SpringBootApplication(
	exclude = {
		DataSourceHealthContributorAutoConfiguration.class,
		JooqAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		MetricsAutoConfiguration.class,
		RedisRepositoriesAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
	}
)
public class OSBAsahUpgradeSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OSBAsahUpgradeSpringBootApplication.class, args);
	}

}