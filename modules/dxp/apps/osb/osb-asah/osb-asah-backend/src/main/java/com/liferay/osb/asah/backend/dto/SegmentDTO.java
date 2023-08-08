/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQMembershipChange;
import com.liferay.osb.asah.common.entity.Segment;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author Inácio Nery
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("segments")
public class SegmentDTO {

	public SegmentDTO() {
	}

	public SegmentDTO(
		Long activeIdentitiesCount, BQMembershipChange bqMembershipChange,
		Date lastActivityDate, Segment segment) {

		AuthorDTO authorDTO = new AuthorDTO(segment);

		if (!authorDTO.isEmpty()) {
			_authorDTO = authorDTO;
		}

		_activeIdentitiesCount = activeIdentitiesCount;

		_activitiesCount = 0L;

		if (bqMembershipChange != null) {
			_identitiesCount = bqMembershipChange.getIdentitiesCount();
			_individualsCount = bqMembershipChange.getIndividualsCount();

			_anonymousIdentitiesCount = _identitiesCount - _individualsCount;
		}
		else {
			_anonymousIdentitiesCount = 0L;
			_identitiesCount = 0L;
			_individualsCount = 0L;
		}

		_channelId = StringUtil.get(segment.getChannelId(), null);
		_createDate = segment.getCreateDate();
		_filterString = segment.getFilter();
		_filterMetadata = segment.getFilterMetadata();
		_id = StringUtil.get(segment.getId(), null);
		_includeAnonymousUsers = segment.getIncludeAnonymousUsers();

		if (lastActivityDate != null) {
			_lastActivityDate = new Date(lastActivityDate.getTime());
		}

		_modifiedDate = segment.getModifiedDate();
		_name = segment.getName();
		_referencedDataSourceIds = SetUtil.map(
			segment.getReferencedDataSourceIds(), String::valueOf);
		_referencedFieldMappingFieldNames = SetUtil.map(
			segment.getReferencedFieldMappingFieldNames(), String::valueOf);
		_scope = segment.getScope();
		_state = segment.getState();
		_status = segment.getStatus();
		_type = StringUtil.get(segment.getType(), null);
	}

	public SegmentDTO(
		Map<Long, BQMembershipChange> bqMembershipChanges,
		List<Segment> segments) {

		_segmentDTOs = SetUtil.map(
			segments,
			segment -> {
				Long segmentId = segment.getId();

				return new SegmentDTO(
					null, bqMembershipChanges.getOrDefault(segmentId, null),
					null, segment);
			});
	}

	public SegmentDTO(Set<SegmentDTO> segmentDTOs) {
		_segmentDTOs = segmentDTOs;
	}

	@JsonAlias("activeIdentitiesCount")
	@JsonProperty("activeIndividualCount")
	public Long getActiveIdentitiesCount() {
		return _activeIdentitiesCount;
	}

	@JsonProperty("activitiesCount")
	public Long getActivitiesCount() {
		return _activitiesCount;
	}

	@JsonAlias("anonymousIdentitiesCount")
	@JsonProperty("anonymousIndividualCount")
	public Long getAnonymousIdentitiesCount() {
		return _anonymousIdentitiesCount;
	}

	@JsonProperty("author")
	public AuthorDTO getAuthorDTO() {
		return _authorDTO;
	}

