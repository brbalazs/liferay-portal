/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.repository.BQIndividualRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.publisher.OSBAsahPublisherSpringTestContext;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * @author Inácio Nery
 */
public class IdentityRestControllerTest
	implements OSBAsahPublisherSpringTestContext {

	@BeforeEach
	public void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testAddIndividual() {
		String emailAddress = StringUtils.lowerCase(
			RandomTestUtil.randomEmailAddress());

		_exchange(
			JSONUtil.put(
				"channelId", "1"
			).put(
				"dataSourceId", "345085929068798696"
			).put(
				"identity",
				JSONUtil.put(
					"email", emailAddress
				).put(
					"name", RandomTestUtil.randomFullName()
				)
			).put(
				"userId", RandomTestUtil.randomUUID()
			).toString());

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture()
		);

		JSONObject messageJSONObject = new JSONObject(
			argumentCaptor.getValue());

		Assertions.assertEquals(
			"345085929068798696", messageJSONObject.getString("dataSourceId"));
		Assertions.assertEquals(
			DigestUtils.sha256Hex(emailAddress),
			messageJSONObject.getString("individualId"));
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testIndividualId() {
		String individualId =
			"47ff64395860b1d498241d907069f649b98c198a95b3ba5303b87094058590c1";

		_exchange(
			JSONUtil.put(
				"channelId", "1"
			).put(
				"dataSourceId", "345085929068798696"
			).put(
				"emailAddressHashed", individualId
			).put(
				"userId", RandomTestUtil.randomUUID()
			).toString());

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture()
		);

		JSONObject messageJSONObject = new JSONObject(
			argumentCaptor.getValue());

		Assertions.assertEquals(
			"345085929068798696", messageJSONObject.getString("dataSourceId"));
		Assertions.assertEquals(
			individualId, messageJSONObject.getString("individualId"));
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testUpdateIndividual1() {
		_testUpdateIndividual(
			"345085929068798696", RandomTestUtil.randomUUID());
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQIndividualRepository.class,
		resourcePath = "osbasahfaroinfo/individuals.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testUpdateIndividual2() {
		_testUpdateIndividual(
			"345085929068798697", RandomTestUtil.randomUUID());
	}

	private <T> ResponseEntity<String> _exchange(T body) {
		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.add(HttpHeaders.COOKIE, "ANONYMOUS_USER_ID=111111");
		httpHeaders.add(HttpHeaders.USER_AGENT, "Google Chrome");
		httpHeaders.add("X-Forwarded-For", "localhost");
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		HttpEntity<T> requestEntity = new HttpEntity<>(body, httpHeaders);

		return _testRestTemplate.exchange(
			"/identity", HttpMethod.POST, requestEntity, String.class);
	}

	private void _testUpdateIndividual(String dataSourceId, String userId) {
		_exchange(
			JSONUtil.put(
				"channelId", "1"
			).put(
				"dataSourceId", dataSourceId
			).put(
				"identity",
				JSONUtil.put(
					"email", "nina.simone@liferay.com"
				).put(
					"name", "Nina Simone"
				)
			).put(
				"userId", userId
			).toString());

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture()
		);

		JSONObject messageJSONObject = new JSONObject(
			argumentCaptor.getValue());

		Assertions.assertEquals(
			dataSourceId, messageJSONObject.getString("dataSourceId"));
		Assertions.assertEquals(
			DigestUtils.sha256Hex("nina.simone@liferay.com"),
			messageJSONObject.getString("individualId"));
	}

	@MockBean
	private MessageBus _messageBus;

	@Autowired
	private TestRestTemplate _testRestTemplate;

}