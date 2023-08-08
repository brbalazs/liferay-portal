/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.BQDataSourceUser;
import com.liferay.osb.asah.common.model.Field;
import com.liferay.osb.asah.common.model.Individual;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.util.CollectionUtils;

/**
 * @author Rachael Koestartyo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("individuals")
public class IndividualDTO {

	public IndividualDTO() {
	}

	public IndividualDTO(Individual individual) {
		_activitiesCount = individual.getActivitiesCount();
		_createDate = individual.getCreateDate();

		_dataSourceIndividualPKDTOs = new HashSet<>();

		for (BQDataSourceUser bqDataSourceUser :
				individual.getBQDataSourceUsers()) {

			_dataSourceIndividualPKDTOs.add(
				new DataSourceIndividualPKDTO(bqDataSourceUser));
		}

		_firstEnrichmentDate = individual.getFirstEnrichmentDate();
		_id = StringUtil.get(individual.getId(), null);
		_individualCustomFieldDTO = new IndividualFieldDTO(
			individual.getCustomFields());
		_individualFieldDTO = new IndividualFieldDTO(individual.getFields());
		_lastActivityDate = individual.getLastActivityDate();
		_lastEnrichmentDate = individual.getLastEnrichmentDate();
		_modifiedDate = individual.getModifiedDate();
	}

	public IndividualDTO(List<Individual> individuals) {
		_individualDTOs = SetUtil.map(individuals, IndividualDTO::new);
	}

	public IndividualDTO(Set<IndividualDTO> individualDTOs) {
		_individualDTOs = individualDTOs;
	}

	@JsonProperty("activitiesCount")
	public Long getActivitiesCount() {
		return _activitiesCount;
	}

	@JsonProperty("activitiesCounts")
	public List<ActivitiesCountDTO> getActivitiesCountDTOs() {
		return _activitiesCountDTOs;
	}

	@JsonProperty("lastActivityDates")
	public Set<ActivityDateDTO> getActivityDateDTOs() {
		return _activityDateDTOs;
	}

	@JsonProperty("channelIds")
	public Set<String> getChannelIds() {
		return _channelIds;
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

	@JsonProperty("dataSourceIndividualPKs")
	public Set<DataSourceIndividualPKDTO> getDataSourceIndividualPKDTOs() {
		return _dataSourceIndividualPKDTOs;
	}

	@JsonProperty("_embedded")
	public Map<String, Object> getEmbedded() {
		return _embedded;
	}

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("firstEnrichmentDate")
	public Date getFirstEnrichmentDate() {
		if (_firstEnrichmentDate == null) {
			return null;
		}

		return new Date(_firstEnrichmentDate.getTime());
	}

	@JsonProperty("groupIds")
	public Set<String> getGroupIds() {
		return _groupIds;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

	@JsonProperty("custom")
	public IndividualFieldDTO getIndividualCustomFieldDTO() {
		return _individualCustomFieldDTO;
	}

	@JsonProperty("individuals")
	public Set<IndividualDTO> getIndividualDTOs() {
		return _individualDTOs;
	}

	@JsonProperty("demographics")
	public IndividualFieldDTO getIndividualFieldDTO() {
		return _individualFieldDTO;
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

	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("lastEnrichmentDate")
	public Date getLastEnrichmentDate() {
		if (_lastEnrichmentDate == null) {
			return null;
		}

		return new Date(_lastEnrichmentDate.getTime());
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

	@JsonProperty("organizationIds")
	public Set<String> getOrganizationIds() {
		return _organizationIds;
	}

	@JsonProperty("roleIds")
	public Set<String> getRoleIds() {
		return _roleIds;
	}

	@JsonProperty("individualSegmentIds")
	public Set<String> getSegmentIds() {
		return _segmentIds;
	}

	@JsonProperty("teamIds")
	public Set<String> getTeamIds() {
		return _teamIds;
	}

	@JsonProperty("userGroupIds")
	public Set<String> getUserGroupIds() {
		return _userGroupIds;
	}

	public void setEmbedded(Map<String, Object> embedded) {
		_embedded = embedded;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ActivitiesCountDTO {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof ActivitiesCountDTO)) {
				return false;
			}

			ActivitiesCountDTO activitiesCountDTO = (ActivitiesCountDTO)obj;

			if (Objects.equals(
					_activitiesCount, activitiesCountDTO._activitiesCount) &&
				Objects.equals(_channelId, activitiesCountDTO._channelId)) {

				return true;
			}

			return false;
		}

		@JsonProperty("activitiesCount")
		public Long getActivitiesCount() {
			return _activitiesCount;
		}

		@JsonProperty("channelId")
		public String getChannelId() {
			return _channelId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_activitiesCount, _channelId);
		}

		public void setActivitiesCount(Long activitiesCount) {
			_activitiesCount = activitiesCount;
		}

		public void setChannelId(String channelId) {
			_channelId = channelId;
		}

		private Long _activitiesCount;
		private String _channelId;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class ActivityDateDTO {

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof ActivityDateDTO)) {
				return false;
			}

			ActivityDateDTO activityDateDTO = (ActivityDateDTO)obj;

			if (Objects.equals(_activityDate, activityDateDTO._activityDate) &&
				Objects.equals(_channelId, activityDateDTO._channelId)) {

				return true;
			}

			return false;
		}

		@JsonFormat(
			pattern = DateUtil.PATTERN_ISO_8601,
			shape = JsonFormat.Shape.STRING, timezone = "UTC"
		)
		@JsonProperty("lastActivityDate")
		public Date getActivityDate() {
			if (_activityDate == null) {
				return null;
			}

			return new Date(_activityDate.getTime());
		}

		@JsonProperty("channelId")
		public String getChannelId() {
			return _channelId;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_activityDate, _channelId);
		}

		public void setActivityDate(Date activityDate) {
			if (activityDate != null) {
				_activityDate = new Date(activityDate.getTime());
			}
		}

		public void setChannelId(String channelId) {
			_channelId = channelId;
		}

		@Transient
		private Date _activityDate;

		@Transient
		private String _channelId;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class DataSourceIndividualPKDTO {

		public DataSourceIndividualPKDTO() {
		}

		public DataSourceIndividualPKDTO(BQDataSourceUser bqDataSourceUser) {
			if (!CollectionUtils.isEmpty(bqDataSourceUser.getUserPKs())) {
				_dataSourceId = String.valueOf(
					bqDataSourceUser.getDataSourceId());
				_userPKs = bqDataSourceUser.getUserPKs();
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof DataSourceIndividualPKDTO)) {
				return false;
			}

			DataSourceIndividualPKDTO dataSourceIndividualPKDTO =
				(DataSourceIndividualPKDTO)obj;

			if (Objects.equals(
					_dataSourceId, dataSourceIndividualPKDTO._dataSourceId) &&
				Objects.equals(_userPKs, dataSourceIndividualPKDTO._userPKs)) {

				return true;
			}

			return false;
		}

		@JsonProperty("dataSourceId")
		public String getDataSourceId() {
			return _dataSourceId;
		}

		@JsonAlias("userPKs")
		@JsonProperty("individualPKs")
		public Set<String> getUserPKs() {
			return _userPKs;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_dataSourceId, _userPKs);
		}

		public void setDataSourceId(String dataSourceId) {
			_dataSourceId = dataSourceId;
		}

		public void setUserPKs(Set<String> userPKs) {
			_userPKs = userPKs;
		}

		@Transient
		private String _dataSourceId;

		@Transient
		private Set<String> _userPKs = new HashSet<>();

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class IndividualCountDTO {

		public IndividualCountDTO() {
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof IndividualCountDTO)) {
				return false;
			}

			IndividualCountDTO individualCountDTO = (IndividualCountDTO)obj;

			if (Objects.equals(_channelId, individualCountDTO._channelId) &&
				Objects.equals(
					_individualsCount, individualCountDTO._individualsCount)) {

				return true;
			}

			return false;
		}

		@JsonProperty("channelId")
		public String getChannelId() {
			return _channelId;
		}

		@JsonAlias("individualsCount")
		@JsonProperty("individualCount")
		public Long getIndividualsCount() {
			return _individualsCount;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_channelId, _individualsCount);
		}

		public void setChannelId(String channelId) {
			_channelId = channelId;
		}

		public void setIndividualsCount(Long individualsCount) {
			_individualsCount = individualsCount;
		}

		private String _channelId;
		private Long _individualsCount;

	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public static class IndividualFieldDTO {

		public IndividualFieldDTO(Set<Field> fields) {
			for (Field field : fields) {
				_fieldMap.put(
					field.getName(),
					Collections.singletonList(new FieldDTO(field)));
			}
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (!(obj instanceof IndividualFieldDTO)) {
				return false;
			}

			IndividualFieldDTO individualFieldDTO = (IndividualFieldDTO)obj;

			if (Objects.equals(_fieldMap, individualFieldDTO._fieldMap)) {
				return true;
			}

			return false;
		}

		@JsonAnyGetter
		public Map<String, Object> getField() {
			return _fieldMap;
		}

		@Override
		public int hashCode() {
			return Objects.hash(_fieldMap);
		}

		public void setFieldMap(Map<String, Object> fieldMap) {
			_fieldMap = fieldMap;
		}

		private Map<String, Object> _fieldMap = new HashMap<>();

	}

	private Field _createField(String name, Object value) {
		Field field = new Field();

		field.setName(name);
		field.setValue(value);

		return field;
	}

	private Long _activitiesCount;
	private List<ActivitiesCountDTO> _activitiesCountDTOs;
	private Set<ActivityDateDTO> _activityDateDTOs;
	private Set<String> _channelIds;
	private Date _createDate;
	private Set<DataSourceIndividualPKDTO> _dataSourceIndividualPKDTOs;
	private Map<String, Object> _embedded;
	private Date _firstEnrichmentDate;
	private Set<String> _groupIds;
	private String _id;
	private IndividualFieldDTO _individualCustomFieldDTO;
	private Set<IndividualDTO> _individualDTOs;
	private IndividualFieldDTO _individualFieldDTO;
	private Date _lastActivityDate;
	private Date _lastEnrichmentDate;
	private Date _modifiedDate;
	private Set<String> _organizationIds;
	private Set<String> _roleIds;
	private Set<String> _segmentIds;
	private Set<String> _teamIds;
	private Set<String> _userGroupIds;

}