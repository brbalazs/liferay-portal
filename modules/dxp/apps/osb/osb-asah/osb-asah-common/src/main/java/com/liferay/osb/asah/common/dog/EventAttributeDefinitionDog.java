/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.dog;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.EventAttributeDefinition;
import com.liferay.osb.asah.common.entity.EventDefinitionEventAttributeDefinition;
import com.liferay.osb.asah.common.model.Sort;
import com.liferay.osb.asah.common.repository.EventAttributeDefinitionRepository;
import com.liferay.osb.asah.common.spring.http.exception.OSBAsahException;
import com.liferay.osb.asah.common.util.ArrayUtil;
import com.liferay.osb.asah.common.util.TimeOrderedUuidGenerator;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author Leslie Wong
 */
@Component
public class EventAttributeDefinitionDog {

	public EventAttributeDefinition addEventAttributeDefinition(
		String description, String displayName, Long eventDefinitionId,
		String name, String sampleValue) {

		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Event attribute name is null");
		}

		EventAttributeDefinition eventAttributeDefinition =
			new EventAttributeDefinition();

		eventAttributeDefinition.setDataType(getDataType(name, sampleValue));
		eventAttributeDefinition.setDescription(description);
		eventAttributeDefinition.setDisplayName(
			_getDisplayName(displayName, name));
		eventAttributeDefinition.setEventDefinitionEventAttributeDefinitions(
			Collections.singleton(
				new EventDefinitionEventAttributeDefinition(
					eventDefinitionId, sampleValue)));
		eventAttributeDefinition.setId(
			_timeOrderedUuidGenerator.generateIdAsLong());
		eventAttributeDefinition.setIsNew(Boolean.TRUE);
		eventAttributeDefinition.setName(name);
		eventAttributeDefinition.setType(EventAttributeDefinition.Type.LOCAL);

