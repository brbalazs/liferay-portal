/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.publisher.rest.controller;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import com.liferay.osb.asah.common.concurrent.BoundedExecutor;
import com.liferay.osb.asah.common.constants.ServiceConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.date.dog.TimeZoneDog;
import com.liferay.osb.asah.common.dog.DataSourceDog;
import com.liferay.osb.asah.common.dog.EventDefinitionDog;
import com.liferay.osb.asah.common.dog.EventStorageDog;
import com.liferay.osb.asah.common.entity.DataSource;
import com.liferay.osb.asah.common.entity.EventDefinition;
import com.liferay.osb.asah.common.messaging.Channel;
import com.liferay.osb.asah.common.messaging.MessageBus;
import com.liferay.osb.asah.common.model.AnalyticsEvent;
import com.liferay.osb.asah.common.model.AnalyticsEventsMessage;
import com.liferay.osb.asah.common.util.AnalyticsEventUtil;
import com.liferay.osb.asah.common.util.MapUtil;
import com.liferay.osb.asah.common.util.ProjectIdThreadLocal;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import javax.validation.Valid;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Eddie Olson
 */
@CrossOrigin
@RequestMapping("/")
@RestController(
	"com.liferay.osb.asah.publisher.rest.controller.AnalyticsEventsRestController"
)
public class AnalyticsEventsRestController {

