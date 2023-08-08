/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import com.liferay.osb.asah.backend.model.util.MetricUtil;
import com.liferay.osb.asah.common.model.IndividualMetricType;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author Matthew Kong
 */
public class IndividualMetric implements AssetMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof IndividualMetric)) {
			return false;
		}

		IndividualMetric individualMetric = (IndividualMetric)obj;

		if (Objects.equals(
				_anonymousIndividualsMetric,
				individualMetric._anonymousIndividualsMetric) &&
			Objects.equals(
				_knownIndividualsMetric,
				individualMetric._knownIndividualsMetric) &&
			Objects.equals(
				_totalIndividualsMetric,
				individualMetric._totalIndividualsMetric)) {

			return true;
		}

		return false;
	}

	public Metric getAnonymousIndividualsMetric() {
		return _anonymousIndividualsMetric;
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
		return AssetType.INDIVIDUAL_METRIC.getValue();
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
		return _knownIndividualsMetric;
	}

	public Metric getKnownIndividualsMetric() {
		return _knownIndividualsMetric;
	}

	public Metric getTotalIndividualsMetric() {
		return _totalIndividualsMetric;
	}

	@Override
	public List<String> getURLs() {
		return _urls;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_anonymousIndividualsMetric, _knownIndividualsMetric);
	}

	public void setAnonymousIndividualsMetric(
		Metric anonymousIndividualsMetric) {

		_anonymousIndividualsMetric = anonymousIndividualsMetric;
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

	public void setKnownIndividualsMetric(Metric knownIndividualsMetric) {
		_knownIndividualsMetric = knownIndividualsMetric;
	}

	public void setTotalIndividualsMetric(Metric totalIndividualsMetric) {
		_totalIndividualsMetric = totalIndividualsMetric;
	}

	@Override
	public void setURLs(List<String> urls) {
		_urls = urls;
	}

	private Metric _anonymousIndividualsMetric = new Metric(
		IndividualMetricType.ANONYMOUS_INDIVIDUALS);
	private String _assetId;
	private List<AssetMetric> _assetMetrics;
	private String _assetTitle;
	private List<String> _canonicalUrls;
	private String _dataSourceId;
	private Metric _knownIndividualsMetric = new Metric(
		IndividualMetricType.KNOWN_INDIVIDUALS);
	private Metric _totalIndividualsMetric = new Metric(
		IndividualMetricType.TOTAL_INDIVIDUALS);
	private List<String> _urls;

}