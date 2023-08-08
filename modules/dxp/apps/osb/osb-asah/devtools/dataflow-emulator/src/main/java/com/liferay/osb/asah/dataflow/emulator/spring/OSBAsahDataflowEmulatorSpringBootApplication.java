/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.emulator.spring;

import com.liferay.osb.asah.common.spring.OSBAsahSpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

/**
 * @author Marcellus Tavares
 */
@ComponentScan("com.liferay.osb.asah.dataflow.emulator")
@EnableJdbcRepositories(basePackages = "com.liferay.osb.asah.dataflow.emulator")
public class OSBAsahDataflowEmulatorSpringBootApplication
	extends OSBAsahSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			OSBAsahDataflowEmulatorSpringBootApplication.class, args);
	}

}