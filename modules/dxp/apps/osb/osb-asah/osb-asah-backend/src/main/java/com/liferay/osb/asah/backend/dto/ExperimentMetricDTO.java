/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.entity.ExperimentMetric;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.SetUtil;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Marcos Martins
 */
@GraphQLType("ExperimentMetric")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentMetricDTO {

	public ExperimentMetricDTO() {
	}

	public ExperimentMetricDTO(ExperimentMetric experimentMetric) {
		_completion = experimentMetric.getCompletion();
		_confidenceLevel = experimentMetric.getConfidenceLevel();
		_elapsedDays = experimentMetric.getElapsedDays();
		_estimatedDaysLeft = experimentMetric.getEstimatedDaysLeft();
		_id = experimentMetric.getId();
		_processedLocalDateTime = experimentMetric.getProcessedLocalDateTime();

		Set<ExperimentVariantMetricDTO> experimentVariantMetricDTOs =
			SetUtil.map(
				experimentMetric.getExperimentVariantMetrics(),
				ExperimentVariantMetricDTO::new);

		if (!experimentVariantMetricDTOs.isEmpty()) {
			_experimentVariantMetricDTOs = experimentVariantMetricDTOs;
		}
	}

	public Double getCompletion() {
		return _completion;
	}

	public Double getConfidenceLevel() {
		return _confidenceLevel;
	}

	public Long getElapsedDays() {
		return _elapsedDays;
	}

	public Long getEstimatedDaysLeft() {
		return _estimatedDaysLeft;
	}

	@GraphQLProperty("variantMetrics")
	@JsonProperty("variantMetrics")
	public Set<ExperimentVariantMetricDTO> getExperimentVariantMetricDTOs() {
		return _experimentVariantMetricDTOs;
	}

	public Long getId() {
		return _id;
	}

	@GraphQLProperty("processedDate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("processedDate")
	public LocalDateTime getProcessedLocalDateTime() {
		return _processedLocalDateTime;
	}

	public void setCompletion(Double completion) {
		_completion = completion;
	}

	public void setConfidenceLevel(Double confidenceLevel) {
		_confidenceLevel = confidenceLevel;
	}

	public void setElapsedDays(Long elapsedDays) {
		_elapsedDays = elapsedDays;
	}

	public void setEstimatedDaysLeft(Long estimatedDaysLeft) {
		_estimatedDaysLeft = estimatedDaysLeft;
	}

	public void setExperimentVariantMetricDTOs(
		Set<ExperimentVariantMetricDTO> experimentVariantMetricDTOs) {

		_experimentVariantMetricDTOs = experimentVariantMetricDTOs;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setProcessedLocalDateTime(
		LocalDateTime processedLocalDateTime) {

		_processedLocalDateTime = processedLocalDateTime;
	}

	private Double _completion;
	private Double _confidenceLevel;
	private Long _elapsedDays;
	private Long _estimatedDaysLeft;
	private Set<ExperimentVariantMetricDTO> _experimentVariantMetricDTOs =
		new HashSet<>();
	private Long _id;
	private LocalDateTime _processedLocalDateTime;

}