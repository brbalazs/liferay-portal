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

import com.liferay.osb.asah.common.validation.PastDate;
import java.io.Serializable;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Inácio Nery
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
	defaultImpl = AnalyticsEventsMessage.class,
	include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "messageFormat",
	use = JsonTypeInfo.Id.NAME, visible = true
)
public class AnalyticsEventsMessage implements Serializable {

	public static AnalyticsEventsMessage toAnalyticsEventsMessage(String json)
		throws Exception {

		return _objectMapper.readValue(json, AnalyticsEventsMessage.class);
	}

	public String getChannelId() {
		return _channelId;
	}

	public String getClientIP() {
		return _clientIP;
	}

	@NotEmpty
	public Map<String, @Size(max = 2048) String> getContext() {
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

	@NotBlank
	public String getDataSourceId() {
		return _dataSourceId;
	}

	@NotEmpty
	@Valid
	public List<Event> getEvents() {
		return _events;
	}

	public String getId() {
		return _id;
	}

	public String getProjectId() {
		return _projectId;
	}

	public String getProtocolVersion() {
		return _protocolVersion;
	}

	@NotBlank
	public String getUserId() {
		return _userId;
	}

	public String getEmailAddressHashed() {
		return _emailAddressHashed;
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

	public void setEvents(List<Event> events) {
		_events = events;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setProtocolVersion(String protocolVersion) {
		_protocolVersion = protocolVersion;
	}

	public void setUserId(String userId) {
		_userId = userId;
	}

	public void setEmailAddressHashed(String emailAddressHashed) {
		_emailAddressHashed = emailAddressHashed;
	}

	public String toJSON() {
		try {
			return _objectMapper.writeValueAsString(this);
		}
		catch (JsonProcessingException jsonProcessingException) {
			throw new RuntimeException(jsonProcessingException);
		}
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Event implements Serializable {

		@NotBlank
		public String getApplicationId() {
			return _applicationId;
		}

		public String getId() {
			return _id;
		}
		
		@JsonFormat(
			pattern = DateUtil.PATTERN_ISO_8601,
			shape = JsonFormat.Shape.STRING, timezone = "UTC"
		)
		@NotNull
		@PastDate(
			allowedLateness = 3600000,
			message = "Date must be within the last hour"
		)
		public Date getEventDate() {
			if (_eventDate == null) {
				return null;
			}

			return new Date(_eventDate.getTime());
		}

		@NotBlank
		@Size(max = 255)
		public String getEventId() {
			return _eventId;
		}

		@Size(max = 25)
		public Map<@NotBlank @Size(max = 255) String, @Size(max = 1024) String>
			getProperties() {

			return Collections.unmodifiableMap(_properties);
		}

		public void setApplicationId(String applicationId) {
			_applicationId = applicationId;
		}

		public void setEventDate(Date eventDate) {
			if (eventDate != null) {
				_eventDate = new Date(eventDate.getTime());
			}
		}

		public void setEventId(String eventId) {
			_eventId = eventId;
		}
		public void setId(String id) {
			_id = id;
		}

		public void setProperties(Map<String, String> properties) {
			_properties = properties;
		}

		private String _id;
		private String _applicationId;
		private Date _eventDate;
		private String _eventId;
		private Map<String, String> _properties = Collections.emptyMap();

	}

	private static final ObjectMapper _objectMapper = new ObjectMapper() {
		{
			disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

			TypeFactory typeFactory = TypeFactory.defaultInstance();

			typeFactory = typeFactory.withClassLoader(
				AnalyticsEventsMessage.class.getClassLoader());

			setTypeFactory(typeFactory);
		}
	};

	private String _channelId;
	private String _clientIP;
	private Map<String, String> _context = Collections.emptyMap();
	private Date _createDate = new Date();
	private String _dataSourceId;
	private List<Event> _events = Collections.emptyList();
	private String _id;
	private String _projectId;
	private String _protocolVersion;
	private String _userId;
	private String _emailAddressHashed;

}