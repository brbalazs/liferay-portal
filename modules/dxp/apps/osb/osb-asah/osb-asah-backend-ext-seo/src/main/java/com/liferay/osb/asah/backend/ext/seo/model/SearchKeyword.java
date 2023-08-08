/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.ext.seo.model;

import com.univocity.parsers.annotations.Parsed;

import java.io.Serializable;

import java.util.Objects;

/**
 * @author David Arques
 */
public class SearchKeyword implements Serializable {

	public SearchKeyword() {
	}

	public SearchKeyword(
		String keyword, int position, long searchVolume, long traffic) {

		_keyword = keyword;
		_position = position;
		_searchVolume = searchVolume;
		_traffic = traffic;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SearchKeyword)) {
			return false;
		}

		SearchKeyword searchKeyword = (SearchKeyword)obj;

		if (Objects.equals(_keyword, searchKeyword._keyword) &&
			Objects.equals(_position, searchKeyword._position) &&
			Objects.equals(_searchVolume, searchKeyword._searchVolume) &&
			Objects.equals(_traffic, searchKeyword._traffic)) {

			return true;
		}

		return false;
	}

	public String getKeyword() {
		return _keyword;
	}

	public int getPosition() {
		return _position;
	}

	public long getSearchVolume() {
		return _searchVolume;
	}

	public long getTraffic() {
		return _traffic;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_keyword, _position, _searchVolume, _traffic);
	}

	public void setKeyword(String keyword) {
		_keyword = keyword;
	}

	public void setPosition(int position) {
		_position = position;
	}

	public void setSearchVolume(long searchVolume) {
		_searchVolume = searchVolume;
	}

	public void setTraffic(long traffic) {
		_traffic = traffic;
	}

	@Parsed(index = 0)
	private String _keyword;

	@Parsed(index = 1)
	private int _position;

	@Parsed(index = 2)
	private long _searchVolume;

	@Parsed(index = 3)
	private long _traffic;

}