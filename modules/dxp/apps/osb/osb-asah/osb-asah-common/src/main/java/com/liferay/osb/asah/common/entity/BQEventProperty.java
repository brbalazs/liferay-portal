/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.liferay.osb.asah.common.spring.annotation.BigQueryColumn;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Leslie Wong
 */
public class BQEventProperty {

	public BQEventProperty() {
	}

	public BQEventProperty(Date eventDate, String name, String value) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}

		_name = name;
		_value = value;
	}

	public BQEventProperty(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQEventProperty)) {
			return false;
		}

		BQEventProperty bqEventProperty = (BQEventProperty)obj;

		if (Objects.equals(_channelId, bqEventProperty._channelId) &&
			Objects.equals(_eventDate, bqEventProperty._eventDate) &&
			Objects.equals(_id, bqEventProperty._id) &&
			Objects.equals(_name, bqEventProperty._name) &&
			Objects.equals(_projectId, bqEventProperty._projectId) &&
			Objects.equals(_value, bqEventProperty._value)) {

			return true;
		}

		return false;
	}

	@BigQueryColumn
	public Long getChannelId() {
		return _channelId;
	}

	@BigQueryColumn
	public Date getEventDate() {
		if (_eventDate == null) {
			return null;
		}

		return new Date(_eventDate.getTime());
	}

	@BigQueryColumn
	public String getId() {
		return _id;
	}

	@BigQueryColumn
	public String getName() {
		return _name;
	}

	@BigQueryColumn
	public String getProjectId() {
		return _projectId;
	}

	@BigQueryColumn
	public String getValue() {
		return _value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _eventDate, _id, _name, _projectId, _value);
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setEventDate(Date eventDate) {
		if (eventDate != null) {
			_eventDate = new Date(eventDate.getTime());
		}
		else {
			_eventDate = null;
		}
	}

	public void setId(String id) {
		_id = id;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setProjectId(String projectId) {
		_projectId = projectId;
	}

	public void setValue(String value) {
		_value = value;
	}

	private Long _channelId;
	private Date _eventDate;
	private String _id;
	private String _name;
	private String _projectId;
	private String _value;

}