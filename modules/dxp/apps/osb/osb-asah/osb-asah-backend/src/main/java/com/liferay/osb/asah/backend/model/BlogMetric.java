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
public class BlogMetric implements AssetMetric {

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BlogMetric)) {
			return false;
		}

		BlogMetric blogMetric = (BlogMetric)obj;

		if (Objects.equals(_assetId, blogMetric._assetId) &&
			Objects.equals(_assetMetrics, blogMetric._assetMetrics) &&
			Objects.equals(_assetTitle, blogMetric._assetTitle) &&
			Objects.equals(_canonicalUrls, blogMetric._canonicalUrls) &&
			Objects.equals(_clicksMetric, blogMetric._clicksMetric) &&
			Objects.equals(_commentsMetric, blogMetric._commentsMetric) &&
			Objects.equals(_dataSourceId, blogMetric._dataSourceId) &&
			Objects.equals(_ratingsMetric, blogMetric._ratingsMetric) &&
			Objects.equals(_readingTimeMetric, blogMetric._readingTimeMetric) &&
			Objects.equals(_urls, blogMetric._urls) &&
			Objects.equals(_viewsMetric, blogMetric._viewsMetric)) {

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
		return AssetType.BLOG.getValue();
	}

	@Override
	public Set<Metric> getAvailableMetrics() {
		return MetricUtil.getAvailableMetrics(this);
	}

	@Override
	public List<String> getCanonicalUrls() {
		return _canonicalUrls;
	}

	public Metric getClicksMetric() {
		return _clicksMetric;
	}

	public Metric getCommentsMetric() {
		return _commentsMetric;
	}

	@Override
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@Override
	public Metric getDefaultMetric() {
		return _viewsMetric;
	}

	public Metric getRatingsMetric() {
		return _ratingsMetric;
	}

	public Metric getReadingTimeMetric() {
		return _readingTimeMetric;
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
			_assetId, _assetMetrics, _assetTitle, _canonicalUrls, _clicksMetric,
			_commentsMetric, _dataSourceId, _ratingsMetric, _readingTimeMetric,
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

	public void setClicksMetric(Metric clicksMetric) {
		_clicksMetric = clicksMetric;
	}

	public void setCommentsMetric(Metric commentsMetric) {
		_commentsMetric = commentsMetric;
	}

	@Override
	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setRatingsMetric(Metric ratingsMetric) {
		_ratingsMetric = ratingsMetric;
	}

	public void setReadingTimeMetric(Metric readingTimeMetric) {
		_readingTimeMetric = readingTimeMetric;
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
	private Metric _clicksMetric = new Metric(BlogMetricType.CLICKS);
	private Metric _commentsMetric = new Metric(BlogMetricType.COMMENTS);
	private String _dataSourceId;
	private Metric _ratingsMetric = new Metric(BlogMetricType.RATINGS);
	private Metric _readingTimeMetric = new Metric(BlogMetricType.READING_TIME);
	private List<String> _urls;
	private Metric _viewsMetric = new Metric(BlogMetricType.VIEWS);

}