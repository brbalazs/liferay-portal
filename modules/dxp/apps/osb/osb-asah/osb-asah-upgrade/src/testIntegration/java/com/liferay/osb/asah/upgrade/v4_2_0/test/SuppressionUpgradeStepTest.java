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

package com.liferay.osb.asah.upgrade.v4_2_0.test;

import com.liferay.osb.asah.common.repository.SuppressionRepository;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.upgrade.OSBAsahUpgradeSpringTestContext;
import com.liferay.osb.asah.upgrade.v4_2_0.SuppressionUpgradeStep;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcellus Tavares
 */
public class SuppressionUpgradeStepTest
	implements OSBAsahTestExecutionListenersContext,
			   OSBAsahUpgradeSpringTestContext {

	@SQLResource(resourcePath = "suppression.sql")
	@Test
	public void testUpgrade() throws Exception {
		_suppressionUpgradeStep.upgrade(null);

		Assertions.assertEquals(
			5, _suppressionRepository.countSuppressions(null));
	}

	@Autowired
	private SuppressionRepository _suppressionRepository;

	@Autowired
	private SuppressionUpgradeStep _suppressionUpgradeStep;

}