	@PostMapping
	public ResponseEntity<?> post(
		@RequestBody @Valid AnalyticsEventsMessage analyticsEventsMessage,
		Errors errors, @RequestHeader HttpHeaders httpHeaders,
		HttpServletRequest httpServletRequest) {

		String analyticsEventsMessageId = analyticsEventsMessage.getId();

		if ((analyticsEventsMessageId != null) &&
			BooleanUtils.isTrue(
				_analyticsEventMessageIds.getIfPresent(
					analyticsEventsMessageId))) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Discarding duplicate message: " +
						analyticsEventsMessage.toJSON());
			}

			return new ResponseEntity<>(
				"Duplicate message " + analyticsEventsMessageId, HttpStatus.OK);
		}

		if (analyticsEventsMessageId != null) {
			_analyticsEventMessageIds.put(analyticsEventsMessageId, true);
		}

		if (ServiceConstants.blockedAnonymousEventProjectIds.contains(
				ProjectIdThreadLocal.getProjectId()) &&
			StringUtils.isEmpty(
				analyticsEventsMessage.getEmailAddressHashed())) {

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Discarding anonymous message: " +
						analyticsEventsMessage.toJSON());
			}

			return new ResponseEntity<>(
				"Anonymous message " + analyticsEventsMessageId, HttpStatus.OK);
		}

		String clientIP = httpHeaders.getFirst("X-Forwarded-For");

		if (clientIP == null) {
			clientIP = httpServletRequest.getRemoteAddr();
		}
		else {
			String[] parts = clientIP.split(",");

			clientIP = parts[0];
		}

		analyticsEventsMessage.setClientIP(clientIP);

		List<AnalyticsEventsMessage.Event> events =
			analyticsEventsMessage.getEvents();

		int eventsSize = events.size();

		List<FieldError> fieldErrors = new ArrayList<>(errors.getFieldErrors());

		if (errors.hasErrors()) {
			if (StringUtils.isEmpty(analyticsEventsMessage.getDataSourceId())) {
				if (analyticsEventsMessageId != null) {
					_analyticsEventMessageIds.invalidate(
						analyticsEventsMessageId);
				}

				return new ResponseEntity<>(
					errors.getAllErrors(), HttpStatus.BAD_REQUEST);
			}

			Stream<FieldError> stream = fieldErrors.stream();

			Optional<FieldError> fieldErrorOptional = stream.filter(
				fieldError -> StringUtils.startsWith(
					fieldError.getField(), "context")
			).findAny();

			if (fieldErrorOptional.isPresent()) {
				return new ResponseEntity<>(
					errors.getFieldError(), HttpStatus.BAD_REQUEST);
			}

			for (int index : _getInvalidEventIndices(fieldErrors)) {
				events.remove(index);
			}
		}

		events.removeIf(
			event -> {
				String analyticsEventId =
					AnalyticsEventUtil.generateAnalyticsEventId(
						analyticsEventsMessage.getDataSourceId(), event,
						analyticsEventsMessage.getProjectId(),
						analyticsEventsMessage.getUserId());

				event.setId(analyticsEventId);

				if (BooleanUtils.isTrue(
						_analyticsEventIds.getIfPresent(analyticsEventId))) {

					if (_log.isDebugEnabled()) {
						_log.debug(
							"Discarding duplicate event: " + analyticsEventId);
					}

					fieldErrors.add(
						new FieldError(
							"analyticsEventsMessage", "analyticsEventId",
							analyticsEventId, false, null, null,
							"Duplicate event"));

					return true;
				}

				_analyticsEventIds.put(analyticsEventId, true);

				return false;
			});

		if (!events.isEmpty()) {
			_sendAnalyticsEvents(analyticsEventsMessage);
		}
		else if (analyticsEventsMessageId != null) {
			_analyticsEventMessageIds.invalidate(analyticsEventsMessageId);
		}

		if (eventsSize != events.size()) {
			return new ResponseEntity<>(fieldErrors, HttpStatus.OK);
		}

		return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
	}

	private Set<Integer> _getInvalidEventIndices(List<FieldError> fieldErrors) {
		Set<Integer> indices = new TreeSet<>(Collections.reverseOrder());

		for (FieldError fieldError : fieldErrors) {
			Matcher matcher = _pattern.matcher(fieldError.getField());

			if (matcher.matches()) {
				indices.add(Integer.parseInt(matcher.group(1)));
			}
		}

		return indices;
	}

	private boolean _isDataSourceActive(
		AnalyticsEventsMessage analyticsEventsMessage) {

		DataSource dataSource = _dataSourceDog.fetchDataSource(
			Long.valueOf(analyticsEventsMessage.getDataSourceId()));

		if ((dataSource != null) &&
			Objects.equals(dataSource.getStatus(), "ACTIVE")) {

			return true;
		}

		return false;
	}

	private void _sendAnalyticsEvents(
		AnalyticsEventsMessage analyticsEventsMessage) {

		if (!_isDataSourceActive(analyticsEventsMessage)) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Discarding message because data source is not active: " +
						analyticsEventsMessage.toJSON());
			}

			return;
		}

		String channelId = analyticsEventsMessage.getChannelId();
		String dataSourceId = analyticsEventsMessage.getDataSourceId();

		if (StringUtil.isNull(channelId) || StringUtils.isBlank(channelId) ||
			!NumberUtils.isCreatable(channelId)) {

			channelId = String.valueOf(
				_dataSourceDog.fetchDefaultChannelId(
					Long.valueOf(dataSourceId)));

			if (StringUtil.isNull(channelId) ||
				StringUtils.isBlank(channelId)) {

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Discarding message because channel ID is invalid: " +
							analyticsEventsMessage.toJSON());
				}

				return;
			}
		}

		String projectTimeZoneId = _timeZoneDog.getTimeZoneId();

		for (AnalyticsEventsMessage.Event event :
				analyticsEventsMessage.getEvents()) {

			EventDefinition eventDefinition =
				_eventDefinitionDog.fetchEventDefinitionByName(
					event.getEventId());

			if ((eventDefinition == null) || !eventDefinition.isBlocked()) {
				AnalyticsEvent analyticsEvent = new AnalyticsEvent();

				analyticsEvent.setApplicationId(event.getApplicationId());
				analyticsEvent.setChannelId(channelId);
				analyticsEvent.setClientIP(
					analyticsEventsMessage.getClientIP());
				analyticsEvent.setContext(analyticsEventsMessage.getContext());
				analyticsEvent.setCreateDate(
					analyticsEventsMessage.getCreateDate());
				analyticsEvent.setDataSourceId(dataSourceId);
				analyticsEvent.setEmailAddressHashed(
					analyticsEventsMessage.getEmailAddressHashed());
				analyticsEvent.setEventDate(event.getEventDate());
				analyticsEvent.setEventId(event.getEventId());
				analyticsEvent.setEventProperties(event.getProperties());
				analyticsEvent.setId(
					AnalyticsEventUtil.generateAnalyticsEventId(
						dataSourceId, event,
						analyticsEventsMessage.getProjectId(),
						analyticsEventsMessage.getUserId()));
				analyticsEvent.setProjectId(
					ProjectIdThreadLocal.getProjectId());
				analyticsEvent.setProjectTimeZoneId(projectTimeZoneId);
				analyticsEvent.setUserId(analyticsEventsMessage.getUserId());

				if (_log.isDebugEnabled()) {
					_log.debug(
						"Pushing analytics event message to the queue: " +
							analyticsEvent.toJSON());
				}

				Map<String, String> messageAttributes = new HashMap<>();

				messageAttributes.put(
					"eventDate",
					DateUtil.toUTCString(analyticsEvent.getEventDate()));
				messageAttributes.put("id", analyticsEvent.getId());

				_messageBus.sendMessage(
					Channel.ANALYTICS_EVENTS, analyticsEvent.toJSON(),
					messageAttributes);

				if (Objects.equals(
						analyticsEvent.getApplicationId(), "Custom") &&
					StringUtils.isNotBlank(
						MapUtil.getString(
							analyticsEvent.getEventProperties(), "assetId"))) {

					_messageBus.sendMessage(
						Channel.ANALYTICS_EVENTS_CUSTOM_ASSET,
						analyticsEvent.toJSON(), messageAttributes);
				}

				if (eventDefinition == null) {
					_boundedExecutor.runAsync(
						() -> {
							try {
								ProjectIdThreadLocal.setProjectId(
									analyticsEvent.getProjectId());

								_eventStorageDog.storeEventDefinition(
									analyticsEvent);
							}
							catch (Exception exception) {
								_log.error(
									"Unable to store event definition " +
										analyticsEvent.toJSON(),
									exception);
							}
						});
				}
			}
		}
	}

	private static final Log _log = LogFactory.getLog(
		AnalyticsEventsRestController.class);

	private static final Cache<String, Boolean> _analyticsEventIds =
		Caffeine.newBuilder(
		).expireAfterAccess(
			7, TimeUnit.DAYS
		).maximumSize(
			5000000
		).build();
	private static final Cache<String, Boolean> _analyticsEventMessageIds =
		Caffeine.newBuilder(
		).expireAfterAccess(
			3, TimeUnit.DAYS
		).maximumSize(
			5000000
		).build();
	private static final Pattern _pattern = Pattern.compile(
		"^events\\[(\\d+)].*");

	private final BoundedExecutor _boundedExecutor =
		BoundedExecutor.newBoundedExecutor(5, 1);

	@Autowired
	private DataSourceDog _dataSourceDog;

	@Autowired
	private EventDefinitionDog _eventDefinitionDog;

	@Autowired
	private EventStorageDog _eventStorageDog;

	@Autowired
	private MessageBus _messageBus;

	@Autowired
	private TimeZoneDog _timeZoneDog;

}