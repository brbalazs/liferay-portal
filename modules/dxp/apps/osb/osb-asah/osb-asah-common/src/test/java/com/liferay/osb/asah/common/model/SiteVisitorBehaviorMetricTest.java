/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marcos Martins
 */
public class SiteVisitorBehaviorMetricTest {

	@Test
	public void testGetSessionsPerVisitor() {
		SiteVisitorBehaviorMetric siteVisitorBehaviorMetric =
			new SiteVisitorBehaviorMetric();

		siteVisitorBehaviorMetric.setSessions(BigDecimal.valueOf(7));
		siteVisitorBehaviorMetric.setVisitors(BigDecimal.valueOf(3));

		Assertions.assertEquals(
			2.33, siteVisitorBehaviorMetric.getSessionsPerVisitor());
	}

}