/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.Objects;

/**
 * @author Leonardo Barros
 */
public class PageReferrerMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof PageReferrerMetric)) {
			return false;
		}

		PageReferrerMetric pageReferrerMetric = (PageReferrerMetric)obj;

		if (Objects.equals(_accessMetric, pageReferrerMetric._accessMetric) &&
			Objects.equals(_assetTitle, pageReferrerMetric._assetTitle) &&
			Objects.equals(_external, pageReferrerMetric._external) &&
			Objects.equals(_referrer, pageReferrerMetric._referrer)) {

			return true;
		}

		return false;
	}

	public Metric getAccessMetric() {
		return _accessMetric;
	}

	public String getAssetTitle() {
		return _assetTitle;
	}

	public boolean getExternal() {
		return _external;
	}

	public String getReferrer() {
		return _referrer;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_accessMetric, _assetTitle, _external, _referrer);
	}

	public boolean isExternal() {
		return _external;
	}

	public void setAccessMetric(Metric accessMetric) {
		_accessMetric = accessMetric;
	}

	public void setAssetTitle(String assetTitle) {
		_assetTitle = assetTitle;
	}

	public void setExternal(boolean external) {
		_external = external;
	}

	public void setReferrer(String referrer) {
		_referrer = referrer;
	}

	private Metric _accessMetric = new Metric(PageReferrerMetricType.ACCESS);
	private String _assetTitle;
	private boolean _external;
	private String _referrer;

}