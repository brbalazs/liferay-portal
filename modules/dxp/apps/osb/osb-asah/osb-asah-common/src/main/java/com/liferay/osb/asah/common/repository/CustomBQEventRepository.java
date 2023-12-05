/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository;

import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.BreakdownRow;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.RecentVisitAsset;
import com.liferay.osb.asah.common.model.RecentVisitPage;
import com.liferay.osb.asah.common.model.RecentVisitSite;
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.TimeRange;

import java.math.BigDecimal;

import java.time.LocalDateTime;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Ivica Cardic
 */
public interface CustomBQEventRepository {

	public Integer countBQEvents(
		@Nullable Long channelId, @Nullable String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId,
		List<String> userIds);

	public Integer countBQEvents(
		@Nullable Long channelId, String individualId,
		@Nullable String keywords, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public Integer countBQEvents(
		String applicationId, String assetId, Long channelId, Long dataSourceId,
		String eventId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime);

	public long countByEventDefinitionId(long eventDefinitionId);

	public Integer countEventSessions(
		Long channelId, String individualId, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public long countTotalBQEvents(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId);

	public long countUniqueIndividuals(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId);

	public Optional<BQEvent> findLastSeenBQEvent(
		@Nullable Long eventDefinitionId);

	public BigDecimal getAverageBQEventCountPerIndividual(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId);

	public List<BreakdownRow> getBQEventPropertyValues(
		AnalysisType analysisType, Long channelId, boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		Pageable pageable, TimeRange timeRange, String timeZoneId);

	public long getBQEventPropertyValuesCount(
		Long channelId, EventAnalysisBreakdown eventAnalysisBreakdown,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		TimeRange timeRange, String timeZoneId);

	public Map<String, Integer> getBQEventsCountGroupByEventDate(
		Long channelId, String individualId, Interval interval, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public Map<String, Integer> getEventSessionsCountGroupByEventDate(
		Long channelId, String individualId, Interval interval, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

	public List<Map<String, String>>
		getKeywordsGroupedByChannelIdAndSessionIdAndUserId(Date eventDate);

	public Map<String, Date> getLastSeenDateDateGroupedByColumnName(
		String columnName, int size);

	public List<RecentVisitAsset> getRecentAssets(
		List<RecentVisitAsset.ContentType> contentTypes,
		@Nullable String groupId, String individualId, Pageable pageable,
		TimeRange timeRange, String timeZoneId);

	public Long getRecentAssetsCount(
		List<RecentVisitAsset.ContentType> contentTypes,
		@Nullable String groupId, String individualId, TimeRange timeRange,
		String timeZoneId);

	public List<RecentVisitPage> getRecentPages(
		@Nullable Long dataSourceId, @Nullable String displayLanguageId,
		@Nullable String groupId, String individualId, Pageable pageable,
		TimeRange timeRange, String timeZoneId);

	public Long getRecentPagesCount(
		@Nullable Long dataSourceId, @Nullable String displayLanguageId,
		@Nullable String groupId, String individualId, TimeRange timeRange,
		String timeZoneId);

	public List<RecentVisitSite> getRecentSites(
		@Nullable String individualId, Pageable pageable,
		@Nullable TimeRange timeRange, String timeZoneId);

	public long getRecentSitesCount(
		@Nullable String individualId, @Nullable TimeRange timeRange,
		String timeZoneId);

	public List<SearchKeyword> getSearchKeywords(
		@Nullable Long dataSourceId, @Nullable String displayLanguageId,
		@Nullable String groupId, @Nullable String individualId, int minCounts,
		Pageable pageable, Set<String> searchQueryParams,
		@Nullable TimeRange timeRange, String timeZoneId);

	public long getSearchKeywordsCount(
		@Nullable Long dataSourceId, @Nullable String displayLanguageId,
		@Nullable String groupId, @Nullable String individualId, int minCounts,
		Set<String> searchQueryParams, @Nullable TimeRange timeRange,
		String timeZoneId);

	public Map<String, BigDecimal> getSearchTerms(
		Long channelId, String[] searchQueryParams, int size, int start,
		TimeRange timeRange, String timeZoneId);

	public long getSearchTermsCount(
		Long channelId, String[] searchQueryParams, TimeRange timeRange,
		String timeZoneId);

	public BQEvent insert(BQEvent bqEvent);

	public List<BQEvent> searchBQEvents(
		@Nullable Long channelId, @Nullable String keywords, Pageable pageable,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId,
		List<String> userIds);

	public List<BQEvent> searchBQEvents(
		@Nullable Long channelId, String individualId,
		@Nullable String keywords, Pageable pageable,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId);

}