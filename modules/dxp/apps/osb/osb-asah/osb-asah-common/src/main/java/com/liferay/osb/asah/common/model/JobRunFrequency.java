/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Marcellus Tavares
 */
public enum JobRunFrequency {

	EVERY_7_DAYS("0 0 0 */7 * ?"), EVERY_14_DAYS("0 0 0 */14 * ?"),
	EVERY_30_DAYS("0 0 0 */30 * ?"), MANUAL(null);

	public String getCronExpression() {
		return _cronExpression;
	}

	private JobRunFrequency(String cronExpression) {
		_cronExpression = cronExpression;
	}

	private final String _cronExpression;

}