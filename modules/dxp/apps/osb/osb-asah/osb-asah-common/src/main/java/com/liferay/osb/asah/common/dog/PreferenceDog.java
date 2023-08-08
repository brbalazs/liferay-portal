/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.repository.PreferenceRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Matthew Kong
 */
@Component
public class PreferenceDog {

	public synchronized Preference getPreference(String id) {
		Optional<Preference> preferenceOptional =
			_preferenceRepository.findById(id);

		if (preferenceOptional.isPresent()) {
			return preferenceOptional.get();
		}

		Preference preference = new Preference(id, _defaultPreferences.get(id));

		preference.setIsNew(Boolean.TRUE);

		return _preferenceRepository.save(preference);
	}

	public synchronized Preference savePreference(String id, String value) {
		Optional<Preference> preferenceOptional =
			_preferenceRepository.findById(id);

		if (preferenceOptional.isPresent()) {
			Preference preference = preferenceOptional.get();

			preference.setValue(value);

			return _preferenceRepository.save(preference);
		}

		Preference preference = new Preference(id, value);

		preference.setIsNew(Boolean.TRUE);

		return _preferenceRepository.save(preference);
	}

	private static final Map<String, String> _defaultPreferences =
		new HashMap<String, String>() {
			{
				put(
					"data-retention-period",
					String.valueOf(13 * DateUtil.MONTH));
				put("time-zone-id", "UTC");
			}
		};

	@Autowired
	private PreferenceRepository _preferenceRepository;

}