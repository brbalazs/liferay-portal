/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util.comparator;

/**
 * @author     Brian Wing Shun Chan
 * @deprecated As of Wilberforce (7.0.x), renamed to {@link
 *             UserLastNameComparator}
 */
@Deprecated
public class ContactLastNameComparator extends UserLastNameComparator {

	public ContactLastNameComparator() {
	}

	public ContactLastNameComparator(boolean ascending) {
		super(ascending);
	}

}