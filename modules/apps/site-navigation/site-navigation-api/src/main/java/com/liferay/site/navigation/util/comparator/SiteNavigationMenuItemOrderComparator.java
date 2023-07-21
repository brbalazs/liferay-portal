/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.site.navigation.util.comparator;

import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.site.navigation.model.SiteNavigationMenuItem;

/**
 * @author Pavel Savinov
 */
public class SiteNavigationMenuItemOrderComparator
	extends OrderByComparator<SiteNavigationMenuItem> {

	public static final String ORDER_BY_ASC =
		"SiteNavigationMenuItem.order_ ASC";

	public static final String ORDER_BY_DESC =
		"SiteNavigationMenuItem.order_ DESC";

	public static final String[] ORDER_BY_FIELDS = {"order"};

	public SiteNavigationMenuItemOrderComparator() {
		this(true);
	}

	public SiteNavigationMenuItemOrderComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(
		SiteNavigationMenuItem siteNavigationMenuItem1,
		SiteNavigationMenuItem siteNavigationMenuItem2) {

		int order1 = siteNavigationMenuItem1.getOrder();
		int order2 = siteNavigationMenuItem2.getOrder();

		if (order1 > order2) {
			return 1;
		}
		else if (order2 > order1) {
			return -1;
		}

		return 0;
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