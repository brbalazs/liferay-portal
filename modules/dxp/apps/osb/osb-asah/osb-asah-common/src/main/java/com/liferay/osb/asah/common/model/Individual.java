/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.model;

import com.liferay.osb.asah.common.constants.FieldMappingConstants;
import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.entity.BQIndividual;
import com.liferay.osb.asah.common.util.BeanUtils;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang.math.NumberUtils;

import org.springframework.util.CollectionUtils;

/**
 * @author Rachael Koestartyo
 */
public class Individual {

	public Individual() {
	}

	public Individual(
		Long activitiesCount, BQIndividual bqIndividual,
		Date lastActivityDate) {

		this(
			activitiesCount, bqIndividual, Collections.emptyList(),
			lastActivityDate);
	}

	public Individual(
		Long activitiesCount, BQIndividual bqIndividual,
		List<Map<String, Object>> dataSourceIndividualPKs,
		Date lastActivityDate) {

		_activitiesCount = activitiesCount;

		if (lastActivityDate != null) {
			_lastActivityDate = new Date(lastActivityDate.getTime());
		}

		_createDate = bqIndividual.getCreateDate();

		List<BQIndividual.Field> bqIndividualFields = bqIndividual.getFields();

		Stream<BQIndividual.Field> stream1 = bqIndividualFields.stream();

		Set<Field> fields = stream1.map(
			bqIndividualField -> {
				Field field = new Field();

				field.setDataSourceId(bqIndividualField.getDataSourceId());
				field.setName(bqIndividualField.getName());
				field.setValue(bqIndividualField.getValue());

				return field;
			}
		).collect(
			Collectors.toSet()
		);

		if (fields != null) {
			Set<String> demographicsFieldNames =
				FieldMappingConstants.demographicsDisplayNames.keySet();

			Stream<Field> stream2 = fields.stream();

			_customFields = stream2.filter(
				bqIndividualField -> !demographicsFieldNames.contains(
					bqIndividualField.getName())
			).collect(
				Collectors.toSet()
			);

			fields.removeAll(_customFields);

			_customFields.forEach(
				customField -> {
					customField.setModifiedDate(bqIndividual.getModifiedDate());
					customField.setSourceName(customField.getName());
				});

			stream2 = fields.stream();

			stream2.forEach(
				field -> {
					field.setModifiedDate(bqIndividual.getModifiedDate());

					String displayName =
						FieldMappingConstants.demographicsDisplayNames.
							getOrDefault(field.getName(), field.getName());

					field.setName(displayName);

					if (displayName.endsWith("Date")) {
						long value = NumberUtils.toLong(
							(String)field.getValue());

						field.setValue(DateUtil.toUTCString(new Date(value)));
					}

					field.setSourceName(field.getName());
				});

			_fields = fields;
		}

		_customDemographics = new Demographics(_customFields);

		if (dataSourceIndividualPKs != null) {
			for (Map<String, Object> dataSourceIndividualPK :
					dataSourceIndividualPKs) {

				BigDecimal bigDecimal = (BigDecimal)dataSourceIndividualPK.get(
					"dataSourceId");
				List<String> userPKs = (List<String>)dataSourceIndividualPK.get(
					"userPKs");

				addBQDataSourceUser(
					new BQDataSourceUser(
						Collections.emptySet(), bigDecimal.longValue(), null,
						new HashSet<>(userPKs)));
			}
		}

		_demographics = new Demographics(_fields);
		_emailAddressHashed = bqIndividual.getId();
		_firstEnrichmentDate = bqIndividual.getCreateDate();
		_id = StringUtil.get(bqIndividual.getId(), null);
		_lastEnrichmentDate = bqIndividual.getModifiedDate();
		_modifiedDate = bqIndividual.getModifiedDate();
	}

	public Individual(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public void addBQDataSourceUser(BQDataSourceUser bqDataSourceUser) {
		if (bqDataSourceUser == null) {
			return;
		}

		if (_bqDataSourceUsers == null) {
			_bqDataSourceUsers = new HashSet<>();
		}

		_bqDataSourceUsers.add(bqDataSourceUser);

		if (_dataSourceUserPKs == null) {
			_dataSourceUserPKs = new HashSet<>();
		}

		_dataSourceUserPKs.add(new DataSourceUserPK(bqDataSourceUser));
	}

	public void addChannelId(Long channelId) {
		if (_channelIds == null) {
			_channelIds = new HashSet<>();
		}

		_channelIds.add(channelId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Individual)) {
			return false;
		}

		Individual individual = (Individual)obj;

		if (Objects.equals(_channelIds, individual._channelIds) &&
			Objects.equals(_createDate, individual._createDate) &&
			Objects.equals(
				_emailAddressHashed, individual._emailAddressHashed) &&
			Objects.equals(
				_firstEnrichmentDate, individual._firstEnrichmentDate) &&
			Objects.equals(_groupIds, individual._groupIds) &&
			Objects.equals(_id, individual._id) &&
			Objects.equals(
				_lastEnrichmentDate, individual._lastEnrichmentDate) &&
			Objects.equals(_modifiedDate, individual._modifiedDate) &&
			Objects.equals(_organizationIds, individual._organizationIds) &&
			Objects.equals(_roleIds, individual._roleIds) &&
			Objects.equals(_segmentIds, individual._segmentIds) &&
			Objects.equals(_teamIds, individual._teamIds) &&
			Objects.equals(_userGroupIds, individual._userGroupIds)) {

			return true;
		}

		return false;
	}

