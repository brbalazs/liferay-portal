/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.IdentityInterestScore;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author Robson Pastor
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("interests")
public class InterestDTO {

	public InterestDTO() {
	}

	public InterestDTO(IdentityInterestScore identityInterestScore) {
		_contributingPagesCount =
			identityInterestScore.getContributingPagesCount();
		_name = identityInterestScore.getKeyword();
		_ownerId = identityInterestScore.getIndividualId();
		_ownerType = "individual";
		_recordedDate = identityInterestScore.getRecordedDate();
		_score = identityInterestScore.getInterestScore();
		_views = null;
	}

	public InterestDTO(Set<InterestDTO> interestDTOS) {
		_interestDTOs = interestDTOS;
	}

	@JsonProperty("relatedPagesCount")
	public Long getContributingPagesCount() {
		return _contributingPagesCount;
	}

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbedded() {
		return _embedded;
	}

	@JsonProperty("interests")
	public Set<InterestDTO> getInterestDTOs() {
		return _interestDTOs;
	}

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("ownerId")
	public String getOwnerId() {
		return _ownerId;
	}

	@JsonProperty("ownerType")
	public String getOwnerType() {
		return _ownerType;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("dateRecorded")
	public Date getRecordedDate() {
		if (_recordedDate == null) {
			return null;
		}

		return new Date(_recordedDate.getTime());
	}

	@JsonProperty("score")
	public Double getScore() {
		return _score;
	}

	@JsonProperty("views")
	public Long getViews() {
		return _views;
	}

	public void setEmbedded(Map<String, Object> embedded) {
		_embedded = embedded;
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

	public void setRecordedDate(Date recordedDate) {
		if (recordedDate != null) {
			_recordedDate = new Date(recordedDate.getTime());
		}
		else {
			_recordedDate = null;
		}
	}

	public void setScore(Double score) {
		_score = score;
	}

	public void setViews(Long views) {
		_views = views;
	}

	private Long _contributingPagesCount;
	private Map<String, Object> _embedded;
	private Set<InterestDTO> _interestDTOs;
	private String _name;
	private String _ownerId;
	private String _ownerType;
	private Date _recordedDate;
	private Double _score;
	private Long _views;

}