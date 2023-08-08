/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.constants.EventDefinitionConstants;
import com.liferay.osb.asah.common.entity.BQEvent;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.BQEventRepository;
import com.liferay.osb.asah.common.repository.EventDefinitionRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class EventDefinitionDog {

	public EventDefinition addEventDefinition(
		String applicationId, String description, String displayName,
		Date eventDate, String name, EventDefinition.Type type, String url) {

		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Event name is null");
		}

		EventDefinition eventDefinition = new EventDefinition();

		if ((type == EventDefinition.Type.CUSTOM) &&
			(countEventDefinitions(false, null, null, type) >=
				EventDefinitionConstants.EVENT_DEFINITION_THRESHOLD)) {

			eventDefinition.setBlocked(true);
			eventDefinition.setBlockedLastSeenDate(eventDate);
			eventDefinition.setBlockedLastSeenURL(url);
			eventDefinition.setBlockedReasonType(
				EventDefinition.BlockedReasonType.THRESHOLD_OVERFLOW);

			if (_log.isWarnEnabled()) {
				_log.warn(
					String.format(
						"Blocking event definition %s as limit is reached",
						name));
			}
		}

		eventDefinition.setApplicationId(applicationId);
		eventDefinition.setDescription(description);
		eventDefinition.setDisplayName(_getDisplayName(displayName, name));
		eventDefinition.setId(_timeOrderedUuidGenerator.generateIdAsLong());
		eventDefinition.setIsNew(Boolean.TRUE);
		eventDefinition.setName(name);
		eventDefinition.setType(type);

		return _eventDefinitionRepository.save(eventDefinition);
	}

	public void blockEventDefinitions(List<Long> eventDefinitionIds) {
		eventDefinitionIds.forEach(this::_blockEventDefinition);
	}

	public Long countEventDefinitions(
		@Nullable Boolean blocked, @Nullable Boolean hidden,
		@Nullable String keyword, @Nullable EventDefinition.Type type) {

		return _eventDefinitionRepository.countEventDefinitions(
			blocked, null, hidden, keyword, type);
	}

	public Long countEventDefinitions(
		@Nullable Boolean blocked,
		@Nullable EventDefinition.BlockedReasonType blockedReason,
		@Nullable Boolean hidden, @Nullable String keyword,
		@Nullable EventDefinition.Type type) {

		return _eventDefinitionRepository.countEventDefinitions(
			blocked, blockedReason, hidden, keyword, type);
	}

	public void deleteEventDefinitionById(Long id) {
		_eventDefinitionRepository.deleteById(id);
	}

	public EventDefinition fetchEventDefinition(Long eventDefinitionId) {
		if (eventDefinitionId == null) {
			return null;
		}

		Optional<EventDefinition> eventDefinitionOptional =
			_eventDefinitionRepository.findById(eventDefinitionId);

		return eventDefinitionOptional.orElse(null);
	}

	public EventDefinition fetchEventDefinitionByDisplayName(
		String displayName) {

		Optional<EventDefinition> eventDefinitionOptional =
			_eventDefinitionRepository.findByDisplayNameIgnoreCase(displayName);

		return eventDefinitionOptional.orElse(null);
	}

	public EventDefinition fetchEventDefinitionByName(String name) {
		Optional<EventDefinition> eventDefinitionOptional =
			_eventDefinitionRepository.findByName(name);

		return eventDefinitionOptional.orElse(null);
	}

	public List<EventDefinition> fetchEventDefinitions(
		List<Long> eventDefinitionIds) {

		return IterableUtils.toList(
			_eventDefinitionRepository.findAllById(eventDefinitionIds));
	}

	public EventDefinition getEventDefinition(Long eventDefinitionId) {
		if (eventDefinitionId == null) {
			throw new IllegalArgumentException("Event definition ID is null");
		}

		Optional<EventDefinition> eventDefinitionOptional =
			_eventDefinitionRepository.findById(eventDefinitionId);

		return eventDefinitionOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no event definition with ID " + eventDefinitionId));
	}

	public Page<EventDefinition> getEventDefinitionPage(
		@Nullable Boolean blocked, @Nullable Boolean hidden,
		@Nullable String keyword, int page, int size, Sort sort,
		EventDefinition.Type type) {

		_validate(blocked, sort);

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_eventDefinitionRepository.searchEventDefinitions(
				blocked, null, hidden, keyword, pageRequest, type),
			pageRequest,
			() -> _eventDefinitionRepository.countEventDefinitions(
				blocked, null, hidden, keyword, type));
	}

	public Map<String, EventDefinition> getEventDefinitions(
		Set<String> eventDefinitionNames) {

		List<EventDefinition> eventDefinitions =
			_eventDefinitionRepository.findByNameIn(eventDefinitionNames);

		Stream<EventDefinition> eventDefinitionsStream =
			eventDefinitions.stream();

		return eventDefinitionsStream.collect(
			Collectors.toMap(
				EventDefinition::getName, eventDefinition -> eventDefinition));
	}

	public void hideEventDefinitions(List<Long> eventDefinitionIds) {
		_eventDefinitionRepository.updateEventDefinitions(
			eventDefinitionIds, Boolean.TRUE);
	}

	public void unblockEventDefinitions(List<Long> eventDefinitionIds) {
		_validateEventDefinitionLimit(eventDefinitionIds.size());

		eventDefinitionIds.forEach(this::_unblockEventDefinition);
	}

	public void unhideEventDefinitions(List<Long> eventDefinitionIds) {
		_eventDefinitionRepository.updateEventDefinitions(
			eventDefinitionIds, Boolean.FALSE);
	}

	public EventDefinition updateEventDefinition(
		@Nullable Date blockedLastSeenDate, @Nullable String blockedLastSeenURL,
		@Nullable String description, @Nullable String displayName,
		Long eventDefinitionId) {

		EventDefinition eventDefinition = getEventDefinition(eventDefinitionId);

		if (blockedLastSeenDate != null) {
			eventDefinition.setBlockedLastSeenDate(blockedLastSeenDate);
		}

		if (blockedLastSeenURL != null) {
			eventDefinition.setBlockedLastSeenURL(blockedLastSeenURL);
		}

		if (description != null) {
			eventDefinition.setDescription(description);
		}

		if (StringUtils.isNotBlank(displayName)) {
			EventDefinition eventDefinitionByDisplayName =
				fetchEventDefinitionByDisplayName(displayName);

			if ((eventDefinitionByDisplayName != null) &&
				!eventDefinitionId.equals(
					eventDefinitionByDisplayName.getId())) {

				throw new OSBAsahException(
					HttpStatus.BAD_REQUEST,
					String.format(
						"Display name %s is already used", displayName));
			}

			eventDefinition.setDisplayName(displayName);
		}

		return _eventDefinitionRepository.save(eventDefinition);
	}

	private void _blockEventDefinition(Long eventDefinitionId) {
		EventDefinition eventDefinition = getEventDefinition(eventDefinitionId);

		if (eventDefinition.getType() != EventDefinition.Type.CUSTOM) {
			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Event definition is not of type custom");
		}

		eventDefinition.setBlocked(true);

		BQEvent lastSeenBQEvent = _fetchLastSeenBQEvent(eventDefinitionId);

		if (lastSeenBQEvent != null) {
			eventDefinition.setBlockedLastSeenDate(
				lastSeenBQEvent.getEventDate());
			eventDefinition.setBlockedLastSeenURL(
				lastSeenBQEvent.getCanonicalUrl());
		}
		else if (_log.isWarnEnabled()) {
			_log.warn(
				"Unable to find event for event definition ID " +
					eventDefinitionId);
		}

		eventDefinition.setBlockedReasonType(
			EventDefinition.BlockedReasonType.BLOCKED_BY_USER);

		_eventDefinitionRepository.save(eventDefinition);
	}

	private BQEvent _fetchLastSeenBQEvent(Long eventDefinitionId) {
		Optional<BQEvent> lastSeenBQEventOptional =
			_bqEventRepository.findLastSeenBQEvent(eventDefinitionId);

		return lastSeenBQEventOptional.orElse(null);
	}

	private String _getDisplayName(String displayName, String name) {
		if (StringUtils.isBlank(displayName)) {
			displayName = name;
		}

		int nameCount = 0;
		String originalName = displayName;

		while (fetchEventDefinitionByDisplayName(displayName) != null) {
			displayName = String.format("%s (%d)", originalName, ++nameCount);
		}

		return displayName;
	}

	private void _unblockEventDefinition(Long eventDefinitionId) {
		EventDefinition eventDefinition = getEventDefinition(eventDefinitionId);

		eventDefinition.setBlocked(false);
		eventDefinition.setBlockedLastSeenDate(null);
		eventDefinition.setBlockedLastSeenURL(null);
		eventDefinition.setBlockedReasonType(null);

		_validateEventDefinitionLimit(1);

		_eventDefinitionRepository.save(eventDefinition);
	}

	private void _validate(Boolean blocked, Sort sort) {
		String sortColumn = sort.getColumn();

		if ((!Objects.equals(sortColumn, "blockedLastSeenDate") ||
			 !BooleanUtils.toBoolean(blocked)) &&
			!Objects.equals(sortColumn, "displayName") &&
			!Objects.equals(sortColumn, "name")) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Unable to sort event definitions by " + sortColumn);
		}
	}

	private void _validateEventDefinitionLimit(int unblockEventDefinitionSize) {
		long count = _eventDefinitionRepository.countEventDefinitions(
			Boolean.FALSE, null, null, null, EventDefinition.Type.CUSTOM);

		if ((count + unblockEventDefinitionSize) >
				EventDefinitionConstants.EVENT_DEFINITION_THRESHOLD) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Processing request will exceed custom event definition limit");
		}
	}

	private static final Log _log = LogFactory.getLog(EventDefinitionDog.class);

	@Autowired
	private BQEventRepository _bqEventRepository;

	@Autowired
	private EventDefinitionRepository _eventDefinitionRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}