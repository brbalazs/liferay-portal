/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.model.util.MetricUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Inácio Nery
 */
public class JournalMetric implements AssetMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof JournalMetric)) {
			return false;
		}

		JournalMetric journalMetric = (JournalMetric)obj;

		if (Objects.equals(_assetId, journalMetric._assetId) &&
			Objects.equals(_assetMetrics, journalMetric._assetMetrics) &&
			Objects.equals(_assetTitle, journalMetric._assetTitle) &&
			Objects.equals(_canonicalUrls, journalMetric._canonicalUrls) &&
			Objects.equals(_dataSourceId, journalMetric._dataSourceId) &&
			Objects.equals(_urls, journalMetric._urls) &&
			Objects.equals(_viewsMetric, journalMetric._viewsMetric)) {

			return true;
		}

		return false;
	}

	@Override
	public String getAssetId() {
		return _assetId;
	}

	@Override
	public List<AssetMetric> getAssetMetrics() {
		return _assetMetrics;
	}

	@Override
	public String getAssetTitle() {
		return _assetTitle;
	}

	@Override
	public String getAssetType() {
		return AssetType.JOURNAL.getValue();
	}

	@Override
	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	@Override
	public List<String> getCanonicalUrls() {
		return _canonicalUrls;
	}

	@Override
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public Metric getDefaultMetric() {
		return _viewsMetric;
	}

	@Override
	public List<String> getURLs() {
		return _urls;
	}

	public Metric getViewsMetric() {
		return _viewsMetric;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_assetId, _assetMetrics, _assetTitle, _canonicalUrls, _dataSourceId,
			_urls, _viewsMetric);
	}

	@Override
	public void setAssetId(String assetId) {
		_assetId = assetId;
	}

	@Override
	public void setAssetMetrics(List<AssetMetric> assetMetrics) {
		_assetMetrics = assetMetrics;
	}

	@Override
	public void setAssetTitle(String assetTitle) {
		_assetTitle = assetTitle;
	}

	@Override
	public void setCanonicalUrls(List<String> canonicalUrls) {
		_canonicalUrls = canonicalUrls;
	}

	@Override
	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	@Override
	public void setURLs(List<String> urls) {
		_urls = urls;
	}

	public void setViewsMetric(Metric viewsMetric) {
		_viewsMetric = viewsMetric;
	}

	private String _assetId;
	private List<AssetMetric> _assetMetrics;
	private String _assetTitle;
	private List<String> _canonicalUrls;
	private String _dataSourceId;
	private List<String> _urls;
	private Metric _viewsMetric = new Metric(JournalMetricType.VIEWS);

}