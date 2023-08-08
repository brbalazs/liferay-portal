/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.ext.seo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import java.util.List;
import java.util.Objects;

/**
 * @author David Arques
 */
public class TrafficSource implements Serializable {

	public TrafficSource() {
	}

	public TrafficSource(
		List<CountrySearchKeywords> countrySearchKeywordsList, String name,
		long trafficAmount, double trafficShare) {

		_countrySearchKeywordsList = countrySearchKeywordsList;
		_name = name;
		_trafficAmount = trafficAmount;
		_trafficShare = trafficShare;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof TrafficSource)) {
			return false;
		}

		TrafficSource trafficSource = (TrafficSource)obj;

		if (Objects.equals(
				_countrySearchKeywordsList,
				trafficSource._countrySearchKeywordsList) &&
			Objects.equals(_name, trafficSource._name) &&
			Objects.equals(_trafficAmount, trafficSource._trafficAmount) &&
			Objects.equals(_trafficShare, trafficSource._trafficShare)) {

			return true;
		}

		return false;
	}

	@JsonProperty("countryKeywords")
	public List<CountrySearchKeywords> getCountrySearchKeywordsList() {
		return _countrySearchKeywordsList;
	}

	public String getName() {
		return _name;
	}

	public long getTrafficAmount() {
		return _trafficAmount;
	}

	public double getTrafficShare() {
		return _trafficShare;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_countrySearchKeywordsList, _name, _trafficAmount, _trafficShare);
	}

	public void setCountrySearchKeywordsList(
		List<CountrySearchKeywords> countrySearchKeywordsList) {

		_countrySearchKeywordsList = countrySearchKeywordsList;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setTrafficAmount(long trafficAmount) {
		_trafficAmount = trafficAmount;
	}

	public void setTrafficShare(double trafficShare) {
		_trafficShare = trafficShare;
	}

	private List<CountrySearchKeywords> _countrySearchKeywordsList;
	private String _name;
	private long _trafficAmount;
	private double _trafficShare;

}