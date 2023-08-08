/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.repository;

import com.liferay.osb.asah.common.model.TimeRange;

import java.time.ZoneId;

import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Marcellus Tavares
 */
public interface PageReferrerRepository {

	public Map<String, Double> getAcquisitionChannelAccesses(
		String canonicalUrl, @Nullable Long channelId, TimeRange timeRange,
		ZoneId zoneId);

	public Map<String, Double> getPageReferrerAccesses(
		String canonicalUrl, @Nullable Long channelId, TimeRange timeRange,
		@Nullable String title, ZoneId zoneId);

	public Map<String, Double>
		getSocialPageReferrerAccessesByReferrerCanonicalUrl(
			String canonicalUrl, @Nullable Long channelId, Pageable pageable,
			TimeRange timeRange, ZoneId zoneId);

	public Map<String, Double> getSocialPageReferrerAccessesByReferrerHost(
		String canonicalUrl, @Nullable Long channelId, Pageable pageable,
		TimeRange timeRange, ZoneId zoneId);

}