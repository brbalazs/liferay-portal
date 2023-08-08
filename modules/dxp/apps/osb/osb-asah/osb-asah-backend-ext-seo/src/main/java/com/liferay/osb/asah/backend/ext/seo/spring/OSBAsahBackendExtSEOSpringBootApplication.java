/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.ext.seo.spring;

import com.liferay.osb.asah.common.spring.OSBAsahSpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author David Arques
 */
@ComponentScan("com.liferay.osb.asah.backend.ext.seo")
public class OSBAsahBackendExtSEOSpringBootApplication
	extends OSBAsahSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(
			OSBAsahBackendExtSEOSpringBootApplication.class, args);
	}

}