/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
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