/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.util.MapUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Leslie Wong
 */
public class AnalyticsEvents {

	public AnalyticsEvents(List<AnalyticsEvent> analyticsEventsList) {
		_analyticsEventsList = analyticsEventsList;

		int interactionsCount = 0;
		int pageViewsCount = 0;

		for (AnalyticsEvent analyticsEvent : analyticsEventsList) {
			if (!_noninteractionEvents.contains(analyticsEvent.getEventId())) {
				interactionsCount++;
			}

			if (Objects.equals(analyticsEvent.getApplicationId(), "Page") &&
				Objects.equals(analyticsEvent.getEventId(), "pageViewed")) {

				pageViewsCount++;
			}

			Map<String, String> context = analyticsEvent.getContext();

			String canonicalUrl = MapUtil.getString(context, "canonicalUrl");

			if (!StringUtil.isNull(canonicalUrl)) {
				_canonicalUrls.add(canonicalUrl);
			}

			String referrer = MapUtil.getString(context, "referrer");

			if (!StringUtil.isNull(referrer)) {
				_referrers.add(referrer);
			}

			String url = MapUtil.getString(context, "url");

			if (!StringUtil.isNull(url)) {
				_urls.add(url);
			}
		}

		_interactionsCount = interactionsCount;
		_pageViewsCount = pageViewsCount;
	}

	public List<AnalyticsEvent> getAnalyticsEventsList() {
		return _analyticsEventsList;
	}

	public Set<String> getCanonicalUrls() {
		return _canonicalUrls;
	}

	public AnalyticsEvent getFirstAnalyticsEvent() {
		return _analyticsEventsList.get(0);
	}

	public Date getFirstAnalyticsEventDate() {
		AnalyticsEvent firstAnalyticsEvent = getFirstAnalyticsEvent();

		return firstAnalyticsEvent.getEventDate();
	}

	public long getInteractionsCount() {
		return _interactionsCount;
	}

	public AnalyticsEvent getLastAnalyticsEvent() {
		return _analyticsEventsList.get(_analyticsEventsList.size() - 1);
	}

	public Date getLastAnalyticsEventDate() {
		AnalyticsEvent lastAnalyticsEvent = getLastAnalyticsEvent();

		return lastAnalyticsEvent.getEventDate();
	}

	public long getPageViewsCount() {
		return _pageViewsCount;
	}

	public Set<String> getReferrers() {
		return _referrers;
	}

	public int getSize() {
		return _analyticsEventsList.size();
	}

	public Set<String> getUrls() {
		return _urls;
	}

	private static final Set<String> _noninteractionEvents =
		new HashSet<String>() {
			{
				add("blogViewed");
				add("documentPreviewed");
				add("formViewed");
				add("pageLoaded");
				add("pageUnloaded");
				add("pageViewed");
				add("webContentViewed");
			}
		};

	private final List<AnalyticsEvent> _analyticsEventsList;
	private final Set<String> _canonicalUrls = new HashSet<>();
	private final long _interactionsCount;
	private final long _pageViewsCount;
	private final Set<String> _referrers = new HashSet<>();
	private final Set<String> _urls = new HashSet<>();

}