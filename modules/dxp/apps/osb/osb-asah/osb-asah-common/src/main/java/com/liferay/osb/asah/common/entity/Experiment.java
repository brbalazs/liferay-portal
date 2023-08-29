/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.model.ExperimentStatus;
import com.liferay.osb.asah.common.model.ExperimentType;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author André Miranda
 */
@Table
public final class Experiment implements Persistable<Long> {

	public Experiment() {
	}

	public Experiment(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public void addExperimentMetric(ExperimentMetric experimentMetric) {
		_experimentMetrics.add(experimentMetric);
	}

	public void addExperimentVariant(ExperimentVariant experimentVariant) {
		_experimentVariants.add(experimentVariant);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof Experiment)) {
			return false;
		}

		Experiment experiment = (Experiment)obj;

		if (Objects.equals(_channelId, experiment._channelId) &&
			Objects.equals(_confidenceLevel, experiment._confidenceLevel) &&
			Objects.equals(_createDate, experiment._createDate) &&
			Objects.equals(_dataSourceId, experiment._dataSourceId) &&
			Objects.equals(_description, experiment._description) &&
			Objects.equals(_dxpExperienceId, experiment._dxpExperienceId) &&
			Objects.equals(_dxpExperienceName, experiment._dxpExperienceName) &&
			Objects.equals(_dxpGroupId, experiment._dxpGroupId) &&
			Objects.equals(_dxpLayoutId, experiment._dxpLayoutId) &&
			Objects.equals(_dxpSegmentId, experiment._dxpSegmentId) &&
			Objects.equals(_dxpSegmentName, experiment._dxpSegmentName) &&
			Objects.equals(_experimentStatus, experiment._experimentStatus) &&
			Objects.equals(
				_experimentVariants, experiment._experimentVariants) &&
			Objects.equals(_experimentType, experiment._experimentType) &&
			Objects.equals(_finishedDate, experiment._finishedDate) &&
			Objects.equals(_goal, experiment._goal) &&
			Objects.equals(_id, experiment._id) &&
			Objects.equals(_modifiedDate, experiment._modifiedDate) &&
			Objects.equals(_name, experiment._name) &&
			Objects.equals(_pageRelativePath, experiment._pageRelativePath) &&
			Objects.equals(_pageTitle, experiment._pageTitle) &&
			Objects.equals(_pageURL, experiment._pageURL) &&
			Objects.equals(_processedDate, experiment._processedDate) &&
			Objects.equals(_publishable, experiment._publishable) &&
			Objects.equals(
				_publishedDXPVariantId, experiment._publishedDXPVariantId) &&
			Objects.equals(_startedDate, experiment._startedDate) &&
			Objects.equals(
				_winnerDXPVariantId, experiment._winnerDXPVariantId)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getChannelId() {
		return _channelId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getConfidenceLevel() {
		return _confidenceLevel;
	}

	@AccessType(AccessType.Type.PROPERTY)
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

	@AccessType(AccessType.Type.PROPERTY)
	@JsonSerialize(using = ToStringSerializer.class)
	public Long getDataSourceId() {
		return _dataSourceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getDescription() {
		return _description;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpExperienceId")
	public String getDXPExperienceId() {
		return _dxpExperienceId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpExperienceName")
	public String getDXPExperienceName() {
		return _dxpExperienceName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpGroupId")
	public String getDXPGroupId() {
		return _dxpGroupId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpLayoutId")
	public String getDXPLayoutId() {
		return _dxpLayoutId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpSegmentId")
	public String getDXPSegmentId() {
		return _dxpSegmentId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpSegmentName")
	public String getDXPSegmentName() {
		return _dxpSegmentName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("metrics")
	@MappedCollection(idColumn = "experimentid")
	public Set<ExperimentMetric> getExperimentMetrics() {
		return _experimentMetrics;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("status")
	@JsonProperty("status")
	public ExperimentStatus getExperimentStatus() {
		return _experimentStatus;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("type")
	@JsonProperty("type")
	public ExperimentType getExperimentType() {
		return _experimentType;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpVariants")
	@MappedCollection(idColumn = "experimentid")
	public Set<ExperimentVariant> getExperimentVariants() {
		return _experimentVariants;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getFinishedDate() {
		if (_finishedDate == null) {
			return null;
		}

		return new Date(_finishedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Embedded(onEmpty = Embedded.OnEmpty.USE_NULL)
	@MappedCollection(idColumn = "experimentid")
	public Goal getGoal() {
		return _goal;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@JsonSerialize(using = ToStringSerializer.class)
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
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
	public String getPageRelativePath() {
		return _pageRelativePath;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getPageTitle() {
		return _pageTitle;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getPageURL() {
		return _pageURL;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getProcessedDate() {
		if (_processedDate == null) {
			return null;
		}

		return new Date(_processedDate.getTime());
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getPublishable() {
		return _publishable;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getPublishedDXPVariantId() {
		return _publishedDXPVariantId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	public Date getStartedDate() {
		if (_startedDate == null) {
			return null;
		}

		return new Date(_startedDate.getTime());
	}

	@JsonIgnore
	public LocalDateTime getStartedDateLocalDateTime() {
		if (_startedDate == null) {
			return null;
		}

		return DateUtil.toLocalDateTime(_startedDate, ZoneOffset.UTC);
	}

	@AccessType(AccessType.Type.PROPERTY)
	public String getWinnerDXPVariantId() {
		return _winnerDXPVariantId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_channelId, _confidenceLevel, _createDate, _dataSourceId,
			_description, _dxpExperienceId, _dxpExperienceName, _dxpGroupId,
			_dxpLayoutId, _dxpSegmentId, _dxpSegmentName, _experimentVariants,
			_experimentStatus, _experimentType, _finishedDate, _goal, _id,
			_modifiedDate, _name, _pageRelativePath, _pageTitle, _pageURL,
			_processedDate, _publishable, _publishedDXPVariantId, _startedDate,
			_winnerDXPVariantId);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean isPublishable() {
		return getPublishable();
	}

	public void setChannelId(Long channelId) {
		_channelId = channelId;
	}

	public void setConfidenceLevel(Double confidenceLevel) {
		_confidenceLevel = confidenceLevel;
	}

	public void setCreateDate(Date createDate) {
		if (createDate != null) {
			_createDate = new Date(createDate.getTime());
		}
	}

	public void setDataSourceId(Long dataSourceId) {
		_dataSourceId = dataSourceId;
	}

	public void setDescription(String description) {
		_description = description;
	}

	public void setDXPExperienceId(String dxpExperienceId) {
		_dxpExperienceId = dxpExperienceId;
	}

	public void setDXPExperienceName(String dxpExperienceName) {
		_dxpExperienceName = dxpExperienceName;
	}

	public void setDXPGroupId(String dxpGroupId) {
		_dxpGroupId = dxpGroupId;
	}

	public void setDXPLayoutId(String dxpLayoutId) {
		_dxpLayoutId = dxpLayoutId;
	}

	public void setDXPSegmentId(String dxpSegmentId) {
		_dxpSegmentId = dxpSegmentId;
	}

	public void setDXPSegmentName(String dxpSegmentName) {
		_dxpSegmentName = dxpSegmentName;
	}

	@JsonDeserialize(as = LinkedHashSet.class)
	public void setExperimentMetrics(Set<ExperimentMetric> experimentMetrics) {
		_experimentMetrics = experimentMetrics;
	}

	public void setExperimentStatus(ExperimentStatus experimentStatus) {
		_experimentStatus = experimentStatus;
	}

	public void setExperimentType(ExperimentType experimentType) {
		_experimentType = experimentType;
	}

	@JsonDeserialize(as = LinkedHashSet.class)
	public void setExperimentVariants(
		Set<ExperimentVariant> experimentVariants) {

		_experimentVariants = experimentVariants;
	}

	public void setFinishedDate(Date finishedDate) {
		if (finishedDate != null) {
			_finishedDate = new Date(finishedDate.getTime());
		}
	}

	public void setGoal(Goal goal) {
		_goal = goal;
	}

	public void setId(Long id) {
		_id = id;
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

	public void setPageRelativePath(String pageRelativePath) {
		_pageRelativePath = pageRelativePath;
	}

	public void setPageTitle(String pageTitle) {
		_pageTitle = pageTitle;
	}

	public void setPageURL(String pageURL) {
		_pageURL = pageURL;
	}

	public void setProcessedDate(Date processedDate) {
		if (processedDate != null) {
			_processedDate = new Date(processedDate.getTime());
		}
	}

	public void setPublishable(Boolean publishable) {
		_publishable = publishable;
	}

	public void setPublishedDXPVariantId(String publishedDXPVariantId) {
		_publishedDXPVariantId = publishedDXPVariantId;
	}

	public void setStartedDate(Date startedDate) {
		if (startedDate != null) {
			_startedDate = new Date(startedDate.getTime());
		}
	}

	public void setWinnerDXPVariantId(String winnerDXPVariantId) {
		_winnerDXPVariantId = winnerDXPVariantId;
	}

	@Transient
	private Long _channelId;

	@Transient
	private Double _confidenceLevel;

	@Transient
	private Date _createDate;

	@Transient
	private Long _dataSourceId;

	@Transient
	private String _description;

	@Transient
	private String _dxpExperienceId;

	@Transient
	private String _dxpExperienceName;

	@Transient
	private String _dxpGroupId;

	@Transient
	private String _dxpLayoutId;

	@Transient
	private String _dxpSegmentId;

	@Transient
	private String _dxpSegmentName;

	@Transient
	private Set<ExperimentMetric> _experimentMetrics = new LinkedHashSet<>();

	@Transient
	private ExperimentStatus _experimentStatus = ExperimentStatus.DRAFT;

	@Transient
	private ExperimentType _experimentType = ExperimentType.AB;

	@Transient
	private Set<ExperimentVariant> _experimentVariants = new LinkedHashSet<>();

	@Transient
	private Date _finishedDate;

	@Transient
	private Goal _goal;

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private Date _modifiedDate;

	@Transient
	private String _name;

	@Transient
	private String _pageRelativePath;

	@Transient
	private String _pageTitle;

	@Transient
	private String _pageURL;

	@Transient
	private Date _processedDate;

	@Transient
	private Boolean _publishable;

	@Transient
	private String _publishedDXPVariantId;

	@Transient
	private Date _startedDate;

	@Transient
	private String _winnerDXPVariantId;

}