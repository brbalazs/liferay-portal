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
import com.liferay.osb.asah.common.entity.Experiment;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.model.Goal;
import com.liferay.osb.asah.common.util.SetUtil;
import com.liferay.osb.asah.common.util.StringUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

/**
 * @author Marcos Martins
 */
@GraphQLType("Experiment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName("experiments")
public class ExperimentDTO {

	public ExperimentDTO() {
	}

	public ExperimentDTO(Experiment experiment) {
		_channelId = experiment.getChannelId();
		_confidenceLevel = experiment.getConfidenceLevel();
		_createDate = experiment.getCreateDate();
		_dataSourceId = StringUtil.get(experiment.getDataSourceId(), null);
		_description = experiment.getDescription();
		_dxpExperienceId = experiment.getDXPExperienceId();
		_dxpExperienceName = experiment.getDXPExperienceName();
		_dxpGroupId = experiment.getDXPGroupId();
		_dxpLayoutId = experiment.getDXPLayoutId();
		_dxpSegmentId = experiment.getDXPSegmentId();
		_dxpSegmentName = experiment.getDXPSegmentName();

		Set<ExperimentVariantDTO> experimentVariantDTOs = SetUtil.map(
			experiment.getExperimentVariants(), ExperimentVariantDTO::new);

		if (!experimentVariantDTOs.isEmpty()) {
			_experimentVariantDTOs = experimentVariantDTOs;
		}

		Set<ExperimentMetricDTO> experimentMetricsDTOs = SetUtil.map(
			experiment.getExperimentMetrics(), ExperimentMetricDTO::new);

		if (!experimentMetricsDTOs.isEmpty()) {
			_experimentMetricDTOs = experimentMetricsDTOs;
		}

		_experimentStatus = StringUtil.get(
			experiment.getExperimentStatus(), null);
		_experimentType = StringUtil.get(experiment.getExperimentType(), null);
		_finishedDate = experiment.getFinishedDate();

		Goal goal = experiment.getGoal();

		if (goal != null) {
			_goalDTO = new GoalDTO(goal);
		}

		_id = StringUtil.get(experiment.getId(), null);
		_modifiedDate = experiment.getModifiedDate();
		_name = experiment.getName();
		_pageRelativePath = experiment.getPageRelativePath();
		_pageTitle = experiment.getPageTitle();
		_pageURL = experiment.getPageURL();
		_processedDate = experiment.getProcessedDate();
		_publishable = experiment.isPublishable();
		_publishedDXPVariantId = experiment.getPublishedDXPVariantId();
		_startedDate = experiment.getStartedDate();
		_winnerDXPVariantId = experiment.getWinnerDXPVariantId();
	}

	public ExperimentDTO(List<Experiment> experiment) {
		_experimentDTOs = SetUtil.map(experiment, ExperimentDTO::new);
	}

	public Long getChannelId() {
		return _channelId;
	}

	public Double getConfidenceLevel() {
		return _confidenceLevel;
	}

	@JsonAlias("dateCreated")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("createDate")
	@NotNull
	public Date getCreateDate() {
		if (_createDate == null) {
			return null;
		}

		return new Date(_createDate.getTime());
	}

	@GraphQLProperty("createDate")
	@JsonIgnore
	public String getCreateDateISO() {
		if (_createDate == null) {
			return null;
		}

		return DateUtil.toUTCString(_createDate);
	}

	public String getDataSourceId() {
		return _dataSourceId;
	}

	public String getDescription() {
		return _description;
	}

	@JsonProperty("dxpExperienceId")
	@NotBlank
	public String getDXPExperienceId() {
		return _dxpExperienceId;
	}

	@GraphQLProperty("dxpExperienceName")
	@JsonProperty("dxpExperienceName")
	@NotBlank
	public String getDXPExperienceName() {
		return _dxpExperienceName;
	}

	@JsonProperty("dxpGroupId")
	public String getDXPGroupId() {
		return _dxpGroupId;
	}

	@JsonProperty("dxpLayoutId")
	@NotBlank
	public String getDXPLayoutId() {
		return _dxpLayoutId;
	}

	@JsonProperty("dxpSegmentId")
	@NotBlank
	public String getDXPSegmentId() {
		return _dxpSegmentId;
	}

	@GraphQLProperty("dxpSegmentName")
	@JsonProperty("dxpSegmentName")
	public String getDXPSegmentName() {
		return _dxpSegmentName;
	}

	@JsonProperty("experiments")
	public Set<ExperimentDTO> getExperimentDTOs() {
		return _experimentDTOs;
	}

	@JsonProperty("metrics")
	public Set<ExperimentMetricDTO> getExperimentMetricDTOs() {
		return _experimentMetricDTOs;
	}

	@GraphQLProperty("status")
	@JsonProperty("status")
	public String getExperimentStatus() {
		return _experimentStatus;
	}

	@GraphQLProperty("type")
	@JsonProperty("type")
	public String getExperimentType() {
		return _experimentType;
	}

	@GraphQLProperty("dxpVariants")
	@JsonProperty("dxpVariants")
	@Valid
	public Set<ExperimentVariantDTO> getExperimentVariantDTOs() {
		return _experimentVariantDTOs;
	}

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

	@GraphQLProperty("finishedDate")
	@JsonIgnore
	public String getFinishedDateISO() {
		if (_finishedDate == null) {
			return null;
		}

		return DateUtil.toUTCString(_finishedDate);
	}

	@GraphQLProperty("goal")
	@JsonProperty("goal")
	@Valid
	public GoalDTO getGoalDTO() {
		return _goalDTO;
	}

	@JsonProperty("id")
	public String getId() {
		return _id;
	}

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

	@GraphQLProperty("modifiedDate")
	@JsonIgnore
	public String getModifiedDateISO() {
		if (_modifiedDate == null) {
			return null;
		}

		return DateUtil.toUTCString(_modifiedDate);
	}

	@NotBlank
	public String getName() {
		return _name;
	}

	@NotBlank
	public String getPageRelativePath() {
		return _pageRelativePath;
	}

	public String getPageTitle() {
		return _pageTitle;
	}

	@NotBlank
	@URL
	public String getPageURL() {
		return _pageURL;
	}

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

	public Boolean getPublishable() {
		return _publishable;
	}

	public String getPublishedDXPVariantId() {
		return _publishedDXPVariantId;
	}

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

	@GraphQLProperty("startedDate")
	@JsonIgnore
	public String getStartedDateISO() {
		if (_startedDate == null) {
			return null;
		}

		return DateUtil.toUTCString(_startedDate);
	}

	@JsonIgnore
	public LocalDateTime getStartedDateLocalDateTime() {
		if (_startedDate == null) {
			return null;
		}

		return DateUtil.toLocalDateTime(_startedDate, ZoneOffset.UTC);
	}

	public String getWinnerDXPVariantId() {
		return _winnerDXPVariantId;
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

	public void setDataSourceId(String dataSourceId) {
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

	public void setExperimentDTOs(Set<ExperimentDTO> experimentDTOs) {
		_experimentDTOs = experimentDTOs;
	}

	public void setExperimentMetricDTOs(
		Set<ExperimentMetricDTO> experimentMetricDTOs) {

		_experimentMetricDTOs = experimentMetricDTOs;
	}

	public void setExperimentStatus(String experimentStatus) {
		_experimentStatus = experimentStatus;
	}

	public void setExperimentType(String experimentType) {
		_experimentType = experimentType;
	}

	public void setExperimentVariantDTOs(
		Set<ExperimentVariantDTO> experimentVariantDTOs) {

		_experimentVariantDTOs = experimentVariantDTOs;
	}

	public void setFinishedDate(Date finishedDate) {
		if (finishedDate != null) {
			_finishedDate = new Date(finishedDate.getTime());
		}
	}

	public void setGoalDTO(GoalDTO goalDTO) {
		_goalDTO = goalDTO;
	}

	public void setId(String id) {
		_id = id;
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

	private Long _channelId;
	private Double _confidenceLevel;
	private Date _createDate;
	private String _dataSourceId;
	private String _description;
	private String _dxpExperienceId;
	private String _dxpExperienceName;
	private String _dxpGroupId;
	private String _dxpLayoutId;
	private String _dxpSegmentId;
	private String _dxpSegmentName;
	private Set<ExperimentDTO> _experimentDTOs = new HashSet<>();
	private Set<ExperimentMetricDTO> _experimentMetricDTOs = new HashSet<>();
	private String _experimentStatus;
	private String _experimentType;
	private Set<ExperimentVariantDTO> _experimentVariantDTOs = new HashSet<>();
	private Date _finishedDate;
	private GoalDTO _goalDTO;
	private String _id;
	private Date _modifiedDate;
	private String _name;
	private String _pageRelativePath;
	private String _pageTitle;
	private String _pageURL;
	private Date _processedDate;
	private Boolean _publishable;
	private String _publishedDXPVariantId;
	private Date _startedDate;
	private String _winnerDXPVariantId;

}