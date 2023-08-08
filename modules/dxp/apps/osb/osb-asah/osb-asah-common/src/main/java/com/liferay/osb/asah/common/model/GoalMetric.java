/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

/**
 * @author Marcellus Tavares
 */
public enum GoalMetric {

	BOUNCE_RATE(true, PageMetricType.BOUNCE),
	CLICK_RATE(false, PageMetricType.CTA_CLICKS);

	public PageMetricType getPageMetricType() {
		return _pageMetricType;
	}

	public boolean isInverseMetric() {
		return _inverseMetric;
	}

	private GoalMetric(boolean inverseMetric, PageMetricType pageMetricType) {
		_inverseMetric = inverseMetric;
		_pageMetricType = pageMetricType;
	}

	private final boolean _inverseMetric;
	private final PageMetricType _pageMetricType;

}