/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.model.AdjacentPageViewsMetric;
import com.liferay.osb.asah.backend.repository.PagePathRepository;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.model.TimeRange;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Marcellus Tavares
 */
@Component
public class PagePathDog {

	public Set<AdjacentPageViewsMetric> getAdjacentPagesViewsMetric(
		String canonicalUrl, @Nullable Long channelId, @Nullable Long segmentId,
		TimeRange timeRange, @Nullable String title) {

		return _pagePathRepository.getAdjacentPagesViewsMetric(
			canonicalUrl, channelId, segmentId, timeRange, title,
			_timeZoneDog.getZoneId());
	}

	@Autowired
	private PagePathRepository _pagePathRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}