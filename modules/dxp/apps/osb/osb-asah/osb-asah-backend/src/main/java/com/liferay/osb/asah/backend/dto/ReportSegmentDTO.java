/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;

/**
 * @author Marcellus Tavares
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("segments")
public class ReportSegmentDTO {

	public ReportSegmentDTO(
		BQMembershipChange bqMembershipChange, Segment segment) {

		_createDate = segment.getCreateDate();
		_id = StringUtil.get(segment.getId());

		if (bqMembershipChange != null) {
			_identitiesCount = bqMembershipChange.getIdentitiesCount();
			_knownIdentitiesCount = bqMembershipChange.getIndividualsCount();
		}
		else {
			_identitiesCount = 0L;
			_knownIdentitiesCount = 0L;
		}

		_includeAnonymousUsers = segment.getIncludeAnonymousUsers();
		_name = segment.getName();
		_segmentType = String.valueOf(segment.getType());
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateCreated")
	public Date getDateCreated() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public String getId() {
		return _id;
	}

	@JsonAlias("identitiesCount")
	@JsonProperty("individualCount")
	public Long getIdentitiesCount() {
		return _identitiesCount;
	}

	@JsonAlias("knownIdentitiesCount")
	@JsonProperty("knownIndividualCount")
	public Long getKnownIdentitiesCount() {
		return _knownIdentitiesCount;
	}

	public String getName() {
		return _name;
	}

	public String getSegmentType() {
		return _segmentType;
	}

	public Boolean isIncludeAnonymousUsers() {
		return _includeAnonymousUsers;
	}

	private final Date _createDate;
	private final String _id;
	private final Long _identitiesCount;
	private final Boolean _includeAnonymousUsers;
	private final Long _knownIdentitiesCount;
	private final String _name;
	private final String _segmentType;

}