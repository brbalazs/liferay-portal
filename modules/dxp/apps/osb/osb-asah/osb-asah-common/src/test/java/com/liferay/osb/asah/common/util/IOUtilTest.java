/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.util;

import com.liferay.osb.asah.common.spring.resource.ResourceUtil;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Marcellus Tavares
 */
public class IOUtilTest {

	@Test
	public void testCountLines() throws Exception {
		String str1 = ResourceUtil.readResourceToString(
			"expandovalue-1.csv", getClass());

		Assertions.assertEquals(1, IOUtil.countLines(str1.getBytes()));

		String str2 = ResourceUtil.readResourceToString(
			"expandovalue-2.csv", getClass());

		Assertions.assertEquals(3, IOUtil.countLines(str2.getBytes()));

	}


}