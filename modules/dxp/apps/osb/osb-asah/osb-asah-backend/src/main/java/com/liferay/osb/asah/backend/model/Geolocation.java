/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.constants.DataConstants;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Adolfo Pérez
 */
public class Geolocation {

	public static Geolocation any() {
		return new Geolocation(DataConstants.ANY, DataConstants.ANY);
	}

	public static Geolocation country(String country) {
		return region(country, DataConstants.ANY);
	}

	public static Geolocation region(String country, String region) {
		if (StringUtils.isEmpty(country)) {
			country = DataConstants.ANY;
		}

		if (StringUtils.isEmpty(region)) {
			region = DataConstants.ANY;
		}

		return new Geolocation(country, region);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Geolocation)) {
			return false;
		}

		Geolocation geolocation = (Geolocation)obj;

		if (Objects.equals(_country, geolocation._country) &&
			Objects.equals(_region, geolocation._region)) {

			return true;
		}

		return false;
	}

	public String getCountry() {
		return _country;
	}

	public String getRegion() {
		return _region;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_country, _region);
	}

	private Geolocation(String country, String region) {
		_country = country;
		_region = region;
	}

	private final String _country;
	private final String _region;

}