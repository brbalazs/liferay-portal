/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.URLUtil;

import java.net.URI;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Matthew Kong
 */
public class PageAcquisition extends Acquisition {

	public PageAcquisition() {
	}

	public PageAcquisition(String referrer, String url) {
		super(referrer, url);
	}

	@Override
	public String getChannel() {
		String channel = super.getChannel();

		if (Objects.equals(channel, "organic") ||
			_contains(_SEARCH_HOST_NAMES, referrerHost)) {

			return "organic";
		}

		if (Objects.equals(channel, "paid search") ||
			!StringUtils.isBlank(decode(queryParams.getFirst("gclid"))) ||
			_contains(_PAID_HOST_NAMES, referrerHost)) {

			return "paid";
		}

		if (Objects.equals(channel, "social") ||
			_contains(_SOCIAL_HOST_NAMES, referrerHost)) {

			return "social";
		}

		if (StringUtils.isBlank(referrerHost)) {
			return "direct";
		}

		if (Objects.equals(channel, "referral") || !_isInternalReferrer()) {
			return "referral";
		}

		return null;
	}

	private boolean _contains(String[] array, String referrer) {
		if (StringUtils.isBlank(referrer)) {
			return false;
		}

		referrer = StringUtils.removeEnd(referrer, "/");

		for (String value : array) {
			if (referrer.endsWith(value)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isInternalReferrer() {
		try {
			URI uri = URLUtil.toURI(url);

			if (StringUtils.equals(uri.getHost(), referrerHost)) {
				return true;
			}
		}
		catch (Exception exception) {
			throw new IllegalArgumentException(exception);
		}

		return false;
	}

	private static final String[] _PAID_HOST_NAMES = {"googleadservices.com"};

	private static final String[] _SEARCH_HOST_NAMES = {
		"ask.com", "baidu.com", "bing.com", "duckduckgo.com", "google.com",
		"yahoo.com", "yandex.com"
	};

	private static final String[] _SOCIAL_HOST_NAMES = {
		"facebook.com", "instagram.com", "linkedin.com", "pinterest.com",
		"snapchat.com", "t.co", "tiktok.com", "twitter.com", "youtube.com"
	};

}