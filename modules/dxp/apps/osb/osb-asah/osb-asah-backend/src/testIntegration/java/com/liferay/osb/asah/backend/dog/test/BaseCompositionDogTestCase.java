/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog.test;

import com.liferay.osb.asah.backend.OSBAsahBackendSpringTestContext;
import com.liferay.osb.asah.common.model.Composition;
import com.liferay.osb.asah.common.model.CompositionResultBag;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;

/**
 * @author Shinn Lok
 */
public abstract class BaseCompositionDogTestCase
	implements OSBAsahBackendSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	protected void checkResults(
		CompositionResultBag compositionResultBag,
		Map<String, Long> expectedResults, long expectedMaxCount,
		long expectedTotal, long expectedTotalCount) {

		List<Composition> results = compositionResultBag.getResults();

		Stream<Composition> stream = results.stream();

		Assertions.assertEquals(
			expectedResults,
			stream.collect(
				Collectors.toMap(
					Composition::getName, Composition::getCount,
					(name, count) -> name, LinkedHashMap::new)));

		Assertions.assertEquals(
			expectedMaxCount, compositionResultBag.getMaxCount());
		Assertions.assertEquals(expectedTotal, compositionResultBag.getTotal());
		Assertions.assertEquals(
			expectedTotalCount, compositionResultBag.getTotalCount());
	}

}