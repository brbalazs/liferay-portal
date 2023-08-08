/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.entity.EventAnalysis;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.AnalysisType;
import com.liferay.osb.asah.common.model.BreakdownItem;
import com.liferay.osb.asah.common.model.BreakdownRow;
import com.liferay.osb.asah.common.model.EventAnalysisBreakdown;
import com.liferay.osb.asah.common.model.EventAnalysisFilter;
import com.liferay.osb.asah.common.model.EventAnalysisResult;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.model.TimeRange;
import com.liferay.osb.asah.common.model.comparator.BreakdownItemComparator;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.EventAnalysisRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahDuplicateNameException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahNameException;
import com.liferay.osb.asah.common.util.GetterUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.StringUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.math.BigDecimal;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author Matthew Kong
 */
@Component
public class EventAnalysisDog {

	public EventAnalysis addEventAnalysis(
		AnalysisType analysisType, Long channelId, boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		String name, LocalDate rangeEndLocalDate, Integer rangeKey,
		LocalDate rangeStartLocalDate, Long userId, String userName) {

		_validateEventAnalysisName(channelId, null, name);
		_validateEventAnalysisBreakdowns(eventAnalysisBreakdowns);

		Date date = new Date();

		EventAnalysis eventAnalysis = new EventAnalysis();

		try {
			eventAnalysis.setEventAnalysisBreakdownJSONArray(
				_objectMapper.convertValue(
					eventAnalysisBreakdowns, JSONArray.class));
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		eventAnalysis.setChannelId(channelId);
		eventAnalysis.setCompareToPrevious(compareToPrevious);
		eventAnalysis.setCreateDate(date);
		eventAnalysis.setCreatedByUserId(userId);
		eventAnalysis.setCreatedByUserName(userName);
		eventAnalysis.setEventAnalysisType(analysisType.name());
		eventAnalysis.setEventDefinitionId(eventDefinitionId);

		try {
			eventAnalysis.setEventAnalysisFilterJSONArray(
				_objectMapper.convertValue(
					eventAnalysisFilters, JSONArray.class));
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		eventAnalysis.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		eventAnalysis.setIsNew(Boolean.TRUE);
		eventAnalysis.setModifiedByUserId(userId);
		eventAnalysis.setModifiedByUserName(userName);
		eventAnalysis.setModifiedDate(date);
		eventAnalysis.setName(name);
		eventAnalysis.setRangeEndLocalDate(rangeEndLocalDate);
		eventAnalysis.setRangeKey(rangeKey);
		eventAnalysis.setRangeStartLocalDate(rangeStartLocalDate);

		return _eventAnalysisRepository.save(eventAnalysis);
	}

	public void deleteEventAnalyses(List<Long> eventAnalysisIds) {
		_eventAnalysisRepository.deleteByIdIn(new HashSet<>(eventAnalysisIds));
	}

	public EventAnalysis getEventAnalysis(Long eventAnalysisId) {
		Optional<EventAnalysis> eventAnalysisOptional =
			_eventAnalysisRepository.findById(eventAnalysisId);

		return eventAnalysisOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no event analysis with ID " + eventAnalysisId));
	}

	public Page<EventAnalysis> getEventAnalysisPage(
		Long channelId, @Nullable String keywords, int page, int size,
		Sort sort) {

		_validate(sort);

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_eventAnalysisRepository.searchEventAnalyses(
				channelId, keywords, pageRequest),
			pageRequest,
			() -> _eventAnalysisRepository.countEventAnalyses(
				channelId, keywords));
	}

	public EventAnalysisResult getEventAnalysisResult(
		AnalysisType analysisType, Long channelId, boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		int page, int size, TimeRange timeRange) {

		_validateEventAnalysisBreakdowns(eventAnalysisBreakdowns);

		EventAnalysisResult eventAnalysisResult = new EventAnalysisResult();

		eventAnalysisResult.setPage(page);

		String projectId = ProjectIdThreadLocal.getProjectId();
		String timeZoneId = _timeZoneDog.getTimeZoneId();

		CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(
			() -> {
				ProjectIdThreadLocal.setProjectId(projectId);

				eventAnalysisResult.setValue(
					_getAnalysisCount(
						analysisType, channelId, eventAnalysisFilters,
						eventDefinitionId, timeRange, timeZoneId));
			},
			_executorService);

		if (compareToPrevious) {
			completableFuture = CompletableFuture.allOf(
				completableFuture,
				CompletableFuture.runAsync(
					() -> {
						ProjectIdThreadLocal.setProjectId(projectId);

						eventAnalysisResult.setPreviousValue(
							_getAnalysisCount(
								analysisType, channelId, eventAnalysisFilters,
								eventDefinitionId,
								timeRange.getPreviousTimeRange(), timeZoneId));
					},
					_executorService));
		}

		completableFuture = CompletableFuture.allOf(
			completableFuture.thenRunAsync(
				() -> {
					ProjectIdThreadLocal.setProjectId(projectId);

					eventAnalysisResult.setBreakdownItems(
						_getBreakdownItems(
							analysisType, channelId, compareToPrevious,
							eventAnalysisResult.getValue(),
							eventAnalysisBreakdowns, eventAnalysisFilters,
							eventDefinitionId, PageRequest.of(page, size),
							eventAnalysisResult.getPreviousValue(), timeRange,
							timeZoneId));
				},
				_executorService),
			CompletableFuture.runAsync(
				() -> {
					ProjectIdThreadLocal.setProjectId(projectId);

					eventAnalysisResult.setCount(
						_getTotalPagesCount(
							channelId, eventAnalysisBreakdowns,
							eventAnalysisFilters, eventDefinitionId, timeRange,
							timeZoneId));
				},
				_executorService));

		completableFuture.join();

		return eventAnalysisResult;
	}

	public EventAnalysis updateEventAnalysis(
		AnalysisType analysisType, Long channelId, boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventAnalysisId,
		Long eventDefinitionId, String name, LocalDate rangeEndLocalDate,
		Integer rangeKey, LocalDate rangeStartLocalDate, Long userId,
		String userName) {

		_validateEventAnalysisName(channelId, eventAnalysisId, name);
		_validateEventAnalysisBreakdowns(eventAnalysisBreakdowns);

		EventAnalysis eventAnalysis = getEventAnalysis(eventAnalysisId);

		try {
			eventAnalysis.setEventAnalysisBreakdownJSONArray(
				_objectMapper.convertValue(
					eventAnalysisBreakdowns, JSONArray.class));
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		eventAnalysis.setChannelId(channelId);
		eventAnalysis.setCompareToPrevious(compareToPrevious);
		eventAnalysis.setEventAnalysisType(analysisType.name());
		eventAnalysis.setEventDefinitionId(eventDefinitionId);

		try {
			eventAnalysis.setEventAnalysisFilterJSONArray(
				_objectMapper.convertValue(
					eventAnalysisFilters, JSONArray.class));
		}
		catch (Exception exception) {
			_log.error(exception, exception);
		}

		eventAnalysis.setModifiedByUserId(userId);
		eventAnalysis.setModifiedByUserName(userName);
		eventAnalysis.setModifiedDate(new Date());
		eventAnalysis.setName(name);
		eventAnalysis.setRangeEndLocalDate(rangeEndLocalDate);
		eventAnalysis.setRangeKey(rangeKey);
		eventAnalysis.setRangeStartLocalDate(rangeStartLocalDate);

		return _eventAnalysisRepository.save(eventAnalysis);
	}

	private List<BreakdownItem> _createBreakdownItems(
		List<BreakdownRow> breakdownRows, boolean compareToPrevious,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		EventDefinition eventDefinition) {

		BreakdownRow firstBreakdownRow = breakdownRows.get(0);

		List<BreakdownItem> breakdownItems = _createBreakdownItems(
			breakdownRows, firstBreakdownRow.getBreakdownColumnsCount(),
			compareToPrevious, 0, eventAnalysisBreakdowns, eventDefinition);

		Stream<BreakdownItem> stream = breakdownItems.stream();

		return stream.filter(
			breakdownItem -> {
				Number value = breakdownItem.getValue();

				return value.floatValue() > 0;
			}
		).collect(
			Collectors.toList()
		);
	}

	private List<BreakdownItem> _createBreakdownItems(
		List<BreakdownRow> breakdownRows, int columnsCount,
		boolean compareToPrevious, int currentColumnIndex,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		EventDefinition eventDefinition) {

		boolean leafBreakdownItem = false;

		if ((compareToPrevious &&
			 (currentColumnIndex ==
				 (columnsCount - _BREAKDOWN_LEAF_INCLUDE_PREVIOUS_OFFSET))) ||
			(currentColumnIndex == (columnsCount - _BREAKDOWN_LEAF_OFFSET))) {

			leafBreakdownItem = true;
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				String.format(
					"compareToPrevious: %b, currentColumnIndex: %d, " +
						"columnsCount: %d, isLeaf %b",
					compareToPrevious, currentColumnIndex, columnsCount,
					leafBreakdownItem));
		}

		if (leafBreakdownItem) {
			Stream<BreakdownRow> stream = breakdownRows.stream();

			return stream.map(
				breakdownRow -> _createLeafBreakdownItem(
					breakdownRow, columnsCount, compareToPrevious,
					currentColumnIndex, eventDefinition)
			).sorted(
				new BreakdownItemComparator(true)
			).collect(
				Collectors.toList()
			);
		}

		List<BreakdownItem> breakdownItems = new ArrayList<>();

		Stream<BreakdownRow> stream1 = breakdownRows.stream();

		Map<BreakdownRow.BreakdownColumn, List<BreakdownRow>> breakdownRowsMap =
			stream1.collect(
				Collectors.groupingBy(
					breakdownRow -> breakdownRow.getBreakdownColumn(
						currentColumnIndex)));

		EventAnalysisBreakdown eventAnalysisBreakdown =
			eventAnalysisBreakdowns.get(currentColumnIndex);

		for (Map.Entry<BreakdownRow.BreakdownColumn, List<BreakdownRow>> entry :
				breakdownRowsMap.entrySet()) {

			BreakdownItem breakdownItem = new BreakdownItem();

			BreakdownRow.BreakdownColumn breakdownColumn = entry.getKey();

			breakdownItem.setInternalName(
				String.valueOf(breakdownColumn.getValue()));

			breakdownItem.setName(
				_getBreakdownItemName(
					eventAnalysisBreakdown,
					String.valueOf(breakdownColumn.getValue())));

			List<BreakdownItem> childrenBreakdownItems = new ArrayList<>();

			BigDecimal previousValue = BigDecimal.ZERO;
			BigDecimal value = BigDecimal.ZERO;

			for (BreakdownItem childBreakdownItem :
					_createBreakdownItems(
						entry.getValue(), columnsCount, compareToPrevious,
						currentColumnIndex + 1, eventAnalysisBreakdowns,
						eventDefinition)) {

				previousValue = previousValue.add(
					new BigDecimal(
						String.valueOf(childBreakdownItem.getPreviousValue())));

				BigDecimal childBreakdownItemValue = new BigDecimal(
					String.valueOf(childBreakdownItem.getValue()));

				value = value.add(childBreakdownItemValue);

				if (childBreakdownItemValue.compareTo(BigDecimal.ZERO) > 0) {
					childrenBreakdownItems.add(childBreakdownItem);
				}
			}

			breakdownItem.setBreakdownItems(childrenBreakdownItems);
			breakdownItem.setPreviousValue(previousValue);
			breakdownItem.setValue(value);

			breakdownItems.add(breakdownItem);
		}

		boolean ascending = false;

		if (StringUtils.equals(eventAnalysisBreakdown.getSortType(), "ASC")) {
			ascending = true;
		}

		breakdownItems.sort(new BreakdownItemComparator(ascending));

		return breakdownItems;
	}

	private List<BreakdownItem> _createComparisonBreakdownItems(
		Number previousValue, Number value) {

		BreakdownItem allIndividualsBreakdownItem = new BreakdownItem();

		allIndividualsBreakdownItem.setName("All Individuals");
		allIndividualsBreakdownItem.setPreviousValue(previousValue);
		allIndividualsBreakdownItem.setValue(value);

		return Collections.singletonList(allIndividualsBreakdownItem);
	}

	private BreakdownItem _createLeafBreakdownItem(
		BreakdownRow breakdownRow, int columnsCount, boolean compareToPrevious,
		int currentColumnIndex, EventDefinition eventDefinition) {

		BreakdownRow.BreakdownColumn breakdownColumn =
			breakdownRow.getBreakdownColumn(currentColumnIndex++);

		Number value = GetterUtil.getNumber(breakdownColumn.getValue());

		Number previousValue = 0;

		if (compareToPrevious && (currentColumnIndex == (columnsCount - 1))) {
			breakdownColumn = breakdownRow.getBreakdownColumn(
				currentColumnIndex);

			previousValue = GetterUtil.getNumber(breakdownColumn.getValue());
		}

		return _createLeafNodeBreakdownItem(
			eventDefinition.getDisplayName(), previousValue, value);
	}

	private BreakdownItem _createLeafNodeBreakdownItem(
		String eventDefinitionName, Number previousValue, Number value) {

		BreakdownItem eventDefinitionBreakdownItem = new BreakdownItem();

		eventDefinitionBreakdownItem.setBreakdownItems(
			_createComparisonBreakdownItems(previousValue, value));
		eventDefinitionBreakdownItem.setLeafNode(true);
		eventDefinitionBreakdownItem.setName(eventDefinitionName);
		eventDefinitionBreakdownItem.setPreviousValue(previousValue);
		eventDefinitionBreakdownItem.setValue(value);

		return eventDefinitionBreakdownItem;
	}

	private Number _getAnalysisCount(
		AnalysisType analysisType, Long channelId,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		TimeRange timeRange, String timeZoneId) {

		if (analysisType.equals(AnalysisType.AVERAGE)) {
			return _bqEventRepository.getAverageBQEventCountPerIndividual(
				channelId, eventAnalysisFilters, eventDefinitionId,
				timeRange.getEndDate(), timeRange.getStartDate(), timeZoneId);
		}

		if (analysisType.equals(AnalysisType.TOTAL)) {
			return _bqEventRepository.countTotalBQEvents(
				channelId, eventAnalysisFilters, eventDefinitionId,
				timeRange.getEndDate(), timeRange.getStartDate(), timeZoneId);
		}

		if (analysisType.equals(AnalysisType.UNIQUE)) {
			return _bqEventRepository.countUniqueIndividuals(
				channelId, eventAnalysisFilters, eventDefinitionId,
				timeRange.getEndDate(), timeRange.getStartDate(), timeZoneId);
		}

		return null;
	}

	private String _getBreakdownItemName(
		EventAnalysisBreakdown eventAnalysisBreakdown, String key) {

		if (StringUtil.isNull(key) || key.equals("undefined")) {
			return "undefined";
		}

		EventAttributeDefinition.DataType dataType =
			eventAnalysisBreakdown.getDataType();

		if (dataType.equals(EventAttributeDefinition.DataType.DURATION)) {
			long binFloor = Math.abs(Long.parseLong(key));
			Number binSize = eventAnalysisBreakdown.getBinSize();

			return binFloor + " - " + (binFloor + binSize.longValue() - 1000);
		}

		if (dataType.equals(EventAttributeDefinition.DataType.NUMBER)) {
			int binFloor = Math.round(Float.parseFloat(key));
			Number binSize = eventAnalysisBreakdown.getBinSize();

			int binCeil = binFloor + binSize.intValue() - 1;

			if (binFloor < 0) {
				String name = "(" + binFloor + ") - ";

				if (binCeil < 0) {
					return name + "(" + binCeil + ")";
				}

				return name + binCeil;
			}

			return binFloor + " - " + binCeil;
		}

		return key;
	}

	private List<BreakdownItem> _getBreakdownItems(
		AnalysisType analysisType, Long channelId, boolean compareToPrevious,
		Number eventAnalysisValue,
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		Pageable pageable, Number previousEventAnalysisValue,
		TimeRange timeRange, String timeZoneId) {

		EventDefinition eventDefinition =
			_eventDefinitionDog.getEventDefinition(eventDefinitionId);

		if (eventAnalysisBreakdowns.isEmpty()) {
			return Collections.singletonList(
				_createLeafNodeBreakdownItem(
					eventDefinition.getDisplayName(),
					previousEventAnalysisValue, eventAnalysisValue));
		}

		List<BreakdownRow> breakdownRows =
			_bqEventRepository.getBQEventPropertyValues(
				analysisType, channelId, compareToPrevious,
				eventAnalysisBreakdowns, eventAnalysisFilters,
				eventDefinitionId, pageable, timeRange, timeZoneId);

		if (breakdownRows.isEmpty()) {
			return Collections.emptyList();
		}

		if (_log.isDebugEnabled()) {
			_log.debug(
				"Processing breakdown rows: " + breakdownRows.toString());
		}

		return _createBreakdownItems(
			breakdownRows, compareToPrevious, eventAnalysisBreakdowns,
			eventDefinition);
	}

	private long _getTotalPagesCount(
		Long channelId, List<EventAnalysisBreakdown> eventAnalysisBreakdowns,
		List<EventAnalysisFilter> eventAnalysisFilters, Long eventDefinitionId,
		TimeRange timeRange, String timeZoneId) {

		if (CollectionUtils.isEmpty(eventAnalysisBreakdowns)) {
			return 1;
		}

		return _bqEventRepository.getBQEventPropertyValuesCount(
			channelId, eventAnalysisBreakdowns.get(0), eventAnalysisFilters,
			eventDefinitionId, timeRange, timeZoneId);
	}

	private void _validate(Sort sort) {
		String sortColumn = sort.getColumn();

		if (Objects.equals(sortColumn, "createdByUserName") ||
			Objects.equals(sortColumn, "name")) {

			return;
		}

		throw new OSBAsahException(
			HttpStatus.BAD_REQUEST,
			"Unable to sort event analysis by " + sortColumn);
	}

	private void _validateEventAnalysisBreakdowns(
		List<EventAnalysisBreakdown> eventAnalysisBreakdowns) {

		for (EventAnalysisBreakdown eventAnalysisBreakdown :
				eventAnalysisBreakdowns) {

			EventAttributeDefinition.DataType dataType =
				eventAnalysisBreakdown.getDataType();

			if (dataType.equals(EventAttributeDefinition.DataType.DATE)) {
				if (eventAnalysisBreakdown.getDateGrouping() == null) {
					throw new OSBAsahException(
						HttpStatus.BAD_REQUEST, "Date grouping is null");
				}
			}
			else if (dataType.equals(
						EventAttributeDefinition.DataType.DURATION) ||
					 dataType.equals(
						 EventAttributeDefinition.DataType.NUMBER)) {

				Number binSize = eventAnalysisBreakdown.getBinSize();

				if ((binSize == null) || (binSize.doubleValue() <= 0)) {
					throw new OSBAsahException(
						HttpStatus.BAD_REQUEST, "Invalid bin size " + binSize);
				}
			}
		}
	}

	private void _validateEventAnalysisName(
		Long channelId, @Nullable Long eventAnalysisId, String name) {

		if (StringUtils.isBlank(name)) {
			throw new OSBAsahNameException();
		}

		Optional<EventAnalysis> eventAnalysisOptional =
			_eventAnalysisRepository.findByChannelIdAndNameIgnoreCase(
				channelId, name);

		EventAnalysis eventAnalysis = eventAnalysisOptional.orElse(null);

		if ((eventAnalysis != null) &&
			!Objects.equals(eventAnalysis.getId(), eventAnalysisId)) {

			throw new OSBAsahDuplicateNameException();
		}
	}

	private static final int _BREAKDOWN_LEAF_INCLUDE_PREVIOUS_OFFSET = 2;

	private static final int _BREAKDOWN_LEAF_OFFSET = 1;

	private static final Log _log = LogFactory.getLog(EventAnalysisDog.class);

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private EventAnalysisRepository _eventAnalysisRepository;

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

	private final ExecutorService _executorService =
		Executors.newFixedThreadPool(4);

	@Autowired
	private ObjectMapper _objectMapper;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

	@Autowired
	private TimeZoneDog _timeZoneDog;

}