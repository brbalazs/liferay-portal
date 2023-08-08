/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event.ip.geocoder;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.regionName;

/**
 * @author Inácio Nery
 */
public class IPInfo {

	public static final IPInfo LOCAL_NETWORK = new IPInfo("Local Network");

	public IPInfo(Location location) {
		_city = location.city;
		_country = location.countryName;
		_region = regionName.regionNameByCode(
			location.countryCode, location.region);
	}

	public String getCity() {
		return _city;
	}

	public String getCountry() {
		return _country;
	}

	public String getRegion() {
		return _region;
	}

	private IPInfo(String location) {
		_city = location;
		_country = location;
		_region = location;
	}

	private final String _city;
	private final String _country;
	private final String _region;

}