/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class BQIdentityInterestPage {

	public BQIdentityInterestPage() {
	}

	public BQIdentityInterestPage(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQIdentityInterestPage)) {
			return false;
		}

		BQIdentityInterestPage bqIdentityInterestPage =
			(BQIdentityInterestPage)obj;

		if (Objects.equals(
				_canonicalUrl, bqIdentityInterestPage._canonicalUrl) &&
			Objects.equals(_channelId, bqIdentityInterestPage._channelId) &&
			Objects.equals(_identityId, bqIdentityInterestPage._identityId) &&
			Objects.equals(_keyword, bqIdentityInterestPage._keyword) &&
			Objects.equals(_title, bqIdentityInterestPage._title) &&
			Objects.equals(_views, bqIdentityInterestPage._views)) {

			return true;
		}

		return false;
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	public long getChannelId() {
		return _channelId;
	}

	public String getIdentityId() {
		return _identityId;
	}

	public String getKeyword() {
		return _keyword;
	}

	public String getTitle() {
		return _title;
	}

	public long getViews() {
		return _views;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_canonicalUrl, _channelId, _identityId, _keyword, _title, _views);
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setChannelId(long channelId) {
		_channelId = channelId;
	}

	public void setIdentityId(String identityId) {
		_identityId = identityId;
	}

	public void setKeyword(String keyword) {
		_keyword = keyword;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setViews(long views) {
		_views = views;
	}

	private String _canonicalUrl;
	private long _channelId;
	private String _identityId;
	private String _keyword;
	private String _title;
	private long _views;

}