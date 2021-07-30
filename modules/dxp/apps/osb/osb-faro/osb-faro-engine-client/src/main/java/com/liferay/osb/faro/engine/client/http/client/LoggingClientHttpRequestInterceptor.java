/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.faro.engine.client.http.client;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 * @author Inácio Nery
 */
public class LoggingClientHttpRequestInterceptor
	implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(
			HttpRequest httpRequest, byte[] bytes,
			ClientHttpRequestExecution clientHttpRequestExecution)
		throws IOException {

		if (_log.isDebugEnabled()) {
			_log.debug(String.format("Request URI: %s", httpRequest.getURI()));
			_log.debug(
				String.format("Request method: %s", httpRequest.getMethod()));
			_log.debug(
				String.format("Request headers: %s", httpRequest.getHeaders()));
			_log.debug(
				String.format(
					"Request body: %s",
					new String(bytes, StandardCharsets.UTF_8)));
		}

		ClientHttpResponse response = clientHttpRequestExecution.execute(
			httpRequest, bytes);

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"Response status code: %s", response.getStatusCode()));
			_log.debug(
				String.format(
					"Response status text: %s", response.getStatusText()));
			_log.debug(
				String.format("Response headers: %s", response.getHeaders()));
			_log.debug(
				String.format(
					"Response body: %s",
					StreamUtils.copyToString(
						response.getBody(), StandardCharsets.UTF_8)));
		}

		return response;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LoggingClientHttpRequestInterceptor.class);

}