/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.commerce.data.integration.manager.constants;

/**
 * @author guywandji
 */
public enum Frequency {

	DAILY("daily", "0 0 * * *"), EXECUTE_ONCE("execute-once", "0 2 2 11 *"),
	HOURLY("hourly", "0 * * * *"), MONTHLY("monthly", "0 0 * 1-12 *");

	public static Frequency getByName(String name) {
		for (Frequency frequency : Frequency.values()) {
			if (name.equals(frequency.getName())) {
				return frequency;
			}
		}

		return EXECUTE_ONCE;
	}

	public String getCronExpression() {
		return _cronExpression;
	}

	public String getName() {
		return _name;
	}

	private Frequency(String name, String cronExpression) {
		_name = name;
		_cronExpression = cronExpression;
	}

	private final String _cronExpression;
	private final String _name;

}