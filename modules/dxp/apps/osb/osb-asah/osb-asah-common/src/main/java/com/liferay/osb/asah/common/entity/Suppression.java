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

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Matthew Kong
 */
public class Suppression {

	public Suppression() {
	}

	public Suppression(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if ((obj == null) || !(obj instanceof Suppression)) {
			return false;
		}

		Suppression suppression = (Suppression)obj;

		if (Objects.equals(_createDate, suppression._createDate) &&
			Objects.equals(
				_dataControlTaskBatchId, suppression._dataControlTaskBatchId) &&
			Objects.equals(
				_dataControlTaskCreateDate,
				suppression._dataControlTaskCreateDate) &&
			Objects.equals(_emailAddress, suppression._emailAddress)) {

			return true;
		}

		return false;
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

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataControlTaskBatchId() {
		return _dataControlTaskBatchId;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getDataControlTaskCreateDate() {
		if (_dataControlTaskCreateDate == null) {
			return null;
		}

		return new Date(_dataControlTaskCreateDate.getTime());
	}

	public String getEmailAddress() {
		return _emailAddress;
	}

	@Override
	public int hashCode() {
		return Objects.hash(_createDate, _emailAddress);
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataControlTaskBatchId(Long dataControlTaskBatchId) {
		_dataControlTaskBatchId = dataControlTaskBatchId;
	}

	public void setDataControlTaskCreateDate(Date dataControlTaskCreateDate) {
		if (dataControlTaskCreateDate != null) {
			_dataControlTaskCreateDate = new Date(
				dataControlTaskCreateDate.getTime());
		}
	}

	public void setEmailAddress(String emailAddress) {
		_emailAddress = emailAddress;
	}

	private Date _createDate;
	private Long _dataControlTaskBatchId;
	private Date _dataControlTaskCreateDate;
	private String _emailAddress;

}