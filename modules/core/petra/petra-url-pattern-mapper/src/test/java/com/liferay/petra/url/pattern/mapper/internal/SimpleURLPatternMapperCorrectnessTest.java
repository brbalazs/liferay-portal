/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.url.pattern.mapper.internal;

import com.liferay.petra.url.pattern.mapper.URLPatternMapper;
import com.liferay.portal.kernel.test.rule.CodeCoverageAssertor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Test;

/**
 * @author Arthur Chan
 */
public class SimpleURLPatternMapperCorrectnessTest
	extends BaseURLPatternMapperCorrectnessTestCase {

	@ClassRule
	public static final CodeCoverageAssertor codeCoverageAssertor =
		new CodeCoverageAssertor() {

			@Override
			public void appendAssertClasses(List<Class<?>> assertClasses) {
				assertClasses.add(SimpleURLPatternMapper.class);
			}

		};

	@Test
	public void testConstructor() {
		try {
			createURLPatternMapper(
				new HashMap() {
					{
						put("", 0);
					}
				});

			Assert.fail();
		}
		catch (IllegalArgumentException illegalArgumentException) {
		}

		try {
			createURLPatternMapper(
				new HashMap() {
					{
						put(null, 0);
					}
				});

			Assert.fail();
		}
		catch (IllegalArgumentException illegalArgumentException) {
		}
	}

	@Override
	protected URLPatternMapper<Integer> createURLPatternMapper(
		Map<String, Integer> values) {

		return new SimpleURLPatternMapper<>(values);
	}

}