	@JsonProperty("channelId")
	public String getChannelId() {
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

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbedded() {
		return _embedded;
	}

	@JsonProperty("filter")
	public String getFilter() {
		return _filterString;
	}

	@JsonProperty("filterMetadata")
	public String getFilterMetadata() {
		return _filterMetadata;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonAlias("identitiesCount")
	@JsonProperty("individualCount")
	public Long getIdentitiesCount() {
		return _identitiesCount;
	}

	@JsonProperty("includeAnonymousUsers")
	public Boolean getIncludeAnonymousUsers() {
		return _includeAnonymousUsers;
	}

	@JsonAlias("knownIdentitiesCount")
	@JsonProperty("knownIndividualCount")
	public Long getKnownIdentitiesCount() {
		return _individualsCount;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("lastActivityDate")
	public Date getLastActivityDate() {
		if (_lastActivityDate == null) {
			return null;
		}

		return new Date(_lastActivityDate.getTime());
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

	@JsonProperty("name")
	public String getName() {
		return _name;
	}

	@JsonProperty("referencedDataSourceIds")
	public Set<String> getReferencedDataSourceIds() {
		return _referencedDataSourceIds;
	}

	@JsonProperty("referencedFieldMappingFieldNames")
	public Set<String> getReferencedFieldMappingFieldNames() {
		return _referencedFieldMappingFieldNames;
	}

	@JsonProperty("scope")
	public String getScope() {
		return _scope;
	}

	@JsonProperty("individual-segments")
	public Set<SegmentDTO> getSegmentDTOs() {
		return _segmentDTOs;
	}

	@JsonProperty("state")
	public String getState() {
		return _state;
	}

	@JsonProperty("status")
	public String getStatus() {
		return _status;
	}

	@JsonAlias("type")
	@JsonProperty("segmentType")
	public String getType() {
		return _type;
	}

	public void setActiveIdentitiesCount(Long activeIdentitiesCount) {
		_activeIdentitiesCount = activeIdentitiesCount;
	}

	public void setActivitiesCount(Long activitiesCount) {
		_activitiesCount = activitiesCount;
	}

	public void setAnonymousIdentitiesCount(Long anonymousIdentitiesCount) {
		_anonymousIdentitiesCount = anonymousIdentitiesCount;
	}

	public void setAuthorDTO(AuthorDTO authorDTO) {
		_authorDTO = authorDTO;
	}

	public void setChannelId(String channelId) {
		_channelId = channelId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setEmbedded(Map<String, Object> embedded) {
		_embedded = embedded;
	}

	public void setFilter(String filterString) {
		_filterString = filterString;
	}

	public void setFilterMetadata(String filterMetadata) {
		_filterMetadata = filterMetadata;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setIdentitiesCount(Long identitiesCount) {
		_identitiesCount = identitiesCount;
	}

	public void setIncludeAnonymousUsers(Boolean includeAnonymousUsers) {
		_includeAnonymousUsers = includeAnonymousUsers;
	}

	public void setKnownIdentitiesCount(Long knownIdentitiesCount) {
		_individualsCount = knownIdentitiesCount;
	}

	public void setLastActivityDate(Date lastActivityDate) {
		if (lastActivityDate != null) {
			_lastActivityDate = new Date(lastActivityDate.getTime());
		}
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setReferencedDataSourceIds(
		Set<String> referencedDataSourceIds) {

		_referencedDataSourceIds = referencedDataSourceIds;
	}

	public void setReferencedFieldMappingFieldNames(
		Set<String> referencedFieldMappingFieldNames) {

		_referencedFieldMappingFieldNames = referencedFieldMappingFieldNames;
	}

	public void setScope(String scope) {
		_scope = scope;
	}

	public void setState(String state) {
		_state = state;
	}

	public void setStatus(String status) {
		_status = status;
	}

	public void setType(String type) {
		_type = type;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class AuthorDTO {

		public AuthorDTO() {
		}

		public AuthorDTO(Segment segment) {
			_id = StringUtil.get(segment.getAuthorId(), null);
			_name = segment.getAuthorName();
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof AuthorDTO)) {
				return false;
			}

			AuthorDTO authorDTO = (AuthorDTO)obj;

			if (Objects.equals(_id, authorDTO._id) &&
				Objects.equals(_name, authorDTO._name)) {

				return true;
			}

			return false;
		}

		@JsonProperty("id")
		public String getId() {
			return _id;
		}

		@JsonProperty("name")
		public String getName() {
			return _name;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_id, _name);
		}

		@JsonIgnore
		public boolean isEmpty() {
			return equals(new AuthorDTO());
		}

		public void setId(String id) {
			_id = id;
		}

		public void setName(String name) {
			_name = name;
		}

		private String _id;
		private String _name;

	}

	private Long _activeIdentitiesCount;
	private Long _activitiesCount;
	private Long _anonymousIdentitiesCount;
	private AuthorDTO _authorDTO;
	private String _channelId;
	private Date _createDate;
	private Map<String, Object> _embedded;
	private String _filterMetadata;
	private String _filterString;
	private String _id;
	private Long _identitiesCount;
	private Boolean _includeAnonymousUsers;
	private Long _individualsCount;
	private Date _lastActivityDate;
	private Date _modifiedDate;
	private String _name;
	private Set<String> _referencedDataSourceIds;
	private Set<String> _referencedFieldMappingFieldNames;
	private String _scope;
	private Set<SegmentDTO> _segmentDTOs;
	private String _state;
	private String _status;
	private String _type;

}