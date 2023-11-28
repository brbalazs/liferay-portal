/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.spring;

import com.liferay.osb.asah.common.spring.OSBAsahSpringBootApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.filter.ForwardedHeaderFilter;

/**
 * @author Vishal Reddy
 */
@ComponentScan("com.liferay.osb.asah.backend")
public class OSBAsahBackendSpringBootApplication
	extends OSBAsahSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(OSBAsahBackendSpringBootApplication.class, args);
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer
			propertySourcesPlaceholderConfigurer()
		throws Exception {

		PropertySourcesPlaceholderConfigurer
			propertySourcesPlaceholderConfigurer =
				new PropertySourcesPlaceholderConfigurer();

		PathMatchingResourcePatternResolver
			pathMatchingResourcePatternResolver =
				new PathMatchingResourcePatternResolver();

		propertySourcesPlaceholderConfigurer.setLocations(
			pathMatchingResourcePatternResolver.getResources(
				"classpath*:/application.properties"));

		return propertySourcesPlaceholderConfigurer;
	}

	@Bean
	public FilterRegistrationBean<ForwardedHeaderFilter>
		forwardedHeaderFilter() {

		FilterRegistrationBean<ForwardedHeaderFilter> bean =
			new FilterRegistrationBean<>();

		bean.setFilter(new ForwardedHeaderFilter());

		return bean;
	}

}