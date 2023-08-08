/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.URLUtil;

import java.io.Serializable;

import java.net.URI;

import java.nio.charset.StandardCharsets;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

/**
 * @author Matthew Kong
 */
public class Acquisition implements Serializable {

	public Acquisition() {
	}

	public Acquisition(String referrer, String url) {
		UriComponentsBuilder urlUriComponentsBuilder =
			UriComponentsBuilder.fromUriString(url);

		UriComponents urlUriComponents = urlUriComponentsBuilder.build();

		queryParams = urlUriComponents.getQueryParams();

		_campaign = decode(queryParams.getFirst("utm_campaign"));
		_content = decode(queryParams.getFirst("utm_content"));
		_medium = decode(queryParams.getFirst("utm_medium"));

		try {
			URI uri = URLUtil.toURI(referrer);

			referrerHost = uri.getHost();
		}
		catch (Exception exception) {
			throw new IllegalArgumentException(exception);
		}

		_source = decode(queryParams.getFirst("utm_source"));
		_term = decode(queryParams.getFirst("utm_term"));

		this.url = url;
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

		if (!StringUtils.isEmpty(referrerHost)) {
			return "referral";
		}

		return null;
	}

	public String getSource() {
		if (!StringUtils.isEmpty(_source)) {
			return _source;
		}

		if (!StringUtils.isEmpty(referrerHost)) {
			return referrerHost;
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
			return UriUtils.decode(value, StandardCharsets.UTF_8.name());
		}
		catch (Exception exception) {
			_log.error(exception, exception);

			return null;
		}
	}

	protected MultiValueMap<String, String> queryParams;
	protected String referrerHost;
	protected String url;

	private static final Log _log = LogFactory.getLog(Acquisition.class);

	private String _campaign;
	private String _content;
	private String _medium;
	private String _source;
	private String _term;

}