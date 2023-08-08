/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;

import com.liferay.osb.asah.common.date.DateUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Inácio Nery
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
	defaultImpl = AnalyticsEvent.class,
	include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "messageFormat",
	use = JsonTypeInfo.Id.NAME, visible = true
)
public class AnalyticsEvent implements Serializable {

	public static AnalyticsEvent toAnalyticsEvent(String json)
		throws Exception {

		return _objectMapper.readValue(json, AnalyticsEvent.class);
	}

	public static List<AnalyticsEvent> toAnalyticsEvents(String json)
		throws Exception {

		AnalyticsEvent[] analyticsEvents = _objectMapper.readValue(
			json, AnalyticsEvent[].class);

		return new ArrayList<>(Arrays.asList(analyticsEvents));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof AnalyticsEvent)) {
			return false;
		}

		AnalyticsEvent analyticsEvent = (AnalyticsEvent)obj;

		if (Objects.equals(_applicationId, analyticsEvent._applicationId) &&
			Objects.equals(_channelId, analyticsEvent._channelId) &&
			Objects.equals(_clientIP, analyticsEvent._clientIP) &&
			Objects.equals(_context, analyticsEvent._context) &&
			Objects.equals(_createDate, analyticsEvent._createDate) &&
			Objects.equals(_dataSourceId, analyticsEvent._dataSourceId) &&
			Objects.equals(
				_emailAddressHashed, analyticsEvent._emailAddressHashed) &&
			Objects.equals(_eventDate, analyticsEvent._eventDate) &&
			Objects.equals(_eventId, analyticsEvent._eventId) &&
			Objects.equals(_eventProperties, analyticsEvent._eventProperties) &&
			Objects.equals(_id, analyticsEvent._id) &&
			Objects.equals(_projectId, analyticsEvent._projectId) &&
			Objects.equals(
				_projectTimeZoneId, analyticsEvent._projectTimeZoneId) &&
			Objects.equals(_userId, analyticsEvent._userId)) {

			return true;
		}

		return false;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public String getChannelId() {
		return _channelId;
	}

	public String getClientIP() {
		return _clientIP;
	}

	public Map<String, String> getContext() {
		return _context;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getDataSourceId() {
		return _dataSourceId;
	}

	public String getEmailAddressHashed() {
		return _emailAddressHashed;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getEventDate() {
		if (_eventDate == null) {
			return null;
		}

		return new Date(_eventDate.getTime());
	}

	public String getEventId() {
		return _eventId;
	}

	public Map<String, String> getEventProperties() {
		return _eventProperties;
	}

	public String getId() {
		return _id;
	}

	public String getIndividualId() {
		return _individualId;
	}

	public Date getNormalizedEventDate() {
		Calendar eventCalendar = Calendar.getInstance();

		eventCalendar.setTime(_eventDate);

		eventCalendar.set(Calendar.MILLISECOND, 0);
		eventCalendar.set(Calendar.MINUTE, 0);
		eventCalendar.set(Calendar.SECOND, 0);

		return eventCalendar.getTime();
	}

	public String getProjectId() {
		return _projectId;
	}

	public String getProjectTimeZoneId() {
		return _projectTimeZoneId;
	}

	public Set<String> getSegmentNames() {
		return _segmentNames;
	}

	public String getUserId() {
		return _userId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_applicationId, _channelId, _clientIP, _id, _context, _createDate,
			_dataSourceId, _emailAddressHashed, _eventDate, _eventId,
			_eventProperties, _projectId, _projectTimeZoneId, _userId);
	}

	public boolean isKnownIndividual() {
		return _knownIndividual;
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setClientIP(String clientIP) {
		_clientIP = clientIP;
	}

	public void setContext(Map<String, String> context) {
		_context = context;
	}

	public void setCreateDate(Date createDate) {
		if (createDate == null) {
			throw new IllegalArgumentException("Create date is null");
		}

		_createDate = new Date(createDate.getTime());
	}

	public void setDataSourceId(String dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setEmailAddressHashed(String emailAddressHashed) {
		_emailAddressHashed = emailAddressHashed;
	}

	public void setEventDate(Date eventDate) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}
	}

	public void setEventId(String eventId) {
		_eventId = eventId;
	}

	public void setEventProperties(Map<String, String> eventProperties) {
		_eventProperties = eventProperties;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	public void setKnownIndividual(boolean knownIndividual) {
		_knownIndividual = knownIndividual;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setProjectTimeZoneId(String projectTimezoneId) {
		_projectTimeZoneId = projectTimezoneId;
	}

	public void setSegmentNames(Set<String> segmentNames) {
		_segmentNames = segmentNames;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	public String toJSON() {
		try {
			return _objectMapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException);
		}
	}

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

			TypeFactory typeFactory = TypeFactory.defaultInstance();

			typeFactory = typeFactory.withClassLoader(
				AnalyticsEvent.class.getClassLoader());

			setTypeFactory(typeFactory);
		}
	};

	private String _applicationId;
	private String _channelId;
	private String _clientIP;
	private Map<String, String> _context;
	private Date _createDate = new Date();
	private String _dataSourceId;
	private String _emailAddressHashed;
	private Date _eventDate = new Date();
	private String _eventId;
	private Map<String, String> _eventProperties = new HashMap<>();
	private String _id;
	private String _individualId;
	private boolean _knownIndividual;
	private String _projectId;
	private String _projectTimeZoneId;
	private Set<String> _segmentNames = new HashSet<>();
	private String _userId;

}