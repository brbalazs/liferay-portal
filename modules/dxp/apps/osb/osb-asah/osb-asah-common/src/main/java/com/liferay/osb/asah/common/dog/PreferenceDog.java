/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.bigquery.BigQuerySchemaManager;
import com.liferay.osb.asah.common.constants.PreferenceConstants;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.repository.PreferenceRepository;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.codec.binary.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
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
		Preference preference = null;

		Optional<Preference> preferenceOptional =
			_preferenceRepository.findById(id);

		if (preferenceOptional.isPresent()) {
			preference = preferenceOptional.get();

			preference.setValue(value);
		}
		else {
			preference = new Preference(id, value);

			preference.setIsNew(Boolean.TRUE);
		}

		preference = _preferenceRepository.save(preference);

		if (_environment.acceptsProfiles(Profiles.of("prod")) &&
			StringUtils.equals(id, "data-retention-period")) {

			_bigQuerySchemaManager.updateTablesExpiration(
				Long.valueOf(preference.getValue()),
				ProjectIdThreadLocal.getProjectId());
		}

		return preference;
	}

	private static final Map<String, String> _defaultPreferences =
		new HashMap<String, String>() {
			{
				put(
					"data-retention-period",
					PreferenceConstants.DATA_RETENTION_PERIOD);
				put("time-zone-id", PreferenceConstants.TIME_ZONE_ID);
			}
		};

	@Autowired
	private BigQuerySchemaManager _bigQuerySchemaManager;

	@Autowired
	private Environment _environment;

	@Autowired
	private PreferenceRepository _preferenceRepository;

}