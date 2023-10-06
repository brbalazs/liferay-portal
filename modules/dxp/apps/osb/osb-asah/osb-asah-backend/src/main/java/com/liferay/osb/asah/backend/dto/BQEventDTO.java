/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.util.Date;

/**
 * @author Alejo Ceballos
 * @author Marcos Martins
 */
@GraphQLType("Event")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BQEventDTO {

	public BQEventDTO(BQEvent bqEvent) {
		_createDate = bqEvent.getCreateDate();
		_canonicalUrl = bqEvent.getCanonicalUrl();
		_name = bqEvent.getEventId();
		_pageDescription = bqEvent.getDescription();
		_pageKeywords = bqEvent.getKeywords();
		_pageTitle = bqEvent.getTitle();
		_referrer = bqEvent.getReferrer();
		_url = bqEvent.getURL();
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getName() {
		return _name;
	}

	public String getPageDescription() {
		return _pageDescription;
	}

	public String getPageKeywords() {
		return _pageKeywords;
	}

	public String getPageTitle() {
		return _pageTitle;
	}

	public String getReferrer() {
		return _referrer;
	}

	@GraphQLProperty("url")
	@JsonProperty("url")
	public String getURL() {
		return _url;
	}

	private final String _canonicalUrl;
	private final Date _createDate;
	private final String _name;
	private final String _pageDescription;
	private final String _pageKeywords;
	private final String _pageTitle;
	private final String _referrer;
	private final String _url;

}