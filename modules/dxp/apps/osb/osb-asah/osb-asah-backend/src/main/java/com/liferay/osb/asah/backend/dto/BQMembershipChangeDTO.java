/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Rachael Koestartyo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("membership-changes")
public class BQMembershipChangeDTO {

	public BQMembershipChangeDTO() {
	}

	public BQMembershipChangeDTO(BQMembershipChange bqMembershipChange) {
		_identitiesCount = bqMembershipChange.getIdentitiesCount();
		_knownIdentitiesCount = bqMembershipChange.getIndividualsCount();
		_segmentId = StringUtil.get(bqMembershipChange.getSegmentId(), null);
	}

	public BQMembershipChangeDTO(List<BQMembershipChange> bqMembershipChanges) {
		_bqMembershipChangeDTOs = SetUtil.map(
			bqMembershipChanges, BQMembershipChangeDTO::new);
	}

	public BQMembershipChangeDTO(
		Set<BQMembershipChangeDTO> bqMembershipChangeDTOs) {

		_bqMembershipChangeDTOs = bqMembershipChangeDTOs;
	}

	@JsonProperty("membership-changes")
	public Set<BQMembershipChangeDTO> getBQMembershipChangeDTOs() {
		return _bqMembershipChangeDTOs;
	}

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbedded() {
		return _embedded;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonAlias("identitiesCount")
	@JsonProperty("individualsCount")
	public Long getIdentitiesCount() {
		return _identitiesCount;
	}

	@JsonAlias("knownIdentitiesCount")
	@JsonProperty("knownIndividualsCount")
	public Long getKnownIdentitiesCount() {
		return _knownIdentitiesCount;
	}

	@JsonProperty("individualSegmentId")
	public String getSegmentId() {
		return _segmentId;
	}

	public void setBQMembershipChangeDTOs(
		Set<BQMembershipChangeDTO> bqMembershipChangeDTOs) {

		_bqMembershipChangeDTOs = bqMembershipChangeDTOs;
	}

	public void setEmbedded(Map<String, Object> embedded) {
		_embedded = embedded;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIdentitiesCount(Long individualsCount) {
		_identitiesCount = individualsCount;
	}

	public void setKnownIdentitiesCount(Long knownIndividualsCount) {
		_knownIdentitiesCount = knownIndividualsCount;
	}

	public void setSegmentId(String segmentId) {
		_segmentId = segmentId;
	}

	private Set<BQMembershipChangeDTO> _bqMembershipChangeDTOs;
	private Map<String, Object> _embedded;
	private String _id;
	private Long _identitiesCount;
	private Long _knownIdentitiesCount;
	private String _segmentId;

}