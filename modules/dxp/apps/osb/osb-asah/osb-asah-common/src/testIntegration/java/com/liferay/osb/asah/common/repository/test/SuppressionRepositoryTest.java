/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

/**
 * @author Leilany Ulisses
 */
public class SuppressionRepositoryTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "suppression_repository_test.sql")
	@Test
	public void testCountSuppressions() {
		Assertions.assertEquals(
			1, _suppressionRepository.countSuppressions("TEST"));
	}

	@BQSQLResource(resourcePath = "suppression_repository_test.sql")
	@Test
	public void testDeleteByEmailAddress() {
		_suppressionRepository.deleteByEmailAddress("test@liferay.com");

		Assertions.assertEquals(
			0, _suppressionRepository.countSuppressions("test@liferay.com"));
	}

	@BQSQLResource(resourcePath = "suppression_repository_test.sql")
	@Test
	public void testGetSuppressions1() {
		List<Suppression> suppressions = _suppressionRepository.getSuppressions(
			"test@liferay.com", PageRequest.of(0, 10));

		Suppression suppression = suppressions.get(0);

		Assertions.assertEquals(
			"test@liferay.com", suppression.getEmailAddress());
	}

	@BQSQLResource(resourcePath = "suppression_repository_test.sql")
	@Test
	public void testGetSuppressions2() {
		Assertions.assertEquals(
			Arrays.asList("superuser@liferay.com", "user@liferay.com"),
			ListUtil.map(
				_suppressionRepository.getSuppressions(
					"u", PageRequest.of(0, 10)),
				Suppression::getEmailAddress));
	}

	@BQSQLResource(resourcePath = "suppression_repository_test.sql")
	@Test
	public void testGetSuppressions3() {
		List<Suppression> suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.asc("emailAddress")));

		Assertions.assertEquals(
			Arrays.asList(
				"admin@liferay.com", "company@liferay.com", "org@liferay.com",
				"superuser@liferay.com", "test@liferay.com",
				"user@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.desc("emailAddress")));

		Assertions.assertEquals(
			Arrays.asList(
				"user@liferay.com", "test@liferay.com", "superuser@liferay.com",
				"org@liferay.com", "company@liferay.com", "admin@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.asc("createDate")));

		Assertions.assertEquals(
			Arrays.asList(
				"user@liferay.com", "test@liferay.com", "superuser@liferay.com",
				"org@liferay.com", "company@liferay.com", "admin@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.desc("createDate")));

		Assertions.assertEquals(
			Arrays.asList(
				"admin@liferay.com", "company@liferay.com", "org@liferay.com",
				"superuser@liferay.com", "test@liferay.com",
				"user@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.asc("dataControlTaskCreateDate")));

		Assertions.assertEquals(
			Arrays.asList(
				"user@liferay.com", "test@liferay.com", "superuser@liferay.com",
				"org@liferay.com", "company@liferay.com", "admin@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null,
			PageRequest.of(0, 10, Sort.desc("dataControlTaskCreateDate")));

		Assertions.assertEquals(
			Arrays.asList(
				"admin@liferay.com", "company@liferay.com", "org@liferay.com",
				"superuser@liferay.com", "test@liferay.com",
				"user@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.asc("dataControlTaskBatchId")));

		Assertions.assertEquals(
			Arrays.asList(
				"admin@liferay.com", "company@liferay.com", "org@liferay.com",
				"superuser@liferay.com", "test@liferay.com",
				"user@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));

		suppressions = _suppressionRepository.getSuppressions(
			null, PageRequest.of(0, 10, Sort.desc("dataControlTaskBatchId")));

		Assertions.assertEquals(
			Arrays.asList(
				"user@liferay.com", "test@liferay.com", "superuser@liferay.com",
				"org@liferay.com", "company@liferay.com", "admin@liferay.com"),
			ListUtil.map(suppressions, Suppression::getEmailAddress));
	}

	@Autowired
	private SuppressionRepository _suppressionRepository;

}