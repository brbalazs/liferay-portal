/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.util.SortUtil;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.BQEventPropertyValue;
import com.liferay.osb.asah.common.model.RecentPage;
import com.liferay.osb.asah.common.model.RecentSite;
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.repository.BQEventPropertyRepository;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.spring.annotation.VisibleForTestingOnly;
import com.liferay.osb.asah.common.util.StringUtil;

import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class BQEventDog {

	@VisibleForTestingOnly
	public BQEvent addBQEvent(
			String applicationId, Set<BQEventProperty> bqEventProperties,
			Long channelId, Date createDate, Long dataSourceId, Date eventDate,
			String eventId, String id, String sessionId, String userId)
		throws Exception {

		return addBQEvent(
			applicationId, bqEventProperties, null, null, channelId, null, null,
			null, null, createDate, dataSourceId, null, null, null, eventDate,
			eventId, null, id, null, null, null, null, null, null, sessionId,
			null, null, null, userId, null);
	}

	@VisibleForTestingOnly
	public BQEvent addBQEvent(
			String applicationId, Set<BQEventProperty> bqEventProperties,
			Long channelId, Date createDate, Long dataSourceId, Date eventDate,
			String eventId, String id, String sessionId, String title,
			String userId)
		throws Exception {

		return addBQEvent(
			applicationId, bqEventProperties, null, null, channelId, null, null,
			null, null, createDate, dataSourceId, null, null, null, eventDate,
			eventId, null, id, null, null, null, null, null, null, sessionId,
			null, title, null, userId, null);
	}

	@VisibleForTestingOnly
	public BQEvent addBQEvent(
			String applicationId, Set<BQEventProperty> bqEventProperties,
			String browserName, String canonicalUrl, Long channelId,
			String city, String contentLanguageId, String context,
			String country, Date createDate, Long dataSourceId,
			String description, String deviceType, String emailAddressHashed,
			Date eventDate, String eventId, String experienceId, String id,
			String keywords, String languageId, String platformName,
			String projectTimeZoneId, String referrer, String region,
			String sessionId, String timezoneOffset, String title, String url,
			String userId, String variantId)
		throws Exception {

		BQEvent bqEvent = _bqEventRepository.insert(
			new BQEvent(
				applicationId, browserName, canonicalUrl, channelId, city,
				contentLanguageId, context, country, createDate, dataSourceId,
				description, deviceType, emailAddressHashed, eventDate, eventId,
				_objectMapper.writeValueAsString(bqEventProperties),
				experienceId, id, keywords, languageId, platformName,
				projectTimeZoneId, referrer, region, sessionId, timezoneOffset,
				title, url, userId, variantId));

		for (BQEventProperty bqEventProperty : bqEventProperties) {
			bqEventProperty.setId(bqEvent.getId());

			_bqEventPropertyRepository.insert(bqEventProperty);
		}

		return bqEvent;
	}

	public Integer countBQEvents(
		Long channelId, @Nullable String individualId,
		@Nullable String keywords, TimeRange timeRange) {

		return _bqEventRepository.countBQEvents(
			channelId, individualId, keywords, timeRange.getEndLocalDateTime(),
			timeRange.getStartLocalDateTime(), _timeZoneDog.getTimeZoneId());
	}

	public Integer countBQEvents(
		String applicationId, String assetId, Long channelId, Long dataSourceId,
		LocalDate endLocalDate, String eventId, LocalDate startLocalDate) {

		return _bqEventRepository.countBQEvents(
			assetId, applicationId, channelId, dataSourceId, eventId,
			endLocalDate.atTime(LocalTime.MAX),
			startLocalDate.atTime(LocalTime.MIN));
	}

	public Page<BQEvent> getBQEventPage(
		@Nullable Long channelId, @Nullable String keywords, int page, int size,
		TimeRange timeRange, List<String> userIds) {

		PageRequest pageRequest = PageRequest.of(
			page, size, Sort.desc("eventDate"));

		if (userIds.isEmpty()) {
			return PageableExecutionUtils.getPage(
				Collections.emptyList(), pageRequest, () -> 0);
		}

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.searchBQEvents(
				channelId, keywords, pageRequest,
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), timeZoneId, userIds),
			pageRequest,
			() -> _bqEventRepository.countBQEvents(
				channelId, keywords, timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), timeZoneId, userIds));
	}

	public List<BQEventPropertyValue> getRecentBQEventPropertyValues(
		Long eventAttributeDefinitionId, int size) {

		List<BQEventPropertyValue> recentBQEventPropertyValues =
			new ArrayList<>();

		Map<String, Date> bqEventPropertyValues =
			_bqEventPropertyRepository.
				findBQEventPropertyValuesByEventAttributeDefinitionName(
					_getEventAttributeDefinitionName(
						eventAttributeDefinitionId),
					size);

		for (Map.Entry<String, Date> entry : bqEventPropertyValues.entrySet()) {
			recentBQEventPropertyValues.add(
				new BQEventPropertyValue(entry.getValue(), entry.getKey()));
		}

		return recentBQEventPropertyValues;
	}

	public Map<String, Date> getRecentGlobalBQEventProperyValues(
		String columnName, int size) {

		return _bqEventRepository.getLastSeenDateDateGroupedByColumnName(
			columnName, size);
	}

	public Page<RecentPage> getRecentPagesPage(
		@Nullable String displayLanguageId, String individualId, int page,
		int rangeKey, int size, String[] sorts) {

		Pageable pageable = PageRequest.of(
			page, size,
			SortUtil.getSort(
				org.springframework.data.domain.Sort.by(
					org.springframework.data.domain.Sort.Order.desc("counts")),
				sorts));

		if (_dataControlTaskDog.isSuppressedEmailAddress(individualId)) {
			return PageableExecutionUtils.getPage(
				Collections.emptyList(), pageable, () -> 0);
		}

		TimeRange timeRange = TimeRange.of(rangeKey);

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.getRecentPages(
				displayLanguageId, individualId, pageable, timeRange,
				timeZoneId),
			pageable,
			() -> _bqEventRepository.getRecentPagesCount(
				displayLanguageId, individualId, timeRange, timeZoneId));
	}

	public Page<RecentSite> getRecentSitePage(
		String individualId, int page, int size, String[] sorts,
		TimeRange timeRange) {

		Pageable pageable = PageRequest.of(page, size, _getSort(sorts));

		if ((individualId != null) &&
			_dataControlTaskDog.isSuppressedEmailAddress(individualId)) {

			return PageableExecutionUtils.getPage(
				Collections.emptyList(), pageable, () -> 0);
		}

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.getRecentSites(
				individualId, pageable, timeRange, timeZoneId),
			pageable,
			() -> _bqEventRepository.getRecentSitesCount(
				individualId, timeRange, timeZoneId));
	}

	public Page<SearchKeyword> getSearchKeywordPage(
		@Nullable String displayLanguageId, @Nullable String groupId,
		@Nullable String individualId, int minCounts, int page, int size,
		String[] sorts, @Nullable TimeRange timeRange) {

		Pageable pageable = PageRequest.of(page, size, _getSort(sorts));

		if (_dataControlTaskDog.isSuppressedEmailAddress(individualId)) {
			return PageableExecutionUtils.getPage(
				Collections.emptyList(), pageable, () -> 0);
		}

		Set<String> searchQueryStrings = _getSearchQueryStrings();

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.getSearchKeywords(
				displayLanguageId, groupId, individualId, minCounts, pageable,
				searchQueryStrings, timeRange, timeZoneId),
			pageable,
			() -> _bqEventRepository.getSearchKeywordsCount(
				displayLanguageId, groupId, individualId, minCounts,
				searchQueryStrings, timeRange, timeZoneId));
	}

	public List<BQEvent> searchBQEvents(
		Long channelId, @Nullable String individualId,
		@Nullable String keywords, int page, int size, TimeRange timeRange) {

		return _bqEventRepository.searchBQEvents(
			channelId, individualId, keywords,
			PageRequest.of(page, size, Sort.desc("eventDate")),
			timeRange.getEndLocalDateTime(), timeRange.getStartLocalDateTime(),
			_timeZoneDog.getTimeZoneId());
	}

	public Map<BQSession, List<BQEvent>> searchBQEventsGroupByUserSessionId(
		Long channelId, String individualId, String keywords, int page,
		int size, TimeRange timeRange) {

		Set<String> userSessionIds = new HashSet<>();

		List<BQEvent> bqEvents = searchBQEvents(
			channelId, individualId, keywords, page, size, timeRange);

		bqEvents.forEach(bqEvent -> userSessionIds.add(bqEvent.getSessionId()));

		List<BQSession> bqSessions = _userSessionDog.findByIds(userSessionIds);

		Stream<BQSession> bqSessionsStream = bqSessions.stream();

		Map<String, BQSession> bqSessionMap = bqSessionsStream.collect(
			Collectors.toMap(BQSession::getId, bqSession -> bqSession));

		Stream<BQEvent> bqEventsStream = bqEvents.stream();

		return bqEventsStream.collect(
			Collectors.groupingBy(
				bqEvent -> bqSessionMap.computeIfAbsent(
					bqEvent.getSessionId(),
					sessionId -> {
						BQSession bqSession = new BQSession();

						bqSession.setId(bqEvent.getSessionId());
						bqSession.setSessionStart(bqEvent.getEventDate());

						return bqSession;
					})));
	}

	private String _getEventAttributeDefinitionName(
		Long eventAttributeDefinitionId) {

		EventAttributeDefinition eventAttributeDefinition =
			_eventAttributeDefinitionDog.getEventAttributeDefinition(
				eventAttributeDefinitionId);

		return eventAttributeDefinition.getName();
	}

	private Set<String> _getSearchQueryStrings() {
		Preference preference = _preferenceDog.getPreference(
			"search-query-strings");

		Set<String> searchQueryStrings = new HashSet<>();

		String preferenceValue = preference.getValue();

		if (!StringUtil.isNull(preferenceValue)) {
			searchQueryStrings = JSONUtil.toStringSet(
				new JSONArray(preferenceValue));
		}

		searchQueryStrings.add("q");

		return searchQueryStrings;
	}

	private org.springframework.data.domain.Sort _getSort(String[] sorts) {
		if (ArrayUtils.isEmpty(sorts)) {
			return org.springframework.data.domain.Sort.by(
				org.springframework.data.domain.Sort.Order.desc("counts"));
		}

		List<org.springframework.data.domain.Sort.Order> orders =
			new ArrayList<>();

		for (int i = 0; i < sorts.length; i++) {
			String sort = sorts[i];

			String order = null;

			String[] properties = sort.split(",");

			if (properties.length == 1) {
				order = sorts[++i];
			}
			else {
				order = properties[1];
			}

			if (Objects.equals(order, "asc")) {
				orders.add(
					org.springframework.data.domain.Sort.Order.asc(
						StringUtils.lowerCase(properties[0])));
			}
			else {
				orders.add(
					org.springframework.data.domain.Sort.Order.desc(
						StringUtils.lowerCase(properties[0])));
			}
		}

		return org.springframework.data.domain.Sort.by(orders);
	}

	@Autowired
	private BQEventPropertyRepository _bqEventPropertyRepository;

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private DataControlTaskDog _dataControlTaskDog;

	@Autowired
	private EventAttributeDefinitionDog _eventAttributeDefinitionDog;

	@Autowired
	private ObjectMapper _objectMapper;

	@Autowired
	private PreferenceDog _preferenceDog;

	@Autowired
	private TimeZoneDog _timeZoneDog;

	@Autowired
	private UserSessionDog _userSessionDog;

}