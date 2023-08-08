/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Inácio Nery
 */
@Table
public class Segment implements Persistable<Long> {

	public Segment() {
	}

	public Segment(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Segment)) {
			return false;
		}

		Segment segment = (Segment)obj;

		if (Objects.equals(_author, segment._author) &&
			Objects.equals(_channelId, segment._channelId) &&
			Objects.equals(_createDate, segment._createDate) &&
			Objects.equals(_filterString, segment._filterString) &&
			Objects.equals(_filterMetadata, segment._filterMetadata) &&
			Objects.equals(_id, segment._id) &&
			Objects.equals(
				_includeAnonymousUsers, segment._includeAnonymousUsers) &&
			Objects.equals(_identitiesCount, segment._identitiesCount) &&
			Objects.equals(_individualsCount, segment._individualsCount) &&
			Objects.equals(_modifiedDate, segment._modifiedDate) &&
			Objects.equals(_name, segment._name) &&
			Objects.equals(_referencedAssetIds, segment._referencedAssetIds) &&
			Objects.equals(
				_referencedDataSourceIds, segment._referencedDataSourceIds) &&
			Objects.equals(
				_referencedFieldMappingFieldNames,
				segment._referencedFieldMappingFieldNames) &&
			Objects.equals(_referencedGroupIds, segment._referencedGroupIds) &&
			Objects.equals(
				_referencedOrganizationIds,
				segment._referencedOrganizationIds) &&
			Objects.equals(_referencedRoleIds, segment._referencedRoleIds) &&
			Objects.equals(_referencedTeamIds, segment._referencedTeamIds) &&
			Objects.equals(
				_referencedUserGroupIds, segment._referencedUserGroupIds) &&
			Objects.equals(_referencedUserIds, segment._referencedUserIds) &&
			Objects.equals(_scope, segment._scope) &&
			Objects.equals(_state, segment._state) &&
			Objects.equals(_status, segment._status) &&
			Objects.equals(_type, segment._type)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public Long getAuthorId() {
		if (_author == null) {
			return null;
		}

		return _author.getId();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonIgnore
	public String getAuthorName() {
		if (_author == null) {
			return null;
		}

		return _author.getName();
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getChannelId() {
		return _channelId;
	}

	@AccessType(AccessType.Type.PROPERTY)
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

	@AccessType(AccessType.Type.PROPERTY)
	public String getFilter() {
		return _filterString;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getFilterMetadata() {
		return _filterMetadata;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getIdentitiesCount() {
		return _identitiesCount;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getIncludeAnonymousUsers() {
		return _includeAnonymousUsers;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getIndividualsCount() {
		return _individualsCount;
	}

	@AccessType(AccessType.Type.PROPERTY)
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

	@AccessType(AccessType.Type.PROPERTY)
	public String getName() {
		return _name;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedAssetIds() {
		return _referencedAssetIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("referencedAssetDataSourceIds")
	@JsonSerialize(contentUsing = ToStringSerializer.class)
	public Set<Long> getReferencedDataSourceIds() {
		return _referencedDataSourceIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedFieldMappingFieldNames() {
		return _referencedFieldMappingFieldNames;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedGroupIds() {
		return _referencedGroupIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedOrganizationIds() {
		return _referencedOrganizationIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedRoleIds() {
		return _referencedRoleIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedTeamIds() {
		return _referencedTeamIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedUserGroupIds() {
		return _referencedUserGroupIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Set<String> getReferencedUserIds() {
		return _referencedUserIds;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getScope() {
		return _scope;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getState() {
		return _state;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getStatus() {
		return _status;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonAlias("type")
	@JsonProperty("segmentType")
	public Type getType() {
		return _type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_author, _channelId, _createDate, _filterString, _filterMetadata,
			_id, _identitiesCount, _includeAnonymousUsers, _individualsCount,
			_modifiedDate, _name, _referencedAssetIds, _referencedDataSourceIds,
			_referencedFieldMappingFieldNames, _referencedGroupIds,
			_referencedOrganizationIds, _referencedRoleIds, _referencedTeamIds,
			_referencedUserGroupIds, _referencedUserIds, _scope, _state,
			_status, _type);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	public void setAuthorId(Long authorId) {
		if (authorId == null) {
			return;
		}

		if (_author == null) {
			_author = new Author();
		}

		_author.setId(authorId);
	}

	public void setAuthorName(String authorName) {
		if (authorName == null) {
			return;
		}

		if (_author == null) {
			_author = new Author();
		}

		_author.setName(authorName);
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setFilter(String filterString) {
		_filterString = filterString;
	}

	public void setFilterMetadata(String filterMetadata) {
		_filterMetadata = filterMetadata;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIdentitiesCount(Long identitiesCount) {
		_identitiesCount = identitiesCount;
	}

	public void setIncludeAnonymousUsers(Boolean includeAnonymousUsers) {
		_includeAnonymousUsers = includeAnonymousUsers;
	}

	public void setIndividualsCount(Long individualsCount) {
		_individualsCount = individualsCount;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setName(String name) {
		_name = name;
	}

	public void setReferencedAssetIds(Set<String> referencedAssetIds) {
		_referencedAssetIds = referencedAssetIds;
	}

	public void setReferencedDataSourceIds(Set<Long> referencedDataSourceIds) {
		_referencedDataSourceIds = referencedDataSourceIds;
	}

	public void setReferencedFieldMappingFieldNames(
		Set<String> referencedFieldMappingFieldNames) {

		_referencedFieldMappingFieldNames = referencedFieldMappingFieldNames;
	}

	public void setReferencedGroupIds(Set<String> referencedGroupIds) {
		_referencedGroupIds = referencedGroupIds;
	}

	public void setReferencedOrganizationIds(
		Set<String> referencedOrganizationIds) {

		_referencedOrganizationIds = referencedOrganizationIds;
	}

	public void setReferencedRoleIds(Set<String> referencedRoleIds) {
		_referencedRoleIds = referencedRoleIds;
	}

	public void setReferencedTeamIds(Set<String> referencedTeamIds) {
		_referencedTeamIds = referencedTeamIds;
	}

	public void setReferencedUserGroupIds(Set<String> referencedUserGroupIds) {
		_referencedUserGroupIds = referencedUserGroupIds;
	}

	public void setReferencedUserIds(Set<String> referencedUserIds) {
		_referencedUserIds = referencedUserIds;
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

	public void setType(Type type) {
		_type = type;
	}

	public enum Type {

		DYNAMIC, STATIC

	}

	@JsonProperty("author")
	protected Author getAuthor() {
		return _author;
	}

	protected void setAuthor(Author author) {
		_author = author;
	}

	@Transient
	private Author _author;

	@Transient
	private Long _channelId;

	@Transient
	private Date _createDate;

	@Transient
	private String _filterMetadata;

	@Transient
	private String _filterString;

	@Transient
	private Long _id;

	@Transient
	private Long _identitiesCount;

	@Transient
	private Boolean _includeAnonymousUsers;

	@Transient
	private Long _individualsCount;

	@Transient
	private Boolean _isNew;

	@Transient
	private Date _modifiedDate;

	@Transient
	private String _name;

	@Transient
	private Set<String> _referencedAssetIds = new HashSet<>();

	@Transient
	private Set<Long> _referencedDataSourceIds = new HashSet<>();

	@Transient
	private Set<String> _referencedFieldMappingFieldNames = new HashSet<>();

	@Transient
	private Set<String> _referencedGroupIds = new HashSet<>();

	@Transient
	private Set<String> _referencedOrganizationIds = new HashSet<>();

	@Transient
	private Set<String> _referencedRoleIds = new HashSet<>();

	@Transient
	private Set<String> _referencedTeamIds = new HashSet<>();

	@Transient
	private Set<String> _referencedUserGroupIds = new HashSet<>();

	@Transient
	private Set<String> _referencedUserIds = new HashSet<>();

	@Transient
	private String _scope;

	@Transient
	private String _state;

	@Transient
	private String _status;

	@Transient
	private Type _type;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private static class Author {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Author)) {
				return false;
			}

			Author author = (Author)obj;

			if (Objects.equals(_id, author._id) &&
				Objects.equals(_name, author._name)) {

				return true;
			}

			return false;
		}

		@JsonSerialize(using = ToStringSerializer.class)
		public Long getId() {
			return _id;
		}

		public String getName() {
			return _name;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_id, _name);
		}

		public void setId(Long id) {
			_id = id;
		}

		public void setName(String name) {
			_name = name;
		}

		private Long _id;
		private String _name;

	}

}