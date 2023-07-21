/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.test;

import java.util.Comparator;

import org.junit.runner.Description;

/**
 * @author Shuyang Zhou
 */
public class DescriptionComparator implements Comparator<Description> {

	@Override
	public int compare(Description description1, Description description2) {
		String displayName1 = description1.getDisplayName();
		String displayName2 = description2.getDisplayName();

		return displayName1.compareTo(displayName2);
	}

}