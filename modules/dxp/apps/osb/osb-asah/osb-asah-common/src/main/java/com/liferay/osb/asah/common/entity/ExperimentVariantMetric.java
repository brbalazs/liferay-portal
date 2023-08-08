/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.math.BigDecimal;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.Range;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author Marcellus Tavares
 */
@Table
public class ExperimentVariantMetric {

	public ExperimentVariantMetric() {
	}

	public ExperimentVariantMetric(boolean control, String dxpVariantId) {
		_control = control;
		_dxpVariantId = dxpVariantId;
	}

	public ExperimentVariantMetric(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ExperimentVariantMetric)) {
			return false;
		}

		ExperimentVariantMetric experimentVariantMetric =
			(ExperimentVariantMetric)obj;

		if (Objects.equals(_control, experimentVariantMetric._control) &&
			Objects.equals(
				_dxpVariantId, experimentVariantMetric._dxpVariantId) &&
			Objects.equals(
				_improvement, experimentVariantMetric._improvement) &&
			Objects.equals(_median, experimentVariantMetric._median) &&
			Objects.equals(
				_probabilityToWin, experimentVariantMetric._probabilityToWin)) {

			return true;
		}

		return false;
	}

	@JsonIgnore
	public Range<Double> getConfidenceIntervalRange() {
		if ((_confidenceIntervals == null) ||
			(_confidenceIntervals.length < 2)) {

			return Range.is(0D);
		}

		return Range.between(
			_confidenceIntervals[0].doubleValue(),
			_confidenceIntervals[1].doubleValue());
	}

	@AccessType(AccessType.Type.PROPERTY)
	@Column("confidenceinterval")
	@JsonProperty("confidenceInterval")
	public BigDecimal[] getConfidenceIntervals() {
		return Arrays.copyOf(_confidenceIntervals, _confidenceIntervals.length);
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean getControl() {
		return _control;
	}

	@AccessType(AccessType.Type.PROPERTY)
	@JsonProperty("dxpVariantId")
	public String getDXPVariantId() {
		return _dxpVariantId;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getImprovement() {
		return _improvement;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getMedian() {
		return _median;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getProbabilityToWin() {
		return _probabilityToWin;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_control, _dxpVariantId, _improvement, _median, _probabilityToWin);
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Boolean isControl() {
		return getControl();
	}

	public void setConfidenceIntervals(BigDecimal[] confidenceIntervals) {
		_confidenceIntervals = Arrays.copyOf(
			confidenceIntervals, confidenceIntervals.length);
	}

	public void setControl(Boolean control) {
		_control = control;
	}

	public void setDXPVariantId(String dxpVariantId) {
		_dxpVariantId = dxpVariantId;
	}

	public void setImprovement(Double improvement) {
		_improvement = improvement;
	}

	public void setMedian(Double median) {
		_median = median;
	}

	public void setProbabilityToWin(Double probabilityToWin) {
		_probabilityToWin = probabilityToWin;
	}

	@Transient
	private BigDecimal[] _confidenceIntervals = {
		BigDecimal.valueOf(0.0), BigDecimal.valueOf(0.0)
	};

	@Transient
	private Boolean _control;

	@Transient
	private String _dxpVariantId;

	@Transient
	private Double _improvement;

	@Transient
	private Double _median;

	@Transient
	private Double _probabilityToWin;

}