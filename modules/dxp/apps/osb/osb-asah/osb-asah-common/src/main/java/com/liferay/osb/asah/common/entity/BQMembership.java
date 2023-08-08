/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author Inácio Nery
 */
public class BQMembership {

	public BQMembership() {
	}

	public BQMembership(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BQMembership)) {
			return false;
		}

		BQMembership bqMembership = (BQMembership)obj;

		if (Objects.equals(_channelId, bqMembership._channelId) &&
			Objects.equals(_createDate, bqMembership._createDate) &&
			Objects.equals(_id, bqMembership._id) &&
			Objects.equals(_identityId, bqMembership._identityId) &&
			Objects.equals(_individualId, bqMembership._individualId) &&
			Objects.equals(_modifiedDate, bqMembership._modifiedDate) &&
			Objects.equals(_removedDate, bqMembership._removedDate) &&
			Objects.equals(_segmentId, bqMembership._segmentId) &&
			Objects.equals(_status, bqMembership._status)) {

			return true;
		}

		return false;
	}

	public Long getChannelId() {
		return _channelId;
	}

	@JsonAlias("createDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateCreated")
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@JsonSerialize(using = ToStringSerializer.class)
	public Long getId() {
		return _id;
	}

	public String getIdentityId() {
		return _identityId;
	}

	public String getIndividualId() {
		return _individualId;
	}

	@JsonAlias("modifiedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateModified")
	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	@JsonAlias("removedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateRemoved")
	public Date getRemovedDate() {
		if (_removedDate == null) {
			return null;
		}

		return new Date(_removedDate.getTime());
	}

	@JsonAlias("segmentId")
	@JsonProperty("individualSegmentId")
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getSegmentId() {
		return _segmentId;
	}

	public String getStatus() {
		return _status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _createDate, _id, _identityId, _individualId,
			_modifiedDate, _removedDate, _segmentId, _status);
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIdentityId(String identityId) {
		_identityId = identityId;
	}

	public void setIndividualId(String individualId) {
		_individualId = individualId;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setRemovedDate(Date removedDate) {
		if (removedDate != null) {
			_removedDate = new Date(removedDate.getTime());
		}
	}

	public void setSegmentId(Long segmentId) {
		_segmentId = segmentId;
	}

	public void setStatus(String status) {
		_status = status;
	}

	private Long _channelId;
	private Date _createDate;
	private Long _id;
	private String _identityId;
	private String _individualId;
	private Date _modifiedDate;
	private Date _removedDate;
	private Long _segmentId;
	private String _status;

}