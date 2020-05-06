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

import com.liferay.osb.faro.engine.client.FaroClientHttpResponse;
import com.liferay.petra.string.StringPool;

import java.io.IOException;

import org.apache.http.HttpStatus;

import org.springframework.cache.Cache;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

/**
 * @author Shinn Lok
 */
public class CacheClientHttpRequestInterceptor
	implements ClientHttpRequestInterceptor {

	public CacheClientHttpRequestInterceptor(Cache cache) {
		_cache = cache;
	}

	@Override
	public ClientHttpResponse intercept(
			HttpRequest httpRequest, byte[] bytes,
			ClientHttpRequestExecution clientHttpRequestExecution)
		throws IOException {

		HttpMethod httpMethod = httpRequest.getMethod();

		if (httpMethod.equals(HttpMethod.PATCH) ||
			httpMethod.equals(HttpMethod.POST) ||
			httpMethod.equals(HttpMethod.PUT) ||
			httpMethod.equals(HttpMethod.DELETE)) {

			_cache.clear();
		}

		if (bytes.length > 0) {
			return clientHttpRequestExecution.execute(httpRequest, bytes);
		}

		String key = getKey(httpRequest);

		FaroClientHttpResponse faroClientHttpResponse =
			getFaroClientHttpResponse(key);

		if (faroClientHttpResponse != null) {
			return faroClientHttpResponse;
		}

		ClientHttpResponse clientHttpResponse =
			clientHttpRequestExecution.execute(httpRequest, bytes);

		if (clientHttpResponse.getRawStatusCode() != HttpStatus.SC_OK) {
			return clientHttpResponse;
		}

		try {
			faroClientHttpResponse = new FaroClientHttpResponse(
				clientHttpResponse);

			_cache.put(key, faroClientHttpResponse);

			return faroClientHttpResponse;
		}
		catch (Exception e) {
			throw new IOException(e);
		}
	}

	protected FaroClientHttpResponse getFaroClientHttpResponse(String key) {
		Cache.ValueWrapper valueWrapper = _cache.get(key);

		if (valueWrapper == null) {
			return null;
		}

		return (FaroClientHttpResponse)valueWrapper.get();
	}

	protected String getKey(HttpRequest httpRequest) {
		HttpMethod httpMethod = httpRequest.getMethod();

		return httpMethod.name() + StringPool.COLON + httpRequest.getURI();
	}

	private final Cache _cache;

}