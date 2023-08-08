/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.servlet.filter.test;

import com.liferay.osb.asah.backend.spring.OSBAsahBackendSpringBootApplication;
import com.liferay.osb.asah.common.constants.HeaderConstants;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.repository.DataSourceRepository;
import com.liferay.osb.asah.test.util.spring.OSBAsahRepositoryTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSQLTestExecutionListener;
import com.liferay.osb.asah.test.util.spring.OSBAsahSpringExtension;
import com.liferay.osb.asah.test.util.util.RandomTestUtil;

import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.StatusResultMatchers;

/**
 * @author Vishal Reddy
 */
@ContextConfiguration(classes = OSBAsahBackendSpringBootApplication.class)
@ExtendWith(OSBAsahSpringExtension.class)
@TestExecutionListeners(
	mergeMode = TestExecutionListeners.MergeMode.REPLACE_DEFAULTS,
	value = {
		DependencyInjectionTestExecutionListener.class,
		OSBAsahRepositoryTestExecutionListener.class,
		OSBAsahSQLTestExecutionListener.class,
		ServletTestExecutionListener.class
	}
)
@TestPropertySource(
	properties = {
		"osb.asah.security.enabled=true",
		"osb.asah.security.token=6KRqGHCuGxX6mn7DaOxD8aQCGq0KV3id"
	}
)
@WebMvcTest
public class SecurityOncePerRequestFilterTest {

	@Test
	public void testGet() throws Exception {
		_testGet(
			"/data-sources",
			DigestUtils.sha256Hex(
				_osbAsahFaroBackendSecurityToken.concat("http://localhost")));
	}

	@Test
	public void testGetAPI() throws Exception {
		String uuid = String.valueOf(UUID.randomUUID());

		String securitySignature = DigestUtils.sha256Hex(
			uuid.concat("http://localhost"));

		DataSource dataSource = new DataSource();

		dataSource.setFaroBackendSecuritySignature(securitySignature);
		dataSource.setIsNew(Boolean.TRUE);

		_dataSourceRepository.save(dataSource);

		_testGet("/api/1.0/individual-segments", securitySignature);

		_testGet(
			"/api/recommendations",
			DigestUtils.sha256Hex(
				_osbAsahFaroBackendSecurityToken.concat("http://localhost")));
		_testGet(
			"/api/////recommendations",
			DigestUtils.sha256Hex(
				_osbAsahFaroBackendSecurityToken.concat("http://localhost")));
	}

	private void _expectStatusForbidden(ResultActions resultActions)
		throws Exception {

		StatusResultMatchers statusResultMatchers =
			MockMvcResultMatchers.status();

		resultActions.andExpect(statusResultMatchers.isForbidden());
	}

	private void _expectStatusOK(ResultActions resultActions) throws Exception {
		StatusResultMatchers statusResultMatchers =
			MockMvcResultMatchers.status();

		resultActions.andExpect(statusResultMatchers.isOk());
	}

	private void _testGet(String url, String securitySignature)
		throws Exception {

		MockHttpServletRequestBuilder mockHttpServletRequestBuilder1 =
			MockMvcRequestBuilders.get(url);

		mockHttpServletRequestBuilder1.header(
			HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE, securitySignature);
		mockHttpServletRequestBuilder1.header(
			HeaderConstants.PROJECT_ID, "test");

		_expectStatusOK(_mockMvc.perform(mockHttpServletRequestBuilder1));

		MockHttpServletRequestBuilder mockHttpServletRequestBuilder2 =
			MockMvcRequestBuilders.get(url);

		mockHttpServletRequestBuilder2.header(
			HeaderConstants.FARO_BACKEND_SECURITY_SIGNATURE,
			RandomTestUtil.randomUUID());
		mockHttpServletRequestBuilder2.header(
			HeaderConstants.PROJECT_ID, "test");

		_expectStatusForbidden(
			_mockMvc.perform(mockHttpServletRequestBuilder2));
	}

	@Autowired
	private DataSourceRepository _dataSourceRepository;

	@Autowired
	private MockMvc _mockMvc;

	@Value("${osb.asah.security.token}")
	private String _osbAsahFaroBackendSecurityToken;

}