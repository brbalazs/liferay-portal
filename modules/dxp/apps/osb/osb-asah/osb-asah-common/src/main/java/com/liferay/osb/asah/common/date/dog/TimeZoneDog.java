/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.date.dog;

import com.liferay.osb.asah.common.date.dog.util.TimeZoneDogUtil;
import com.liferay.osb.asah.common.dog.PreferenceDog;
import com.liferay.osb.asah.common.entity.Preference;

import java.time.ZoneId;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Geyson Silva
 */
@Component
public class TimeZoneDog {

	public String getTimeZoneId() {
		Preference preference = _preferenceDog.getPreference("time-zone-id");

		return preference.getValue();
	}

	public ZoneId getZoneId() {
		return ZoneId.of(getTimeZoneId());
	}

	@PostConstruct
	private void _init() {
		TimeZoneDogUtil.setTimeZoneDog(this);
	}

	@Autowired
	private PreferenceDog _preferenceDog;

}