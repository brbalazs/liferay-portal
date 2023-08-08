/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import com.liferay.osb.asah.common.util.ResourceTemplateUtil;
import com.liferay.osb.asah.publisher.OSBAsahPublisherSpringTestContext;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import org.assertj.core.api.Assertions;

import org.json.JSONArray;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

/**
 * @author Inácio Nery
 */
public class AnalyticsEventsRestControllerTest
	implements OSBAsahPublisherSpringTestContext {

	@BeforeEach
	public void setUp() {
		JacksonTester.initFields(this, new ObjectMapper());
	}

	@Test
	public void testGetStatusCode400() throws Exception {
		ResponseEntity<String> responseEntity = _exchange(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_events_message_invalid_syntax.json",
				this));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(400)
		);
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPushAnalyticsEventsMessage() throws Exception {
		String newDateString = DateUtil.newDateString();

		String body = ResourceTemplateUtil.replaceVariables(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_events_message_1.json", this),
			newDateString);

		ResponseEntity<String> responseEntity = _exchange(body);

		Assertions.assertThat(
			responseEntity.getBody()
		).isEqualTo(
			"[]"
		);

		ArgumentCaptor<String> messageArgumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), messageArgumentCaptor.capture(),
			ArgumentMatchers.any()
		);

		_verifyCapturedMessages(
			messageArgumentCaptor.getAllValues(), newDateString,
			"dependencies/analytics_events_message_1_events.json");
	}

	@Test
	public void testPushAnalyticsEventsMessageIfDuplicate() throws Exception {
		String body = ResourceTemplateUtil.replaceVariables(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_events_message_with_id.json", this));

		_exchange(body);

		for (int i = 0; i <= 2; i++) {
			ResponseEntity<String> responseEntity = _exchange(body);

			Assertions.assertThat(
				responseEntity.getBody()
			).startsWith(
				"Duplicate message"
			);
		}
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPushAnalyticsEventsMessageIfDuplicateEvents()
		throws Exception {

		ResponseEntity<String> responseEntity = _exchange(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies" +
						"/analytics_events_message_duplicated_events_1.json",
					this)));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);

		String newDateString = DateUtil.newDateString();

		responseEntity = _exchange(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies" +
						"/analytics_events_message_duplicated_events_2.json",
					this),
				newDateString));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(1)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.any()
		);

		_verifyCapturedMessages(
			argumentCaptor.getAllValues(), newDateString,
			"dependencies/analytics_events_duplicated_events_removed.json");
	}

	@Test
	public void testPushAnalyticsEventsMessageIfEmptyEvents() throws Exception {
		ResponseEntity<String> responseEntity = _exchange(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies/analytics_events_message_empty_events.json",
					this)));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);

		Mockito.verify(
			_messageBus, Mockito.never()
		).sendMessage(
			ArgumentMatchers.any(), ArgumentMatchers.any()
		);
	}

	@Test
	public void testPushAnalyticsEventsMessageIfInvalidContext() {
		ResponseEntity<String> responseEntity = _exchange(
			JSONUtil.put(
				"context",
				JSONUtil.put("description", RandomStringUtils.random(2049))
			).put(
				"dataSourceId", "370975939087274525"
			).put(
				"events",
				JSONUtil.put(
					JSONUtil.put(
						"applicationId", "Layout"
					).put(
						"eventDate", "2017-11-07T09:34:45.345Z"
					).put(
						"eventId", "view"
					).put(
						"properties",
						JSONUtil.put("referrer", "http://www.google.com")
					))
			).put(
				"protocolVersion", "1.0"
			).put(
				"userId", "id-i4xbdldy1af"
			).toString());

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(400)
		);
	}

	@Test
	public void testPushAnalyticsEventsMessageIfInvalidDataSourceId()
		throws Exception {

		ResponseEntity<String> responseEntity = _exchange(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies" +
						"/analytics_events_message_invalid_data_source_id.json",
					this)));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(400)
		);
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPushAnalyticsEventsMessageIfInvalidEvents()
		throws Exception {

		String newDateString = DateUtil.newDateString();

		ResponseEntity<String> responseEntity = _exchange(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					"dependencies/analytics_events_message_invalid_events.json",
					this),
				newDateString));

		Assertions.assertThat(
			responseEntity.getStatusCode()
		).isEqualTo(
			HttpStatus.valueOf(200)
		);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(3)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.any()
		);

		_verifyCapturedMessages(
			argumentCaptor.getAllValues(), newDateString,
			"dependencies/analytics_events_message_invalid_events_removed." +
				"json");
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPushAnalyticsEventsMessageIfInvalidXForwardedFor()
		throws Exception {

		String newDateString = DateUtil.newDateString();

		String body = ResourceTemplateUtil.replaceVariables(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_events_message_2.json", this),
			newDateString);

		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		ResponseEntity<String> responseEntity = _exchange(body, httpHeaders);

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
			ArgumentMatchers.any()
		);

		_verifyCapturedMessages(
			argumentCaptor.getAllValues(), newDateString,
			"dependencies/analytics_events_message_2_events.json");
	}

	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPushAnalyticsEventsMessageIfMoreThanOneEvent()
		throws Exception {

		String newDateString = DateUtil.newDateString();

		String body = ResourceTemplateUtil.replaceVariables(
			ResourceUtil.readResourceToString(
				"dependencies/analytics_events_message_4.json", this),
			newDateString);

		_exchange(body);

		ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(
			String.class);

		Mockito.verify(
			_messageBus, Mockito.times(2)
		).sendMessage(
			ArgumentMatchers.any(), argumentCaptor.capture(),
			ArgumentMatchers.any()
		);

		_verifyCapturedMessages(
			argumentCaptor.getAllValues(), newDateString,
			"dependencies/analytics_events_message_4_events.json");
	}

	private <T> ResponseEntity<String> _exchange(T body) {
		HttpHeaders httpHeaders = new HttpHeaders();

		httpHeaders.add(HeaderConstants.PROJECT_ID, "test");
		httpHeaders.add(HttpHeaders.COOKIE, "ANONYMOUS_USER_ID=111111");
		httpHeaders.add(HttpHeaders.USER_AGENT, "Google Chrome");
		httpHeaders.add("X-Forwarded-For", "localhost");
		httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

		return _exchange(body, httpHeaders);
	}

	private <T> ResponseEntity<String> _exchange(
		T body, HttpHeaders httpHeaders) {

		HttpEntity<T> requestEntity = new HttpEntity<>(body, httpHeaders);

		return _testRestTemplate.exchange(
			"/", HttpMethod.POST, requestEntity, String.class);
	}

	private void _verifyCapturedMessages(
			List<String> actualCapturedMessages, @Nullable String newDateString,
			String expectedCapturedMessagesResourceName)
		throws Exception {

		JSONArray jsonArray = new JSONArray(
			ResourceTemplateUtil.replaceVariables(
				ResourceUtil.readResourceToString(
					expectedCapturedMessagesResourceName, this),
				newDateString));

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONAssert.assertEquals(
				String.valueOf(jsonArray.getJSONObject(i)),
				actualCapturedMessages.get(i), false);
		}
	}

	@MockBean
	private MessageBus _messageBus;

	@Autowired
	private TestRestTemplate _testRestTemplate;

}