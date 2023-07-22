/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.BQEventProperty;
import com.liferay.osb.asah.common.entity.BQSession;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.Preference;
import com.liferay.osb.asah.common.json.JSONUtil;
import com.liferay.osb.asah.common.model.BQEventPropertyValue;
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
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
			null, null, createDate, dataSourceId, null, null, eventDate,
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
			null, null, createDate, dataSourceId, null, null, eventDate,
			eventId, null, id, null, null, null, null, null, null, sessionId,
			null, title, null, userId, null);
	}

	@VisibleForTestingOnly
	public BQEvent addBQEvent(
			String applicationId, Set<BQEventProperty> bqEventProperties,
			String browserName, String canonicalUrl, Long channelId,
			String city, String contentLanguageId, String context,
			String country, Date createDate, Long dataSourceId,
			String description, String deviceType, Date eventDate,
			String eventId, String experienceId, String id, String keywords,
			String languageId, String platformName, String projectTimeZoneId,
			String referrer, String region, String sessionId,
			String timezoneOffset, String title, String url, String userId,
			String variantId)
		throws Exception {

		BQEvent bqEvent = _bqEventRepository.insert(
			new BQEvent(
				applicationId, browserName, canonicalUrl, channelId, city,
				contentLanguageId, context, country, createDate, dataSourceId,
				description, deviceType, eventDate, eventId,
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
		@Nullable Long channelId, @Nullable String individualId,
		@Nullable String keywords, int page, int size, TimeRange timeRange) {

		PageRequest pageRequest = PageRequest.of(
			page, size, Sort.desc("eventDate"));

		String timeZoneId = _timeZoneDog.getTimeZoneId();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.searchBQEvents(
				channelId, individualId, keywords, pageRequest,
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), timeZoneId),
			pageRequest,
			() -> _bqEventRepository.countBQEvents(
				channelId, individualId, keywords,
				timeRange.getEndLocalDateTime(),
				timeRange.getStartLocalDateTime(), timeZoneId));
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

	public Page<SearchKeyword> getSearchKeywordPage(
		@Nullable String displayLanguageId, @Nullable String groupId,
		int minCounts, int page, int size,
		org.springframework.data.domain.Sort sort) {

		Set<String> searchQueryStrings = _getSearchQueryStrings();

		return PageableExecutionUtils.getPage(
			_bqEventRepository.getSearchKeywords(
				displayLanguageId, groupId, minCounts,
				PageRequest.of(page, size, sort), searchQueryStrings),
			PageRequest.of(page, size, sort),
			() -> _bqEventRepository.getSearchKeywordsCount(
				displayLanguageId, groupId, minCounts, searchQueryStrings));
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

	@Autowired
	private BQEventPropertyRepository _bqEventPropertyRepository;

	@Autowired
	private BQEventRepository _bqEventRepository;

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