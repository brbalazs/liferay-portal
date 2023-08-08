/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.math.BigDecimal;

import java.util.Map;

/**
 * @author Leslie Wong
 */
public class PageVisitorBehaviorMetric extends SiteVisitorBehaviorMetric {

	public PageVisitorBehaviorMetric() {
	}

	public PageVisitorBehaviorMetric(Map<String, Object> source) {
		super(source);

		BeanUtils.copyProperties(source, this);
	}

	public long getAvgTimeOnPage() {
		if ((_timeOnPage == null) || (getSessions() <= 0)) {
			return 0;
		}

		return _timeOnPage.longValue() / getSessions();
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	public double getExitRate() {
		if ((_exits == null) || (getSessions() <= 0)) {
			return 0;
		}

		return _exits.doubleValue() / getSessions();
	}

	public long getExits() {
		return _exits.longValue();
	}

	public long getTimeOnPage() {
		if (_timeOnPage == null) {
			return 0;
		}

		return _timeOnPage.longValue();
	}

	public String getTitle() {
		return _title;
	}

	public long getViews() {
		if (_views == null) {
			return 0;
		}

		return _views.longValue();
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setExits(BigDecimal exits) {
		_exits = exits;
	}

	public void setTimeOnPage(BigDecimal timeOnPage) {
		_timeOnPage = timeOnPage;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setViews(BigDecimal views) {
		_views = views;
	}

	private String _canonicalUrl;
	private BigDecimal _exits;
	private BigDecimal _timeOnPage;
	private String _title;
	private BigDecimal _views;

}