/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.test.util.util;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;

/**
 * @author Brian Wing Shun Chan
 */
public class AssertTestUtil {

	public static void assertSize(int expectedSize, Collection<?> actualList) {
		Assertions.assertEquals(
			expectedSize, actualList.size(), actualList.toString());
	}

}