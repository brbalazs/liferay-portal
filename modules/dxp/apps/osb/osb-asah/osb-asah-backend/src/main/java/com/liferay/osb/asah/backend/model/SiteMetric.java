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
 * @author Rachael Koestartyo
 */
public class SiteMetric implements AssetMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SiteMetric)) {
			return false;
		}

		SiteMetric siteMetric = (SiteMetric)obj;

		if (Objects.equals(
				_anonymousVisitorsMetric,
				siteMetric._anonymousVisitorsMetric) &&
			Objects.equals(_assetId, siteMetric._assetId) &&
			Objects.equals(_assetMetrics, siteMetric._assetMetrics) &&
			Objects.equals(_assetTitle, siteMetric._assetTitle) &&
			Objects.equals(_bounceRateMetric, siteMetric._bounceRateMetric) &&
			Objects.equals(_canonicalUrls, siteMetric._canonicalUrls) &&
			Objects.equals(_dataSourceId, siteMetric._dataSourceId) &&
			Objects.equals(
				_knownVisitorsMetric, siteMetric._knownVisitorsMetric) &&
			Objects.equals(
				_sessionDurationMetric, siteMetric._sessionDurationMetric) &&
			Objects.equals(_sessionsMetric, siteMetric._sessionsMetric) &&
			Objects.equals(
				_sessionsPerVisitorMetric,
				siteMetric._sessionsPerVisitorMetric) &&
			Objects.equals(_visitorsMetric, siteMetric._visitorsMetric)) {

			return true;
		}

		return false;
	}

	public Metric getAnonymousVisitorsMetric() {
		return _anonymousVisitorsMetric;
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
		return AssetType.SITE.getValue();
	}

	@Override
	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	public Metric getBounceRateMetric() {
		return _bounceRateMetric;
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
		return _visitorsMetric;
	}

	public Metric getKnownVisitorsMetric() {
		return _knownVisitorsMetric;
	}

	public Metric getSessionDurationMetric() {
		return _sessionDurationMetric;
	}

	public Metric getSessionsMetric() {
		return _sessionsMetric;
	}

	public Metric getSessionsPerVisitorMetric() {
		return _sessionsPerVisitorMetric;
	}

	@Override
	public List<String> getURLs() {
		return _urls;
	}

	public Metric getVisitorsMetric() {
		return _visitorsMetric;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_anonymousVisitorsMetric, _assetId, _assetMetrics, _assetTitle,
			_bounceRateMetric, _dataSourceId, _knownVisitorsMetric,
			_sessionDurationMetric, _sessionsMetric, _sessionsPerVisitorMetric,
			_visitorsMetric);
	}

	public void setAnonymousVisitorsMetric(Metric anonymousVisitorsMetric) {
		_anonymousVisitorsMetric = anonymousVisitorsMetric;
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

	public void setBounceRateMetric(Metric bounceRateMetric) {
		_bounceRateMetric = bounceRateMetric;
	}

	@Override
	public void setCanonicalUrls(List<String> canonicalUrls) {
		_canonicalUrls = canonicalUrls;
	}

	@Override
	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setKnownVisitorsMetric(Metric knownVisitorsMetric) {
		_knownVisitorsMetric = knownVisitorsMetric;
	}

	public void setSessionDurationMetric(Metric sessionDurationMetric) {
		_sessionDurationMetric = sessionDurationMetric;
	}

	public void setSessionsMetric(Metric sessionsMetric) {
		_sessionsMetric = sessionsMetric;
	}

	public void setSessionsPerVisitorMetric(Metric sessionsPerVisitorMetric) {
		_sessionsPerVisitorMetric = sessionsPerVisitorMetric;
	}

	@Override
	public void setURLs(List<String> urls) {
		_urls = urls;
	}

	public void setVisitorsMetric(Metric visitorsMetric) {
		_visitorsMetric = visitorsMetric;
	}

	private Metric _anonymousVisitorsMetric = new Metric(
		SiteMetricType.ANONYMOUS_VISITORS);
	private String _assetId;
	private List<AssetMetric> _assetMetrics;
	private String _assetTitle;
	private Metric _bounceRateMetric = new Metric(SiteMetricType.BOUNCE_RATE);
	private List<String> _canonicalUrls;
	private String _dataSourceId;
	private Metric _knownVisitorsMetric = new Metric(
		SiteMetricType.KNOWN_VISITORS);
	private Metric _sessionDurationMetric = new Metric(
		SiteMetricType.SESSION_DURATION);
	private Metric _sessionsMetric = new Metric(SiteMetricType.SESSIONS);
	private Metric _sessionsPerVisitorMetric = new Metric(
		SiteMetricType.SESSIONS_PER_VISITOR);
	private List<String> _urls;
	private Metric _visitorsMetric = new Metric(SiteMetricType.VISITORS);

}