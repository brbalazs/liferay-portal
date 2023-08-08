/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring;

import org.springframework.boot.actuate.autoconfigure.jdbc.DataSourceHealthContributorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author Eddie Olson
 * @author Rachael Koestartyo
 */
@ComponentScan("com.liferay.osb.asah.common")
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication(
	exclude = {
		DataSourceHealthContributorAutoConfiguration.class,
		JooqAutoConfiguration.class,
		ManagementWebSecurityAutoConfiguration.class,
		RedisRepositoriesAutoConfiguration.class,
		SecurityAutoConfiguration.class,
		UserDetailsServiceAutoConfiguration.class
	}
)
public class OSBAsahSpringBootApplication {
}