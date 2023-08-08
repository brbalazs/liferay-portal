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
public class CountrySearchKeywords implements Serializable {

	public CountrySearchKeywords() {
	}

	public CountrySearchKeywords(
		String countryCode, List<SearchKeyword> searchKeywords) {

		_countryCode = countryCode;
		_searchKeywords = searchKeywords;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof CountrySearchKeywords)) {
			return false;
		}

		CountrySearchKeywords countrySearchKeywords =
			(CountrySearchKeywords)obj;

		if (Objects.equals(_countryCode, countrySearchKeywords._countryCode) &&
			Objects.equals(
				_searchKeywords, countrySearchKeywords._searchKeywords)) {

			return true;
		}

		return false;
	}

	public String getCountryCode() {
		return _countryCode;
	}

	@JsonProperty("keywords")
	public List<SearchKeyword> getSearchKeywords() {
		return _searchKeywords;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_countryCode, _searchKeywords);
	}

	public void setCountryCode(String countryCode) {
		_countryCode = countryCode;
	}

	public void setSearchKeywords(List<SearchKeyword> searchKeywords) {
		_searchKeywords = searchKeywords;
	}

	private String _countryCode;
	private List<SearchKeyword> _searchKeywords;

}