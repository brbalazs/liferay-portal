/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.configuration;

import com.google.cloud.NoCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;

import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.spring.annotation.ConditionalOnGoogleApplicationCredentials;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author Marcellus Tavares
 */
@Configuration
public class BigQueryConfiguration {

	@Bean
	@ConditionalOnGoogleApplicationCredentials(matchIfMissing = true)
	@Profile("dev")
	public BigQuery devBigQuery() {
		BigQueryOptions.Builder builder = BigQueryOptions.newBuilder();

		BigQueryOptions bigQueryOptions = builder.setCredentials(
			NoCredentials.getInstance()
		).setHost(
			"http://bigqueryemulator:9050"
		).setProjectId(
			"osbasahdev"
		).build();

		return bigQueryOptions.getService();
	}

	@Bean
	@ConditionalOnGoogleApplicationCredentials
	@Primary
	public BigQuery prodBigQuery() {
		String region = _environment.getProperty("gcloud.compute.region");

		if (region == null) {
			throw new IllegalStateException(
				"Please set the environment variable " +
					"\"gcloud.compute.region\"");
		}

		BigQueryOptions.Builder builder = BigQueryOptions.newBuilder();

		BigQueryOptions bigQueryOptions = builder.setLocation(
			region
		).build();

		return bigQueryOptions.getService();
	}

	@Bean
	@ConditionalOnGoogleApplicationCredentials(matchIfMissing = true)
	@Profile("test")
	public BigQuery testBigQuery() {
		BigQueryOptions.Builder builder = BigQueryOptions.newBuilder();

		BigQueryOptions bigQueryOptions = builder.setCredentials(
			NoCredentials.getInstance()
		).setHost(
			ServiceConstants.URL_BIG_QUERY
		).setProjectId(
			"osbasah"
		).build();

		return bigQueryOptions.getService();
	}

	@Autowired
	private Environment _environment;

}