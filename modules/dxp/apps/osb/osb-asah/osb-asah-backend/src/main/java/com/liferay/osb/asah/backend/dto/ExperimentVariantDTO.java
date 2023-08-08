/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.entity.ExperimentVariant;
import com.liferay.osb.asah.common.graphql.GraphQLProperty;
import com.liferay.osb.asah.common.graphql.GraphQLType;
import com.liferay.osb.asah.common.util.SetUtil;

import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Marcos Martins
 */
@GraphQLType("ExperimentVariant")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExperimentVariantDTO {

	public ExperimentVariantDTO() {
	}

	public ExperimentVariantDTO(ExperimentVariant experimentVariant) {
		_changes = experimentVariant.getChanges();
		_control = experimentVariant.getControl();
		_dxpVariantId = experimentVariant.getDXPVariantId();
		_dxpVariantName = experimentVariant.getDXPVariantName();
		_trafficSplit = experimentVariant.getTrafficSplit();
	}

	public ExperimentVariantDTO(Set<ExperimentVariant> experimentVariants) {
		SetUtil.map(experimentVariants, ExperimentVariantDTO::new);
	}

	@Min(0)
	@NotNull
	public Integer getChanges() {
		return _changes;
	}

	@NotNull
	public Boolean getControl() {
		return _control;
	}

	@GraphQLProperty("dxpVariantId")
	@JsonProperty("dxpVariantId")
	@NotBlank
	public String getDXPVariantId() {
		return _dxpVariantId;
	}

	@GraphQLProperty("dxpVariantName")
	@JsonProperty("dxpVariantName")
	@NotBlank
	public String getDXPVariantName() {
		return _dxpVariantName;
	}

	@Max(100)
	@Min(0)
	@NotNull
	public Double getTrafficSplit() {
		return _trafficSplit;
	}

	public void setChanges(Integer changes) {
		_changes = changes;
	}

	public void setControl(Boolean control) {
		_control = control;
	}

	public void setDXPVariantId(String dxpVariantId) {
		_dxpVariantId = dxpVariantId;
	}

	public void setDXPVariantName(String dxpVariantName) {
		_dxpVariantName = dxpVariantName;
	}

	public void setTrafficSplit(Double trafficSplit) {
		_trafficSplit = trafficSplit;
	}

	private Integer _changes;
	private Boolean _control;
	private String _dxpVariantId;
	private String _dxpVariantName;
	private Double _trafficSplit;

}