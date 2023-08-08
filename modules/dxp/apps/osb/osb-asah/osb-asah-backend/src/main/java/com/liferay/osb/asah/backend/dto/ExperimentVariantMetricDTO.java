/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.entity.ExperimentVariantMetric;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;

import java.math.BigDecimal;

import java.util.Arrays;

/**
 * @author Marcos Martins
 */
@GraphQLType("ExperimentVariantMetric")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentVariantMetricDTO {

	public ExperimentVariantMetricDTO() {
	}

	public ExperimentVariantMetricDTO(
		ExperimentVariantMetric experimentVariantMetric) {

		_confidenceIntervals = experimentVariantMetric.getConfidenceIntervals();
		_control = experimentVariantMetric.getControl();
		_dxpVariantId = experimentVariantMetric.getDXPVariantId();
		_improvement = experimentVariantMetric.getImprovement();
		_median = experimentVariantMetric.getMedian();
		_probabilityToWin = experimentVariantMetric.getProbabilityToWin();
	}

	@GraphQLProperty("confidenceInterval")
	@JsonProperty("confidenceInterval")
	public BigDecimal[] getConfidenceIntervals() {
		return Arrays.copyOf(_confidenceIntervals, _confidenceIntervals.length);
	}

	public boolean getControl() {
		return _control;
	}

	@GraphQLProperty("dxpVariantId")
	@JsonProperty("dxpVariantId")
	public String getDXPVariantId() {
		return _dxpVariantId;
	}

	public Double getImprovement() {
		return _improvement;
	}

	public Double getMedian() {
		return _median;
	}

	public Double getProbabilityToWin() {
		return _probabilityToWin;
	}

	public void setConfidenceIntervals(BigDecimal[] confidenceIntervals) {
		if (confidenceIntervals != null) {
			_confidenceIntervals = confidenceIntervals.clone();
		}
	}

	public void setControl(boolean control) {
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

	private BigDecimal[] _confidenceIntervals;
	private boolean _control;
	private String _dxpVariantId;
	private Double _improvement;
	private Double _median;
	private Double _probabilityToWin;

}