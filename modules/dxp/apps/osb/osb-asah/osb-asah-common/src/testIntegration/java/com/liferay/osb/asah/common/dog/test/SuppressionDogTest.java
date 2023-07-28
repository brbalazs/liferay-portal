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

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.dog.SuppressionDog;
import com.liferay.osb.asah.common.entity.Suppression;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.util.ListUtil;
import com.liferay.osb.asah.test.util.annotation.BQSQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author Matthew Kong
 */
public class SuppressionDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@BQSQLResource(resourcePath = "suppression_dog_test.sql")
	@Test
	public void testGetSuppressionResultBag() {
		Page<Suppression> suppressionPage = _suppressionDog.getSuppressionPage(
			null, 0, 10, Sort.desc("createDate"));

		Assertions.assertEquals(3, suppressionPage.getTotalElements());

		Assertions.assertEquals(
			Arrays.asList(
				"jane.doe@gmail.com", "test@liferay.com", "john.doe@gmail.com"),
			ListUtil.map(
				suppressionPage.getContent(), Suppression::getEmailAddress));
	}

	@BQSQLResource(resourcePath = "suppression_dog_test.sql")
	@Test
	public void testGetSuppressionResultBagSearch() {
		Page<Suppression> suppressionPage = _suppressionDog.getSuppressionPage(
			"liferay", 0, 10, Sort.desc("createDate"));

		Assertions.assertEquals(1, suppressionPage.getTotalElements());

		List<Suppression> suppressions = suppressionPage.getContent();

		Suppression suppression = suppressions.get(0);

		Assertions.assertEquals(
			"test@liferay.com", suppression.getEmailAddress());
	}

	@BQSQLResource(resourcePath = "suppression_dog_test.sql")
	@Test
	public void testGetSuppressionResultBagWithCache() {
		IntStream.range(
			1, 4
		).forEach(
			i -> {
				Page<Suppression> suppressionPage =
					_suppressionDog.getSuppressionPage(
						null, 0, 10, Sort.desc("createDate"));

				Assertions.assertEquals(3, suppressionPage.getTotalElements());
				Assertions.assertEquals(
					Arrays.asList(
						"jane.doe@gmail.com", "test@liferay.com",
						"john.doe@gmail.com"),
					ListUtil.map(
						suppressionPage.getContent(),
						Suppression::getEmailAddress));
			}
		);
	}

	@Autowired
	private SuppressionDog _suppressionDog;

}