/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.model;

import java.util.List;
import java.util.Set;

/**
 * @author Inácio Nery
 */
public interface AssetMetric {

	public String getAssetId();

	public List<AssetMetric> getAssetMetrics();

	public String getAssetTitle();

	public String getAssetType();

	public Set<Metric> getAvailableMetrics();

	public List<String> getCanonicalUrls();

	public String getDataSourceId();

	public Metric getDefaultMetric();

	public List<String> getURLs();

	public void setAssetId(String assetId);

	public void setAssetMetrics(List<AssetMetric> assetMetrics);

	public void setAssetTitle(String assetTitle);

	public void setCanonicalUrls(List<String> canonicalUrls);

	public void setDataSourceId(String dataSourceId);

	public void setURLs(List<String> urls);

}