/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Rachael Koestartyo
 */
public enum Interval {

	DAY("D"), HOUR("H"), MONTH("M"), WEEK("W");

	public static Interval of(String interval) {
		return Optional.ofNullable(
			_intervals.get(interval)
		).orElseThrow(
			IllegalArgumentException::new
		);
	}

	public String getKey() {
		return _key;
	}

	private Interval(String key) {
		_key = key;
	}

	private static final Map<String, Interval> _intervals =
		new HashMap<String, Interval>() {
			{
				put("D", DAY);
				put("H", HOUR);
				put("M", MONTH);
				put("W", WEEK);
			}
		};

	private final String _key;

}