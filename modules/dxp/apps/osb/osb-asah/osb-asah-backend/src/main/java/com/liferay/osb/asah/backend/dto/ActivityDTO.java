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

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQEvent;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Marcellus Tavares
 */
public class ActivityDTO {

	public ActivityDTO(
		BQEvent bqEvent, String bqIndividualId,
		Map<String, String> eventContext, Map<String, String> eventProperties) {

		_eventContext = eventContext;
		_eventProperties = eventProperties;

		_applicationId = bqEvent.getApplicationId();
		_eventId = bqEvent.getId();
		_ownerId = bqIndividualId;
		_startTime = bqEvent.getEventDate();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ActivityDTO)) {
			return false;
		}

		ActivityDTO activityDTO = (ActivityDTO)obj;

		if (Objects.equals(_applicationId, activityDTO._applicationId) &&
			Objects.equals(_eventId, activityDTO._eventId) &&
			Objects.equals(_eventContext, activityDTO._eventContext) &&
			Objects.equals(_eventProperties, activityDTO._eventProperties) &&
			Objects.equals(_ownerId, activityDTO._ownerId) &&
			Objects.equals(_startTime, activityDTO._startTime)) {

			return true;
		}

		return false;
	}

	public String getApplicationId() {
		return _applicationId;
	}

	public Map<String, String> getEventContext() {
		return _eventContext;
	}

	public String getEventId() {
		return _eventId;
	}

	public Map<String, String> getEventProperties() {
		return _eventProperties;
	}

	public String getOwnerId() {
		return _ownerId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getStartTime() {
		if (_startTime == null) {
			return null;
		}

		return new Date(_startTime.getTime());
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_applicationId, _eventId, _eventContext, _eventProperties, _ownerId,
			_startTime);
	}

	public void setApplicationId(String applicationId) {
		_applicationId = applicationId;
	}

	public void setEventContext(Map<String, String> eventContext) {
		_eventContext = eventContext;
	}

	public void setEventId(String eventId) {
		_eventId = eventId;
	}

	public void setEventProperties(Map<String, String> eventProperties) {
		_eventProperties = eventProperties;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setStartTime(Date startTime) {
		if (startTime != null) {
			_startTime = new Date(startTime.getTime());
		}
	}

	private String _applicationId;
	private Map<String, String> _eventContext;
	private String _eventId;
	private Map<String, String> _eventProperties;
	private String _ownerId;
	private Date _startTime;

}