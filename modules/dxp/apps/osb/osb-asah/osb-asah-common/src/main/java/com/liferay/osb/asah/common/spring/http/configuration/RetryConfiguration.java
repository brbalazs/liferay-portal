/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.configuration;

import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy;
import org.springframework.retry.policy.NeverRetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.HttpClientErrorException;

/**
 * @author André Miranda
 * @author Rachael Koestartyo
 */
@Configuration
public class RetryConfiguration {

	@Bean
	public RetryTemplate retryTemplate() {
		RetryPolicy retryPolicy = new ExceptionClassifierRetryPolicy() {
			{
				setExceptionClassifier(
					(Classifier<Throwable, RetryPolicy>)classifier -> {
						if (classifier instanceof HttpClientErrorException) {
							HttpClientErrorException httpClientErrorException =
								(HttpClientErrorException)classifier;

							if ((httpClientErrorException.getStatusCode() ==
									HttpStatus.FORBIDDEN) ||
								(httpClientErrorException.getStatusCode() ==
									HttpStatus.UNAUTHORIZED)) {

								return _neverRetryPolicy;
							}
						}

						return _simplyRetryPolicy;
					});
			}
		};

		return new RetryTemplate() {
			{
				setRetryPolicy(retryPolicy);
				setThrowLastExceptionOnExhausted(true);
			}
		};
	}

	private static final RetryPolicy _neverRetryPolicy = new NeverRetryPolicy();
	private static final RetryPolicy _simplyRetryPolicy =
		new SimpleRetryPolicy();

}