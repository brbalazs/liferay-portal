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

package com.liferay.osb.faro.engine.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Matthew Kong
 */
public class ActivityGroup {

	public ActivityGroup() {
	}

	public String getActivityType() {
		return _activityType;
	}

	public Date getDay() {
		return _day;
	}

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbeddedResources() {
		return _embeddedResources;
	}

	public Date getEndTime() {
		return _endTime;
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public String getOwnerId() {
		return _ownerId;
	}

	public String getOwnerType() {
		return _ownerType;
	}

	public Date getStartTime() {
		return _startTime;
	}

	public void setActivityType(String activityType) {
		_activityType = activityType;
	}

	public void setDay(Date day) {
		_day = day;
	}

	public void setEmbeddedResources(Map<String, Object> embeddedResources) {
		_embeddedResources = embeddedResources;
	}

	public void setEndTime(Date endTime) {
		_endTime = endTime;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setOwnerId(String ownerId) {
		_ownerId = ownerId;
	}

	public void setOwnerType(String ownerType) {
		_ownerType = ownerType;
	}

	public void setStartTime(Date startTime) {
		_startTime = startTime;
	}

	private String _activityType;
	private Date _day;
	private Map<String, Object> _embeddedResources = new HashMap<>();
	private Date _endTime;
	private String _id;
	private String _name;
	private String _ownerId;
	private String _ownerType;
	private Date _startTime;

}