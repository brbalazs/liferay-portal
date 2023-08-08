/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.publisher.OSBAsahPublisherSpringTestContext;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import org.skyscreamer.jsonassert.JSONAssert;

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
public class DXPEntitiesRestControllerTest
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
	public void testAddAndUpdateContactDXPEntityWithoutDates()
		throws Exception {

		Date date = DateUtil.newDate();

		_exchange(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_message_2.json", this));

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.eq(
				Collections.singletonMap(
					"projectId", ProjectIdThreadLocal.getProjectId()))
		);

		for (String message : argumentCaptor.getAllValues()) {
			JSONArray jsonArray = new JSONArray(message);

			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject messageJSONObject = jsonArray.getJSONObject(i);

				JSONObject objectJSONObject = messageJSONObject.getJSONObject(
					"object");

				Assertions.assertThat(
					objectJSONObject.getLong("createDate")
				).isGreaterThanOrEqualTo(
					date.getTime()
				);

				Assertions.assertThat(
					objectJSONObject.getLong("modifiedDate")
				).isGreaterThanOrEqualTo(
					date.getTime()
				);
			}
		}
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testAddDXPEntities() throws Exception {
		ResponseEntity<String> responseEntity = _exchange(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_message_1.json", this));

		Assertions.assertThat(
			responseEntity.getBody()
		).isEqualTo(
			"[]"
		);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.eq(
				Collections.singletonMap(
					"projectId", ProjectIdThreadLocal.getProjectId()))
		);

		List<String> messages = argumentCaptor.getAllValues();

		org.junit.jupiter.api.Assertions.assertEquals(
			1, messages.size(), messages.toString());

		JSONAssert.assertEquals(
			ResourceUtil.readResourceToString(
				"dependencies/dxp_entities_message.json", this),
			new JSONArray(messages.get(0)), false);
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
			"/dxp-entities", HttpMethod.POST, requestEntity, String.class);
	}

	@MockBean
	private MessageBus _messageBus;

	@Autowired
	private TestRestTemplate _testRestTemplate;

}