/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.test.util.BaseBeanTestCase;
import com.liferay.osb.asah.common.model.TrendClassification;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Inácio Nery
 */
public class TrendTest extends BaseBeanTestCase<Trend> {

	@Test
	public void testConstructor() {
		Trend trend = new Trend();

		trend.setPercentage(BigDecimal.valueOf(50D));
		trend.setTrendClassification(TrendClassification.POSITIVE);

		Assertions.assertEquals(
			trend,
			new Trend(TrendClassification.POSITIVE, BigDecimal.valueOf(50D)));
	}

	@Override
	protected Trend newInstance() {
		return new Trend();
	}

}