/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.date;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

/**
 * @author André Miranda
 */
public class CountdownTimer {

	public CountdownTimer(TemporalUnit temporalUnit, int value) {
		LocalDateTime nowLocalDateTime = LocalDateTime.now();

		_expirationLocalDateTime = nowLocalDateTime.plus(value, temporalUnit);
	}

	public boolean isRunning() {
		return _expirationLocalDateTime.isAfter(LocalDateTime.now());
	}

	private final LocalDateTime _expirationLocalDateTime;

}