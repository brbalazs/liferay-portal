/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.rest.controller.api.data.source.v1.test;

import com.liferay.osb.asah.backend.spring.OSBAsahBackendSpringBootApplication;
import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.repository.ChannelRepository;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import org.hamcrest.Matchers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

/**
 * @author Riccardo Ferrari
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(
	classes = OSBAsahBackendSpringBootApplication.class,
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ChannelRestControllerTest
	implements OSBAsahTestExecutionListenersContext {

	@RepositoryResource(
		repositoryClass = ChannelRepository.class,
		resourcePath = "osbasahfaroinfo/channels.json"
	)
	@RepositoryResource(
		repositoryClass = DataSourceRepository.class,
		resourcePath = "osbasahfaroinfo/data_sources.json"
	)
	@Test
	public void testPatchChannel() {
		RequestSpecification requestSpecification = RestAssured.given();

		requestSpecification.header(HeaderConstants.PROJECT_ID, "test");
		requestSpecification.port(_serverPort);

		Response response = requestSpecification.body(
			JSONUtil.put(
				"commerceChannels",
				JSONUtil.putAll(
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					),
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					))
			).put(
				"dataSourceId", "331238757269547423"
			).put(
				"groups",
				JSONUtil.putAll(
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					),
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					))
			).toString()
		).request(
			Method.PATCH, "/api/1.0/channels/12345"
		);

		ValidatableResponse validatableResponse = response.then();

		validatableResponse.body(
			"channel.dataSources[1].commerceChannelIds", Matchers.hasSize(2)
		).body(
			"channel.dataSources[1].groupIds", Matchers.hasSize(2)
		).contentType(
			ContentType.JSON
		);
	}

	@Test
	public void testPostDataSource() {
		RequestSpecification requestSpecification = RestAssured.given();

		requestSpecification.header(HeaderConstants.PROJECT_ID, "test");
		requestSpecification.port(_serverPort);

		Response response = requestSpecification.body(
			JSONUtil.put("name", "Test Channel")
		).request(
			Method.POST, "/api/1.0/channels"
		);

		ValidatableResponse validatableResponse = response.then();

		validatableResponse.body(
			".", Matchers.hasSize(1)
		).body(
			"[0].name", Matchers.equalTo("Test Channel")
		).body(
			"[0].id", Matchers.anything()
		).contentType(
			ContentType.JSON
		);
	}

	@Test
	public void testPostDataSources() throws Exception {
		RequestSpecification requestSpecification = RestAssured.given();

		requestSpecification.header(HeaderConstants.PROJECT_ID, "test");
		requestSpecification.port(_serverPort);

		Response response = requestSpecification.body(
			JSONUtil.put(
				"channelType", "multiple"
			).put(
				"dataSourceId", String.valueOf(RandomTestUtil.randomNumber())
			).put(
				"groups",
				JSONUtil.putAll(
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					),
					JSONUtil.put(
						"id", String.valueOf(RandomTestUtil.randomNumber())
					).put(
						"name", RandomTestUtil.randomString()
					))
			).toString()
		).request(
			Method.POST, "/api/1.0/channels"
		);

		ValidatableResponse validatableResponse = response.then();

		validatableResponse.body(
			".", Matchers.hasSize(2)
		).contentType(
			ContentType.JSON
		);
	}

	@LocalServerPort
	private int _serverPort;

}