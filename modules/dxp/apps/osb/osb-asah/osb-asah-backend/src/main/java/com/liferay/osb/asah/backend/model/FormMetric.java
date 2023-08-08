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
 * @author Marcellus Tavares
 * @author Inácio Nery
 */
public class FormMetric implements AssetMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof FormMetric)) {
			return false;
		}

		FormMetric formMetric = (FormMetric)obj;

		if (Objects.equals(
				_abandonmentsMetric, formMetric._abandonmentsMetric) &&
			Objects.equals(_assetId, formMetric._assetId) &&
			Objects.equals(_assetMetrics, formMetric._assetMetrics) &&
			Objects.equals(_assetTitle, formMetric._assetTitle) &&
			Objects.equals(_canonicalUrls, formMetric._canonicalUrls) &&
			Objects.equals(
				_completionTimeMetric, formMetric._completionTimeMetric) &&
			Objects.equals(_dataSourceId, formMetric._dataSourceId) &&
			Objects.equals(_submissionsMetric, formMetric._submissionsMetric) &&
			Objects.equals(_urls, formMetric._urls) &&
			Objects.equals(_viewsMetric, formMetric._viewsMetric)) {

			return true;
		}

		return false;
	}

	public Metric getAbandonmentsMetric() {
		return _abandonmentsMetric;
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
		return AssetType.FORM.getValue();
	}

	@Override
	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	@Override
	public List<String> getCanonicalUrls() {
		return _canonicalUrls;
	}

	public Metric getCompletionTimeMetric() {
		return _completionTimeMetric;
	}

	@Override
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public Metric getDefaultMetric() {
		return _submissionsMetric;
	}

	public Metric getSubmissionsMetric() {
		return _submissionsMetric;
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
			_abandonmentsMetric, _assetId, _assetMetrics, _assetTitle,
			_canonicalUrls, _completionTimeMetric, _dataSourceId,
			_submissionsMetric, _urls, _viewsMetric);
	}

	public void setAbandonmentsMetric(Metric abandonmentsMetric) {
		_abandonmentsMetric = abandonmentsMetric;
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

	public void setCompletionTimeMetric(Metric completionTimeMetric) {
		_completionTimeMetric = completionTimeMetric;
	}

	@Override
	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setSubmissionsMetric(Metric submissionsMetric) {
		_submissionsMetric = submissionsMetric;
	}

	@Override
	public void setURLs(List<String> urls) {
		_urls = urls;
	}

	public void setViewsMetric(Metric viewsMetric) {
		_viewsMetric = viewsMetric;
	}

	private Metric _abandonmentsMetric = new Metric(
		FormMetricType.ABANDONMENTS);
	private String _assetId;
	private List<AssetMetric> _assetMetrics;
	private String _assetTitle;
	private List<String> _canonicalUrls;
	private Metric _completionTimeMetric = new Metric(
		FormMetricType.COMPLETION_TIME);
	private String _dataSourceId;
	private Metric _submissionsMetric = new Metric(FormMetricType.SUBMISSIONS);
	private List<String> _urls;
	private Metric _viewsMetric = new Metric(FormMetricType.VIEWS);

}