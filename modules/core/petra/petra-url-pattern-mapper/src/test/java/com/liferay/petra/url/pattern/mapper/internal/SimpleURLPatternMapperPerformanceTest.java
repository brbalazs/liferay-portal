/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.petra.url.pattern.mapper.internal;

import com.liferay.petra.url.pattern.mapper.URLPatternMapper;

import java.util.Map;

/**
 * @author Arthur Chan
 */
public class SimpleURLPatternMapperPerformanceTest
	extends BaseURLPatternMapperPerformanceTestCase {

	@Override
	protected URLPatternMapper<Integer> createURLPatternMapper(
		Map<String, Integer> values) {

		return new SimpleURLPatternMapper<>(values);
	}

}