	public Long getActivitiesCount() {
		return _activitiesCount;
	}

	public Set<BQDataSourceUser> getBQDataSourceUsers() {
		return _bqDataSourceUsers;
	}

	public Set<Long> getChannelIds() {
		return _channelIds;
	}

	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	public Demographics getCustomDemographics() {
		return new Demographics(_customFields);
	}

	public Set<Field> getCustomFields() {
		return _customFields;
	}

	public Set<DataSourceUserPK> getDataSourceUserPKs() {
		return _dataSourceUserPKs;
	}

	public Demographics getDemographics() {
		return new Demographics(_fields);
	}

	public String getEmailAddressHashed() {
		return _emailAddressHashed;
	}

	public Set<Field> getFields() {
		return _fields;
	}

	public Date getFirstEnrichmentDate() {
		if (_firstEnrichmentDate == null) {
			return null;
		}

		return new Date(_firstEnrichmentDate.getTime());
	}

	public Set<Long> getGroupIds() {
		return _groupIds;
	}

	public String getId() {
		return _id;
	}

	public Date getLastActivityDate() {
		if (_lastActivityDate == null) {
			return null;
		}

		return new Date(_lastActivityDate.getTime());
	}

	public Date getLastEnrichmentDate() {
		if (_lastEnrichmentDate == null) {
			return null;
		}

		return new Date(_lastEnrichmentDate.getTime());
	}

	public Date getModifiedDate() {
		if (_modifiedDate == null) {
			return null;
		}

		return new Date(_modifiedDate.getTime());
	}

	public Set<Long> getOrganizationIds() {
		return _organizationIds;
	}

	public Set<Long> getRoleIds() {
		return _roleIds;
	}

	public Set<Long> getSegmentIds() {
		return _segmentIds;
	}

	public Set<Long> getTeamIds() {
		return _teamIds;
	}

