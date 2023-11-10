/**
 * SPDX-FileCopyrightText: (c) 2023 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.upgrade.v4_3_0.test;

import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.repository.ExperimentRepository;
import com.liferay.osb.asah.test.util.annotation.SQLResource;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;
import com.liferay.osb.asah.upgrade.OSBAsahUpgradeSpringTestContext;
import com.liferay.osb.asah.upgrade.v4_3_0.ExperimentUpgradeStep;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.IterableUtils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Marcos Martins
 */
public class ExperimentUpgradeStepTest
	implements OSBAsahTestExecutionListenersContext,
			   OSBAsahUpgradeSpringTestContext {

	@SQLResource(resourcePath = "experiment.sql")
	@Test
	public void testUpgrade() throws Exception {
		_postgreSQLUpgradeStep.upgrade(null);

		_assertExperimentPublishable(
			new HashMap<Long, Boolean>() {
				{
					put(1L, false);
					put(2L, false);
					put(3L, false);
					put(4L, false);
					put(5L, false);
					put(6L, true);
					put(7L, true);
					put(8L, true);
					put(9L, false);
					put(10L, true);
				}
			},
			IterableUtils.toList(_experimentRepository.findAll()));
	}

	private void _assertExperimentPublishable(
		Map<Long, Boolean> expectedValuesMap, List<Experiment> experiments) {

		for (Experiment experiment : experiments) {
			Assertions.assertEquals(
				expectedValuesMap.get(experiment.getId()),
				experiment.isPublishable());
		}
	}

	@Autowired
	private ExperimentRepository _experimentRepository;

	@Autowired
	private ExperimentUpgradeStep _postgreSQLUpgradeStep;

}