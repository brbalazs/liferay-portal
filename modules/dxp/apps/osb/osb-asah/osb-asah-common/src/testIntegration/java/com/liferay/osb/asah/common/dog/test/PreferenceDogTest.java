/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog.test;

import com.liferay.osb.asah.common.OSBAsahCommonSpringTestContext;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.test.util.spring.OSBAsahTestExecutionListenersContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Matthew Kong
 */
public class PreferenceDogTest
	implements OSBAsahCommonSpringTestContext,
			   OSBAsahTestExecutionListenersContext {

	@Test
	public void testGetPreference() {
		Preference preference = _preferenceDog.getPreference("key");

		Assertions.assertEquals(null, preference.getValue());

		_preferenceDog.savePreference("key", "value");

		preference = _preferenceDog.getPreference("key");

		Assertions.assertEquals("value", preference.getValue());

		preference = _preferenceDog.getPreference("time-zone-id");

		Assertions.assertEquals("UTC", preference.getValue());
	}

	@Test
	public void testSavePreference() {
		Preference preference = _preferenceDog.getPreference(
			"data-retention-period");

		Assertions.assertEquals(
			String.valueOf(13 * DateUtil.MONTH), preference.getValue());

		String dataRetentionPeriod = String.valueOf(7 * DateUtil.MONTH);

		_preferenceDog.savePreference(
			"data-retention-period", dataRetentionPeriod);

		preference = _preferenceDog.getPreference("data-retention-period");

		Assertions.assertEquals(dataRetentionPeriod, preference.getValue());

		_preferenceDog.savePreference(
			"data-retention-period", dataRetentionPeriod);

		preference = _preferenceDog.getPreference("data-retention-period");

		Assertions.assertEquals(dataRetentionPeriod, preference.getValue());
	}

	@Autowired
	private PreferenceDog _preferenceDog;

}