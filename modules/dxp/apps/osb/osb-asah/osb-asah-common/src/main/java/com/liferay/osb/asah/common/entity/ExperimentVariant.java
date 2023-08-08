/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.osb.asah.common.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.liferay.osb.asah.common.util.BeanUtils;

import java.util.Map;
import java.util.Objects;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @author André Miranda
 */
@Table
public class ExperimentVariant {

	public ExperimentVariant() {
	}

	public ExperimentVariant(Map<String, Object> source) {
		BeanUtils.copyProperties(source, this);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof ExperimentVariant)) {
			return false;
		}

		ExperimentVariant experimentVariant = (ExperimentVariant)obj;

		if (Objects.equals(_changes, experimentVariant._changes) &&
			Objects.equals(_control, experimentVariant._control) &&
			Objects.equals(_dxpVariantId, experimentVariant._dxpVariantId) &&
			Objects.equals(
				_dxpVariantName, experimentVariant._dxpVariantName) &&
			Objects.equals(_trafficSplit, experimentVariant._trafficSplit)) {

			return true;
		}

		return false;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Integer getChanges() {
		return _changes;
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
	@JsonProperty("dxpVariantName")
	public String getDXPVariantName() {
		return _dxpVariantName;
	}

	@AccessType(AccessType.Type.PROPERTY)
	public Double getTrafficSplit() {
		return _trafficSplit;
	}

	@Override
	public int hashCode() {
		return Objects.hash(
			_changes, _control, _dxpVariantId, _dxpVariantName, _trafficSplit);
	}

	public Boolean isControl() {
		return getControl();
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

	@Transient
	private Integer _changes;

	@Transient
	private Boolean _control;

	@Transient
	private String _dxpVariantId;

	@Transient
	private String _dxpVariantName;

	@Transient
	private Double _trafficSplit;

}