	public Set<Long> getUserGroupIds() {
		return _userGroupIds;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelIds, _createDate, _emailAddressHashed, _firstEnrichmentDate,
			_groupIds, _id, _lastEnrichmentDate, _modifiedDate,
			_organizationIds, _roleIds, _segmentIds, _teamIds, _userGroupIds);
	}

	public void setActivitiesCount(Long activitiesCount) {
		_activitiesCount = activitiesCount;
	}

	public void setBQDataSourceUsers(Set<BQDataSourceUser> bqDataSourceUsers) {
		_bqDataSourceUsers = bqDataSourceUsers;

		_dataSourceUserPKs = SetUtil.map(
			_bqDataSourceUsers, DataSourceUserPK::new);
	}

	public void setChannelIds(Set<Long> channelIds) {
		_channelIds = channelIds;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setCustomDemographics(Demographics demographics) {
		_customDemographics = demographics;

		if (demographics != null) {
			_customFields = demographics._fields;
		}
	}

	public void setCustomFields(Set<Field> fields) {
		_customFields = fields;

		_customDemographics = new Demographics(fields);
	}

	public void setDataSourceUserPKs(Set<DataSourceUserPK> dataSourceUserPKs) {
		_dataSourceUserPKs = dataSourceUserPKs;

		for (DataSourceUserPK dataSourceUserPK : dataSourceUserPKs) {
			Stream<BQDataSourceUser> bqDataSourceUserStream =
				_bqDataSourceUsers.stream();

			bqDataSourceUserStream.filter(
				dataSourceUser -> Objects.equals(
					dataSourceUser.getDataSourceId(),
					dataSourceUserPK.getDataSourceId())
			).forEach(
				bqDataSourceUser -> bqDataSourceUser.setUserPKs(
					dataSourceUserPK.getUserPKs())
			);
		}
	}

	public void setDemographics(Demographics demographics) {
		_demographics = demographics;
		_fields = demographics._fields;
	}

	public void setEmailAddressHashed(String emailAddressHashed) {
		_emailAddressHashed = emailAddressHashed;
	}

	public void setFields(Set<Field> fields) {
		_fields = fields;

		_demographics = new Demographics(fields);
	}

	public void setFirstEnrichmentDate(Date firstEnrichmentDate) {
		if (firstEnrichmentDate != null) {
			_firstEnrichmentDate = new Date(firstEnrichmentDate.getTime());
		}
	}

	public void setGroupIds(Set<Long> groupIds) {
		_groupIds = groupIds;
	}

	public void setId(String id) {
		_id = id;
	}

	public void setLastActivityDate(Date lastActivityDate) {
		if (lastActivityDate != null) {
			_lastActivityDate = new Date(lastActivityDate.getTime());
		}
	}

	public void setLastEnrichmentDate(Date lastEnrichmentDate) {
		if (lastEnrichmentDate != null) {
			_lastEnrichmentDate = new Date(lastEnrichmentDate.getTime());
		}
	}

	public void setModifiedDate(Date modifiedDate) {
		if (modifiedDate != null) {
			_modifiedDate = new Date(modifiedDate.getTime());
		}
	}

	public void setOrganizationIds(Set<Long> organizationIds) {
		_organizationIds = organizationIds;
	}

	public void setRoleIds(Set<Long> roleIds) {
		_roleIds = roleIds;
	}

	public void setSegmentIds(Set<Long> segmentIds) {
		_segmentIds = segmentIds;
	}

	public void setTeamIds(Set<Long> teamIds) {
		_teamIds = teamIds;
	}

	public void setUserGroupIds(Set<Long> userGroupIds) {
		_userGroupIds = userGroupIds;
	}

	public static class DataSourceUserPK {

		public DataSourceUserPK() {
		}

		public DataSourceUserPK(BQDataSourceUser bqDataSourceUser) {
			if (!CollectionUtils.isEmpty(bqDataSourceUser.getUserPKs())) {
				_dataSourceId = bqDataSourceUser.getDataSourceId();
				_userPKs = bqDataSourceUser.getUserPKs();
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof DataSourceUserPK)) {
				return false;
			}

			DataSourceUserPK dataSourceUserPK = (DataSourceUserPK)obj;

			if (Objects.equals(_dataSourceId, dataSourceUserPK._dataSourceId) &&
				Objects.equals(_userPKs, dataSourceUserPK._userPKs)) {

				return true;
			}

			return false;
		}

		public Long getDataSourceId() {
			return _dataSourceId;
		}

		public Set<String> getUserPKs() {
			return _userPKs;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_dataSourceId, _userPKs);
		}

		public void setDataSourceId(Long dataSourceId) {
			_dataSourceId = dataSourceId;
		}

		public void setUserPKs(Set<String> userPKs) {
			_userPKs = userPKs;
		}

		private Long _dataSourceId;
		private Set<String> _userPKs = new HashSet<>();

	}

	public static class Demographics {

		public Demographics() {
		}

		public Demographics(Set<Field> fields) {
			_fields = fields;
		}

		public void addField(String key, List<Field> fields) {
			Field field = fields.get(0);

			field.setName(key);

			_fields.add(field);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Demographics)) {
				return false;
			}

			Demographics demographics = (Demographics)obj;

			if (Objects.equals(_fields, demographics._fields)) {
				return true;
			}

			return false;
		}

		public Map<String, List<Field>> getField() {
			Stream<Field> stream = _fields.stream();

			return stream.collect(
				Collectors.toMap(
					Field::getName, Collections::singletonList,
					(existing, replacement) -> replacement));
		}

		public Set<Field> getFields() {
			return _fields;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_fields);
		}

		private Set<Field> _fields = new HashSet<>();

	}

	private Long _activitiesCount;
	private Set<BQDataSourceUser> _bqDataSourceUsers = new HashSet<>();
	private Set<Long> _channelIds = new HashSet<>();
	private Date _createDate;
	private Demographics _customDemographics;
	private Set<Field> _customFields = new HashSet<>();
	private Set<DataSourceUserPK> _dataSourceUserPKs = new HashSet<>();
	private Demographics _demographics;
	private String _emailAddressHashed;
	private Set<Field> _fields = new HashSet<>();
	private Date _firstEnrichmentDate;
	private Set<Long> _groupIds = new HashSet<>();
	private String _id;
	private Date _lastActivityDate;
	private Date _lastEnrichmentDate;
	private Date _modifiedDate;
	private Set<Long> _organizationIds = new HashSet<>();
	private Set<Long> _roleIds = new HashSet<>();
	private Set<Long> _segmentIds = new HashSet<>();
	private Set<Long> _teamIds = new HashSet<>();
	private Set<Long> _userGroupIds = new HashSet<>();

}