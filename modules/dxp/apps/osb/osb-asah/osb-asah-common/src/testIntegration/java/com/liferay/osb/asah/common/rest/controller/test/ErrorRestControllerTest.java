/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.rest.controller.test;

import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.spring.OSBAsahSpringBootApplication;
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
 * @author André Miranda
 */
@ExtendWith(OSBAsahSpringExtension.class)
@SpringBootTest(
	classes = OSBAsahSpringBootApplication.class,
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ErrorRestControllerTest
	implements OSBAsahTestExecutionListenersContext {

	@Test
	public void test() {
		RequestSpecification requestSpecification = RestAssured.given();

		requestSpecification.header(HeaderConstants.PROJECT_ID, "test");
		requestSpecification.port(_serverPort);

		Response response = requestSpecification.request(
			Method.GET, "/" + RandomTestUtil.randomUUID());

		ValidatableResponse validatableResponse = response.then();

		validatableResponse.body(
			"error", Matchers.equalTo("Not Found")
		).body(
			"message",
			Matchers.equalTo("Encountered error with status code 404")
		).body(
			"status", Matchers.equalTo(404)
		).contentType(
			ContentType.JSON
		);
	}

	@LocalServerPort
	private int _serverPort;

}