		return _eventAttributeDefinitionRepository.save(
			eventAttributeDefinition);
	}

	public Long countEventAttributeDefinitions(
		Long eventDefinitionId, String keyword,
		EventAttributeDefinition.Type type) {

		return _eventAttributeDefinitionRepository.
			countEventAttributeDefinitions(eventDefinitionId, keyword, type);
	}

	public EventAttributeDefinition fetchEventAttributeDefinitionByDisplayName(
		String displayName) {

		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findByDisplayNameIgnoreCase(
				displayName);

		return eventAttributeDefinitionOptional.orElse(null);
	}

	public EventAttributeDefinition fetchEventAttributeDefinitionByName(
		String name) {

		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findByName(name);

		return eventAttributeDefinitionOptional.orElse(null);
	}

	public EventAttributeDefinition.DataType getDataType(
		String propertyName, String propertyValue) {

		if (StringUtils.isEmpty(propertyValue)) {
			throw new IllegalArgumentException(
				"Unable to determine data type for property " + propertyName);
		}

		if (_isBoolean(propertyValue)) {
			return EventAttributeDefinition.DataType.BOOLEAN;
		}

		if (_isDate(propertyValue)) {
			return EventAttributeDefinition.DataType.DATE;
		}

		if (NumberUtils.isParsable(propertyValue)) {
			String lowerCasePropertyName = propertyName.toLowerCase();

			if (lowerCasePropertyName.contains("duration") &&
				!propertyValue.startsWith("-")) {

				return EventAttributeDefinition.DataType.DURATION;
			}

			return EventAttributeDefinition.DataType.NUMBER;
		}

		return EventAttributeDefinition.DataType.STRING;
	}

	public EventAttributeDefinition getEventAttributeDefinition(
		Long eventAttributeDefinitionId) {

		Optional<EventAttributeDefinition> eventAttributeDefinitionOptional =
			_eventAttributeDefinitionRepository.findById(
				eventAttributeDefinitionId);

		return eventAttributeDefinitionOptional.orElseThrow(
			() -> new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"There is no event attribute definition with ID " +
					eventAttributeDefinitionId));
	}

	public Map<String, Long> getEventAttributeDefinitionIdsByType(
		EventAttributeDefinition.Type type) {

		List<EventAttributeDefinition> eventAttributeDefinitions =
			_eventAttributeDefinitionRepository.findByType(type);

		Stream<EventAttributeDefinition> eventAttributeDefinitionsStream =
			eventAttributeDefinitions.stream();

		return eventAttributeDefinitionsStream.collect(
			Collectors.toMap(
				EventAttributeDefinition::getName,
				EventAttributeDefinition::getId));
	}

	public Page<EventAttributeDefinition> getEventAttributeDefinitionPage(
		Long eventDefinitionId, String keyword, int page, int size, Sort sort,
		EventAttributeDefinition.Type type) {

		_validate(sort);

		PageRequest pageRequest = PageRequest.of(page, size, sort);

		return PageableExecutionUtils.getPage(
			_eventAttributeDefinitionRepository.searchEventAttributeDefinitions(
				eventDefinitionId, keyword, pageRequest, type),
			pageRequest,
			() ->
				_eventAttributeDefinitionRepository.
					countEventAttributeDefinitions(
						eventDefinitionId, keyword, type));
	}

	public List<EventAttributeDefinition> getEventAttributeDefinitions(
		List<Long> eventAttributeDefinitionIds) {

		return IterableUtils.toList(
			_eventAttributeDefinitionRepository.findAllById(
				eventAttributeDefinitionIds));
	}

	public List<EventAttributeDefinition>
		getEventAttributeDefinitionsByEventDefinitionId(
			Long eventDefinitionId) {

		return _eventAttributeDefinitionRepository.findByEventDefinitionId(
			eventDefinitionId);
	}

	public List<EventAttributeDefinition> getEventAttributeDefinitionsByType(
		EventAttributeDefinition.Type type) {

		if (Objects.equals(type, EventAttributeDefinition.Type.ALL)) {
			return IterableUtils.toList(
				_eventAttributeDefinitionRepository.findAll());
		}

		return _eventAttributeDefinitionRepository.findByType(type);
	}

	public EventAttributeDefinition updateEventAttributeDefinition(
		EventAttributeDefinition.DataType dataType, String description,
		String displayName, Long eventAttributeDefinitionId,
		Set<EventDefinitionEventAttributeDefinition>
			eventDefinitionEventAttributeDefinitions,
		String name) {

		EventAttributeDefinition eventAttributeDefinition =
			getEventAttributeDefinition(eventAttributeDefinitionId);

		if (dataType != null) {
			eventAttributeDefinition.setDataType(dataType);
		}

		if (description != null) {
			if (description.isEmpty()) {
				eventAttributeDefinition.setDescription(null);
			}
			else {
				eventAttributeDefinition.setDescription(description);
			}
		}

		if (StringUtils.isNotBlank(displayName)) {
			EventAttributeDefinition eventAttributeDefinitionByDisplayName =
				fetchEventAttributeDefinitionByDisplayName(displayName);

			if ((eventAttributeDefinitionByDisplayName != null) &&
				!eventAttributeDefinitionId.equals(
					eventAttributeDefinitionByDisplayName.getId())) {

				throw new OSBAsahException(
					HttpStatus.BAD_REQUEST,
					String.format(
						"Display name %s is already used", displayName));
			}

			eventAttributeDefinition.setDisplayName(displayName);
		}

		if (StringUtils.isNotBlank(name)) {
			eventAttributeDefinition.setName(name);
		}

		if (eventDefinitionEventAttributeDefinitions != null) {
			eventAttributeDefinition.
				setEventDefinitionEventAttributeDefinitions(
					eventDefinitionEventAttributeDefinitions);
		}

		return _eventAttributeDefinitionRepository.save(
			eventAttributeDefinition);
	}

	private String _getDisplayName(String displayName, String name) {
		if (StringUtils.isBlank(displayName)) {
			displayName = name;
		}

		int nameCount = 0;
		String originalName = displayName;

		while (fetchEventAttributeDefinitionByDisplayName(displayName) !=
					null) {

			displayName = String.format("%s (%d)", originalName, ++nameCount);
		}

		return displayName;
	}

	private boolean _isBoolean(String value) {
		return ArrayUtil.contains(_BOOLEANS, value.toLowerCase());
	}

	private boolean _isDate(String value) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(
				DateUtil.PATTERN_ISO_8601);

			dateFormat.parse(value);

			return true;
		}
		catch (ParseException parseException) {
			if (_log.isDebugEnabled()) {
				_log.debug(parseException, parseException);
			}

			return false;
		}
	}

	private void _validate(Sort sort) {
		String sortColumn = sort.getColumn();

		if (!Objects.equals(sortColumn, "name") &&
			!Objects.equals(sortColumn, "displayName")) {

			throw new OSBAsahException(
				HttpStatus.BAD_REQUEST,
				"Unable to sort event attribute definitions by " + sortColumn);
		}
	}

	private static final String[] _BOOLEANS = {"false", "true"};

	private static final Log _log = LogFactory.getLog(
		EventAttributeDefinitionDog.class);

	@Autowired
	private EventAttributeDefinitionRepository
		_eventAttributeDefinitionRepository;

	private final TimeOrderedUuidGenerator _timeOrderedUuidGenerator =
		new TimeOrderedUuidGenerator();

}