/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dog;

import com.liferay.osb.asah.backend.dog.helper.SearchQueryContext;
import com.liferay.osb.asah.backend.dog.title.TitleDog;
import com.liferay.osb.asah.backend.repository.PageReferrerRepository;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

/**
 * @author Inácio Nery
 */
@Component
public class PageReferrerDog {

	public Map<String, Double> getAcquisitionChannels(
		SearchQueryContext searchQueryContext) {

		return _pageReferrerRepository.getAcquisitionChannelAccesses(
			searchQueryContext.getCanonicalUrl(),
			searchQueryContext.getChannelIdAsLong(),
			searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());
	}

	public Map<String, Double> getPageReferrers(
		String fieldName, SearchQueryContext searchQueryContext, int size) {

		if (Objects.equals(fieldName, "referrerHost")) {
			return _pageReferrerRepository.
				getSocialPageReferrerAccessesByReferrerHost(
					searchQueryContext.getCanonicalUrl(),
					searchQueryContext.getChannelIdAsLong(),
					PageRequest.of(0, size), searchQueryContext.getTimeRange(),
					_timeZoneDog.getZoneId());
		}

		return _pageReferrerRepository.
			getSocialPageReferrerAccessesByReferrerCanonicalUrl(
				searchQueryContext.getCanonicalUrl(),
				searchQueryContext.getChannelIdAsLong(),
				PageRequest.of(0, size), searchQueryContext.getTimeRange(),
				_timeZoneDog.getZoneId());
	}

	public Map<String, Double> getSocialPageReferrers(
		SearchQueryContext searchQueryContext) {

		Map<String, Double> socialPageReferrerAccessesByReferrerHost =
			_pageReferrerRepository.getSocialPageReferrerAccessesByReferrerHost(
				searchQueryContext.getCanonicalUrl(),
				searchQueryContext.getChannelIdAsLong(), PageRequest.of(0, 20),
				searchQueryContext.getTimeRange(), _timeZoneDog.getZoneId());

		Map<String, Double> starredSocialReferrers = new HashMap<>();

		for (Map.Entry<String, List<String>> entry :
				_socialHostNames.entrySet()) {

			for (String referrerHost : entry.getValue()) {
				Double accesses = socialPageReferrerAccessesByReferrerHost.get(
					referrerHost);

				if ((accesses != null) && (accesses != 0)) {
					Double value = starredSocialReferrers.getOrDefault(
						entry.getKey(), 0D);

					starredSocialReferrers.put(
						entry.getKey(), value + accesses);
				}
			}
		}

		Double socialReferrersAccessesSum = _sumMapValues(
			socialPageReferrerAccessesByReferrerHost);

		Double starredSocialReferrersAccessesSum = _sumMapValues(
			starredSocialReferrers);

		if (socialReferrersAccessesSum > starredSocialReferrersAccessesSum) {
			starredSocialReferrers.put(
				"other",
				socialReferrersAccessesSum - starredSocialReferrersAccessesSum);
		}

		Set<Map.Entry<String, Double>> set = starredSocialReferrers.entrySet();

		Stream<Map.Entry<String, Double>> stream = set.stream();

		return stream.sorted(
			Map.Entry.comparingByValue(Comparator.reverseOrder())
		).collect(
			Collectors.toMap(
				Map.Entry::getKey, Map.Entry::getValue,
				(value1, value2) -> value1, LinkedHashMap::new)
		);
	}

	private Double _sumMapValues(Map<String, Double> map) {
		Double sum = 0D;

		for (Double value : map.values()) {
			sum += value;
		}

		return sum;
	}

	private static final Map<String, List<String>> _socialHostNames =
		new HashMap<String, List<String>>() {
			{
				put("facebook", Collections.singletonList("facebook.com"));
				put("instagram", Collections.singletonList("instagram.com"));
				put("linkedin", Collections.singletonList("linkedin.com"));
				put("pinterest", Collections.singletonList("pinterest.com"));
				put("snapchat", Collections.singletonList("snapchat.com"));
				put("tiktok", Collections.singletonList("tiktok.com"));
				put("twitter", Arrays.asList("twitter.com", "t.co"));
				put("youtube", Collections.singletonList("youtube.com"));
			}
		};

	@Autowired
	private PageDog _pageDog;

	@Autowired
	private PageReferrerRepository _pageReferrerRepository;

	@Autowired
	private TimeZoneDog _timeZoneDog;

	@Autowired
	private TitleDog _titleDog;

}