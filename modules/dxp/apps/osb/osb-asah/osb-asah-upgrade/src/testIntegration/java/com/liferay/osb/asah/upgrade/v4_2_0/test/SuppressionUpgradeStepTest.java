/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
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