/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.repository.impl;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.AttributeType;
import com.liferay.osb.asah.common.model.BreakdownRow;
import com.liferay.osb.asah.common.model.DateGrouping;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.Interval;
import com.liferay.osb.asah.common.model.SearchKeyword;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.filter.FilterOperator;
import com.liferay.osb.asah.common.model.filter.FilterOperators;
import com.liferay.osb.asah.common.repository.CustomBQEventRepository;
import com.liferay.osb.asah.common.repository.EventAttributeDefinitionRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.common.repository.executor.QueryExecutor;
import com.liferay.osb.asah.common.repository.helper.DSLHelper;
import com.liferay.osb.asah.common.util.BQSQLUtil;
import com.liferay.osb.asah.common.util.GetterUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.io.UnsupportedEncodingException;

import java.math.BigDecimal;

import java.net.URLDecoder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import org.jooq.AggregateFunction;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.DatePart;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Record3;
import org.jooq.SelectConditionStep;
import org.jooq.SelectFinalStep;
import org.jooq.SelectHavingStep;
import org.jooq.SelectJoinStep;
import org.jooq.SelectSelectStep;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

/**
 * @author Leslie Wong
 */
public class BQEventRepositoryImpl
	extends BaseRepository implements CustomBQEventRepository {

	public BQEventRepositoryImpl(DSLContext dslContext) {
		_dslContext = dslContext;
	}

	@Override
	public Integer countBQEvents(
		@Nullable Long channelId, @Nullable String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId,
		List<String> userIds) {

		return (int)_queryExecutor.queryForLong(
			_dslContext.selectCount(
			).from(
				"BQEvent"
			).where(
				_createConditions(
					channelId, keywords, rangeEndLocalDateTime,
					rangeStartLocalDateTime, timeZoneId, userIds)
			));
	}

	@Override
	public Integer countBQEvents(
		@Nullable Long channelId, String individualId,
		@Nullable String keywords, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		return (int)_queryExecutor.queryForLong(
			_getEventsCount(
				channelId, DSL.count(), individualId, keywords,
				rangeEndLocalDateTime, rangeStartLocalDateTime, timeZoneId));
	}

	@Override
	public Integer countBQEvents(
		String applicationId, String assetId, Long channelId, Long dataSourceId,
		String eventId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		Condition condition = DSL.and(
			DSL.field(
				"applicationId"
			).eq(
				applicationId
			),
			DSL.field(
				"assetId"
			).eq(
				assetId
			),
			DSL.field(
				"eventId"
			).eq(
				eventId
			));

		if (channelId != null) {
			condition = condition.and(
				DSL.field(
					"channelId"
				).eq(
					channelId
				));
		}

		if (dataSourceId != null) {
			condition = condition.and(
				DSL.field(
					"dataSourceId"
				).eq(
					dataSourceId
				));
		}

		if (rangeEndLocalDateTime != null) {
			condition = condition.and(
				DSL.field(
					"eventDate"
				).lt(
					rangeEndLocalDateTime
				));
		}

		if (rangeStartLocalDateTime != null) {
			condition = condition.and(
				DSL.field(
					"eventDate"
				).ge(
					rangeStartLocalDateTime
				));
		}

		return (int)_queryExecutor.queryForLong(
			selectSelectStep.from(
				"BQEvent"
			).where(
				condition
			));
	}

	@Override
	public long countByEventDefinitionId(long eventDefinitionId) {
		SelectJoinStep<Record1<Integer>> selectJoinStep =
			_getEventSelectJoinStep(_dslContext.selectCount());

		return _queryExecutor.queryForLong(
			selectJoinStep.where(
				_getConditions(null, eventDefinitionId, null, null)));
	}

	@Override
	public Integer countEventSessions(
		Long channelId, String individualId, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		return (int)_queryExecutor.queryForLong(
			_getEventsCount(
				channelId, DSL.countDistinct(DSL.field("sessionId")),
				individualId, keywords, rangeEndLocalDateTime,
				rangeStartLocalDateTime, timeZoneId));
	}

	@Override
	public long countTotalBQEvents(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId) {

		SelectJoinStep<Record1<Integer>> selectJoinStep;

		String filteredEventsTableName = null;

		if ((eventAnalysisFilters == null) ||
			!_hasOnlyLocalAttributes(eventAnalysisFilters)) {

			selectJoinStep = _getEventSelectJoinStep(_dslContext.selectCount());
		}
		else {
			filteredEventsTableName = "_filteredEvents";

			selectJoinStep = _getFiltersCommonTableSelectJoinStep(
				channelId, eventDefinitionId, filteredEventsTableName,
				rangeEndDate, rangeStartDate);
		}

		return _queryExecutor.queryForLong(
			selectJoinStep.where(
				_getConditions(
					channelId, eventAnalysisFilters, eventDefinitionId,
					filteredEventsTableName, rangeEndDate, rangeStartDate,
					timeZoneId)));
	}

	@Override
	public long countUniqueIndividuals(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.select(_getUniqueIndividualsField(null));

		SelectJoinStep<Record1<Integer>> selectJoinStep =
			_getEventSelectJoinStep(selectSelectStep);

		return _queryExecutor.queryForLong(
			selectJoinStep.where(
				_getConditions(
					channelId, eventAnalysisFilters, eventDefinitionId,
					rangeEndDate, rangeStartDate, timeZoneId)));
	}

	@Override
	public Optional<BQEvent> findLastSeenBQEvent(
		@Nullable Long eventDefinitionId) {

		Table<Record> eventTable = DSL.table("BQEvent");

		SelectSelectStep<Record> selectSelectStep = _dslContext.select(
			eventTable.asterisk());

		Condition condition = DSL.noCondition();

		if (eventDefinitionId != null) {
			Optional<EventDefinition> eventDefinitionOptional =
				_eventDefinitionRepository.findById(eventDefinitionId);

			EventDefinition eventDefinition = eventDefinitionOptional.get();

			condition = DSL.field(
				"BQEvent.eventId"
			).eq(
				eventDefinition.getName()
			);
		}

		return _queryExecutor.queryForObject(
			BQEvent::new,
			selectSelectStep.from(
				"BQEvent"
			).where(
				condition
			).orderBy(
				DSL.field(
					"eventDate"
				).desc()
			).limit(
				1
			));
	}

	@Override
	public BigDecimal getAverageBQEventCountPerIndividual(
		@Nullable Long channelId,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, @Nullable Date rangeEndDate,
		@Nullable Date rangeStartDate, String timeZoneId) {

		Field totalEventCount = DSL.count();

		totalEventCount = totalEventCount.cast(SQLDataType.DECIMAL);

		SelectSelectStep<Record1<Number>> selectSelectStep = _dslContext.select(
			totalEventCount.div(
				DSL.nullif(_getUniqueIndividualsField(null), 0)));

		SelectJoinStep<Record1<Number>> selectJoinStep =
			_getEventSelectJoinStep(selectSelectStep);

		return _queryExecutor.queryForBigDecimal(
			selectJoinStep.where(
				_getConditions(
					channelId, eventAnalysisFilters, eventDefinitionId,
					rangeEndDate, rangeStartDate, timeZoneId)));
	}

	@Override
	public List<BreakdownRow> getBQEventPropertyValues(
		AnalysisType analysisType, @Nullable Long channelId,
		boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		Pageable pageable, TimeRange timeRange, String timeZoneId) {

		Stream<EventAnalysisBreakdown> stream =
			eventAnalysisBreakdowns.stream();

		Map<String, EventAttributeDefinition> eventAttributeDefinitions =
			_getEventAttributeDefinitions(
				stream.map(
					eventAnalysisBreakdown -> Long.valueOf(
						eventAnalysisBreakdown.getAttributeId())
				).collect(
					Collectors.toList()
				));

		EventAnalysisBreakdown lastEventAnalysisBreakdown = null;

		List<Field> valueFields = new ArrayList();

		if (!eventAnalysisBreakdowns.isEmpty()) {
			lastEventAnalysisBreakdown = eventAnalysisBreakdowns.get(
				eventAnalysisBreakdowns.size() - 1);

			for (EventAnalysisBreakdown eventAnalysisBreakdown :
					eventAnalysisBreakdowns) {

				valueFields.add(
					_getValueField(
						true, eventAnalysisBreakdown,
						eventAttributeDefinitions.get(
							eventAnalysisBreakdown.getAttributeId()),
						timeZoneId));
			}
		}

		List<Field<Number>> selectFields = new ArrayList<>();

		selectFields.add(
			_getSelectField(
				analysisType,
				_getEventAttributeDefinition(
					lastEventAnalysisBreakdown, eventAttributeDefinitions),
				timeRange));

		if (compareToPrevious) {
			Field previousSelectField = _getSelectField(
				analysisType,
				_getEventAttributeDefinition(
					lastEventAnalysisBreakdown, eventAttributeDefinitions),
				timeRange.getPreviousTimeRange());

			selectFields.add(previousSelectField.as("previous"));

			timeRange = timeRange.getIncludePreviousTimeRange();
		}

		SelectJoinStep<Record> selectJoinStep;

		if (!eventAnalysisBreakdowns.isEmpty()) {
			selectJoinStep = _getBQEventPropertyValuesSelectJoinStep(
				analysisType, channelId, eventAnalysisBreakdowns,
				eventAnalysisFilters, eventAttributeDefinitions,
				eventDefinitionId, ListUtils.union(valueFields, selectFields),
				pageable, timeRange, timeZoneId);
		}
		else {
			selectJoinStep = _buildSelectJoinStep(
				channelId, eventAnalysisBreakdowns, eventAttributeDefinitions,
				_dslContext.select(
					selectFields
				).from(
					"BQEvent"
				),
				timeRange);
		}

		SelectConditionStep<Record> selectConditionStep = selectJoinStep.where(
			_getConditions(
				channelId, eventAnalysisBreakdowns, eventAnalysisFilters,
				eventAttributeDefinitions, eventDefinitionId, timeRange,
				timeZoneId));

		if (!valueFields.isEmpty()) {
			return _queryExecutor.queryForList(
				BreakdownRow::new, selectConditionStep.groupBy(valueFields));
		}

		return _queryExecutor.queryForList(
			BreakdownRow::new, selectConditionStep);
	}

	@Override
	public long getBQEventPropertyValuesCount(
		@Nullable Long channelId, EventAnalysisBreakdown eventAnalysisBreakdown,
		@Nullable List<EventAnalysisFilter> eventAnalysisFilters,
		@Nullable Long eventDefinitionId, TimeRange timeRange,
		String timeZoneId) {

		EventAttributeDefinition eventAttributeDefinition =
			_getEventAttributeDefinition(
				eventAnalysisBreakdown.getAttributeId());

		if (eventAttributeDefinition == null) {
			return 0;
		}

		Field valueField = _getValueField(
			false, eventAnalysisBreakdown, eventAttributeDefinition,
			timeZoneId);

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.select(
				DSL.countDistinct(
					valueField
				).plus(
					DSL.count(DSL.when(valueField.isNull(), 1))
				));

		SelectJoinStep<Record1<Integer>> selectJoinStep = selectSelectStep.from(
			"BQEvent");

		selectJoinStep = _buildSelectJoinStep(
			channelId, Arrays.asList(eventAnalysisBreakdown),
			Collections.singletonMap(
				eventAnalysisBreakdown.getAttributeId(),
				eventAttributeDefinition),
			selectJoinStep, timeRange);

		return _queryExecutor.queryForLong(
			selectJoinStep.where(
				_getConditions(
					channelId, eventAnalysisFilters, eventDefinitionId,
					timeRange.getEndDate(), timeRange.getStartDate(),
					timeZoneId)));
	}

	@Override
	public Map<String, Integer> getBQEventsCountGroupByEventDate(
		Long channelId, String individualId, Interval interval, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		Field<OffsetDateTime> eventDateField = _getTruncatedEventDateField(
			"BQEvent.eventDate", interval, timeZoneId);

		eventDateField = eventDateField.as("eventDateTrunc");

		SelectJoinStep<Record2<OffsetDateTime, Integer>> selectJoinStep =
			_dslContext.select(
				eventDateField, DSL.count()
			).from(
				"BQEvent"
			);

		selectJoinStep = _getIndividualSelectJoinStep(
			individualId, selectJoinStep);

		return _queryExecutor.queryForMap(
			GetterUtil::getDateString,
			selectJoinStep.where(
				_createConditions(
					channelId, individualId, keywords, rangeEndLocalDateTime,
					rangeStartLocalDateTime, timeZoneId)
			).groupBy(
				eventDateField
			),
			GetterUtil::getInteger);
	}

	@Override
	public Map<String, Integer> getEventSessionsCountGroupByEventDate(
		Long channelId, String individualId, Interval interval, String keywords,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		Field<OffsetDateTime> eventDateField = _getTruncatedEventDateField(
			"BQEvent.eventDate", interval, timeZoneId);

		eventDateField = eventDateField.as("eventDate");

		Field<OffsetDateTime> event1EventDateField = DSL.field(
			"event1.eventDate", OffsetDateTime.class);

		SelectJoinStep<Record2<OffsetDateTime, Object>> selectJoinStep =
			_dslContext.selectDistinct(
				eventDateField, DSL.field("sessionId")
			).from(
				"BQEvent"
			);

		selectJoinStep = _getIndividualSelectJoinStep(
			individualId, selectJoinStep);

		return _queryExecutor.queryForMap(
			object -> GetterUtil.getDateString(object),
			_dslContext.select(
				event1EventDateField, DSL.count()
			).from(
				DSL.table(
					selectJoinStep.where(
						_createConditions(
							channelId, individualId, keywords,
							rangeEndLocalDateTime, rangeStartLocalDateTime,
							timeZoneId))
				).as(
					"event1"
				)
			).groupBy(
				event1EventDateField
			),
			GetterUtil::getInteger);
	}

	@Override
	public List<Map<String, String>>
		getKeywordsGroupedByChannelIdAndSessionIdAndUserId(Date eventDate) {

		List<Condition> conditions = new ArrayList<>();

		conditions.add(
			DSL.or(
				DSL.field(
					"event.assetTitle"
				).isNotNull(),
				DSL.field(
					"event.description"
				).isNotNull(),
				DSL.field(
					"event.keywords"
				).isNotNull()));
		conditions.add(
			DSL.field(
				"event.eventDate", Date.class
			).gt(
				eventDate
			));
		conditions.add(
			DSL.field(
				"event.eventId", String.class
			).eq(
				"pageUnloaded"
			));
		conditions.add(
			DSL.field(
				"eventproperty.name", String.class
			).eq(
				"viewDuration"
			));
		conditions.add(
			DSL.field(
				"eventproperty.eventDate", Date.class
			).gt(
				eventDate
			));

		return _queryExecutor.queryForList(
			recordMap -> {
				Map<String, String> keywordMap = new HashMap<>();

				keywordMap.put(
					"channelId", String.valueOf(recordMap.get("channelId")));
				keywordMap.put(
					"keywords", String.valueOf(recordMap.get("keywords")));
				keywordMap.put(
					"sessionId", String.valueOf(recordMap.get("sessionId")));
				keywordMap.put(
					"userId", String.valueOf(recordMap.get("userId")));

				return keywordMap;
			},
			_dslContext.select(
				DSL.field(
					"event.channelId"
				).as(
					"channelId"
				),
				DSL.lower(
					DSL.max(
						_dslHelper.concat(
							DSL.coalesce(DSL.field("assetTitle"), ""),
							DSL.val(" "),
							DSL.coalesce(DSL.field("description"), ""),
							DSL.val(" "),
							DSL.coalesce(DSL.field("keywords"), "")))
				).as(
					"keywords"
				),
				DSL.field(
					"event.sessionId"
				).as(
					"sessionId"
				),
				DSL.field(
					"event.userId"
				).as(
					"userId"
				)
			).from(
				DSL.table(
					"BQEvent"
				).as(
					"event"
				)
			).join(
				DSL.table(
					"BQEventProperty"
				).as(
					"eventproperty"
				)
			).on(
				DSL.field(
					"event.id"
				).eq(
					DSL.field("eventproperty.id")
				)
			).where(
				conditions
			).groupBy(
				DSL.field("channelId"), DSL.field("sessionId"),
				DSL.field("userId")
			).orderBy(
				DSL.field("userId")
			));
	}

	@Override
	public Map<String, Date> getLastSeenDateDateGroupedByColumnName(
		String columnName, int size) {

		Field<String> columnField = DSL.field(columnName, String.class);

		SelectSelectStep<Record2<String, Date>> selectSelectStep =
			_dslContext.select(
				columnField,
				DSL.max(
					DSL.field("eventdate", Date.class)
				).as(
					"lastseendate"
				));

		SelectHavingStep<Record2<String, Date>> selectHavingStep =
			selectSelectStep.from(
				"BQEvent"
			).where(
				DSL.field(
					"eventdate"
				).ge(
					_dslHelper.getDateParam(
						DateUtil.addDays(DateUtil.newDate(), -7))
				)
			).groupBy(
				columnField
			);

		return _queryExecutor.queryForMap(
			key -> (String)key,
			_dslContext.select(
			).from(
				selectHavingStep
			).orderBy(
				DSL.field(
					"lastseendate"
				).desc()
			).limit(
				size
			),
			value -> (Date)value);
	}

	@Override
	public List<SearchKeyword> getSearchKeywords(
		@Nullable String displayLanguageId, @Nullable String groupId,
		int minCounts, Pageable pageable, Set<String> searchQueryParams) {

		return _queryExecutor.queryForList(
			recordMap -> {
				Object counts = recordMap.get("counts");

				if (counts instanceof Integer) {
					recordMap.put("counts", Long.valueOf((Integer)counts));
				}

				Object keywords = recordMap.get("keywords");

				try {
					recordMap.put(
						"keywords",
						URLDecoder.decode((String)keywords, "UTF-8"));
				}
				catch (UnsupportedEncodingException
							unsupportedEncodingException) {

					recordMap.put("keywords", keywords);
				}

				return new SearchKeyword(recordMap);
			},
			_dslContext.select(
				DSL.count(
					DSL.asterisk()
				).as(
					"counts"
				),
				DSL.min(
					DSL.field("eventDate", Date.class)
				).as(
					"createdate"
				),
				DSL.field(
					"contentLanguageId"
				).as(
					"displaylanguageid"
				),
				_dslHelper.jsonExtractScalar(
					"context", "groupId"
				).as(
					"groupid"
				),
				DSL.field(
					_getKeywordsField(searchQueryParams)
				).as(
					"keywords"
				),
				DSL.max(
					DSL.field("eventDate", Date.class)
				).as(
					"lastmodifieddate"
				)
			).from(
				DSL.table("BQEvent")
			).where(
				_createCondition(displayLanguageId, groupId, searchQueryParams)
			).groupBy(
				DSL.field("displaylanguageid"), DSL.field("groupid"),
				_dslHelper.getField(
					DSL.field("keywords"), _getKeywordsField(searchQueryParams))
			).having(
				DSL.field(
					"counts"
				).ge(
					minCounts
				)
			).orderBy(
				getSortFields(pageable.getSort(), null)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public long getSearchKeywordsCount(
		@Nullable String displayLanguageId, @Nullable String groupId,
		int minCounts, Set<String> searchQueryParams) {

		SelectSelectStep<Record1<Integer>> selectSelectStep =
			_dslContext.selectCount();

		return _queryExecutor.queryForLong(
			selectSelectStep.from(
				_dslContext.select(
					DSL.field(
						"contentLanguageId"
					).as(
						"displaylanguageid"
					),
					DSL.count(
						DSL.asterisk()
					).as(
						"counts"
					),
					_dslHelper.jsonExtractScalar(
						"context", "groupId"
					).as(
						"groupid"
					),
					DSL.field(
						_getKeywordsField(searchQueryParams)
					).as(
						"keywords"
					)
				).from(
					"BQEvent"
				).where(
					_createCondition(
						displayLanguageId, groupId, searchQueryParams)
				).groupBy(
					DSL.field("displaylanguageid"), DSL.field("groupid"),
					_dslHelper.getField(
						DSL.field("keywords"),
						_getKeywordsField(searchQueryParams))
				).having(
					DSL.field(
						"counts"
					).ge(
						minCounts
					)
				)));
	}

	@Override
	public Map<String, BigDecimal> getSearchTerms(
		Long channelId, String[] searchQueryParams, int size, int start,
		TimeRange timeRange, String timeZoneId) {

		Field<String> searchTermField = DSL.function(
			ProjectIdThreadLocal.getProjectId() + ".search_term", String.class,
			DSL.array(searchQueryParams), DSL.field("url"));

		Field<BigDecimal> countField = DSL.count(
			DSL.asterisk()
		).cast(
			BigDecimal.class
		);

		searchTermField = DSL.lower(searchTermField);

		return _queryExecutor.queryForMap(
			GetterUtil::getString,
			_dslContext.select(
				searchTermField.as("searchTermField"), countField
			).from(
				"BQEvent"
			).where(
				_createSearchTermsCondition(
					channelId, timeRange.getEndLocalDateTime(),
					timeRange.getStartLocalDateTime(), searchTermField,
					timeZoneId)
			).groupBy(
				DSL.field("searchTermField")
			).orderBy(
				countField.desc()
			).limit(
				size
			).offset(
				start
			),
			GetterUtil::getBigDecimal);
	}

	@Override
	public long getSearchTermsCount(
		Long channelId, String[] searchQueryParams, TimeRange timeRange,
		String timeZoneId) {

		Field<String> searchTermField = DSL.function(
			ProjectIdThreadLocal.getProjectId() + ".search_term", String.class,
			DSL.array(searchQueryParams), DSL.field("url"));

		searchTermField = DSL.lower(searchTermField);

		return _queryExecutor.queryForLong(
			_dslContext.select(
				DSL.count(searchTermField)
			).from(
				"BQEvent"
			).where(
				_createSearchTermsCondition(
					channelId, timeRange.getEndLocalDateTime(),
					timeRange.getStartLocalDateTime(), searchTermField,
					timeZoneId)
			));
	}

	@Override
	public BQEvent insert(BQEvent bqEvent) {
		_queryExecutor.queryExecute(BQSQLUtil.createInsertStatement(bqEvent));

		return bqEvent;
	}

	@Override
	public List<BQEvent> searchBQEvents(
		@Nullable Long channelId, @Nullable String keywords, Pageable pageable,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId,
		List<String> userIds) {

		Table<Record> eventTable = DSL.table("BQEvent");

		SelectJoinStep<?> selectJoinStep = _dslContext.select(
			DSL.field("applicationId"), DSL.field("context"),
			DSL.field("eventDate"), DSL.field("eventId"),
			DSL.field("eventProperties"), DSL.field("userId")
		).from(
			eventTable
		);

		List<Condition> conditions = _createConditions(
			channelId, keywords, rangeEndLocalDateTime, rangeStartLocalDateTime,
			timeZoneId, userIds);

		return _queryExecutor.queryForList(
			BQEvent::new,
			selectJoinStep.where(
				conditions
			).orderBy(
				getSortFields(pageable.getSort(), eventTable)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	@Override
	public List<BQEvent> searchBQEvents(
		@Nullable Long channelId, String individualId,
		@Nullable String keywords, Pageable pageable,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		Table<Record> eventTable = DSL.table("BQEvent");

		SelectJoinStep<Record> selectJoinStep = _dslContext.select(
			eventTable.asterisk()
		).from(
			"BQEvent"
		);

		if (StringUtils.isNotBlank(individualId)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table("BQIdentity")
			).on(
				DSL.field(
					"BQEvent.userId"
				).eq(
					DSL.field("BQIdentity.id")
				)
			);
		}

		List<Condition> conditions = _createConditions(
			channelId, keywords, rangeEndLocalDateTime, rangeStartLocalDateTime,
			timeZoneId);

		if (StringUtils.isNotBlank(individualId)) {
			conditions.add(
				DSL.field(
					"BQIdentity.individualId"
				).eq(
					individualId
				));
		}

		return _queryExecutor.queryForList(
			BQEvent::new,
			selectJoinStep.where(
				conditions
			).orderBy(
				getSortFields(pageable.getSort(), eventTable)
			).limit(
				pageable.getPageSize()
			).offset(
				pageable.getOffset()
			));
	}

	private SelectJoinStep _buildSelectJoinStep(
		Long channelId, List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		Map<String, EventAttributeDefinition> eventAttributeDefinitions,
		SelectJoinStep selectJoinStep, TimeRange timeRange) {

		for (EventAnalysisBreakdown eventAnalysisBreakdown :
				eventAnalysisBreakdowns) {

			EventAttributeDefinition eventAttributeDefinition =
				eventAttributeDefinitions.get(
					eventAnalysisBreakdown.getAttributeId());

			if (Objects.equals(
					eventAttributeDefinition.getType(),
					EventAttributeDefinition.Type.GLOBAL)) {

				continue;
			}

			AttributeType attributeType =
				eventAnalysisBreakdown.getAttributeType();

			Table<Record> table = DSL.table(
				String.format(
					"%s as %s", attributeType.getTableName(),
					_getSanitizedEventAttributeDefinitionName(
						eventAttributeDefinition)));

			Condition condition = DSL.and(
				_getChannelIdFilter(
					channelId,
					String.format(
						"%s.channelId",
						_getSanitizedEventAttributeDefinitionName(
							eventAttributeDefinition)))
			).and(
				DSL.field(
					_getJoinFieldTableName(attributeType)
				).eq(
					DSL.field(
						String.format(
							"%s.%s",
							_getSanitizedEventAttributeDefinitionName(
								eventAttributeDefinition),
							attributeType.getJoinFieldName()))
				)
			).and(
				DSL.field(
					String.format(
						"%s.%s",
						_getSanitizedEventAttributeDefinitionName(
							eventAttributeDefinition),
						attributeType.getAttributeIdFieldName())
				).eq(
					_getEventAttributeDefinitionName(eventAttributeDefinition)
				)
			);

			if (Objects.equals(attributeType, AttributeType.EVENT)) {
				condition = condition.and(
					_getEventDateRangeFilter(
						String.format(
							"%s.eventDate",
							_getSanitizedEventAttributeDefinitionName(
								eventAttributeDefinition)),
						timeRange.getEndDate(), timeRange.getStartDate()));
			}

			selectJoinStep = selectJoinStep.join(
				table
			).on(
				condition
			);
		}

		return selectJoinStep;
	}

	private Condition _createCondition(
		@Nullable String displayLanguageId, @Nullable String groupId,
		Set<String> searchQueryParams) {

		Condition condition = DSL.and(
			DSL.field(
				"eventId"
			).eq(
				"pageViewed"
			),
			_dslHelper.regexpContains(
				"url",
				"[?&](?:" + String.join("|", searchQueryParams) + ")=([^&]+)"));

		if (Objects.nonNull(displayLanguageId)) {
			condition = condition.and(
				DSL.field(
					"contentLanguageId"
				).eq(
					displayLanguageId
				));
		}

		if (Objects.nonNull(groupId)) {
			condition = condition.and(
				_dslHelper.jsonExtractScalar(
					"context", "groupId"
				).eq(
					groupId
				));
		}

		return condition;
	}

	private List<Condition> _createConditions(
		@Nullable Long channelId, String keyword,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		return _createConditions(
			channelId, keyword, rangeEndLocalDateTime, rangeStartLocalDateTime,
			timeZoneId, Collections.emptyList());
	}

	private List<Condition> _createConditions(
		@Nullable Long channelId, String keyword,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId,
		List<String> userIds) {

		List<Condition> conditions = new ArrayList<Condition>() {
			{
				add(
					DSL.field(
						"BQEvent.applicationId"
					).in(
						_eventDefinitionRepository.
							getEventDefinitionApplicationIds(false)
					));
				add(
					DSL.field(
						"BQEvent.eventDate"
					).between(
						_dslHelper.getDateParam(
							rangeStartLocalDateTime, timeZoneId),
						_dslHelper.getDateParam(
							rangeEndLocalDateTime, timeZoneId)
					));
				add(
					DSL.field(
						"BQEvent.eventId"
					).in(
						_eventDefinitionRepository.getEventDefinitionNames(
							false)
					));
			}
		};

		if (channelId != null) {
			conditions.add(
				DSL.field(
					"BQEvent.channelId"
				).eq(
					channelId
				));
		}

		if (!StringUtils.isEmpty(keyword)) {
			Condition keywordCondition = DSL.or(
				DSL.lower(
					DSL.field("BQEvent.eventId", String.class)
				).like(
					"%" + _getSanitizedKeywords(keyword) + "%"
				),
				DSL.exists(
					DSL.select(
						DSL.field("id")
					).from(
						DSL.table(
							"BQEventProperty"
						).where(
							DSL.and(
								DSL.field(
									"BQEventProperty.id"
								).eq(
									DSL.field("BQEvent.id")
								)),
							DSL.lower(
								DSL.field("BQEventProperty.value", String.class)
							).like(
								"%" + _getSanitizedKeywords(keyword) + "%"
							)
						)
					)));

			for (String globalAttribute : _globalAttributes.values()) {
				keywordCondition = keywordCondition.or(
					_dslHelper.containsSubstring(
						"BQEvent." + globalAttribute,
						_getSanitizedKeywords(keyword)));
			}

			conditions.add(keywordCondition);
		}

		if (!userIds.isEmpty()) {
			conditions.add(
				DSL.field(
					"BQEvent.userId"
				).in(
					userIds
				));
		}

		return conditions;
	}

	private List<Condition> _createConditions(
		Long channelId, String individualId, String keyword,
		LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		List<Condition> conditions = _createConditions(
			channelId, keyword, rangeEndLocalDateTime, rangeStartLocalDateTime,
			timeZoneId);

		if (StringUtils.isNotBlank(individualId)) {
			conditions.add(
				DSL.field(
					"BQIdentity.individualId"
				).eq(
					individualId
				));
		}

		return conditions;
	}

	private Condition _createSearchTermsCondition(
		Long channelId, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, Field<String> searchTermField,
		String timeZoneId) {

		return DSL.and(
			DSL.field(
				"BQEvent.applicationId"
			).eq(
				"Page"
			),
			DSL.field(
				"BQEvent.channelId"
			).eq(
				channelId
			),
			DSL.field(
				"BQEvent.eventDate"
			).between(
				_dslHelper.getDateParam(rangeStartLocalDateTime, timeZoneId),
				_dslHelper.getDateParam(rangeEndLocalDateTime, timeZoneId)
			),
			DSL.field(
				"BQEvent.eventId"
			).eq(
				"pageViewed"
			),
			searchTermField.isNotNull(),
			DSL.trim(
				DSL.coalesce(searchTermField, "")
			).notEqual(
				""
			));
	}

	private SelectJoinStep<Record> _getBQEventPropertyValuesSelectJoinStep(
		AnalysisType analysisType, Long channelId,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters,
		Map<String, EventAttributeDefinition> eventAttributeDefinitions,
		Long eventDefinitionId, List<Field> fields, Pageable pageable,
		TimeRange timeRange, String timeZoneId) {

		List<EventAnalysisFilter> filteredEventAnalysisFilters = null;

		EventAnalysisBreakdown firstEventAnalysisBreakdown =
			eventAnalysisBreakdowns.get(0);

		if (eventAnalysisFilters != null) {
			Stream<EventAnalysisFilter> stream = eventAnalysisFilters.stream();

			filteredEventAnalysisFilters = stream.filter(
				eventAnalysisFilter -> eventAttributeDefinitions.containsKey(
					firstEventAnalysisBreakdown.getAttributeId())
			).collect(
				Collectors.toList()
			);
		}

		Field selectField = _getSelectField(
			analysisType,
			_getEventAttributeDefinition(
				firstEventAnalysisBreakdown.getAttributeId()),
			timeRange);

		Field valueField = _getValueField(
			false, firstEventAnalysisBreakdown,
			_getEventAttributeDefinition(
				firstEventAnalysisBreakdown, eventAttributeDefinitions),
			timeZoneId);

		SelectJoinStep buildSelectJoinStep = _dslContext.selectDistinct(
			valueField.as("_firstValueField"), selectField
		).from(
			"BQEvent"
		);

		return _buildSelectJoinStep(
			channelId, eventAnalysisBreakdowns, eventAttributeDefinitions,
			_dslContext.with(
				"_filteredEvents"
			).as(
				_buildSelectJoinStep(
					channelId, eventAnalysisBreakdowns,
					eventAttributeDefinitions, buildSelectJoinStep, timeRange
				).where(
					_getConditions(
						channelId, filteredEventAnalysisFilters,
						eventDefinitionId, timeRange.getEndDate(),
						timeRange.getStartDate(), timeZoneId)
				).groupBy(
					DSL.field("_firstValueField")
				).orderBy(
					_getSortField(
						firstEventAnalysisBreakdown, selectField.getName())
				).limit(
					pageable.getPageSize()
				).offset(
					pageable.getOffset()
				)
			).select(
				fields
			).from(
				"BQEvent"
			),
			timeRange);
	}

	private Condition _getChannelIdFilter(Long channelId, String fieldName) {
		if (channelId == null) {
			return DSL.noCondition();
		}

		Field<Object> field = DSL.field(fieldName);

		return field.eq(channelId);
	}

	private List<Condition> _getConditions(
		Long channelId, List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters,
		Map<String, EventAttributeDefinition> eventAttributeDefinitions,
		Long eventDefinitionId, TimeRange timeRange, String timeZoneId) {

		List<Condition> conditions = _getConditions(
			channelId, eventAnalysisFilters, eventDefinitionId,
			timeRange.getEndDate(), timeRange.getStartDate(), timeZoneId);

		if (eventAnalysisBreakdowns.isEmpty()) {
			return conditions;
		}

		EventAnalysisBreakdown firstEventAnalysisBreakdown =
			eventAnalysisBreakdowns.get(0);

		Field firstValueField = _getValueField(
			false, firstEventAnalysisBreakdown,
			_getEventAttributeDefinition(
				firstEventAnalysisBreakdown, eventAttributeDefinitions),
			timeZoneId);

		conditions.add(
			firstValueField.in(
				_dslContext.select(
					DSL.field("_firstValueField")
				).from(
					"_filteredEvents"
				)));

		return conditions;
	}

	private List<Condition> _getConditions(
		Long channelId, List<EventAnalysisFilter> eventAnalysisFilters,
		Long eventDefinitionId, Date rangeEndDate, Date rangeStartDate,
		String timeZoneId) {

		return _getConditions(
			channelId, eventAnalysisFilters, eventDefinitionId, null,
			rangeEndDate, rangeStartDate, timeZoneId);
	}

	private List<Condition> _getConditions(
		Long channelId, List<EventAnalysisFilter> eventAnalysisFilters,
		Long eventDefinitionId, String filteredEventsTableName,
		Date rangeEndDate, Date rangeStartDate, String timeZoneId) {

		List<Condition> conditions = new ArrayList<>();

		if (filteredEventsTableName == null) {
			conditions = _getConditions(
				channelId, eventDefinitionId, rangeEndDate, rangeStartDate);
		}

		if (eventAnalysisFilters != null) {
			conditions.addAll(
				_getEventAnalysisFilterConditions(
					eventAnalysisFilters, filteredEventsTableName, rangeEndDate,
					rangeStartDate, timeZoneId));
		}

		return conditions;
	}

	private List<Condition> _getConditions(
		Long channelId, Long eventDefinitionId, Date rangeEndDate,
		Date rangeStartDate) {

		List<Condition> conditions = new ArrayList<>();

		EventDefinition eventDefinition = null;

		if (eventDefinitionId != null) {
			Optional<EventDefinition> eventDefinitionOptional =
				_eventDefinitionRepository.findById(eventDefinitionId);

			eventDefinition = eventDefinitionOptional.orElse(null);
		}

		if (eventDefinition != null) {
			Field<Object> field = DSL.field("BQEvent.applicationId");

			conditions.add(
				field.in(
					SetUtil.of(
						"CustomEvent", eventDefinition.getApplicationId())));
		}

		if (channelId != null) {
			Field<Object> field = DSL.field("BQEvent.channelId");

			conditions.add(field.eq(channelId));
		}

		if (eventDefinition != null) {
			Field<Object> field = DSL.field("BQEvent.eventId");

			conditions.add(field.eq(eventDefinition.getName()));
		}

		conditions.add(
			_getEventDateRangeFilter(
				"BQEvent.eventDate", rangeEndDate, rangeStartDate));

		return conditions;
	}

	private Condition _getEventAnalysisFilterCondition(
		AttributeType attributeType, EventAnalysisFilter eventAnalysisFilter,
		String filteredEventsTableName, Date rangeEndDate, Date rangeStartDate,
		String timeZoneId) {

		Condition conditions = DSL.noCondition();

		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_getEventAttributeDefinition(eventAnalysisFilter);

		if (!eventAttributeDefinitionOptional.isPresent()) {
			return conditions;
		}

		EventAttributeDefinition eventAttributeDefinition =
			eventAttributeDefinitionOptional.get();

		Condition condition = _getValueCondition(
			attributeType, eventAttributeDefinition, filteredEventsTableName,
			rangeEndDate, rangeStartDate);

		FilterOperator filterOperator = FilterOperators.of(
			eventAnalysisFilter.getDataType(), _dslHelper,
			eventAnalysisFilter.getOperator(), eventAnalysisFilter.getValues());

		String fieldTableName = "BQEventProperty";

		if (filteredEventsTableName != null) {
			fieldTableName = filteredEventsTableName;
		}

		Field field = DSL.field(fieldTableName + ".value");

		if (_isGlobalAttribute(eventAttributeDefinition)) {
			if (filteredEventsTableName == null) {
				field = _getGlobalAttributeField(
					eventAttributeDefinition.getName());
			}
			else {
				String attributeDefinitionName = _globalAttributes.get(
					eventAttributeDefinition.getName());

				field = DSL.field(
					filteredEventsTableName + "." + attributeDefinitionName);
			}
		}

		conditions = conditions.and(
			condition.and(
				filterOperator.getCondition(
					_getField(eventAnalysisFilter, field, timeZoneId))));

		Field<Object> joinFieldTableField = DSL.field(
			_getJoinFieldTableName(attributeType));

		if (filteredEventsTableName == null) {
			return joinFieldTableField.in(
				_getFilterCondition(
					attributeType, conditions, eventAttributeDefinition,
					rangeEndDate, rangeStartDate));
		}

		return joinFieldTableField.in(
			_getFilterConditionForCommonTable(
				conditions, filteredEventsTableName));
	}

	private List<Condition> _getEventAnalysisFilterConditions(
		List<EventAnalysisFilter> eventAnalysisFilters,
		String filteredEventsTableName, Date rangeEndDate, Date rangeStartDate,
		String timeZoneId) {

		List<Condition> conditions = new ArrayList<>();

		for (EventAnalysisFilter eventAnalysisFilter : eventAnalysisFilters) {
			conditions.add(
				_getEventAnalysisFilterCondition(
					eventAnalysisFilter.getAttributeType(), eventAnalysisFilter,
					filteredEventsTableName, rangeEndDate, rangeStartDate,
					timeZoneId));
		}

		return conditions;
	}

	private EventAttributeDefinition _getEventAttributeDefinition(
		EventAnalysisBreakdown eventAnalysisBreakdown,
		Map<String, EventAttributeDefinition> eventAttributeDefinitions) {

		if (eventAnalysisBreakdown == null) {
			return null;
		}

		return eventAttributeDefinitions.get(
			eventAnalysisBreakdown.getAttributeId());
	}

	private Optional<EventAttributeDefinition> _getEventAttributeDefinition(
		EventAnalysisFilter eventAnalysisFilter) {

		return _eventAttributeDefinitionRepository.findById(
			Long.valueOf(eventAnalysisFilter.getAttributeId()));
	}

	private EventAttributeDefinition _getEventAttributeDefinition(
		String attributeId) {

		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findById(
				Long.valueOf(attributeId));

		return eventAttributeDefinitionOptional.orElse(null);
	}

	private String _getEventAttributeDefinitionName(
		EventAttributeDefinition eventAttributeDefinition) {

		String name = eventAttributeDefinition.getName();

		return name.replace("'", "\\'");
	}

	private Map<String, EventAttributeDefinition> _getEventAttributeDefinitions(
		List<Long> attributeIds) {

		List<EventAttributeDefinition> attributeDefinitions =
			IterableUtils.toList(
				_eventAttributeDefinitionRepository.findAllById(attributeIds));

		Stream<EventAttributeDefinition> stream = attributeDefinitions.stream();

		return stream.collect(
			Collectors.toMap(
				eventAttributeDefinition -> String.valueOf(
					eventAttributeDefinition.getId()),
				Function.identity()));
	}

	private List<EventAttributeDefinition> _getEventAttributesDefinitions(
		List<EventAnalysisFilter> eventAnalysisFilters) {

		Stream<EventAnalysisFilter> eventAnalysisFiltersStream =
			eventAnalysisFilters.stream();

		return IterableUtils.toList(
			_eventAttributeDefinitionRepository.findAllById(
				eventAnalysisFiltersStream.map(
					EventAnalysisFilter::getAttributeId
				).map(
					Long::valueOf
				).collect(
					Collectors.toList()
				)));
	}

	private Condition _getEventDateRangeFilter(
		String fieldName, Date rangeEndDate, Date rangeStartDate) {

		if ((rangeEndDate != null) && (rangeStartDate != null)) {
			Field<Object> field = DSL.field(fieldName);

			return field.between(
				_dslHelper.getDateParam(rangeStartDate),
				_dslHelper.getDateParam(rangeEndDate));
		}

		return DSL.noCondition();
	}

	private SelectFinalStep<Record1<Integer>> _getEventsCount(
		@Nullable Long channelId,
		AggregateFunction<Integer> countAggregateFunction, String individualId,
		String keywords, LocalDateTime rangeEndLocalDateTime,
		LocalDateTime rangeStartLocalDateTime, String timeZoneId) {

		SelectJoinStep<Record1<Integer>> selectJoinStep = _dslContext.select(
			countAggregateFunction
		).from(
			"BQEvent"
		);

		if (StringUtils.isNotBlank(individualId)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table("BQIdentity")
			).on(
				DSL.field(
					"BQEvent.userId"
				).eq(
					DSL.field("BQIdentity.id")
				)
			);
		}

		List<Condition> conditions = _createConditions(
			channelId, keywords, rangeEndLocalDateTime, rangeStartLocalDateTime,
			timeZoneId);

		if (StringUtils.isNotBlank(individualId)) {
			conditions.add(
				DSL.field(
					"BQIdentity.individualId"
				).eq(
					individualId
				));
		}

		return selectJoinStep.where(conditions);
	}

	private <T> SelectJoinStep<Record1<T>> _getEventSelectJoinStep(
		SelectSelectStep<Record1<T>> selectSelectStep) {

		return selectSelectStep.from("BQEvent");
	}

	private Field _getField(
		EventAnalysisFilter eventAnalysisFilter, Field field,
		String timeZoneId) {

		EventAttributeDefinition.DataType dataType =
			eventAnalysisFilter.getDataType();

		if (dataType.equals(EventAttributeDefinition.DataType.BOOLEAN)) {
			return _dslHelper.getCastBooleanField(field);
		}

		if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
			return DSL.field(
				String.format(
					"DATE(SAFE_CAST(%s AS TIMESTAMP), '%s')", field.getName(),
					timeZoneId));
		}

		if (dataType.equals(EventAttributeDefinition.DataType.DURATION)) {
			return _dslHelper.getCastDurationField(field);
		}

		if (dataType.equals(EventAttributeDefinition.DataType.NUMBER)) {
			return _dslHelper.getCastNumberField(field);
		}

		return field;
	}

	private SelectHavingStep<Record1<Object>> _getFilterCondition(
		AttributeType attributeType, Condition conditions,
		EventAttributeDefinition eventAttributeDefinition, Date rangeEndDate,
		Date rangeStartDate) {

		if ((rangeEndDate != null) && (rangeStartDate != null)) {
			Field<Object> eventDateField = DSL.field("BQEvent.eventDate");

			conditions = conditions.and(
				eventDateField.between(
					_dslHelper.getDateParam(rangeStartDate),
					_dslHelper.getDateParam(rangeEndDate)));
		}

		Field<Object> field = DSL.field(_getJoinFieldTableName(attributeType));

		SelectJoinStep<Record1<Object>> selectJoinStep = _dslContext.select(
			field
		).from(
			"BQEvent"
		);

		if (!_isGlobalAttribute(eventAttributeDefinition)) {
			selectJoinStep = selectJoinStep.join(
				attributeType.getTableName()
			).on(
				field.eq(
					DSL.field(attributeType.getQualifiedJoinFieldName(null)))
			);
		}

		return selectJoinStep.where(
			conditions
		).groupBy(
			field
		);
	}

	private SelectConditionStep<Record1<Object>>
		_getFilterConditionForCommonTable(
			Condition conditions, String filteredEventsTableName) {

		Field<Object> field = DSL.field("id");

		SelectJoinStep<Record1<Object>> selectJoinStep = _dslContext.select(
			field
		).from(
			filteredEventsTableName
		);

		return selectJoinStep.where(conditions);
	}

	private SelectJoinStep<Record1<Integer>>
		_getFiltersCommonTableSelectJoinStep(
			Long channelId, Long eventDefinitionId,
			String filteredEventsTableName, Date rangeEndDate,
			Date rangeStartDate) {

		SelectSelectStep<Record3<Object, Object, Object>> selectConditionStep =
			_dslContext.select(
				DSL.field("BQEvent.*"), DSL.field("BQEventProperty.name"),
				DSL.field("BQEventProperty.value"));

		SelectConditionStep<Record3<Object, Object, Object>>
			filtersCommonTable = selectConditionStep.from(
				"BQEvent"
			).join(
				"BQEventProperty"
			).on(
				"BQEvent.id = BQEventProperty.id"
			).where(
				_getConditions(
					channelId, eventDefinitionId, rangeEndDate, rangeStartDate)
			);

		return _getEventSelectJoinStep(
			_dslContext.with(
				filteredEventsTableName
			).as(
				filtersCommonTable
			).select(
				DSL.count(DSL.field("BQEvent.eventId"))
			));
	}

	private Field _getGlobalAttributeField(String name) {
		return _getGlobalAttributeField("BQEvent", name);
	}

	private Field _getGlobalAttributeField(String alias, String name) {
		return DSL.field(
			alias.concat(
				"."
			).concat(
				_globalAttributes.get(name)
			));
	}

	private <T extends Record> SelectJoinStep<T> _getIndividualSelectJoinStep(
		String individualId, SelectJoinStep<T> selectJoinStep) {

		if (StringUtils.isNotBlank(individualId)) {
			selectJoinStep = selectJoinStep.join(
				DSL.table("BQIdentity")
			).on(
				DSL.field(
					"BQEvent.userId"
				).eq(
					DSL.field("BQIdentity.id")
				)
			);
		}

		return selectJoinStep;
	}

	private String _getJoinFieldTableName(AttributeType attributeType) {
		if (attributeType.equals(AttributeType.EVENT)) {
			return "BQEvent.id";
		}

		return "BQEvent.individualId";
	}

	private Field<String> _getKeywordsField(Set<String> searchQueryParams) {
		return DSL.lower(
			_dslHelper.regexpExtract(
				_dslHelper.regexpReplace("url", "(?:%20|\\s)", "+"),
				"[?&](?:" + String.join("|", searchQueryParams) + ")=([^&]+)"));
	}

	private String _getSanitizedEventAttributeDefinitionName(
		EventAttributeDefinition eventAttributeDefinition) {

		return "_" + _getSanitizedName(eventAttributeDefinition.getName());
	}

	private String _getSanitizedKeywords(String keywords) {
		keywords = StringUtil.unquoteAndDecodeInnerQuotes(keywords);

		return StringUtils.lowerCase(keywords.replace("'", "\\'"));
	}

	private String _getSanitizedName(String name) {
		return name.replaceAll("[\\W]", "_");
	}

	private Field _getSelectField(
		AnalysisType analysisType,
		EventAttributeDefinition eventAttributeDefinition,
		TimeRange timeRange) {

		Field field = null;

		if (eventAttributeDefinition == null) {
			field = DSL.when(
				_getEventDateRangeFilter(
					"BQEvent.eventDate", timeRange.getEndDate(),
					timeRange.getStartDate()),
				DSL.field("BQEvent.id"));
		}
		else if (Objects.equals(
					eventAttributeDefinition.getType(),
					EventAttributeDefinition.Type.GLOBAL)) {

			field = DSL.when(
				_getEventDateRangeFilter(
					"BQEvent.eventDate", timeRange.getEndDate(),
					timeRange.getStartDate()),
				_getGlobalAttributeField(eventAttributeDefinition.getName()));
		}
		else {
			String eventAttributeDefinitionName =
				_getSanitizedEventAttributeDefinitionName(
					eventAttributeDefinition);

			field = DSL.when(
				_getEventDateRangeFilter(
					eventAttributeDefinitionName + ".eventDate",
					timeRange.getEndDate(), timeRange.getStartDate()),
				DSL.field(eventAttributeDefinitionName + ".value"));
		}

		if (analysisType.equals(AnalysisType.AVERAGE)) {
			Field<Integer> uniqueIndividualsField = _getUniqueIndividualsField(
				timeRange);

			field = DSL.when(
				uniqueIndividualsField.notEqual(0),
				DSL.count(
					field
				).cast(
					SQLDataType.DECIMAL
				).div(
					uniqueIndividualsField
				)
			).otherwise(
				BigDecimal.ZERO
			);

			return field.as("average");
		}

		if (analysisType.equals(AnalysisType.TOTAL)) {
			return DSL.count(
				field
			).as(
				"count"
			);
		}

		Field<Integer> uniqueIndividualsField = _getUniqueIndividualsField(
			timeRange);

		return uniqueIndividualsField.as("count");
	}

	private Field _getSortField(
		EventAnalysisBreakdown eventAnalysisBreakdown, String name) {

		if (eventAnalysisBreakdown == null) {
			return null;
		}

		if (eventAnalysisBreakdown.getSortType() == null) {
			eventAnalysisBreakdown.setSortType("ASC");
		}

		return DSL.field(name + " " + eventAnalysisBreakdown.getSortType());
	}

	private Field<OffsetDateTime> _getTruncatedEventDateField(
		String fieldName, Interval interval, String timeZoneId) {

		Field<OffsetDateTime> eventDateField =
			_dslHelper.getDateAtTimeZoneField(fieldName, timeZoneId);

		if (interval == Interval.DAY) {
			return _dslHelper.dateTrunc(DatePart.DAY, eventDateField);
		}
		else if (interval == Interval.HOUR) {
			return _dslHelper.dateTrunc(DatePart.HOUR, eventDateField);
		}
		else if (interval == Interval.MONTH) {
			return _dslHelper.dateTrunc(DatePart.MONTH, eventDateField);
		}

		return _dslHelper.dateTrunc(DatePart.WEEK, eventDateField);
	}

	private Field<Integer> _getUniqueIndividualsField(TimeRange timeRange) {
		if (timeRange == null) {
			return DSL.countDistinct(DSL.field("BQEvent.userId"));
		}

		return DSL.countDistinct(
			DSL.when(
				_getEventDateRangeFilter(
					"BQEvent.eventDate", timeRange.getEndDate(),
					timeRange.getStartDate()),
				DSL.field("BQEvent.userId")));
	}

	private Condition _getValueCondition(
		AttributeType attributeFilterType,
		EventAttributeDefinition eventAttributeDefinition,
		String filteredEventsTableName, Date rangeEndDate,
		Date rangeStartDate) {

		Condition condition = DSL.noCondition();

		if (attributeFilterType.equals(AttributeType.EVENT) &&
			!Objects.equals(
				eventAttributeDefinition.getType(),
				EventAttributeDefinition.Type.GLOBAL)) {

			if (filteredEventsTableName == null) {
				filteredEventsTableName = "BQEventProperty";
			}

			Field eventAttributeDefinitionIdField = DSL.field(
				filteredEventsTableName + ".name");

			condition = condition.and(
				eventAttributeDefinitionIdField.eq(
					_getEventAttributeDefinitionName(
						eventAttributeDefinition)));

			if ((rangeEndDate != null) && (rangeStartDate != null)) {
				Field<Object> eventDateField = DSL.field(
					filteredEventsTableName + ".eventDate");

				condition = condition.and(
					eventDateField.between(
						_dslHelper.getDateParam(rangeStartDate),
						_dslHelper.getDateParam(rangeEndDate)));
			}
		}

		return condition;
	}

	private Field _getValueField(
		boolean alias, EventAnalysisBreakdown eventAnalysisBreakdown,
		EventAttributeDefinition eventAttributeDefinition, String timeZoneId) {

		Field field = null;

		EventAttributeDefinition.DataType dataType =
			eventAnalysisBreakdown.getDataType();

		Field attributeField = null;

		if (Objects.equals(
				eventAttributeDefinition.getType(),
				EventAttributeDefinition.Type.GLOBAL)) {

			attributeField = _getGlobalAttributeField(
				eventAttributeDefinition.getName());
		}
		else {
			AttributeType attributeType =
				eventAnalysisBreakdown.getAttributeType();

			attributeField = DSL.field(
				attributeType.getQualifiedAttributeValueFieldName(
					_getSanitizedEventAttributeDefinitionName(
						eventAttributeDefinition)));
		}

		if (dataType.equals(EventAttributeDefinition.DataType.BOOLEAN)) {
			field = _dslHelper.getCastBooleanField(attributeField);
		}
		else if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
			DateGrouping dateGrouping =
				eventAnalysisBreakdown.getDateGrouping();

			if (dateGrouping.equals(DateGrouping.DAY)) {
				field = DSL.field(
					String.format(
						"FORMAT_DATE('%%Y-%%m-%%d', DATE(SAFE_CAST(%s AS " +
							"TIMESTAMP), '%s'))",
						attributeField, timeZoneId));
			}
			else if (dateGrouping.equals(DateGrouping.MONTH)) {
				field = DSL.field(
					String.format(
						"FORMAT_DATE('%%Y-%%m', DATE(SAFE_CAST(%s AS " +
							"TIMESTAMP), '%s'))",
						attributeField, timeZoneId));
			}
			else if (dateGrouping.equals(DateGrouping.YEAR)) {
				field = DSL.field(
					String.format(
						"FORMAT_DATE('%%Y', DATE(SAFE_CAST(%s AS TIMESTAMP), " +
							"'%s'))",
						attributeField, timeZoneId));
			}
			else {
				field = _dslHelper.getDateAtTimeZoneField(
					attributeField.getName(), timeZoneId);
			}
		}
		else if (dataType.equals(EventAttributeDefinition.DataType.DURATION)) {
			field = DSL.floor(
				DSL.round(
					DSL.abs(_dslHelper.getCastDurationField(attributeField)), -3
				).div(
					eventAnalysisBreakdown.getBinSize()
				)
			).multiply(
				eventAnalysisBreakdown.getBinSize()
			);
		}
		else if (dataType.equals(EventAttributeDefinition.DataType.NUMBER)) {
			Field castField = _dslHelper.getCastNumberField(attributeField);

			field = DSL.floor(
				castField.div(eventAnalysisBreakdown.getBinSize())
			).multiply(
				eventAnalysisBreakdown.getBinSize()
			);
		}
		else {
			field = DSL.lower(_dslHelper.getCastStringField(attributeField));
		}

		field = DSL.when(
			field.isNotNull(), _dslHelper.getCastStringField(field)
		).else_(
			"undefined"
		);

		if (alias) {
			return field.as(
				_getSanitizedEventAttributeDefinitionName(
					eventAttributeDefinition));
		}

		return field;
	}

	private boolean _hasOnlyLocalAttributes(
		List<EventAnalysisFilter> eventAnalysisFilters) {

		for (EventAttributeDefinition filter :
				_getEventAttributesDefinitions(eventAnalysisFilters)) {

			if (!_isGlobalAttribute(filter)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isGlobalAttribute(
		EventAttributeDefinition eventAttributeDefinition) {

		return Objects.equals(
			eventAttributeDefinition.getType(),
			EventAttributeDefinition.Type.GLOBAL);
	}

	private static final Map<String, String> _globalAttributes =
		new HashMap<String, String>() {
			{
				put("canonicalUrl", "canonicalUrl");
				put("pageDescription", "description");
				put("pageKeywords", "keywords");
				put("pageTitle", "title");
				put("referrer", "referrer");
				put("url", "url");
			}
		};

	private final DSLContext _dslContext;

	@Autowired
	private DSLHelper _dslHelper;

	@Autowired
	private EventAttributeDefinitionRepository
		_eventAttributeDefinitionRepository;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

	@Autowired
	private QueryExecutor _queryExecutor;

}