/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.filter.expression.FilterExpressionParserException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Marcos Martins
 */
public class SegmentDogTest {

	@Test
	public void testValidateFilterString() {
		Assertions.assertThrowsExactly(
			FilterExpressionParserException.class,
			() -> ReflectionTestUtils.invokeMethod(
				_segmentDog, "_validateFilterString",
				"demographics/age/value ge " +
					"12345678901234567262899398937898378787878"));
		Assertions.assertThrowsExactly(
			FilterExpressionParserException.class,
			() -> ReflectionTestUtils.invokeMethod(
				_segmentDog, "_validateFilterString",
				"demographics/age/value ge 1.2345678901234568e+21"));
		Assertions.assertThrowsExactly(
			FilterExpressionParserException.class,
			() -> ReflectionTestUtils.invokeMethod(
				_segmentDog, "_validateFilterString",
				"organizations.filter(filter='(dateModified gt " +
					"1580256740750)')"));
	}

	private final SegmentDog _segmentDog = new SegmentDog();

}