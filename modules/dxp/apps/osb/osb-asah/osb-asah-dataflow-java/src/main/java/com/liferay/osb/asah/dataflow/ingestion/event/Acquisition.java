/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.dataflow.ingestion.event;

import com.liferay.osb.asah.dataflow.common.URLUtil;

import java.net.URI;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Leslie Wong
 */
public class Acquisition {

	public Acquisition() {
	}

	public Acquisition(String referrer, String url) {
		try {
			URI uri = URLUtil.createURI(url);

			Map<String, String> queryParams = new HashMap<>();

			String query = uri.getQuery();

			if (query != null) {
				for (String queryParam : query.split("&")) {
					int index = queryParam.indexOf("=");

					if (index != -1) {
						String key = queryParam.substring(0, index);

						if (!queryParams.containsKey(key)) {
							queryParams.put(
								key, queryParam.substring(index + 1));
						}
					}
				}
			}

			_campaign = decode(queryParams.get("utm_campaign"));
			_content = decode(queryParams.get("utm_content"));
			_medium = decode(queryParams.get("utm_medium"));

			if (StringUtils.isNotEmpty(referrer)) {
				URI referrerURI = new URI(referrer);

				_referrerHost = referrerURI.getHost();
			}

			_source = decode(queryParams.get("utm_source"));
			_term = decode(queryParams.get("utm_term"));
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Unable to create acquisition from analytics event",
					exception);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof Acquisition)) {
			return false;
		}

		Acquisition acquisition = (Acquisition)obj;

		if (Objects.equals(_campaign, acquisition._campaign) &&
			Objects.equals(_content, acquisition._content) &&
			Objects.equals(_medium, acquisition._medium) &&
			Objects.equals(_source, acquisition._source) &&
			Objects.equals(_term, acquisition._term)) {

			return true;
		}

		return false;
	}

	public String getCampaign() {
		return _campaign;
	}

	public String getChannel() {
		if (Objects.equals(_medium, "affiliate")) {
			return "affiliates";
		}

		if (Objects.isNull(_medium)) {
			return "direct";
		}

		if (Objects.equals(_medium, "banner") ||
			Objects.equals(_medium, "cpm") ||
			Objects.equals(_medium, "display")) {

			return "display";
		}

		if (Objects.equals(_medium, "email")) {
			return "email";
		}

		if (Objects.equals(_medium, "organic")) {
			return "organic";
		}

		if (Objects.equals(_medium, "content-text") ||
			Objects.equals(_medium, "cpa") || Objects.equals(_medium, "cpp") ||
			Objects.equals(_medium, "cpv")) {

			return "other advertising";
		}

		if (Objects.equals(_medium, "cpc") ||
			Objects.equals(_medium, "paidsearch") ||
			Objects.equals(_medium, "ppc")) {

			return "paid search";
		}

		if (Objects.equals(_medium, "referral")) {
			return "referral";
		}

		if (Objects.equals(_medium, "sm") ||
			Objects.equals(_medium, "social") ||
			Objects.equals(_medium, "social media") ||
			Objects.equals(_medium, "social network") ||
			Objects.equals(_medium, "social-media") ||
			Objects.equals(_medium, "social-network")) {

			return "social";
		}

		return "other";
	}

	public String getContent() {
		return _content;
	}

	public String getMedium() {
		if (!StringUtils.isEmpty(_medium)) {
			return _medium;
		}

		if (!StringUtils.isEmpty(_referrerHost)) {
			return "referral";
		}

		return null;
	}

	public String getSource() {
		if (!StringUtils.isEmpty(_source)) {
			return _source;
		}

		if (!StringUtils.isEmpty(_referrerHost)) {
			return _referrerHost;
		}

		return null;
	}

	public String getTerm() {
		return _term;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_campaign, _content, _medium, _source, _term);
	}

	public void setCampaign(String campaign) {
		_campaign = campaign;
	}

	public void setContent(String content) {
		_content = content;
	}

	public void setMedium(String medium) {
		_medium = medium;
	}

	public void setSource(String source) {
		_source = source;
	}

	public void setTerm(String term) {
		_term = term;
	}

	protected String decode(String value) {
		if (Objects.isNull(value)) {
			return null;
		}

		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return null;
		}
	}

	private static final Log _log = LogFactory.getLog(Acquisition.class);

	private String _campaign;
	private String _content;
	private String _medium;
	private String _referrerHost;
	private String _source;
	private String _term;

}