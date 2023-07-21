/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.odata.normalizer;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Eduardo García
 */
public class NormalizerTest {

	@Test
	public void testNormalizeIdentifier() {
		String identifier = Normalizer.normalizeIdentifier(
			"Aaa Ááá Üüü B'bb (Ccc) Ñññ d_d _[]+-./&ªº!|\"·$=?¿¡`^*¨´Ç};:-");

		Assert.assertEquals("Aaa_Ááá_Üüü_Bbb_Ccc_Ñññ_d_d__ªºÇ", identifier);
	}

}