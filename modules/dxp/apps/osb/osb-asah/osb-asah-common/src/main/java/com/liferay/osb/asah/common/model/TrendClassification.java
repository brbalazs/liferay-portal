/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Inácio Nery
 */
public enum TrendClassification {

	NEGATIVE, NEUTRAL, POSITIVE;

	public static TrendClassification classify(int trend, Order order) {
		if (trend == 0) {
			return NEUTRAL;
		}

		if (((trend > 0) && (order == Order.ASC)) ||
			((trend < 0) && (order == Order.DESC))) {

			return POSITIVE;
		}

		return NEGATIVE;
	}

	public enum Order {

		ASC, DESC

	}

}