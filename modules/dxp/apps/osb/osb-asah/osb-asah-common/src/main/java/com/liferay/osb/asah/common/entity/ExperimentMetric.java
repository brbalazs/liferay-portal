/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.liferay.osb.asah.common.date.DateUtil;
import com.liferay.osb.asah.common.util.BeanUtils;

import java.time.LocalDateTime;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class ExperimentMetric implements Persistable<Long> {

	public ExperimentMetric() {
	}

	public ExperimentMetric(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	public void addExperimentVariantMetric(
		ExperimentVariantMetric experimentVariantMetric) {

		_experimentVariantMetrics.add(experimentVariantMetric);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ExperimentMetric)) {
			return false;
		}

		ExperimentMetric experimentMetric = (ExperimentMetric)obj;

		if (Objects.equals(_completion, experimentMetric._completion) &&
			Objects.equals(
				_confidenceLevel, experimentMetric._confidenceLevel) &&
			Objects.equals(_elapsedDays, experimentMetric._elapsedDays) &&
			Objects.equals(
				_estimatedDaysLeft, experimentMetric._estimatedDaysLeft) &&
			Objects.equals(
				_experimentVariantMetrics,
				experimentMetric._experimentVariantMetrics) &&
			Objects.equals(_id, experimentMetric._id) &&
			Objects.equals(
				_processedLocalDateTime,
				experimentMetric._processedLocalDateTime)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getCompletion() {
		return _completion;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getConfidenceLevel() {
		return _confidenceLevel;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getElapsedDays() {
		return _elapsedDays;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Long getEstimatedDaysLeft() {
		return _estimatedDaysLeft;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("variantMetrics")
	@MappedCollection(idColumn = "experimentmetricid")
	public Set<ExperimentVariantMetric> getExperimentVariantMetrics() {
		return _experimentVariantMetrics;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Id
	@Override
	public Long getId() {
		return _id;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("processeddate")
	@JsonFormat(
		pattern = DateUtil.PATTERN_ISO_8601, shape = JsonFormat.Shape.STRING,
		timezone = "UTC"
	)
	@JsonProperty("processedDate")
	public LocalDateTime getProcessedLocalDateTime() {
		return _processedLocalDateTime;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_completion, _confidenceLevel, _elapsedDays, _estimatedDaysLeft,
			_experimentVariantMetrics, _id, _processedLocalDateTime);
	}

	@JsonIgnore
	@Override
	public boolean isNew() {
		if ((_id == null) || ((_isNew != null) && _isNew)) {
			return true;
		}

		return false;
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

	@JsonDeserialize(as = LinkedHashSet.class)
	public void setExperimentVariantMetrics(
		Set<ExperimentVariantMetric> experimentVariantMetrics) {

		_experimentVariantMetrics = experimentVariantMetrics;
	}

	public void setId(Long id) {
		_id = id;
	}

	public void setIsNew(Boolean isNew) {
		_isNew = isNew;
	}

	public void setProcessedLocalDateTime(
		LocalDateTime processedLocalDateTime) {

		_processedLocalDateTime = processedLocalDateTime;
	}

	@Transient
	private Double _completion;

	@Transient
	private Double _confidenceLevel;

	@Transient
	private Long _elapsedDays;

	@Transient
	private Long _estimatedDaysLeft;

	@Transient
	private Set<ExperimentVariantMetric> _experimentVariantMetrics =
		new LinkedHashSet<>();

	@Transient
	private Long _id;

	@Transient
	private Boolean _isNew;

	@Transient
	private LocalDateTime _processedLocalDateTime;

}