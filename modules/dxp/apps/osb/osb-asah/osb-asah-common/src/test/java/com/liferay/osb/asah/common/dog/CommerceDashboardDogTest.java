/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author Riccardo Ferrari
 */
public class CommerceDashboardDogTest {

	@Test
	public void testGetPercentageVariation() {
		Assertions.assertEquals(
			0.0,
			ReflectionTestUtils.invokeMethod(
				_commerceDashboardDog, "_getPercentageVariation",
				new BigDecimal("737"), BigDecimal.ZERO));
		Assertions.assertEquals(
			659.8,
			ReflectionTestUtils.invokeMethod(
				_commerceDashboardDog, "_getPercentageVariation",
				new BigDecimal("737"), new BigDecimal("97")));
	}

	private static final CommerceDashboardDog _commerceDashboardDog =
		new CommerceDashboardDog();

}