/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.dog.UserSessionDog;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.faro.info.dog.test.BaseFaroInfoDogTestCase;
import com.liferay.osb.asah.common.repository.BQSessionRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.annotation.RepositoryResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Robson Pastor
 */
public class UserSessionDogTest
	extends BaseFaroInfoDogTestCase
	implements OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "test_bq_sessions.sql")
	@Test
	public void testFindByIds() {
		List<BQSession> bqSessions1 = _userSessionDog.findByIds(
			Arrays.asList("366909399944213421"));

		Assertions.assertEquals(1, bqSessions1.size());

		List<BQSession> bqSessions2 = _userSessionDog.findByIds(
			Arrays.asList("366909399944213421", "366909399944215919"));

		Assertions.assertEquals(2, bqSessions2.size());

		List<BQSession> bqSessions3 = _userSessionDog.findByIds(
			Arrays.asList(
				"366909399944213421", "366909399944215919",
				"366909399944215920"));

		Assertions.assertEquals(2, bqSessions3.size());
	}

	@BQSQLResource(resourcePath = "test_bq_sessions_field_values.sql")
	@Test
	public void testGetBQSessionFieldValuePage() {
		Page<String> bqSessionFieldValuePage =
			_userSessionDog.getBQSessionFieldValuePage(
				"browserName", null, 0, 10, null);

		Assertions.assertEquals(2, bqSessionFieldValuePage.getTotalElements());

		List<String> content = bqSessionFieldValuePage.getContent();

		Assertions.assertEquals(Arrays.asList("Chrome", "Firefox"), content);

		bqSessionFieldValuePage = _userSessionDog.getBQSessionFieldValuePage(
			"browserName", null, 0, 10, "Chro");

		Assertions.assertEquals(1, bqSessionFieldValuePage.getTotalElements());

		content = bqSessionFieldValuePage.getContent();

		Assertions.assertEquals(Arrays.asList("Chrome"), content);

		bqSessionFieldValuePage = _userSessionDog.getBQSessionFieldValuePage(
			"referrer", null, 0, 10, null);

		Assertions.assertEquals(4, bqSessionFieldValuePage.getTotalElements());

		content = bqSessionFieldValuePage.getContent();

		Assertions.assertEquals(
			Arrays.asList(
				"http://192.168.118.3:7400/",
				"http://192.168.118.3:7400/c/portal/logout",
				"http://192.168.118.3:7400/web/forms/shared/-/form/44224?" +
					"p_p_auth=V2Mnn7B1&p_p_state=pop_up",
				"http://192.168.118.3:7400/web/guest/home"),
			content);

		bqSessionFieldValuePage = _userSessionDog.getBQSessionFieldValuePage(
			"referrer", null, 0, 10, "http://192.168.118.3:7400/web/forms");

		Assertions.assertEquals(1, bqSessionFieldValuePage.getTotalElements());

		content = bqSessionFieldValuePage.getContent();

		Assertions.assertEquals(
			Arrays.asList(
				"http://192.168.118.3:7400/web/forms/shared/-/form/44224?" +
					"p_p_auth=V2Mnn7B1&p_p_state=pop_up"),
			content);
	}

	@Disabled
	@RepositoryResource(
		repositoryClass = BQSessionRepository.class,
		resourcePath = "osbasahcerebroinfo/user_sessions_info.json"
	)
	@Test
	public void testGetIndividualIds() {
		List<Long> individualIds1 = _userSessionDog.getIndividualIds(null);

		Assertions.assertEquals(3, individualIds1.size());

		List<Long> individualIds2 = _userSessionDog.getIndividualIds(
			"(id eq '366909399944215919')");

		Assertions.assertEquals(1, individualIds2.size());

		Assertions.assertEquals(
			Collections.emptyList(),
			_userSessionDog.getIndividualIds(
				"(country eq 'Germany' and completeDate gt 'last24Hours')"));

		List<Long> individualIds3 = _userSessionDog.getIndividualIds(
			"(browserName eq 'Firefox')");

		Assertions.assertEquals(3, individualIds3.size());

		Assertions.assertEquals(
			Collections.emptyList(),
			_userSessionDog.getIndividualIds(
				"(country eq 'Germany' and completeDate gt 'last24Hours')"));
	}

	@Autowired
	private UserSessionDog _userSessionDog;

}