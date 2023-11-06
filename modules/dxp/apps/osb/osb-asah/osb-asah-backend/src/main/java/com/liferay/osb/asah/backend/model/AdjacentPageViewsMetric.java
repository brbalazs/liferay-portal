/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.math.BigDecimal;

import java.util.Map;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class AdjacentPageViewsMetric {

	public AdjacentPageViewsMetric() {
	}

	public AdjacentPageViewsMetric(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public AdjacentPageViewsMetric(
		String canonicalUrl, Boolean previous, String title, BigDecimal views) {

		_canonicalUrl = canonicalUrl;
		_previous = previous;
		_title = title;
		_views = views;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof AdjacentPageViewsMetric)) {
			return false;
		}

		AdjacentPageViewsMetric adjacentPageViewsMetric =
			(AdjacentPageViewsMetric)obj;

		if (Objects.equals(
				_canonicalUrl, adjacentPageViewsMetric._canonicalUrl) &&
			Objects.equals(_previous, adjacentPageViewsMetric._previous) &&
			Objects.equals(_title, adjacentPageViewsMetric._title) &&
			Objects.equals(_views, adjacentPageViewsMetric._views)) {

			return true;
		}

		return false;
	}

	public String getCanonicalUrl() {
		return _canonicalUrl;
	}

	public String getTitle() {
		return _title;
	}

	public BigDecimal getViews() {
		return _views;
	}

	public Long getViewsAsLong() {
		if (_views == null) {
			return null;
		}

		return _views.longValue();
	}

	@Override
	public int hashCode() {
		return Objects.hash(_canonicalUrl, _previous, _title, _views);
	}

	public Boolean isPrevious() {
		return _previous;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		_canonicalUrl = canonicalUrl;
	}

	public void setPrevious(Boolean previous) {
		_previous = previous;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setViews(BigDecimal views) {
		_views = views;
	}

	private String _canonicalUrl;
	private Boolean _previous;
	private String _title;
	private BigDecimal _views;

}