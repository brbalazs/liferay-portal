/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseEnumTestCase;
import com.liferay.osb.asah.common.model.TrendClassification;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class TrendClassificationTest
	extends BaseEnumTestCase<TrendClassification> {

	@Test
	public void testNegativeTrendClassification1() {
		TrendClassification trendClassification = TrendClassification.classify(
			-10, TrendClassification.Order.ASC);

		Assertions.assertEquals(
			TrendClassification.NEGATIVE, trendClassification);
	}

	@Test
	public void testNegativeTrendClassification2() {
		TrendClassification trendClassification = TrendClassification.classify(
			10, TrendClassification.Order.DESC);

		Assertions.assertEquals(
			TrendClassification.NEGATIVE, trendClassification);
	}

	@Test
	public void testNeutralTrendClassification1() {
		TrendClassification trendClassification = TrendClassification.classify(
			0, TrendClassification.Order.ASC);

		Assertions.assertEquals(
			TrendClassification.NEUTRAL, trendClassification);
	}

	@Test
	public void testNeutralTrendClassification2() {
		TrendClassification trendClassification = TrendClassification.classify(
			0, TrendClassification.Order.DESC);

		Assertions.assertEquals(
			TrendClassification.NEUTRAL, trendClassification);
	}

	@Test
	public void testPositiveTrendClassification1() {
		TrendClassification trendClassification = TrendClassification.classify(
			10, TrendClassification.Order.ASC);

		Assertions.assertEquals(
			TrendClassification.POSITIVE, trendClassification);
	}

	@Test
	public void testPositiveTrendClassification2() {
		TrendClassification trendClassification = TrendClassification.classify(
			-10, TrendClassification.Order.DESC);

		Assertions.assertEquals(
			TrendClassification.POSITIVE, trendClassification);
	}

	@Override
	protected Class<? extends Enum<?>> getClazz() {
		return TrendClassification.class;
	}

}