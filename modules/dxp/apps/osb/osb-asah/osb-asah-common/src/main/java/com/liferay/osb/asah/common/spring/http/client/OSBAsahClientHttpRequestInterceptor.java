/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http.client;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author Shinn Lok
 */
public class OSBAsahClientHttpRequestInterceptor
	implements ClientHttpRequestInterceptor {

	@Override
	public ClientHttpResponse intercept(
			HttpRequest httpRequest, byte[] body,
			ClientHttpRequestExecution clientHttpRequestExecution)
		throws IOException {

		HttpHeaders httpHeaders = httpRequest.getHeaders();

		httpHeaders.putIfAbsent(
			HeaderConstants.PROJECT_ID,
			Collections.singletonList(ProjectIdThreadLocal.getProjectId()));
		httpHeaders.putIfAbsent(
			HttpHeaders.USER_AGENT,
			Collections.singletonList("LiferayAnalyticsCloud"));
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		if (_log.isDebugEnabled()) {
			JSONObject requestJSONObject = JSONUtil.put(
				"body", new String(body, StandardCharsets.UTF_8)
			).put(
				"headers", httpRequest.getHeaders()
			).put(
				"method", httpRequest.getMethod()
			).put(
				"uri", httpRequest.getURI()
			);

			_log.debug("Request " + requestJSONObject);
		}

		long startTimeMillis = System.currentTimeMillis();

		ClientHttpResponse clientHttpResponse =
			clientHttpRequestExecution.execute(httpRequest, body);

		if (_log.isDebugEnabled()) {
			JSONObject responseJSONObject = JSONUtil.put(
				"headers", clientHttpResponse.getHeaders()
			).put(
				"requestUri", httpRequest.getURI()
			).put(
				"responseTime", System.currentTimeMillis() - startTimeMillis
			).put(
				"statusCode", clientHttpResponse.getStatusCode()
			).put(
				"statusText", clientHttpResponse.getStatusText()
			);

			_log.debug("Response " + responseJSONObject);
		}

		return clientHttpResponse;
	}

	private static final Log _log = LogFactory.getLog(
		OSBAsahClientHttpRequestInterceptor.class);

}