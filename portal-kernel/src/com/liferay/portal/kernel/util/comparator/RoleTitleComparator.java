/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.portal.kernel.util.comparator;

import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.util.OrderByComparator;

/**
 * @author Balazs Breier
 */
public class RoleTitleComparator extends OrderByComparator<Role> {

	public static final String ORDER_BY_ASC = "Role_.title ASC";

	public static final String ORDER_BY_DESC = "Role_.title DESC";

	public static final String[] ORDER_BY_FIELDS = {"title"};

	public RoleTitleComparator() {
		this(false);
	}

	public RoleTitleComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(Role role1, Role role2) {
		String key1 = role1.getName();
		String key2 = role2.getName();

		String title1 = role1.getTitle();
		String title2 = role2.getTitle();

		int value = title1.compareTo(title2);

		if (title1.isEmpty()) {
			value = key1.compareTo(title2);
		}
		else if (title2.isEmpty()) {
			value = key2.compareTo(title1);
		}
		else if (title1.isEmpty() && title2.isEmpty()) {
			value = key1.compareTo(key2);
		}

		if (_ascending) {
			return value;
		}

		return -value;
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}

		return ORDER_BY_DESC;
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}