/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model.comparator;

import com.liferay.osb.asah.common.model.BreakdownItem;

import java.io.Serializable;

import java.util.Comparator;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * @author Marcos Martins
 */
public class BreakdownItemComparator
	implements Comparator<BreakdownItem>, Serializable {

	public BreakdownItemComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(
		BreakdownItem breakdownItem1, BreakdownItem breakdownItem2) {

		Number value1 = breakdownItem1.getValue();
		Number value2 = breakdownItem2.getValue();

		int compare = Double.compare(
			value1.doubleValue(), value2.doubleValue());

		if (compare != 0) {
			if (_ascending) {
				return compare;
			}

			return compare * -1;
		}

		String internalName1 = breakdownItem1.getInternalName();
		String internalName2 = breakdownItem2.getInternalName();

		if (NumberUtils.isCreatable(internalName1) &&
			NumberUtils.isCreatable(internalName2)) {

			compare = Double.compare(
				NumberUtils.toDouble(internalName1),
				NumberUtils.toDouble(internalName2));
		}
		else {
			compare = internalName1.compareTo(internalName2);
		}

		if (_ascending) {
			return compare;
		}

		return compare * -1;
	}

	private final boolean _ascending;

}