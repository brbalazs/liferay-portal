/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.spring.http;

import com.liferay.osb.asah.common.spring.http.client.OSBAsahClientHttpRequestInterceptor;
import com.liferay.osb.asah.common.spring.http.client.OSBAsahHttpComponentsClientHttpRequestFactory;
import com.liferay.osb.asah.common.spring.http.client.OSBAsahResponseErrorHandler;

import java.net.UnknownHostException;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * @author Vishal Reddy
 * @author Rachael Koestartyo
 */
@Component
public class Http {

	public String exchange(
		String url, String path, HttpMethod httpMethod, Object body) {

		ResponseEntity<String> responseEntity = exchangeResponseEntity(
			url, path, httpMethod, body);

		return responseEntity.getBody();
	}

	public String exchange(
		String url, String path, HttpMethod httpMethod, Object body,
		HttpHeaders httpHeaders) {

		ResponseEntity<String> responseEntity = exchangeResponseEntity(
			url, path, httpMethod, body, httpHeaders);

		return responseEntity.getBody();
	}

	public String exchangeIfUp(
		String url, String path, HttpMethod httpMethod, Object body) {

		try {
			return exchange(url, path, httpMethod, body);
		}
		catch (ResourceAccessException resourceAccessException) {
			Throwable throwable = resourceAccessException.getCause();

			if (!(throwable instanceof UnknownHostException)) {
				throw resourceAccessException;
			}

			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to reach " + url + ": " + resourceAccessException);
			}
		}

		return null;
	}

	public ResponseEntity<String> exchangeResponseEntity(
		String url, String path, HttpMethod httpMethod, Object body) {

		RestTemplate restTemplate = new RestTemplate();

		_configureRestTemplate(restTemplate);

		return _exchangeResponseEntity(
			restTemplate, url, path, httpMethod, _getHttpEntity(body, null));
	}

	public ResponseEntity<String> exchangeResponseEntity(
		String url, String path, HttpMethod httpMethod, Object body,
		BasicAuthorizationInterceptor basicAuthorizationInterceptor) {

		RestTemplate restTemplate = new RestTemplate();

		_configureRestTemplate(restTemplate);

		List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors =
			restTemplate.getInterceptors();

		clientHttpRequestInterceptors.add(basicAuthorizationInterceptor);

		return _exchangeResponseEntity(
			restTemplate, url, path, httpMethod, _getHttpEntity(body, null));
	}

	public ResponseEntity<String> exchangeResponseEntity(
		String url, String path, HttpMethod httpMethod, Object body,
		HttpHeaders httpHeaders) {

		RestTemplate restTemplate = new RestTemplate();

		_configureRestTemplate(restTemplate);

		return _exchangeResponseEntity(
			restTemplate, url, path, httpMethod,
			_getHttpEntity(body, httpHeaders));
	}

	private void _configureRestTemplate(RestTemplate restTemplate) {
		List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors =
			restTemplate.getInterceptors();

		clientHttpRequestInterceptors.add(
			new OSBAsahClientHttpRequestInterceptor());

		restTemplate.setErrorHandler(new OSBAsahResponseErrorHandler());
		restTemplate.setRequestFactory(
			new OSBAsahHttpComponentsClientHttpRequestFactory());
	}

	private ResponseEntity<String> _exchangeResponseEntity(
		RestTemplate restTemplate, String url, String path,
		HttpMethod httpMethod, HttpEntity<?> httpEntity) {

		AtomicReference<ResponseEntity<String>> responseEntityAtomicReference =
			new AtomicReference<>();

		String fullURL;

		if (StringUtils.isNotEmpty(path)) {
			fullURL = url.concat(path);
		}
		else {
			fullURL = url;
		}

		_retryTemplate.execute(
			retryContext -> {
				responseEntityAtomicReference.set(
					restTemplate.exchange(
						fullURL, httpMethod, httpEntity, String.class));

				return null;
			});

		return responseEntityAtomicReference.get();
	}

	private HttpEntity<?> _getHttpEntity(Object body, HttpHeaders httpHeaders) {
		if ((body == null) || StringUtils.isEmpty(String.valueOf(body))) {
			return new HttpEntity<>(httpHeaders);
		}

		return new HttpEntity<>(String.valueOf(body), httpHeaders);
	}

	private static final Log _log = LogFactory.getLog(Http.class);

	@Autowired
	private RetryTemplate _retryTemplate;

}