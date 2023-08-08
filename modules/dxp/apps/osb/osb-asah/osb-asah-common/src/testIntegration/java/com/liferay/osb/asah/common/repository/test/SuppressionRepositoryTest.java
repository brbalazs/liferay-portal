/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

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
	public void testGetSuppressions() {
		List<Suppression> suppressions = _suppressionRepository.getSuppressions(
			"test@liferay.com", PageRequest.of(0, 10));

		Suppression suppression = suppressions.get(0);

		Assertions.assertEquals(
			"test@liferay.com", suppression.getEmailAddress());
	}

	@Autowired
	private SuppressionRepository _suppressionRepository;

}